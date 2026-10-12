package com.eventpass.ordenes.service;

import com.eventpass.auth.model.Usuario;
import com.eventpass.auth.repository.UsuarioRepository;
import com.eventpass.common.exception.ConflictException;
import com.eventpass.common.exception.ResourceNotFoundException;
import com.eventpass.eventos.model.Evento;
import com.eventpass.eventos.repository.EventoRepository;
import com.eventpass.ordenes.dto.OrdenRequest;
import com.eventpass.ordenes.dto.OrdenResponse;
import com.eventpass.ordenes.model.Orden;
import com.eventpass.ordenes.repository.OrdenRepository;
import com.eventpass.tickets.model.Ticket;
import com.eventpass.tickets.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Lógica de negocio de la venta de entradas.
 *
 * Flujo de compra (diagrama EventPass):
 *   1. Se valida usuario y evento.
 *   2. Se descuenta el aforo de forma atómica; si no queda, se rechaza con 409.
 *   3. Se emite la orden y un ticket por entrada.
 */
@Service
public class OrdenService {

    private final OrdenRepository ordenes;
    private final UsuarioRepository usuarios;
    private final EventoRepository eventos;
    private final TicketRepository tickets;

    public OrdenService(OrdenRepository ordenes,
                        UsuarioRepository usuarios,
                        EventoRepository eventos,
                        TicketRepository tickets) {
        this.ordenes = ordenes;
        this.usuarios = usuarios;
        this.eventos = eventos;
        this.tickets = tickets;
    }

    @Transactional(readOnly = true)
    public List<OrdenResponse> listar() {
        return ordenes.findAll().stream().map(OrdenResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public OrdenResponse obtener(Long id) {
        return OrdenResponse.desde(buscar(id));
    }

    @Transactional
    public OrdenResponse crear(OrdenRequest request) {
        Usuario usuario = usuarios.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + request.usuarioId()));

        Evento evento = eventos.findById(request.eventoId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id " + request.eventoId()));

        int descontados = eventos.descontarAforo(evento.getId(), request.cantidad());
        if (descontados == 0) {
            throw new ConflictException(
                    "Aforo insuficiente para el evento '" + evento.getNombre()
                            + "'. Quedan " + evento.getAforoDisponible() + " entradas.");
        }

        BigDecimal total = evento.getPrecio().multiply(BigDecimal.valueOf(request.cantidad()));
        Orden orden = ordenes.save(new Orden(usuario, evento, request.cantidad(), total));

        List<Ticket> emitidos = new ArrayList<>();
        for (int i = 0; i < request.cantidad(); i++) {
            emitidos.add(new Ticket(orden, generarCodigo()));
        }
        orden.setTickets(tickets.saveAll(emitidos));

        return OrdenResponse.desde(orden);
    }

    /**
     * Anula la orden: devuelve el aforo al evento y elimina sus tickets.
     */
    @Transactional
    public void eliminar(Long id) {
        Orden orden = buscar(id);
        eventos.devolverAforo(orden.getEvento().getId(), orden.getCantidad());
        ordenes.delete(orden);
    }

    private Orden buscar(Long id) {
        return ordenes.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + id));
    }

    private String generarCodigo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

}

package com.eventpass.tickets.service;

import com.eventpass.common.exception.ConflictException;
import com.eventpass.common.exception.ResourceNotFoundException;
import com.eventpass.tickets.dto.TicketResponse;
import com.eventpass.tickets.model.EstadoTicket;
import com.eventpass.tickets.model.Ticket;
import com.eventpass.tickets.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio de emisión y validación de tickets (MS Tickets).
 */
@Service
public class TicketService {

    private final TicketRepository tickets;

    public TicketService(TicketRepository tickets) {
        this.tickets = tickets;
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> listar() {
        return tickets.findAll().stream().map(TicketResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse obtener(Long id) {
        return TicketResponse.desde(buscar(id));
    }

    /**
     * Valida un ticket en puerta a partir de su código QR.
     * Devuelve 404 si el código no existe y 409 si ya fue validado.
     */
    @Transactional
    public TicketResponse validar(String codigoQr) {
        Ticket ticket = tickets.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el código " + codigoQr));

        if (ticket.getEstado() != EstadoTicket.EMITIDO) {
            throw new ConflictException("El ticket " + codigoQr + " ya fue " + ticket.getEstado());
        }

        ticket.setEstado(EstadoTicket.VALIDADO);
        return TicketResponse.desde(tickets.save(ticket));
    }

    private Ticket buscar(Long id) {
        return tickets.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con id " + id));
    }

}

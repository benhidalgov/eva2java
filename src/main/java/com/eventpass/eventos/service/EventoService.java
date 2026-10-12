package com.eventpass.eventos.service;

import com.eventpass.eventos.dto.EventoRequest;
import com.eventpass.eventos.dto.EventoResponse;
import com.eventpass.eventos.model.Evento;
import com.eventpass.eventos.repository.EventoRepository;
import com.eventpass.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio del catálogo de eventos.
 */
@Service
public class EventoService {

    private final EventoRepository eventos;

    public EventoService(EventoRepository eventos) {
        this.eventos = eventos;
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listar(String nombre) {
        List<Evento> resultado = (nombre == null || nombre.isBlank())
                ? eventos.findAll()
                : eventos.findByNombreContainingIgnoreCase(nombre);
        return resultado.stream().map(EventoResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public EventoResponse obtener(Long id) {
        return EventoResponse.desde(buscar(id));
    }

    @Transactional
    public EventoResponse crear(EventoRequest request) {
        Evento evento = new Evento(
                request.nombre(),
                request.descripcion(),
                request.fecha(),
                request.lugar(),
                request.aforoTotal()
        );
        return EventoResponse.desde(eventos.save(evento));
    }

    @Transactional
    public EventoResponse actualizar(Long id, EventoRequest request) {
        Evento evento = buscar(id);
        evento.setNombre(request.nombre());
        evento.setDescripcion(request.descripcion());
        evento.setFecha(request.fecha());
        evento.setLugar(request.lugar());
        evento.setAforoTotal(request.aforoTotal());
        if (evento.getAforoDisponible() > request.aforoTotal()) {
            evento.setAforoDisponible(request.aforoTotal());
        }
        return EventoResponse.desde(eventos.save(evento));
    }

    @Transactional
    public void eliminar(Long id) {
        eventos.delete(buscar(id));
    }

    private Evento buscar(Long id) {
        return eventos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con id " + id));
    }

}

package com.eventpass.eventos.dto;

import com.eventpass.eventos.model.Evento;

import java.time.LocalDateTime;

/**
 * Cuerpo de salida de los endpoints de eventos.
 */
public record EventoResponse(
        Long id,
        String nombre,
        String descripcion,
        LocalDateTime fecha,
        String lugar,
        int aforoTotal,
        int aforoDisponible
) {

    public static EventoResponse desde(Evento evento) {
        return new EventoResponse(
                evento.getId(),
                evento.getNombre(),
                evento.getDescripcion(),
                evento.getFecha(),
                evento.getLugar(),
                evento.getAforoTotal(),
                evento.getAforoDisponible()
        );
    }

}

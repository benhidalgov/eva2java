package com.eventpass.tickets.dto;

import com.eventpass.tickets.model.EstadoTicket;
import com.eventpass.tickets.model.Ticket;

import java.time.LocalDateTime;

/**
 * Cuerpo de salida de los endpoints de tickets.
 */
public record TicketResponse(
        Long id,
        Long ordenId,
        Long eventoId,
        String eventoNombre,
        String codigoQr,
        EstadoTicket estado,
        LocalDateTime fechaEmision
) {

    public static TicketResponse desde(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getOrden().getId(),
                ticket.getOrden().getEvento().getId(),
                ticket.getOrden().getEvento().getNombre(),
                ticket.getCodigoQr(),
                ticket.getEstado(),
                ticket.getFechaEmision()
        );
    }

}

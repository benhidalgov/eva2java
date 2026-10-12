package com.eventpass.ordenes.dto;

import com.eventpass.ordenes.model.EstadoOrden;
import com.eventpass.ordenes.model.Orden;
import com.eventpass.tickets.dto.TicketResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Cuerpo de salida de los endpoints de órdenes.
 */
public record OrdenResponse(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        Long eventoId,
        String eventoNombre,
        int cantidad,
        BigDecimal total,
        EstadoOrden estado,
        LocalDateTime fecha,
        List<TicketResponse> tickets
) {

    public static OrdenResponse desde(Orden orden) {
        return new OrdenResponse(
                orden.getId(),
                orden.getUsuario().getId(),
                orden.getUsuario().getNombre(),
                orden.getEvento().getId(),
                orden.getEvento().getNombre(),
                orden.getCantidad(),
                orden.getTotal(),
                orden.getEstado(),
                orden.getFecha(),
                orden.getTickets().stream().map(TicketResponse::desde).toList()
        );
    }

}

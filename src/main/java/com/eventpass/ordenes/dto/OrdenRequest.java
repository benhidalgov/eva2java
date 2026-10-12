package com.eventpass.ordenes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo de entrada para crear una orden de compra.
 */
public record OrdenRequest(
        @NotNull(message = "el id del usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "el id del evento es obligatorio")
        Long eventoId,

        @NotNull(message = "la cantidad es obligatoria")
        @Min(value = 1, message = "la cantidad debe ser al menos 1")
        @Max(value = 50, message = "la cantidad máxima por orden es 50")
        Integer cantidad
) {
}

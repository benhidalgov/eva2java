package com.eventpass.eventos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cuerpo de entrada para crear o actualizar un evento.
 */
public record EventoRequest(
        @NotBlank(message = "el nombre es obligatorio")
        String nombre,

        String descripcion,

        @NotNull(message = "la fecha es obligatoria")
        LocalDateTime fecha,

        @NotBlank(message = "el lugar es obligatorio")
        String lugar,

        @NotNull(message = "el aforo total es obligatorio")
        @Min(value = 1, message = "el aforo total debe ser al menos 1")
        Integer aforoTotal,

        @NotNull(message = "el precio es obligatorio")
        @DecimalMin(value = "0", message = "el precio no puede ser negativo")
        BigDecimal precio
) {
}

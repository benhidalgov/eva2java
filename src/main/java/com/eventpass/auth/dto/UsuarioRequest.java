package com.eventpass.auth.dto;

import com.eventpass.auth.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo de entrada para crear o actualizar un usuario.
 */
public record UsuarioRequest(
        @NotBlank(message = "el nombre es obligatorio")
        String nombre,

        @NotBlank(message = "el email es obligatorio")
        @Email(message = "el email debe tener un formato válido")
        String email,

        @NotBlank(message = "la contraseña es obligatoria")
        String password,

        @NotNull(message = "el rol es obligatorio")
        Rol rol
) {
}

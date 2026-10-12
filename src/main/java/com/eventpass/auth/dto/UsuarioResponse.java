package com.eventpass.auth.dto;

import com.eventpass.auth.model.Rol;
import com.eventpass.auth.model.Usuario;

/**
 * Cuerpo de salida de los endpoints de usuarios.
 * No expone la contraseña.
 */
public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        Rol rol
) {

    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol());
    }

}

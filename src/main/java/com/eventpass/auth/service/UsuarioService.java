package com.eventpass.auth.service;

import com.eventpass.auth.dto.UsuarioRequest;
import com.eventpass.auth.dto.UsuarioResponse;
import com.eventpass.auth.model.Usuario;
import com.eventpass.auth.repository.UsuarioRepository;
import com.eventpass.common.exception.ConflictException;
import com.eventpass.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio de la gestión de usuarios.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarios.findAll().stream().map(UsuarioResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long id) {
        return UsuarioResponse.desde(buscar(id));
    }

    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarios.existsByEmail(request.email())) {
            throw new ConflictException("Ya existe un usuario con el email " + request.email());
        }
        Usuario usuario = new Usuario(
                request.nombre(),
                request.email(),
                request.password(),
                request.rol()
        );
        return UsuarioResponse.desde(usuarios.save(usuario));
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscar(id);
        if (!usuario.getEmail().equals(request.email()) && usuarios.existsByEmail(request.email())) {
            throw new ConflictException("Ya existe un usuario con el email " + request.email());
        }
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setPassword(request.password());
        usuario.setRol(request.rol());
        return UsuarioResponse.desde(usuarios.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        usuarios.delete(buscar(id));
    }

    private Usuario buscar(Long id) {
        return usuarios.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
    }

}

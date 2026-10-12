package com.eventpass.auth.controller;

import com.eventpass.auth.dto.UsuarioRequest;
import com.eventpass.auth.dto.UsuarioResponse;
import com.eventpass.auth.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CRUD RESTful de usuarios (MS Autenticación).
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarios;

    public UsuarioController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    /** GET /api/usuarios */
    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarios.listar();
    }

    /** GET /api/usuarios/{id} */
    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable Long id) {
        return usuarios.obtener(id);
    }

    /** POST /api/usuarios -> 201 Created */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest request) {
        return usuarios.crear(request);
    }

    /** PUT /api/usuarios/{id} */
    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return usuarios.actualizar(id, request);
    }

    /** DELETE /api/usuarios/{id} -> 204 No Content */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarios.eliminar(id);
    }

}

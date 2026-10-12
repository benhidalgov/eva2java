package com.eventpass.eventos.controller;

import com.eventpass.eventos.dto.EventoRequest;
import com.eventpass.eventos.dto.EventoResponse;
import com.eventpass.eventos.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CRUD RESTful del catálogo de eventos.
 */
@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventos;

    public EventoController(EventoService eventos) {
        this.eventos = eventos;
    }

    /** GET /api/eventos  |  GET /api/eventos?nombre=festival */
    @GetMapping
    public List<EventoResponse> listar(@RequestParam(required = false) String nombre) {
        return eventos.listar(nombre);
    }

    /** GET /api/eventos/{id} */
    @GetMapping("/{id}")
    public EventoResponse obtener(@PathVariable Long id) {
        return eventos.obtener(id);
    }

    /** POST /api/eventos -> 201 Created */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoResponse crear(@Valid @RequestBody EventoRequest request) {
        return eventos.crear(request);
    }

    /** PUT /api/eventos/{id} */
    @PutMapping("/{id}")
    public EventoResponse actualizar(@PathVariable Long id, @Valid @RequestBody EventoRequest request) {
        return eventos.actualizar(id, request);
    }

    /** DELETE /api/eventos/{id} -> 204 No Content */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        eventos.eliminar(id);
    }

}

package com.eventpass.ordenes.controller;

import com.eventpass.ordenes.dto.OrdenRequest;
import com.eventpass.ordenes.dto.OrdenResponse;
import com.eventpass.ordenes.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de órdenes de compra (MS Órdenes).
 */
@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenes;

    public OrdenController(OrdenService ordenes) {
        this.ordenes = ordenes;
    }

    /** GET /api/ordenes */
    @GetMapping
    public List<OrdenResponse> listar() {
        return ordenes.listar();
    }

    /** GET /api/ordenes/{id} */
    @GetMapping("/{id}")
    public OrdenResponse obtener(@PathVariable Long id) {
        return ordenes.obtener(id);
    }

    /** POST /api/ordenes -> 201 Created */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenResponse crear(@Valid @RequestBody OrdenRequest request) {
        return ordenes.crear(request);
    }

    /** DELETE /api/ordenes/{id} -> 204 No Content (devuelve el aforo) */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        ordenes.eliminar(id);
    }

}

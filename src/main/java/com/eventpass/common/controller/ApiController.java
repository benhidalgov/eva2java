package com.eventpass.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Raíz de la API: devuelve el índice de endpoints disponibles,
 * para que abrir http://localhost:8080/ muestre las rutas en vez de un 404.
 */
@RestController
public class ApiController {

    private record Ruta(String metodo, String path, String descripcion) {
    }

    private static final List<Ruta> RUTAS = List.of(
            new Ruta("GET", "/api/eventos", "Lista eventos (filtro ?nombre=)"),
            new Ruta("GET", "/api/eventos/{id}", "Detalle de evento"),
            new Ruta("POST", "/api/eventos", "Crea evento"),
            new Ruta("PUT", "/api/eventos/{id}", "Actualiza evento"),
            new Ruta("DELETE", "/api/eventos/{id}", "Elimina evento"),
            new Ruta("GET", "/api/usuarios", "Lista usuarios"),
            new Ruta("GET", "/api/usuarios/{id}", "Detalle de usuario"),
            new Ruta("POST", "/api/usuarios", "Crea usuario"),
            new Ruta("PUT", "/api/usuarios/{id}", "Actualiza usuario"),
            new Ruta("DELETE", "/api/usuarios/{id}", "Elimina usuario"),
            new Ruta("GET", "/api/ordenes", "Lista órdenes"),
            new Ruta("GET", "/api/ordenes/{id}", "Detalle de orden"),
            new Ruta("POST", "/api/ordenes", "Compra entradas y emite tickets"),
            new Ruta("DELETE", "/api/ordenes/{id}", "Anula orden y devuelve el aforo"),
            new Ruta("GET", "/api/tickets", "Lista tickets"),
            new Ruta("GET", "/api/tickets/{id}", "Detalle de ticket"),
            new Ruta("POST", "/api/tickets/validar/{codigoQr}", "Valida ticket en puerta")
    );

    /** GET / */
    @GetMapping
    public Map<String, Object> indice() {
        return Map.of("api", "EventPass 1.0.0", "endpoints", RUTAS);
    }

}

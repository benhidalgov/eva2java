package com.eventpass.tickets.controller;

import com.eventpass.tickets.dto.TicketResponse;
import com.eventpass.tickets.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de consulta y validación de tickets (MS Tickets).
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService tickets;

    public TicketController(TicketService tickets) {
        this.tickets = tickets;
    }

    /** GET /api/tickets */
    @GetMapping
    public List<TicketResponse> listar() {
        return tickets.listar();
    }

    /** GET /api/tickets/{id} */
    @GetMapping("/{id}")
    public TicketResponse obtener(@PathVariable Long id) {
        return tickets.obtener(id);
    }

    /** POST /api/tickets/validar/{codigoQr} -> 200 OK */
    @PostMapping("/validar/{codigoQr}")
    public TicketResponse validar(@PathVariable String codigoQr) {
        return tickets.validar(codigoQr);
    }

}

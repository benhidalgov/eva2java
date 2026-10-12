package com.eventpass.tickets.model;

import com.eventpass.ordenes.model.Orden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Ticket de acceso con su código de validación (QR).
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Muchos tickets pertenecen a una sola orden. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Column(name = "codigo_qr", nullable = false, unique = true)
    private String codigoQr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTicket estado;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    protected Ticket() {
    }

    public Ticket(Orden orden, String codigoQr) {
        this.orden = orden;
        this.codigoQr = codigoQr;
        this.estado = EstadoTicket.EMITIDO;
        this.fechaEmision = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Orden getOrden() {
        return orden;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

}

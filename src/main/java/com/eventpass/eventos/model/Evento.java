package com.eventpass.eventos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Catálogo de eventos disponibles para la venta de tickets.
 */
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String lugar;

    /** Aforo total del recinto para este evento. */
    @Column(nullable = false)
    private int aforoTotal;

    /** Entradas que quedan por vender. Se descuenta de forma atómica al comprar. */
    @Column(nullable = false)
    private int aforoDisponible;

    protected Evento() {
    }

    public Evento(String nombre, String descripcion, LocalDateTime fecha, String lugar, int aforoTotal) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.lugar = lugar;
        this.aforoTotal = aforoTotal;
        this.aforoDisponible = aforoTotal;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getAforoTotal() {
        return aforoTotal;
    }

    public void setAforoTotal(int aforoTotal) {
        this.aforoTotal = aforoTotal;
    }

    public int getAforoDisponible() {
        return aforoDisponible;
    }

    public void setAforoDisponible(int aforoDisponible) {
        this.aforoDisponible = aforoDisponible;
    }

}

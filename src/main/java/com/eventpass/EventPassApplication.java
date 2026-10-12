package com.eventpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la API EventPass.
 *
 * @SpringBootApplication escanea el paquete {@code com.eventpass} y sus
 * subpaquetes, registrando automáticamente los controladores, servicios,
 * repositorios y entidades de cada módulo (auth, eventos, ordenes, tickets).
 */
@SpringBootApplication
public class EventPassApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventPassApplication.class, args);
    }

}

package com.eventpass.common.exception;

/**
 * Se lanza cuando la petición es correcta pero choca con el estado actual
 * del recurso (aforo agotado, email duplicado, etc.).
 * El {@link GlobalExceptionHandler} la traduce a HTTP 409.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String mensaje) {
        super(mensaje);
    }

}

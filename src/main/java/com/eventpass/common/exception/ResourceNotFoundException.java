package com.eventpass.common.exception;

/**
 * Se lanza cuando un recurso solicitado no existe.
 * El {@link GlobalExceptionHandler} la traduce a HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

}

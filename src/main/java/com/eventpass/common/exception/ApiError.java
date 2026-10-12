package com.eventpass.common.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Cuerpo estándar de error devuelto por toda la API.
 * Misma forma para 400, 404 y 409, de modo que el cliente siempre
 * reciba {status, message, path, fieldErrors}.
 *
 * @param status      código HTTP
 * @param message     mensaje legible para el cliente
 * @param path        recurso que se solicitó
 * @param fieldErrors errores de validación por campo (solo en HTTP 400)
 * @param timestamp   momento en que ocurrió el error
 */
public record ApiError(
        int status,
        String message,
        String path,
        Map<String, String> fieldErrors,
        Instant timestamp
) {

    public static ApiError of(int status, String message, String path) {
        return new ApiError(status, message, path, Map.of(), Instant.now());
    }

    public static ApiError of(int status, String message, String path, Map<String, String> fieldErrors) {
        return new ApiError(status, message, path, fieldErrors, Instant.now());
    }

}

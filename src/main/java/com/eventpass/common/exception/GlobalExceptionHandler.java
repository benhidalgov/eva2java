package com.eventpass.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce las excepciones de la aplicación a respuestas HTTP con
 * el código y el cuerpo documentados en {@link ApiError}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Recurso inexistente -> 404 */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> noEncontrado(ResourceNotFoundException ex, HttpServletRequest req) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    /** Ruta inexistente (ej. abrir "/" en el navegador) -> 404 con la guía */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> rutaInexistente(NoResourceFoundException ex, HttpServletRequest req) {
        String mensaje = "Ruta '" + req.getRequestURI() + "' inexistente. "
                + "Usa /api/eventos, /api/usuarios, /api/ordenes o /api/tickets";
        return responder(HttpStatus.NOT_FOUND, mensaje, req);
    }

    /** Conflicto de estado (aforo agotado, duplicado) -> 409 */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> conflicto(ConflictException ex, HttpServletRequest req) {
        return responder(HttpStatus.CONFLICT, ex.getMessage(), req);
    }

    /** Fallos de @Valid sobre los DTO de entrada -> 400 con detalle por campo */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        String mensaje = "Validación fallida para uno o más campos";
        ApiError cuerpo = ApiError.of(HttpStatus.BAD_REQUEST.value(), mensaje, req.getRequestURI(), campos);
        return ResponseEntity.badRequest().body(cuerpo);
    }

    /** JSON mal formado o tipo de dato inválido en el cuerpo -> 400 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> cuerpoInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return responder(HttpStatus.BAD_REQUEST, "Cuerpo de la petición inválido o no legible", req);
    }

    /** Parámetro de path/query con tipo incorrecto (id = "abc") -> 400 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> parametroInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String mensaje = "Parámetro '" + ex.getName() + "' inválido";
        return responder(HttpStatus.BAD_REQUEST, mensaje, req);
    }

    /** Cualquier otro error no previsto -> 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> inesperado(Exception ex, HttpServletRequest req) {
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", req);
    }

    private ResponseEntity<ApiError> responder(HttpStatus status, String mensaje, HttpServletRequest req) {
        ApiError cuerpo = ApiError.of(status.value(), mensaje, req.getRequestURI());
        return ResponseEntity.status(status).body(cuerpo);
    }

}

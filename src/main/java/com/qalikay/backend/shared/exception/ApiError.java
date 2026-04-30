package com.qalikay.backend.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estructura estandar de respuesta cuando ocurre un error en el backend.
 *
 * Todos los errores devueltos al frontend tienen este formato unificado.
 * Esto facilita al frontend manejar errores de manera consistente.
 *
 * Ejemplo:
 * {
 *   "timestamp": "2026-04-28T21:30:00",
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "El correo ya esta registrado",
 *   "path": "/api/v1/auth/register/cliente",
 *   "fieldErrors": [
 *     { "field": "email", "message": "Formato de correo invalido" }
 *   ]
 * }
 */
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final List<FieldError> fieldErrors;

    /**
     * Helper para construir un ApiError sin errores de campo.
     */
    public static ApiError of(HttpStatus status, String message, String path) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }

    /**
     * Detalle de un error de validacion en un campo especifico.
     */
    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private final String field;
        private final String message;
    }
}

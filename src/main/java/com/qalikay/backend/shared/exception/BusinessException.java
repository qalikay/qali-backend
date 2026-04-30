package com.qalikay.backend.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Excepcion base para errores de reglas de negocio.
 *
 * Se lanza cuando una operacion viola una regla del dominio
 * (por ejemplo, intentar registrar un correo ya existente).
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

package com.qalikay.backend.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Se lanza cuando se intenta crear un recurso que viola una restriccion unica.
 *
 * Genera respuesta HTTP 409 Conflict.
 *
 * Ejemplo: registrar un correo que ya existe en la BD.
 */
public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

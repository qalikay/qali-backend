package com.qalikay.backend.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Se lanza cuando se intenta acceder a un recurso que no existe.
 *
 * Genera respuesta HTTP 404 Not Found.
 *
 * Ejemplo: GET /usuarios/999 cuando no existe el usuario 999.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " no encontrado con identificador: " + id, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

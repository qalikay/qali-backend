package com.qalikay.backend.consultation.domain;

/**
 * Ciclo de vida de una consulta entre cliente y experto.
 *
 *  SOLICITADA -> el cliente la creo, el experto debe responder
 *  ACEPTADA   -> el experto acepta, ambos pueden chatear
 *  RECHAZADA  -> el experto rechaza, no se chatea
 *  COMPLETADA -> resuelta exitosamente
 *  CANCELADA  -> cancelada por cualquier parte
 */
public enum EstadoConsulta {
    SOLICITADA,
    ACEPTADA,
    RECHAZADA,
    COMPLETADA,
    CANCELADA
}

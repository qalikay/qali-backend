package com.qalikay.backend.recipe.domain;

/**
 * Ciclo de vida de una receta dentro de la plataforma.
 *
 *  BORRADOR  -> el experto la esta armando, no es visible al publico
 *  PUBLICADA -> visible y disponible para clientes
 *  ARCHIVADA -> ya no se muestra, pero mantiene historial de compras
 */
public enum EstadoReceta {
    BORRADOR,
    PUBLICADA,
    ARCHIVADA
}

package com.qalikay.backend.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Version reducida de receta para mostrar en listados (cards, busqueda).
 *
 * Omite ingredientes y preparacion para reducir el payload y proteger
 * el contenido pago: solo se muestra el detalle completo cuando el cliente
 * accede al endpoint de detalle (en futuro, condicionado a la compra).
 */
@Builder
@Schema(description = "Resumen de receta para listados")
public record RecipeSummary(
        Long id,
        String title,
        String shortDescription,
        String imageUrl,
        BigDecimal price,
        String categoryName,
        String authorFullName,
        Long views,
        String status
) {}

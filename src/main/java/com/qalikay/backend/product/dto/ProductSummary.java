package com.qalikay.backend.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Resumen de insumo para listados")
public record ProductSummary(
        Long id,
        String name,
        String shortDescription,
        String imageUrl,
        BigDecimal price,
        Integer stock,
        String type,
        String unit,
        String categoryName,
        String sellerFullName
) {}

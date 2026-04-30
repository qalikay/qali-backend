package com.qalikay.backend.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Schema(description = "Detalle completo de un insumo")
public record ProductResponse(
        Long id,
        String name,
        String shortDescription,
        String description,
        String imageUrl,
        BigDecimal price,
        Integer stock,
        String unit,
        String type,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        SellerDto seller,
        CategoryDto category
) {

    @Builder
    public record SellerDto(Long id, String firstName, String lastName, String specialty) {}

    @Builder
    public record CategoryDto(Long id, String name, String icon) {}
}

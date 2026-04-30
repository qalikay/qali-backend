package com.qalikay.backend.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Detalle completo de una receta para vista de detalle.
 */
@Builder
@Schema(description = "Detalle completo de una receta")
public record RecipeResponse(
        Long id,
        String title,
        String shortDescription,
        String ingredients,
        String preparation,
        String usage,
        String warnings,
        String imageUrl,
        BigDecimal price,
        Integer preparationMinutes,
        String status,
        Long views,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AuthorDto author,
        CategoryDto category
) {

    @Builder
    public record AuthorDto(
            Long id,
            String firstName,
            String lastName,
            String specialty
    ) {}

    @Builder
    public record CategoryDto(
            Long id,
            String name,
            String icon
    ) {}
}

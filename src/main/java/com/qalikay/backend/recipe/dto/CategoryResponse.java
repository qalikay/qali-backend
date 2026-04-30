package com.qalikay.backend.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Categoria del catalogo")
public record CategoryResponse(
        Long id,
        String name,
        String description,
        String icon
) {}

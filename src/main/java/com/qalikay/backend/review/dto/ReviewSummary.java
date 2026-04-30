package com.qalikay.backend.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Resumen agregado de resenas de un item")
public record ReviewSummary(
        Double averageRating,
        Long totalReviews
) {}

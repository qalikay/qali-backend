package com.qalikay.backend.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Resena publica de un item")
public record ReviewResponse(
        Long id,
        Integer rating,
        String comment,
        String authorFullName,
        String itemType,
        Long itemRefId,
        LocalDateTime createdAt
) {}

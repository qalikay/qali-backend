package com.qalikay.backend.consultation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Resumen de una consulta para listados")
public record ConsultationSummary(
        Long id,
        String subject,
        String status,
        String otherPartyFullName,
        Integer messageCount,
        LocalDateTime updatedAt
) {}

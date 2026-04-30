package com.qalikay.backend.consultation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "Detalle de una consulta")
public record ConsultationResponse(
        Long id,
        String subject,
        String status,
        ParticipantDto client,
        ParticipantDto expert,
        List<MessageDto> messages,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    @Builder
    public record ParticipantDto(Long id, String firstName, String lastName) {}

    @Builder
    public record MessageDto(
            Long id,
            String content,
            ParticipantDto sender,
            Boolean read,
            LocalDateTime sentAt
    ) {}
}

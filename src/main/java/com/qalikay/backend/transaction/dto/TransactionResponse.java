package com.qalikay.backend.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "Detalle de una transaccion comercial")
public record TransactionResponse(
        Long id,
        BigDecimal total,
        String status,
        String paymentMethod,
        String paymentReference,
        LocalDateTime paidAt,
        LocalDateTime createdAt,
        List<ItemDto> items
) {

    @Builder
    public record ItemDto(
            String type,
            Long refId,
            String name,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {}
}

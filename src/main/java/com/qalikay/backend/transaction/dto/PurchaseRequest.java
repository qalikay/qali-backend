package com.qalikay.backend.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(description = "Datos para realizar una compra (uno o varios items)")
public record PurchaseRequest(

        @Schema(description = "Lista de items a comprar")
        @NotEmpty
        @Valid
        List<Item> items,

        @Schema(example = "YAPE", description = "TARJETA, YAPE, PLIN, TRANSFERENCIA, EFECTIVO")
        @NotBlank
        String paymentMethod,

        @Schema(example = "REF-987654321", description = "Referencia opcional del pago")
        String paymentReference

) {

    public record Item(

            @Schema(example = "RECETA", description = "RECETA o INSUMO")
            @NotBlank
            String type,

            @Schema(example = "1", description = "ID del item")
            @NotNull @Positive
            Long refId,

            @Schema(example = "1", description = "Cantidad (siempre 1 para RECETA, variable para INSUMO)")
            @NotNull @Min(1)
            Integer quantity

    ) {}
}

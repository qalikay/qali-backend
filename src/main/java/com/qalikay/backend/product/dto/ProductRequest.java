package com.qalikay.backend.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para crear o actualizar un insumo natural")
public record ProductRequest(

        @Schema(example = "Hojas de muna deshidratadas")
        @NotBlank @Size(min = 5, max = 150)
        String name,

        @Schema(example = "Bolsa de 100 gramos de muna seleccionada de los Andes")
        @NotBlank @Size(min = 20, max = 300)
        String shortDescription,

        @Schema(example = "Muna cosechada manualmente a 3000 msnm en Cusco. Secada al sol durante 5 dias.")
        @Size(max = 4000)
        String description,

        @Schema(example = "https://cdn.qalikay.com/insumos/muna.jpg")
        @Size(max = 500)
        String imageUrl,

        @Schema(example = "12.00")
        @NotNull @DecimalMin("0.0") @DecimalMax("9999.99")
        BigDecimal price,

        @Schema(example = "50", description = "Cantidad disponible en stock")
        @NotNull @Min(0)
        Integer stock,

        @Schema(example = "100g", description = "Unidad de medida del producto")
        @Size(max = 30)
        String unit,

        @Schema(example = "HOJAS",
                description = "HOJAS, RAIZ, FLOR, FRUTO, SEMILLA, CORTEZA, ACEITE, MIEL, EXTRACTO, POLVO, OTRO")
        @NotBlank
        String type,

        @Schema(example = "1", description = "ID NUMERICO de una categoria existente")
        @NotNull @Positive
        Long categoryId

) {}

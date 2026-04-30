package com.qalikay.backend.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Resena para una receta o insumo comprado")
public record ReviewRequest(

        @Schema(example = "5", description = "Calificacion de 1 a 5 estrellas")
        @NotNull @Min(1) @Max(5)
        Integer rating,

        @Schema(example = "Excelente receta, me alivio el malestar estomacal en 2 dias. La muna es muy efectiva.")
        @Size(max = 1000)
        String comment

) {}

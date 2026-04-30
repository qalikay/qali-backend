package com.qalikay.backend.consultation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para abrir una consulta privada con un experto")
public record ConsultationCreateRequest(

        @Schema(example = "2", description = "ID del experto al que se dirige la consulta")
        @NotNull @Positive
        Long expertId,

        @Schema(example = "Consulta sobre infusion para gastritis cronica")
        @NotBlank @Size(min = 5, max = 200)
        String subject,

        @Schema(example = "Hola, tengo gastritis cronica diagnosticada hace 5 anios. Quisiera saber si la muna es segura en mi caso o si recomienda otra opcion natural.")
        @NotBlank @Size(min = 20, max = 2000)
        String initialMessage

) {}

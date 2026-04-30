package com.qalikay.backend.consultation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Mensaje a enviar dentro de una consulta")
public record MessageRequest(

        @Schema(example = "Buenos dias, muchas gracias por su interes. La muna es generalmente segura, pero le recomendaria iniciar con dosis moderada.")
        @NotBlank @Size(min = 1, max = 2000)
        String content

) {}

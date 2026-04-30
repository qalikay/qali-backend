package com.qalikay.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

/**
 * Informacion publica de un usuario que se devuelve al cliente.
 *
 * NUNCA incluye el passwordHash ni informacion sensible.
 */
@Builder
@Schema(description = "Informacion publica de un usuario")
public record UserResponse(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Jherry")
        String firstName,

        @Schema(example = "Herrera")
        String lastName,

        @Schema(example = "jherry@qalikay.com")
        String email,

        @Schema(example = "+51999999999")
        String phone,

        @Schema(example = "https://...")
        String photoUrl,

        @Schema(example = "true")
        Boolean emailVerified,

        @Schema(example = "[\"CLIENTE\"]")
        Set<String> roles

) {}

package com.qalikay.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Solicitud de renovacion de access token usando un refresh token valido.
 */
@Schema(description = "Refresh token para obtener un nuevo access token")
public record RefreshTokenRequest(

        @Schema(example = "eyJhbGciOiJIUzUxMiJ9...")
        @NotBlank(message = "El refresh token es obligatorio")
        String refreshToken

) {}

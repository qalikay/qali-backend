package com.qalikay.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Respuesta exitosa de autenticacion (registro o login).
 *
 * Contiene los tokens JWT y la informacion publica del usuario.
 * El frontend debe guardar el accessToken (memoria) y refreshToken (cookie httpOnly idealmente).
 */
@Builder
@Schema(description = "Tokens JWT e informacion del usuario autenticado")
public record AuthResponse(

        @Schema(example = "eyJhbGciOiJIUzUxMiJ9...")
        String accessToken,

        @Schema(example = "eyJhbGciOiJIUzUxMiJ9...")
        String refreshToken,

        @Schema(example = "Bearer")
        String tokenType,

        @Schema(example = "3600", description = "Segundos de validez del access token")
        long expiresIn,

        UserResponse user

) {}

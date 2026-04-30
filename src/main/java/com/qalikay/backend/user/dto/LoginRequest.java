package com.qalikay.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Credenciales de inicio de sesion.
 *
 * Si son validas, el backend devuelve un AuthResponse con un par de JWT.
 */
@Schema(description = "Credenciales de inicio de sesion")
public record LoginRequest(

        @Schema(example = "maria.lopez@example.com", description = "Correo del usuario")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es valido")
        String email,

        @Schema(example = "Cliente123", description = "Password del usuario")
        @NotBlank(message = "El password es obligatorio")
        String password

) {}

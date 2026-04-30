package com.qalikay.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Cambio de password con verificacion del password actual.
 *
 * Por seguridad, exigimos que el usuario reingrese su password actual.
 * Si solo se valida el token JWT, alguien con acceso al PC podria cambiar
 * el password sin saber el original.
 */
@Schema(description = "Cambio de password con verificacion del actual")
public record ChangePasswordRequest(

        @Schema(example = "Cliente123", description = "Password actual del usuario")
        @NotBlank(message = "El password actual es obligatorio")
        String currentPassword,

        @Schema(example = "NuevoPass123", description = "Nuevo password (min 8, mayus, minus, numero)")
        @NotBlank(message = "El nuevo password es obligatorio")
        @Size(min = 8, max = 100)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "El nuevo password debe tener al menos una mayuscula, una minuscula y un numero"
        )
        String newPassword

) {}

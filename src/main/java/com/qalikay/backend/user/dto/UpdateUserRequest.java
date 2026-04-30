package com.qalikay.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos editables del perfil de usuario.
 *
 * NO permite cambiar el email (requiere flujo separado con verificacion).
 * NO permite cambiar el password (existe endpoint separado /me/password).
 *
 * Todos los campos son opcionales: solo se actualizan los que no son null.
 */
@Schema(description = "Campos editables del perfil propio")
public record UpdateUserRequest(

        @Schema(example = "Maria Fernanda")
        @Size(min = 2, max = 80)
        String firstName,

        @Schema(example = "Lopez Garcia")
        @Size(min = 2, max = 80)
        String lastName,

        @Schema(example = "+51987654321")
        @Pattern(regexp = "^$|^\\+?[0-9]{7,15}$")
        String phone,

        @Schema(example = "https://cdn.qalikay.com/profiles/maria.jpg")
        @Size(max = 500)
        String photoUrl

) {}

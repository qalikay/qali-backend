package com.qalikay.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos requeridos para registrar un usuario CLIENTE (US02).
 *
 * Todos los campos se validan automaticamente cuando el controller
 * usa la anotacion @Valid.
 *
 * Los ejemplos definidos en @Schema(example=...) cumplen TODAS las
 * validaciones, asi que pueden usarse tal cual desde Swagger UI.
 */
@Schema(description = "Datos para registrar un cliente")
public record RegisterClienteRequest(

        @Schema(example = "Maria", description = "Nombre del cliente (entre 2 y 80 caracteres)")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
        String firstName,

        @Schema(example = "Lopez", description = "Apellido del cliente (entre 2 y 80 caracteres)")
        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 80, message = "El apellido debe tener entre 2 y 80 caracteres")
        String lastName,

        @Schema(example = "maria.lopez@example.com", description = "Correo unico (max 120 caracteres)")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es valido")
        @Size(max = 120, message = "El email no puede exceder 120 caracteres")
        String email,

        @Schema(
                example = "Cliente123",
                description = "Minimo 8 caracteres, una mayuscula, una minuscula y un numero"
        )
        @NotBlank(message = "El password es obligatorio")
        @Size(min = 8, max = 100, message = "El password debe tener entre 8 y 100 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "El password debe tener al menos una mayuscula, una minuscula y un numero"
        )
        String password,

        @Schema(example = "+51987654321", description = "Opcional. 7 a 15 digitos")
        @Pattern(
                regexp = "^$|^\\+?[0-9]{7,15}$",
                message = "El telefono debe tener entre 7 y 15 digitos"
        )
        String phone

) {}

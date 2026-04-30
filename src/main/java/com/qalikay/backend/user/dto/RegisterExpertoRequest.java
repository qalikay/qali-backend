package com.qalikay.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos requeridos para registrar un usuario EXPERTO (US03).
 *
 * Adicional a los datos basicos del cliente, requiere:
 *  - specialtyId:        id de una especialidad existente en el catalogo
 *  - trajectory:         experiencia profesional (texto detallado)
 *  - yearsOfExperience:  cuantos anios ejerce la actividad
 *
 * Al registrarse, el experto queda con verificado=false hasta que un
 * ADMIN lo apruebe.
 *
 * Los ejemplos definidos en @Schema(example=...) cumplen TODAS las
 * validaciones, asi que pueden usarse tal cual desde Swagger UI.
 */
@Schema(description = "Datos para registrar un experto en medicina natural")
public record RegisterExpertoRequest(

        @Schema(example = "Carlos", description = "Nombre del experto")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 80)
        String firstName,

        @Schema(example = "Quispe", description = "Apellido del experto")
        @NotBlank(message = "El apellido es obligatorio")
        @Size(min = 2, max = 80)
        String lastName,

        @Schema(example = "carlos.quispe@example.com")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es valido")
        @Size(max = 120)
        String email,

        @Schema(
                example = "Experto123",
                description = "Minimo 8 caracteres, una mayuscula, una minuscula y un numero"
        )
        @NotBlank(message = "El password es obligatorio")
        @Size(min = 8, max = 100)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "El password debe tener al menos una mayuscula, una minuscula y un numero"
        )
        String password,

        @Schema(example = "+51987654321")
        @Pattern(
                regexp = "^$|^\\+?[0-9]{7,15}$",
                message = "El telefono debe tener entre 7 y 15 digitos"
        )
        String phone,

        @Schema(example = "1", description = "ID numerico de una especialidad existente en el catalogo")
        @NotNull(message = "Debe seleccionar una especialidad")
        @Positive(message = "El id de especialidad debe ser positivo")
        Long specialtyId,

        @Schema(
                example = "Diez anios atendiendo pacientes con medicina ancestral andina en Cusco y Apurimac",
                description = "Trayectoria profesional. Texto largo entre 30 y 1000 caracteres."
        )
        @NotBlank(message = "La trayectoria es obligatoria")
        @Size(min = 30, max = 1000, message = "La trayectoria debe tener entre 30 y 1000 caracteres")
        String trajectory,

        @Schema(
                example = "Especialista en herbolaria andina con conocimientos heredados de generacion en generacion",
                description = "Biografia opcional (max 2000 caracteres)"
        )
        @Size(max = 2000, message = "La biografia no puede exceder 2000 caracteres")
        String biography,

        @Schema(example = "10", description = "Numero de anios ejerciendo la actividad")
        @NotNull(message = "Los anios de experiencia son obligatorios")
        @Min(value = 0, message = "Los anios de experiencia no pueden ser negativos")
        Integer yearsOfExperience

) {}

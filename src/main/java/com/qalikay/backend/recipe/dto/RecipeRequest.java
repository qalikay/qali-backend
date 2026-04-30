package com.qalikay.backend.recipe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Datos para crear o actualizar una receta.
 *
 * Misma estructura para POST y PUT (consistencia y simplicidad).
 * El estado y la fecha de publicacion no se envian: se manejan por el servicio.
 *
 * Los ejemplos definidos en @Schema(example=...) cumplen TODAS las
 * validaciones, asi que el JSON sugerido por Swagger es 100% valido.
 */
@Schema(description = "Datos para crear o actualizar una receta de medicina natural")
public record RecipeRequest(

        @Schema(
                example = "Infusion de muna para la digestion",
                description = "Titulo de la receta (entre 5 y 150 caracteres)"
        )
        @NotBlank(message = "El titulo es obligatorio")
        @Size(min = 5, max = 150)
        String title,

        @Schema(
                example = "Receta tradicional andina para aliviar malestares estomacales y gases",
                description = "Descripcion corta visible en el listado (entre 20 y 300 caracteres)"
        )
        @NotBlank(message = "La descripcion corta es obligatoria")
        @Size(min = 20, max = 300)
        String shortDescription,

        @Schema(
                example = "10 hojas de muna fresca, 1 taza de agua hirviendo, miel al gusto",
                description = "Lista de ingredientes (entre 10 y 4000 caracteres)"
        )
        @NotBlank(message = "Los ingredientes son obligatorios")
        @Size(min = 10, max = 4000)
        String ingredients,

        @Schema(
                example = "Hervir el agua y verterla sobre las hojas de muna. Tapar y dejar reposar por 5 minutos. Colar y endulzar con miel al gusto.",
                description = "Pasos de preparacion (entre 20 y 4000 caracteres)"
        )
        @NotBlank(message = "La preparacion es obligatoria")
        @Size(min = 20, max = 4000)
        String preparation,

        @Schema(
                example = "Beber 1 taza tibia despues de cada comida principal por 3 dias seguidos",
                description = "Modo de uso opcional (max 1000 caracteres)"
        )
        @Size(max = 1000)
        String usage,

        @Schema(
                example = "No consumir durante el embarazo. Consultar con un especialista en caso de gastritis cronica.",
                description = "Precauciones opcionales (max 1000 caracteres)"
        )
        @Size(max = 1000)
        String warnings,

        @Schema(
                example = "https://cdn.qalikay.com/recetas/muna.jpg",
                description = "URL opcional de la imagen (max 500 caracteres)"
        )
        @Size(max = 500)
        String imageUrl,

        @Schema(example = "15.50", description = "Precio en soles (entre 0 y 9999.99)")
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
        @DecimalMax(value = "9999.99", message = "El precio no puede exceder 9999.99")
        BigDecimal price,

        @Schema(example = "10", description = "Tiempo estimado de preparacion en minutos")
        @Min(value = 0)
        Integer preparationMinutes,

        @Schema(example = "1", description = "ID NUMERICO de una categoria del catalogo (ver GET /catalogs/categories)")
        @NotNull(message = "Debe seleccionar una categoria")
        @Positive
        Long categoryId

) {}

package com.qalikay.backend.recipe.dto;

import com.qalikay.backend.recipe.domain.Categoria;
import com.qalikay.backend.recipe.domain.Receta;
import com.qalikay.backend.user.domain.PerfilExperto;
import com.qalikay.backend.user.domain.Usuario;
import com.qalikay.backend.user.repository.PerfilExpertoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Convierte entidades Receta y Categoria a DTOs publicos.
 */
@Component
@RequiredArgsConstructor
public class RecipeMapper {

    private final PerfilExpertoRepository perfilExpertoRepository;

    public RecipeResponse toResponse(Receta receta) {
        return RecipeResponse.builder()
                .id(receta.getId())
                .title(receta.getTitulo())
                .shortDescription(receta.getDescripcionCorta())
                .ingredients(receta.getIngredientes())
                .preparation(receta.getPreparacion())
                .usage(receta.getModoUso())
                .warnings(receta.getPrecauciones())
                .imageUrl(receta.getImagenUrl())
                .price(receta.getPrecio())
                .preparationMinutes(receta.getDuracionPreparacionMin())
                .status(receta.getEstado().name())
                .views(receta.getVistas())
                .publishedAt(receta.getFechaPublicacion())
                .createdAt(receta.getCreatedAt())
                .updatedAt(receta.getUpdatedAt())
                .author(toAuthor(receta.getExperto()))
                .category(toCategory(receta.getCategoria()))
                .build();
    }

    public RecipeSummary toSummary(Receta receta) {
        Usuario experto = receta.getExperto();
        return RecipeSummary.builder()
                .id(receta.getId())
                .title(receta.getTitulo())
                .shortDescription(receta.getDescripcionCorta())
                .imageUrl(receta.getImagenUrl())
                .price(receta.getPrecio())
                .categoryName(receta.getCategoria().getNombre())
                .authorFullName(experto.getNombre() + " " + experto.getApellido())
                .views(receta.getVistas())
                .status(receta.getEstado().name())
                .build();
    }

    public CategoryResponse toCategoryResponse(Categoria categoria) {
        return CategoryResponse.builder()
                .id(categoria.getId())
                .name(categoria.getNombre())
                .description(categoria.getDescripcion())
                .icon(categoria.getIcono())
                .build();
    }

    private RecipeResponse.AuthorDto toAuthor(Usuario experto) {
        String specialty = perfilExpertoRepository.findByUsuarioId(experto.getId())
                .map(PerfilExperto::getEspecialidad)
                .map(esp -> esp.getNombre())
                .orElse(null);

        return RecipeResponse.AuthorDto.builder()
                .id(experto.getId())
                .firstName(experto.getNombre())
                .lastName(experto.getApellido())
                .specialty(specialty)
                .build();
    }

    private RecipeResponse.CategoryDto toCategory(Categoria categoria) {
        return RecipeResponse.CategoryDto.builder()
                .id(categoria.getId())
                .name(categoria.getNombre())
                .icon(categoria.getIcono())
                .build();
    }
}

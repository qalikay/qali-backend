package com.qalikay.backend.recipe.controller;

import com.qalikay.backend.recipe.dto.RecipeRequest;
import com.qalikay.backend.recipe.dto.RecipeResponse;
import com.qalikay.backend.recipe.dto.RecipeSummary;
import com.qalikay.backend.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Endpoints de recetas (US05, US07, US08).
 *
 * Estructura:
 *  - Endpoints publicos (GET /recipes, GET /recipes/{id}) -> sin autenticacion
 *  - Endpoints de gestion (POST/PUT/DELETE/publicar/archivar) -> solo EXPERTO autor
 *  - Endpoint /me/recipes -> mis recetas (todos los estados)
 */
@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Tag(name = "Recetas", description = "Gestion y consulta de recetas de medicina natural")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "Listar recetas publicadas (busqueda paginada)",
            description = """
                    Devuelve recetas en estado PUBLICADA con filtros opcionales:
                      - categoryId: filtra por categoria
                      - q: texto para buscar en titulo o descripcion
                      - minPrice / maxPrice: rango de precio
                      - page, size, sort: paginacion estandar de Spring Data""")
    public ResponseEntity<Page<RecipeSummary>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(recipeService.searchPublished(categoryId, q, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver detalle de receta publicada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receta encontrada"),
            @ApiResponse(responseCode = "404", description = "Receta no existe o no esta publicada")
    })
    public ResponseEntity<RecipeResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getPublishedById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear una receta (solo EXPERTO)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Receta creada en estado BORRADOR"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "403", description = "El usuario no tiene rol EXPERTO")
    })
    public ResponseEntity<RecipeResponse> create(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody RecipeRequest request
    ) {
        RecipeResponse created = recipeService.create(principal.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar receta propia")
    public ResponseEntity<RecipeResponse> update(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest request
    ) {
        return ResponseEntity.ok(recipeService.update(principal.getUsername(), id, request));
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Publicar receta propia (la hace visible a clientes)")
    public ResponseEntity<RecipeResponse> publish(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(recipeService.publish(principal.getUsername(), id));
    }

    @PostMapping("/{id}/archive")
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Archivar receta propia (deja de ser visible)")
    public ResponseEntity<RecipeResponse> archive(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(recipeService.archive(principal.getUsername(), id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Eliminar receta propia")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        recipeService.delete(principal.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Ver detalle completo de una receta propia (incluye borradores)")
    public ResponseEntity<RecipeResponse> getOwn(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(recipeService.getOwnById(principal.getUsername(), id));
    }
}

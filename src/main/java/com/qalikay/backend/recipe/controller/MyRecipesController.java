package com.qalikay.backend.recipe.controller;

import com.qalikay.backend.recipe.dto.RecipeSummary;
import com.qalikay.backend.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints del experto autenticado para gestionar SUS propias recetas
 * (incluyendo borradores y archivadas).
 */
@RestController
@RequestMapping("/me/recipes")
@RequiredArgsConstructor
@Tag(name = "Mis Recetas", description = "Gestion de recetas del experto autenticado")
@SecurityRequirement(name = "bearerAuth")
public class MyRecipesController {

    private final RecipeService recipeService;

    @GetMapping
    @PreAuthorize("hasRole('EXPERTO')")
    @Operation(summary = "Listar mis recetas (todos los estados)")
    public ResponseEntity<Page<RecipeSummary>> list(
            @AuthenticationPrincipal UserDetails principal,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(recipeService.listMyRecipes(principal.getUsername(), pageable));
    }
}

package com.qalikay.backend.recipe.controller;

import com.qalikay.backend.recipe.dto.CategoryResponse;
import com.qalikay.backend.recipe.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalogs/categories")
@RequiredArgsConstructor
@Tag(name = "Catalogos", description = "Listas de referencia: roles, especialidades, etc.")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Listar categorias de recetas e insumos")
    public ResponseEntity<List<CategoryResponse>> list() {
        return ResponseEntity.ok(categoryService.findAll());
    }
}

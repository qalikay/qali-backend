package com.qalikay.backend.product.controller;

import com.qalikay.backend.product.dto.ProductSummary;
import com.qalikay.backend.product.service.ProductService;
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

@RestController
@RequestMapping("/me/products")
@RequiredArgsConstructor
@Tag(name = "Mis Insumos", description = "Insumos del experto autenticado")
@SecurityRequirement(name = "bearerAuth")
public class MyProductsController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('EXPERTO')")
    @Operation(summary = "Listar mis insumos (incluye descontinuados)")
    public ResponseEntity<Page<ProductSummary>> list(
            @AuthenticationPrincipal UserDetails principal,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(productService.listMyProducts(principal.getUsername(), pageable));
    }
}

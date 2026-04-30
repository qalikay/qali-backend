package com.qalikay.backend.product.controller;

import com.qalikay.backend.product.dto.ProductRequest;
import com.qalikay.backend.product.dto.ProductResponse;
import com.qalikay.backend.product.dto.ProductSummary;
import com.qalikay.backend.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Insumos", description = "Gestion y consulta de insumos naturales")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Listar insumos disponibles (paginado, con filtros)")
    public ResponseEntity<Page<ProductSummary>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(productService.search(categoryId, type, q, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle publico de un insumo")
    public ResponseEntity<ProductResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear insumo (solo EXPERTO)")
    public ResponseEntity<ProductResponse> create(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody ProductRequest request
    ) {
        ProductResponse created = productService.create(principal.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar insumo propio")
    public ResponseEntity<ProductResponse> update(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(productService.update(principal.getUsername(), id, request));
    }

    @PostMapping("/{id}/discontinue")
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Descontinuar insumo (deja de venderse)")
    public ResponseEntity<ProductResponse> discontinue(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.discontinue(principal.getUsername(), id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Eliminar insumo propio")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        productService.delete(principal.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}

package com.qalikay.backend.review.controller;

import com.qalikay.backend.review.dto.ReviewRequest;
import com.qalikay.backend.review.dto.ReviewResponse;
import com.qalikay.backend.review.dto.ReviewSummary;
import com.qalikay.backend.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de resenas.
 * El path incluye {type} (RECETA o INSUMO) + {refId} para reusar el
 * mismo controller en ambos tipos de item.
 */
@RestController
@RequestMapping("/reviews/{type}/{refId}")
@RequiredArgsConstructor
@Tag(name = "Resenas", description = "Calificaciones y comentarios sobre recetas e insumos")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "Listar resenas publicas de un item")
    public ResponseEntity<Page<ReviewResponse>> list(
            @PathVariable String type,
            @PathVariable Long refId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.listForItem(type, refId, pageable));
    }

    @GetMapping("/summary")
    @Operation(summary = "Resumen agregado: promedio y total")
    public ResponseEntity<ReviewSummary> summary(
            @PathVariable String type,
            @PathVariable Long refId
    ) {
        return ResponseEntity.ok(reviewService.summaryForItem(type, refId));
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear o actualizar mi resena (solo si compre el item)")
    public ResponseEntity<ReviewResponse> createOrUpdate(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable String type,
            @PathVariable Long refId,
            @Valid @RequestBody ReviewRequest request
    ) {
        return ResponseEntity.ok(
                reviewService.createOrUpdate(principal.getUsername(), type, refId, request)
        );
    }
}

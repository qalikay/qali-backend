package com.qalikay.backend.transaction.controller;

import com.qalikay.backend.transaction.dto.PurchaseRequest;
import com.qalikay.backend.transaction.dto.TransactionResponse;
import com.qalikay.backend.transaction.service.TransactionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transacciones", description = "Compras de recetas e insumos")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Realizar una compra (uno o varios items)")
    public ResponseEntity<TransactionResponse> purchase(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody PurchaseRequest request
    ) {
        TransactionResponse response = transactionService.purchase(principal.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de una transaccion propia")
    public ResponseEntity<TransactionResponse> getById(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(transactionService.getById(principal.getUsername(), id));
    }

    @GetMapping("/me")
    @Operation(summary = "Mis compras (paginado)")
    public ResponseEntity<Page<TransactionResponse>> listMyPurchases(
            @AuthenticationPrincipal UserDetails principal,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(transactionService.listMyPurchases(principal.getUsername(), pageable));
    }
}

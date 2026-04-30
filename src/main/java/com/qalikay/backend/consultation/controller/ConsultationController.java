package com.qalikay.backend.consultation.controller;

import com.qalikay.backend.consultation.dto.ConsultationCreateRequest;
import com.qalikay.backend.consultation.dto.ConsultationResponse;
import com.qalikay.backend.consultation.dto.ConsultationSummary;
import com.qalikay.backend.consultation.dto.MessageRequest;
import com.qalikay.backend.consultation.service.ConsultationService;
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
@RequestMapping("/consultations")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Consultas privadas y mensajeria entre cliente y experto")
@SecurityRequirement(name = "bearerAuth")
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Crear nueva consulta a un experto (solo CLIENTE)")
    public ResponseEntity<ConsultationResponse> create(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody ConsultationCreateRequest request
    ) {
        ConsultationResponse created = consultationService.create(principal.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver detalle de una consulta donde participo")
    public ResponseEntity<ConsultationResponse> getDetail(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(consultationService.getById(principal.getUsername(), id));
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('EXPERTO')")
    @Operation(summary = "Aceptar consulta (solo el experto destinatario)")
    public ResponseEntity<ConsultationResponse> accept(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(consultationService.accept(principal.getUsername(), id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('EXPERTO')")
    @Operation(summary = "Rechazar consulta")
    public ResponseEntity<ConsultationResponse> reject(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(consultationService.reject(principal.getUsername(), id));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Marcar consulta como completada")
    public ResponseEntity<ConsultationResponse> complete(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(consultationService.complete(principal.getUsername(), id));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancelar consulta")
    public ResponseEntity<ConsultationResponse> cancel(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(consultationService.cancel(principal.getUsername(), id));
    }

    @PostMapping("/{id}/messages")
    @Operation(summary = "Enviar mensaje en una consulta aceptada")
    public ResponseEntity<ConsultationResponse> sendMessage(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id,
            @Valid @RequestBody MessageRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultationService.sendMessage(principal.getUsername(), id, request));
    }

    @GetMapping("/me/as-client")
    @Operation(summary = "Mis consultas como cliente")
    public ResponseEntity<Page<ConsultationSummary>> listAsClient(
            @AuthenticationPrincipal UserDetails principal,
            @PageableDefault(size = 12, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(consultationService.listAsClient(principal.getUsername(), pageable));
    }

    @GetMapping("/me/as-expert")
    @PreAuthorize("hasRole('EXPERTO')")
    @Operation(summary = "Mis consultas como experto")
    public ResponseEntity<Page<ConsultationSummary>> listAsExpert(
            @AuthenticationPrincipal UserDetails principal,
            @PageableDefault(size = 12, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(consultationService.listAsExpert(principal.getUsername(), pageable));
    }
}

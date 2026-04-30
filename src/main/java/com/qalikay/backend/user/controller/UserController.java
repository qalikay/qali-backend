package com.qalikay.backend.user.controller;

import com.qalikay.backend.user.dto.ChangePasswordRequest;
import com.qalikay.backend.user.dto.UpdateUserRequest;
import com.qalikay.backend.user.dto.UserResponse;
import com.qalikay.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints del usuario autenticado (US04).
 *
 * Todos requieren un JWT valido en el header "Authorization: Bearer <token>".
 * Mediante @AuthenticationPrincipal recibimos los UserDetails inyectados
 * por el JwtAuthenticationFilter.
 */
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Operaciones del usuario autenticado")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Obtener mi perfil",
            description = "Devuelve los datos del usuario autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil obtenido"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(userService.getByEmail(principal.getUsername()));
    }

    @PutMapping
    @Operation(summary = "Actualizar mi perfil",
            description = "Permite editar nombre, apellido, telefono y foto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<UserResponse> updateMyProfile(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UserResponse current = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(userService.updateProfile(current.id(), request));
    }

    @PutMapping("/password")
    @Operation(summary = "Cambiar mi password",
            description = "Requiere reingresar el password actual por seguridad")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password cambiado"),
            @ApiResponse(responseCode = "400", description = "Password actual incorrecto o nuevo invalido"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        UserResponse current = userService.getByEmail(principal.getUsername());
        userService.changePassword(current.id(), request);
        return ResponseEntity.noContent().build();
    }
}

package com.qalikay.backend.user.controller;

import com.qalikay.backend.user.dto.AuthResponse;
import com.qalikay.backend.user.dto.LoginRequest;
import com.qalikay.backend.user.dto.RefreshTokenRequest;
import com.qalikay.backend.user.dto.RegisterClienteRequest;
import com.qalikay.backend.user.dto.RegisterExpertoRequest;
import com.qalikay.backend.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints publicos de autenticacion.
 *
 * - POST /auth/register/cliente   Registro de nuevo cliente (US02)
 * - POST /auth/register/experto   Registro de nuevo experto (US03)
 * - POST /auth/login              Inicio de sesion (US01)
 * - POST /auth/refresh            Renovacion del access token (US14)
 *
 * Todos devuelven AuthResponse con el par de tokens y los datos del usuario.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Registro, login y refresh de tokens JWT")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/cliente")
    @Operation(summary = "Registrar un nuevo cliente",
            description = "Crea una cuenta con rol CLIENTE y devuelve un par de tokens JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "El correo ya esta registrado")
    })
    public ResponseEntity<AuthResponse> registerCliente(@Valid @RequestBody RegisterClienteRequest request) {
        AuthResponse response = authService.registerCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/experto")
    @Operation(summary = "Registrar un nuevo experto",
            description = "Crea una cuenta con rol EXPERTO. Queda pendiente de verificacion por un admin.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Experto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
            @ApiResponse(responseCode = "409", description = "El correo ya esta registrado")
    })
    public ResponseEntity<AuthResponse> registerExperto(@Valid @RequestBody RegisterExpertoRequest request) {
        AuthResponse response = authService.registerExperto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion",
            description = "Valida credenciales y devuelve el par de tokens JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar el access token",
            description = "Recibe un refresh token y devuelve un nuevo par de tokens")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens renovados"),
            @ApiResponse(responseCode = "401", description = "Refresh token invalido o expirado")
    })
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}

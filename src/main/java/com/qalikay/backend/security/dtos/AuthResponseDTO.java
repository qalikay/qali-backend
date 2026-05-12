package com.qalikay.backend.security.dtos;

import lombok.Data;

import java.util.Set;

// Respuesta de /api/authenticate. El frontend guarda jwt y lo envia en Authorization: Bearer <jwt>.
@Data
public class AuthResponseDTO {
    private String jwt;             // Token firmado HS512
    private String username;
    private Set<String> roles;      // Ej: ["ROLE_ADMIN"] -- util para mostrar/ocultar menus en el frontend
}

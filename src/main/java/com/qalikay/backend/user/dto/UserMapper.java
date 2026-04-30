package com.qalikay.backend.user.dto;

import com.qalikay.backend.user.domain.Rol;
import com.qalikay.backend.user.domain.Usuario;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Convierte entidades de la BD a DTOs publicos.
 *
 * Mantiene el principio de no exponer detalles internos (ej. passwordHash)
 * al cliente.
 *
 * Cuando agreguemos varios mappers consideraremos MapStruct, pero para
 * este alcance simple un mapper manual es suficiente y mas claro.
 */
@Component
public class UserMapper {

    public UserResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UserResponse.builder()
                .id(usuario.getId())
                .firstName(usuario.getNombre())
                .lastName(usuario.getApellido())
                .email(usuario.getCorreo())
                .phone(usuario.getTelefono())
                .photoUrl(usuario.getFotoUrl())
                .emailVerified(usuario.getEmailVerificado())
                .roles(usuario.getRoles().stream()
                        .map(Rol::getNombre)
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .build();
    }
}

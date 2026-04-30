package com.qalikay.backend.user.service;

import com.qalikay.backend.shared.exception.BusinessException;
import com.qalikay.backend.shared.exception.ResourceNotFoundException;
import com.qalikay.backend.user.domain.Usuario;
import com.qalikay.backend.user.dto.ChangePasswordRequest;
import com.qalikay.backend.user.dto.UpdateUserRequest;
import com.qalikay.backend.user.dto.UserMapper;
import com.qalikay.backend.user.dto.UserResponse;
import com.qalikay.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio con operaciones sobre el usuario autenticado.
 *
 * Maneja:
 * - Consulta de perfil propio
 * - Actualizacion de datos basicos
 * - Cambio de password con verificacion del actual
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsuarioRepository usuarioRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return userMapper.toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + email));
        return userMapper.toResponse(usuario);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateUserRequest request) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));

        if (request.firstName() != null && !request.firstName().isBlank()) {
            usuario.setNombre(request.firstName());
        }
        if (request.lastName() != null && !request.lastName().isBlank()) {
            usuario.setApellido(request.lastName());
        }
        if (request.phone() != null) {
            usuario.setTelefono(request.phone());
        }
        if (request.photoUrl() != null) {
            usuario.setFotoUrl(request.photoUrl());
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        log.info("Usuario {} actualizo su perfil", userId);
        return userMapper.toResponse(actualizado);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));

        if (!passwordEncoder.matches(request.currentPassword(), usuario.getPasswordHash())) {
            throw new BusinessException("El password actual es incorrecto");
        }

        if (passwordEncoder.matches(request.newPassword(), usuario.getPasswordHash())) {
            throw new BusinessException("El nuevo password debe ser distinto al actual");
        }

        usuario.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        usuarioRepository.save(usuario);
        log.info("Usuario {} cambio su password", userId);
    }
}

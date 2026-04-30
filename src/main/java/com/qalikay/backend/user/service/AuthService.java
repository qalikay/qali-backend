package com.qalikay.backend.user.service;

import com.qalikay.backend.security.jwt.JwtService;
import com.qalikay.backend.shared.exception.BusinessException;
import com.qalikay.backend.shared.exception.DuplicateResourceException;
import com.qalikay.backend.shared.exception.ResourceNotFoundException;
import com.qalikay.backend.user.domain.Especialidad;
import com.qalikay.backend.user.domain.PerfilExperto;
import com.qalikay.backend.user.domain.Rol;
import com.qalikay.backend.user.domain.RolNombre;
import com.qalikay.backend.user.domain.Usuario;
import com.qalikay.backend.user.dto.AuthResponse;
import com.qalikay.backend.user.dto.LoginRequest;
import com.qalikay.backend.user.dto.RefreshTokenRequest;
import com.qalikay.backend.user.dto.RegisterClienteRequest;
import com.qalikay.backend.user.dto.RegisterExpertoRequest;
import com.qalikay.backend.user.dto.UserMapper;
import com.qalikay.backend.user.dto.UserResponse;
import com.qalikay.backend.user.repository.EspecialidadRepository;
import com.qalikay.backend.user.repository.PerfilExpertoRepository;
import com.qalikay.backend.user.repository.RolRepository;
import com.qalikay.backend.user.repository.UsuarioRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Servicio de autenticacion: registro, login y refresh.
 *
 * Reglas de negocio cubiertas:
 *  - US02: Cliente se registra con datos basicos -> obtiene rol CLIENTE
 *  - US03: Experto se registra con especialidad -> obtiene rol EXPERTO
 *          (queda con verificado=false hasta aprobacion del admin)
 *  - Login: valida credenciales y devuelve par (accessToken, refreshToken)
 *  - Refresh: emite un nuevo accessToken usando un refresh valido
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EspecialidadRepository especialidadRepository;
    private final PerfilExpertoRepository perfilExpertoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse registerCliente(RegisterClienteRequest request) {
        validateEmailIsAvailable(request.email());

        Rol rolCliente = rolRepository.findByNombre(RolNombre.CLIENTE)
                .orElseThrow(() -> new ResourceNotFoundException("Rol CLIENTE no inicializado"));

        Usuario usuario = Usuario.builder()
                .nombre(request.firstName())
                .apellido(request.lastName())
                .correo(request.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .telefono(request.phone())
                .emailVerificado(false)
                .roles(new HashSet<>(Set.of(rolCliente)))
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Cliente registrado: id={} email={}", guardado.getId(), guardado.getCorreo());
        return buildAuthResponse(guardado);
    }

    @Transactional
    public AuthResponse registerExperto(RegisterExpertoRequest request) {
        validateEmailIsAvailable(request.email());

        Rol rolExperto = rolRepository.findByNombre(RolNombre.EXPERTO)
                .orElseThrow(() -> new ResourceNotFoundException("Rol EXPERTO no inicializado"));

        Especialidad especialidad = especialidadRepository.findById(request.specialtyId())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", request.specialtyId()));

        Usuario usuario = Usuario.builder()
                .nombre(request.firstName())
                .apellido(request.lastName())
                .correo(request.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .telefono(request.phone())
                .emailVerificado(false)
                .roles(new HashSet<>(Set.of(rolExperto)))
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        PerfilExperto perfil = PerfilExperto.builder()
                .usuario(usuarioGuardado)
                .especialidad(especialidad)
                .trayectoria(request.trajectory())
                .biografia(request.biography())
                .aniosExperiencia(request.yearsOfExperience())
                .verificado(false)
                .build();

        perfilExpertoRepository.save(perfil);
        log.info("Experto registrado: id={} email={} especialidad={}",
                usuarioGuardado.getId(), usuarioGuardado.getCorreo(), especialidad.getNombre());

        return buildAuthResponse(usuarioGuardado);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email().toLowerCase(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BusinessException("Credenciales invalidas",
                    org.springframework.http.HttpStatus.UNAUTHORIZED);
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new BusinessException("No fue posible iniciar sesion: " + ex.getMessage(),
                    org.springframework.http.HttpStatus.UNAUTHORIZED);
        }

        Usuario usuario = usuarioRepository.findByCorreoWithRoles(request.email().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + request.email()));
        return buildAuthResponse(usuario);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        try {
            if (!jwtService.isRefreshToken(refreshToken)) {
                throw new BusinessException("El token enviado no es un refresh token",
                        org.springframework.http.HttpStatus.UNAUTHORIZED);
            }
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                throw new BusinessException("Refresh token invalido o expirado",
                        org.springframework.http.HttpStatus.UNAUTHORIZED);
            }

            Usuario usuario = usuarioRepository.findByCorreoWithRoles(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + username));
            return buildAuthResponse(usuario);

        } catch (JwtException | IllegalArgumentException ex) {
            throw new BusinessException("Refresh token invalido o expirado",
                    org.springframework.http.HttpStatus.UNAUTHORIZED);
        }
    }

    private void validateEmailIsAvailable(String email) {
        if (usuarioRepository.existsByCorreo(email.toLowerCase())) {
            throw new DuplicateResourceException("El correo " + email + " ya esta registrado");
        }
    }

    private AuthResponse buildAuthResponse(Usuario usuario) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        UserResponse userResponse = userMapper.toResponse(usuario);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationSeconds())
                .user(userResponse)
                .build();
    }
}

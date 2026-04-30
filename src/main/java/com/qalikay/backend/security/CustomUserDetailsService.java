package com.qalikay.backend.security;

import com.qalikay.backend.user.domain.Usuario;
import com.qalikay.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapta nuestra entidad Usuario al modelo UserDetails de Spring Security.
 *
 * Spring Security llama a este servicio durante el login (AuthenticationManager)
 * y durante la validacion del JWT en cada request protegido.
 *
 * Convencion: los roles se exponen como "ROLE_<NOMBRE>" para que las
 * anotaciones @PreAuthorize("hasRole('CLIENTE')") funcionen.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreoWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        Set<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre().name()))
                .collect(Collectors.toSet());

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPasswordHash())
                .authorities(authorities)
                .accountLocked(!Boolean.TRUE.equals(usuario.getActive()))
                .disabled(!Boolean.TRUE.equals(usuario.getActive()))
                .build();
    }
}

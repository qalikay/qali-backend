package com.qalikay.backend.security.config;

import com.qalikay.backend.security.filters.JwtRequestFilter;
import com.qalikay.backend.security.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuracion central de Spring Security:
 *  - Define que rutas son publicas y cuales requieren JWT
 *  - Registra el filtro que valida el JWT en cada request
 *  - Configura BCrypt como algoritmo de hash de passwords
 *  - Habilita CORS para que el frontend Angular pueda llamar al API
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)     // Habilita @PreAuthorize en los controllers
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // Bean que el AuthController usa para validar usuario+password en /api/authenticate
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // BCrypt: hash unidireccional. Se usa para guardar y comparar passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cadena de filtros HTTP: define el comportamiento de seguridad por ruta
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                                              // CSRF off: usamos JWT (stateless), no cookies
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))    // para H2 console
                .authorizeHttpRequests(auth -> auth
                        // Endpoints publicos (sin token)
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers("/api/registro/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // Lecturas publicas (GET): catalogos, recetas, productos, especialidades
                        .requestMatchers(HttpMethod.GET, "/api/categorias", "/api/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/especialidades", "/api/especialidades/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/recetas", "/api/recetas/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/insumos", "/api/insumos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/expertos", "/api/expertos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/resenas").permitAll()
                        // El resto requiere JWT valido (y rol especifico via @PreAuthorize)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)                     // No HttpSession: cada request valida su JWT
                );

        // Inserta el filtro JWT ANTES del filtro estandar de usuario+password
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // CORS: permite que el frontend (ip.frontend en application.properties) llame a /api/**
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter(@Value("${ip.frontend}") String frontendUrl) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(frontendUrl);        // Solo el origen del frontend (no "*")
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");    // Permite leer el header Authorization desde JS

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

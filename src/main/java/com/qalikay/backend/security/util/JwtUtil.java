package com.qalikay.backend.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Clase encargada de generar y validar tokens JWT.
 * Un JWT tiene 3 partes (header.payload.signature) y se firma con HS512 + jwt.secret.
 */
@Component
public class JwtUtil {

    // Clave secreta inyectada desde application.properties (jwt.secret en base64).
    @Value("${jwt.secret}")
    private String secretKey;

    // Extrae el username (campo "sub" del JWT)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic helper: extrae cualquier claim aplicando una funcion al objeto Claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parsea el token y verifica la firma usando secretKey
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Crea un JWT a partir de los datos del usuario autenticado
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Llenando el payload con info util para el frontend
        claims.put("username", userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList()));
        return createToken(claims, userDetails.getUsername());
    }

    // Construye el JWT con iat, exp y lo firma con HS512
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)                                                  // "sub" del JWT
                .setIssuedAt(new Date(System.currentTimeMillis()))                    // "iat"
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 4))  // "exp": 4 horas
                .signWith(SignatureAlgorithm.HS512, secretKey)                        // Firma HMAC-SHA512
                .compact();
    }

    // Token valido = pertenece al usuario y no expiro
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

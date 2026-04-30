package com.qalikay.backend.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Propiedades JWT tipadas, cargadas desde application.properties.
 *
 * Bind automatico con prefijo "qalikay.security.jwt".
 * Ejemplo: qalikay.security.jwt.secret -> this.secret
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "qalikay.security.jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenExpirationMs;
    private long refreshTokenExpirationMs;
    private String issuer;
}

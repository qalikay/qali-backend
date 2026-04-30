package com.qalikay.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuracion global de JPA.
 *
 * Activa la auditoria automatica de fechas (@CreatedDate, @LastModifiedDate)
 * en todas las entidades que extiendan de BaseEntity.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}

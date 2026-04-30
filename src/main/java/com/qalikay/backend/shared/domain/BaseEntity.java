package com.qalikay.backend.shared.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Clase base para todas las entidades del sistema.
 *
 * Provee campos comunes de auditoria que toda tabla profesional debe tener:
 * - createdAt: fecha de creacion (autollenado por JPA)
 * - updatedAt: ultima modificacion (autollenado por JPA)
 * - active: bandera de borrado logico (soft delete)
 * - version: control de concurrencia optimista
 *
 * Las entidades heredan de esta clase con extends BaseEntity.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "version")
    private Long version;
}

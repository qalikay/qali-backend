package com.qalikay.backend.user.domain;

import com.qalikay.backend.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad central que representa a cualquier usuario de la plataforma.
 *
 * Un mismo usuario puede tener varios roles simultaneos:
 * por ejemplo CLIENTE + EXPERTO (un experto que tambien compra recetas).
 *
 * El password se guarda SIEMPRE hasheado con BCrypt en passwordHash.
 * Nunca se debe guardar texto plano.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "usuario",
        indexes = {
                @Index(name = "idx_usuario_correo", columnList = "correo", unique = true)
        }
)
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 80)
    private String apellido;

    @Column(name = "correo", nullable = false, unique = true, length = 120)
    private String correo;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "email_verificado", nullable = false)
    @Builder.Default
    private Boolean emailVerificado = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();

    /** Helper para agregar un rol sin manipular el Set directamente. */
    public void agregarRol(Rol rol) {
        this.roles.add(rol);
    }

    /** Helper para verificar si el usuario tiene un rol especifico. */
    public boolean tieneRol(RolNombre nombre) {
        return roles.stream().anyMatch(r -> r.getNombre() == nombre);
    }
}

package com.qalikay.backend.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

// Entidad de credenciales. Cada User tiene asociado un Cliente O un Experto (perfil).
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)         // username unico en la BD
    private String username;

    @JsonIgnore                                       // Nunca expongas el hash de password en la API
    @Column(nullable = false)
    private String password;                          // Almacenado con BCrypt, NUNCA en texto plano

    // Un usuario puede tener varios roles (ej. ROLE_ADMIN + ROLE_CLIENTE).
    // EAGER: se cargan junto con el User (Spring Security los necesita en el login).
    // JoinTable: tabla intermedia user_roles(user_id, role_id).
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}

package com.qalikay.backend.security.entities;

import jakarta.persistence.*;
import lombok.*;

// Rol de Spring Security. Valores: ROLE_ADMIN, ROLE_CLIENTE, ROLE_EXPERTO.
// El prefijo "ROLE_" es importante: @PreAuthorize("hasRole('CLIENTE')") lo agrega solo.
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}

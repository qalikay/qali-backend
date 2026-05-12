package com.qalikay.backend.entities;

import com.qalikay.backend.security.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Perfil de experto (curandero/herbolario). Publica recetas e insumos y responde consultas.
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expertos")
public class Experto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    private String telefono;

    @Column(length = 1000)                  // VARCHAR(1000) para texto biografico largo
    private String trayectoria;

    private Integer anosExperiencia;

    // Relacion N-a-1: muchos expertos pueden tener la misma especialidad
    @ManyToOne
    @JoinColumn(name = "especialidad_id")   // FK = expertos.especialidad_id
    private Especialidad especialidad;

    // Relacion 1-a-1 con User (credenciales)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}

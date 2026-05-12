package com.qalikay.backend.entities;

import com.qalikay.backend.security.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(length = 1000)
    private String trayectoria;

    private Integer anosExperiencia;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}

package com.qalikay.backend.user.domain;

import com.qalikay.backend.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Catalogo de especialidades disponibles para los expertos.
 *
 * Ejemplos: Herbolaria Andina, Fitoterapia, Aromaterapia,
 * Medicina Tradicional Amazonica, Naturopatia, etc.
 *
 * Esto reemplaza el campo "especialidad" como string libre que tenia
 * el modelo original, evitando duplicados con tildes/mayusculas distintas.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "especialidad")
public class Especialidad extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;
}

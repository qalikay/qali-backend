package com.qalikay.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Categoria a la que pertenecen recetas e insumos (Infusiones, Pomadas, Jarabes, etc.)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity                          // Marca la clase como entidad JPA -> tabla en la BD
@Table(name = "categorias")      // Nombre exacto de la tabla en PostgreSQL
public class Categoria {

    @Id                                                  // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental delegado a la BD
    private Long id;

    @Column(nullable = false, unique = true)             // NOT NULL + UNIQUE en BD
    private String nombre;

    @Column(length = 500)                                // VARCHAR(500)
    private String descripcion;
}

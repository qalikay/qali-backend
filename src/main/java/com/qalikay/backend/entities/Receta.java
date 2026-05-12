package com.qalikay.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// Receta natural creada por un Experto. Solo las PUBLICADA se muestran al publico.
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 4000)                       // Texto largo para receta completa
    private String ingredientes;

    @Column(length = 4000)
    private String preparacion;

    @Column(length = 1000)
    private String advertencias;

    private Integer minutosPreparacion;
    private Double precio;
    private String imagenUrl;

    /** "BORRADOR" o "PUBLICADA" */
    @Column(nullable = false)
    private String estado = "BORRADOR";          // Valor por defecto cuando se crea

    private LocalDate fechaCreacion = LocalDate.now();

    // N-a-1: una receta pertenece a una categoria; una categoria tiene varias recetas
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // N-a-1: cada receta es autoria de un experto
    @ManyToOne
    @JoinColumn(name = "experto_id")
    private Experto experto;
}

package com.qalikay.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    @Column(length = 800)
    private String descripcion;

    @Column(length = 4000)
    private String ingredientes;

    @Column(length = 4000)
    private String preparacion;

    @Column(length = 1500)
    private String advertencias;

    private Integer minutosPreparacion;
    private Double precio;
    private String imagenUrl;

    /** "BORRADOR" o "PUBLICADA" */
    @Column(nullable = false)
    private String estado = "BORRADOR";

    private LocalDate fechaCreacion = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "experto_id")
    private Experto experto;
}

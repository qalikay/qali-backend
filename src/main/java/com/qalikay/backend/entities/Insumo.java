package com.qalikay.backend.entities;

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
@Table(name = "insumos")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    private Double precio;
    private Integer stock;
    private String unidad;

    /** "HIERBA", "ACEITE", "EXTRACTO", "POLVO", "OTRO" */
    private String tipo;

    private String imagenUrl;

    /** "DISPONIBLE", "AGOTADO" */
    @Column(nullable = false)
    private String estado = "DISPONIBLE";

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "experto_id")
    private Experto experto;
}

package com.qalikay.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "detalle_orden")
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** "RECETA" o "INSUMO" */
    @Column(nullable = false)
    private String tipoItem;

    /** id de la receta o insumo segun tipoItem */
    @Column(nullable = false)
    private Long itemId;

    private String descripcion;

    private Integer cantidad;

    private Double precioUnitario;

    private Double subtotal;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden orden;
}

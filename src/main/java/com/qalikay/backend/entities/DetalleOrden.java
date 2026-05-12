package com.qalikay.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Linea de una Orden: representa una Receta o un Insumo comprado, con cantidad y subtotal.
// Usamos tipoItem + itemId (polimorfico) en vez de FK fijas para permitir comprar mezcla.
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
    private String tipoItem;                     // Discrimina el tipo de item

    /** id de la receta o insumo segun tipoItem */
    @Column(nullable = false)
    private Long itemId;                         // Apunta a recetas.id O insumos.id

    private String descripcion;                  // Nombre/titulo al momento de la compra (snapshot)

    private Integer cantidad;

    private Double precioUnitario;

    private Double subtotal;                     // cantidad * precioUnitario

    // BackReference: en el JSON, este lado de la relacion NO se serializa
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden orden;
}

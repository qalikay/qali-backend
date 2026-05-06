package com.qalikay.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha = LocalDateTime.now();

    private Double total;

    /** "PENDIENTE", "PAGADA", "CANCELADA" */
    @Column(nullable = false)
    private String estado = "PENDIENTE";

    /** "TARJETA", "YAPE", "PLIN", "EFECTIVO" */
    private String metodoPago;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalles = new ArrayList<>();
}

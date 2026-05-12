package com.qalikay.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 1 a 5 */
    @Column(nullable = false)
    private Integer calificacion;

    @Column(length = 1000)
    private String comentario;

    /** "RECETA", "INSUMO" o "EXPERTO" */
    @Column(nullable = false)
    private String tipoItem;

    @Column(nullable = false)
    private Long itemId;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}

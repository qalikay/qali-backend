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
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String asunto;

    /** "ABIERTA", "CERRADA" */
    @Column(nullable = false)
    private String estado = "ABIERTA";

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "experto_id")
    private Experto experto;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mensaje> mensajes = new ArrayList<>();
}

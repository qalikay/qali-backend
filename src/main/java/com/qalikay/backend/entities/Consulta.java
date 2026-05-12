package com.qalikay.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Hilo de conversacion entre un Cliente y un Experto. Contiene una lista de Mensajes.
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

    // N-a-1: muchas consultas pueden ser del mismo cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // N-a-1: las consultas se dirigen a un experto
    @ManyToOne
    @JoinColumn(name = "experto_id")
    private Experto experto;

    // 1-a-N: una consulta tiene varios mensajes. mappedBy = campo "consulta" en Mensaje.
    // CascadeType.ALL = al guardar/borrar la consulta se propaga a los mensajes.
    // orphanRemoval = si se quita un mensaje de la lista, se borra de la BD.
    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mensaje> mensajes = new ArrayList<>();
}

package com.qalikay.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Mensaje individual dentro de una Consulta (chat cliente-experto).
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String contenido;

    /** "CLIENTE" o "EXPERTO" - quien envio el mensaje */
    @Column(nullable = false)
    private String remitente;

    private LocalDateTime fechaEnvio = LocalDateTime.now();

    // Evita el bucle infinito al serializar (Consulta -> mensajes -> consulta -> ...)
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "consulta_id")            // FK a consultas.id
    private Consulta consulta;
}

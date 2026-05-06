package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecetaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String ingredientes;
    private String preparacion;
    private String advertencias;
    private Integer minutosPreparacion;
    private Double precio;
    private String imagenUrl;
    private String estado;
    private LocalDate fechaCreacion;
    private CategoriaDTO categoria;
    private ExpertoDTO experto;
}

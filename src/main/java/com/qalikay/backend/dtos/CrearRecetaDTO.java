package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrearRecetaDTO {
    private String titulo;
    private String descripcion;
    private String ingredientes;
    private String preparacion;
    private String advertencias;
    private Integer minutosPreparacion;
    private Double precio;
    private String imagenUrl;
    private Long categoriaId;
}

package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrearInsumoDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String unidad;
    private String tipo;
    private String imagenUrl;
    private Long categoriaId;
}

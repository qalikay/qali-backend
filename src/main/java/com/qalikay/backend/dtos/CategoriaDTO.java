package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}

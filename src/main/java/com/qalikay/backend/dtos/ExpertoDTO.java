package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExpertoDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String trayectoria;
    private Integer anosExperiencia;
    private EspecialidadDTO especialidad;
    private String username;
}

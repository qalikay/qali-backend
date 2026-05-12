package com.qalikay.backend.security.dtos;

import lombok.Data;

@Data
public class RegistroExpertoDTO {
    private String username;
    private String password;
    private String nombres;
    private String apellidos;
    private String telefono;
    private Long especialidadId;
    private String trayectoria;
    private Integer anosExperiencia;
}

package com.qalikay.backend.security.dtos;

import lombok.Data;

@Data
public class RegistroClienteDTO {
    private String username;
    private String password;
    private String nombres;
    private String apellidos;
    private String telefono;
}

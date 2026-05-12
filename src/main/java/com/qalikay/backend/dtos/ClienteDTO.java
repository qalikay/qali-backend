package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String username;
}

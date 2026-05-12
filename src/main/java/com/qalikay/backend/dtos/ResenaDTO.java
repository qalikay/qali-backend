package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResenaDTO {
    private Long id;
    private Integer calificacion;
    private String comentario;
    private String tipoItem;
    private Long itemId;
    private LocalDateTime fechaCreacion;
    private ClienteDTO cliente;
}

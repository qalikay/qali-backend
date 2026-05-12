package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrearResenaDTO {
    private Integer calificacion;
    private String comentario;
    private String tipoItem;
    private Long itemId;
}

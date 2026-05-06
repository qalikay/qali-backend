package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrdenDTO {
    private Long id;
    private String tipoItem;
    private Long itemId;
    private String descripcion;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}

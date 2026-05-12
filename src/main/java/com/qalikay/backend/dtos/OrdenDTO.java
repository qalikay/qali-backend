package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {
    private Long id;
    private LocalDateTime fecha;
    private Double total;
    private String estado;
    private String metodoPago;
    private ClienteDTO cliente;
    private List<DetalleOrdenDTO> detalles;
}

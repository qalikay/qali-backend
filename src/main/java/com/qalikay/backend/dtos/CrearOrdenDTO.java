package com.qalikay.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// Body que recibe POST /api/ordenes. El service calcula totales a partir de los detalles.
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrearOrdenDTO {
    private String metodoPago;                  // "TARJETA" | "YAPE" | "PLIN" | "EFECTIVO"
    private List<DetalleOrdenDTO> detalles;     // Items a comprar (recetas o insumos)
}

package com.qalikay.backend.services;

import com.qalikay.backend.dtos.CrearOrdenDTO;
import com.qalikay.backend.entities.Orden;

import java.util.List;

public interface OrdenService {
    Orden crearComoCliente(CrearOrdenDTO dto, String username);
    List<Orden> listarMisOrdenes(String username);
    Orden buscarPorId(Long id);
    Orden cambiarEstado(Long id, String estado);
    List<Orden> listarTodas();
}

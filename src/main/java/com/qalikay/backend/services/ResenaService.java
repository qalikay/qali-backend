package com.qalikay.backend.services;

import com.qalikay.backend.dtos.CrearResenaDTO;
import com.qalikay.backend.entities.Resena;

import java.util.List;

public interface ResenaService {
    List<Resena> listarPorItem(String tipoItem, Long itemId);
    List<Resena> listarMisResenas(String username);
    Resena crearComoCliente(CrearResenaDTO dto, String username);
    void eliminar(Long id, String username);
}

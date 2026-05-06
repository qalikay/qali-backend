package com.qalikay.backend.services;

import com.qalikay.backend.dtos.CrearInsumoDTO;
import com.qalikay.backend.entities.Insumo;

import java.util.List;

public interface InsumoService {
    List<Insumo> listar();
    List<Insumo> listarPorCategoria(Long categoriaId);
    List<Insumo> listarPorTipo(String tipo);
    List<Insumo> buscarPorNombre(String nombre);
    List<Insumo> listarPorExperto(Long expertoId);
    Insumo buscarPorId(Long id);
    Insumo crearComoExperto(CrearInsumoDTO dto, String username);
    Insumo modificarComoExperto(Long id, CrearInsumoDTO dto, String username);
    void eliminar(Long id, String username);
}

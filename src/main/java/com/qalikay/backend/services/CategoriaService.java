package com.qalikay.backend.services;

import com.qalikay.backend.entities.Categoria;

import java.util.List;

public interface CategoriaService {
    List<Categoria> listar();
    Categoria buscarPorId(Long id);
    Categoria insertar(Categoria categoria);
    Categoria modificar(Categoria categoria);
    void eliminar(Long id);
}

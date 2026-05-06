package com.qalikay.backend.services;

import com.qalikay.backend.dtos.CrearRecetaDTO;
import com.qalikay.backend.entities.Receta;

import java.util.List;

public interface RecetaService {
    List<Receta> listarPublicadas();
    List<Receta> listarPublicadasPorCategoria(Long categoriaId);
    List<Receta> buscarPublicadasPorTitulo(String titulo);
    List<Receta> listarTodas();
    List<Receta> listarPorExperto(Long expertoId);
    Receta buscarPorId(Long id);
    Receta crearComoExperto(CrearRecetaDTO dto, String username);
    Receta modificarComoExperto(Long id, CrearRecetaDTO dto, String username);
    Receta publicar(Long id, String username);
    Receta archivar(Long id, String username);
    void eliminar(Long id, String username);
}

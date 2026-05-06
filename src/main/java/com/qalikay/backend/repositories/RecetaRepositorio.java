package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaRepositorio extends JpaRepository<Receta, Long> {
    List<Receta> findByEstado(String estado);
    List<Receta> findByExpertoId(Long expertoId);
    List<Receta> findByCategoriaId(Long categoriaId);
    List<Receta> findByEstadoAndCategoriaId(String estado, Long categoriaId);
    List<Receta> findByEstadoAndTituloContainingIgnoreCase(String estado, String titulo);
}

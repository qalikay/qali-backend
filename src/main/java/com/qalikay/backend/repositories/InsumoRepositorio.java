package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsumoRepositorio extends JpaRepository<Insumo, Long> {
    List<Insumo> findByExpertoId(Long expertoId);
    List<Insumo> findByCategoriaId(Long categoriaId);
    List<Insumo> findByTipo(String tipo);
    List<Insumo> findByNombreContainingIgnoreCase(String nombre);
}

package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);
}

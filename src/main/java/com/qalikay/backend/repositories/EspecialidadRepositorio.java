package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadRepositorio extends JpaRepository<Especialidad, Long> {
    boolean existsByNombre(String nombre);
}

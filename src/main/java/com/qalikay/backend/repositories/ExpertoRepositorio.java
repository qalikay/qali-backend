package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Experto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpertoRepositorio extends JpaRepository<Experto, Long> {
    Optional<Experto> findByUserUsername(String username);
    List<Experto> findByEspecialidadId(Long especialidadId);
}

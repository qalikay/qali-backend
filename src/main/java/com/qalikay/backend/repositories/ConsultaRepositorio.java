package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepositorio extends JpaRepository<Consulta, Long> {
    List<Consulta> findByClienteId(Long clienteId);
    List<Consulta> findByExpertoId(Long expertoId);
}

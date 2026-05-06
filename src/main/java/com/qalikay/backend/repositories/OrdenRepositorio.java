package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepositorio extends JpaRepository<Orden, Long> {
    List<Orden> findByClienteId(Long clienteId);
    List<Orden> findByClienteIdOrderByFechaDesc(Long clienteId);
}

package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResenaRepositorio extends JpaRepository<Resena, Long> {
    List<Resena> findByTipoItemAndItemId(String tipoItem, Long itemId);
    List<Resena> findByClienteId(Long clienteId);
}

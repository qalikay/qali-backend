package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepositorio extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByConsultaIdOrderByFechaEnvioAsc(Long consultaId);
}

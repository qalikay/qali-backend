package com.qalikay.backend.consultation.repository;

import com.qalikay.backend.consultation.domain.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findByConsultaIdOrderByCreatedAtAsc(Long consultaId);
}

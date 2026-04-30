package com.qalikay.backend.consultation.repository;

import com.qalikay.backend.consultation.domain.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findByClienteId(Long clienteId, Pageable pageable);

    Page<Consulta> findByExpertoId(Long expertoId, Pageable pageable);
}

package com.qalikay.backend.user.repository;

import com.qalikay.backend.user.domain.PerfilExperto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilExpertoRepository extends JpaRepository<PerfilExperto, Long> {

    Optional<PerfilExperto> findByUsuarioId(Long usuarioId);

    List<PerfilExperto> findByVerificadoTrue();

    List<PerfilExperto> findByEspecialidadIdAndVerificadoTrue(Long especialidadId);
}

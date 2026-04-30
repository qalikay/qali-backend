package com.qalikay.backend.review.repository;

import com.qalikay.backend.review.domain.Resena;
import com.qalikay.backend.transaction.domain.TipoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    Optional<Resena> findByClienteIdAndTipoAndRefId(Long clienteId, TipoItem tipo, Long refId);

    Page<Resena> findByTipoAndRefId(TipoItem tipo, Long refId, Pageable pageable);

    @Query("""
            SELECT AVG(r.calificacion) FROM Resena r
            WHERE r.tipo = :tipo AND r.refId = :refId
            """)
    Double promedioCalificacion(@Param("tipo") TipoItem tipo, @Param("refId") Long refId);

    long countByTipoAndRefId(TipoItem tipo, Long refId);
}

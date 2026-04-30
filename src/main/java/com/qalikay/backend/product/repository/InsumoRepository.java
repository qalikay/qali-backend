package com.qalikay.backend.product.repository;

import com.qalikay.backend.product.domain.EstadoInsumo;
import com.qalikay.backend.product.domain.Insumo;
import com.qalikay.backend.product.domain.TipoInsumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    @Query("""
            SELECT i FROM Insumo i
            WHERE i.estado = :estado
              AND (:categoriaId IS NULL OR i.categoria.id = :categoriaId)
              AND (:tipo IS NULL OR i.tipo = :tipo)
              AND (:texto IS NULL OR LOWER(i.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                                  OR LOWER(i.descripcionCorta) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:precioMin IS NULL OR i.precio >= :precioMin)
              AND (:precioMax IS NULL OR i.precio <= :precioMax)
            """)
    Page<Insumo> buscar(
            @Param("estado") EstadoInsumo estado,
            @Param("categoriaId") Long categoriaId,
            @Param("tipo") TipoInsumo tipo,
            @Param("texto") String texto,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            Pageable pageable
    );

    Page<Insumo> findByExpertoId(Long expertoId, Pageable pageable);

    /**
     * Carga el insumo con bloqueo pesimista para operaciones criticas
     * de stock (transacciones de compra concurrentes).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Insumo i WHERE i.id = :id")
    Optional<Insumo> findByIdForUpdate(@Param("id") Long id);
}

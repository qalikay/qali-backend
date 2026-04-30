package com.qalikay.backend.recipe.repository;

import com.qalikay.backend.recipe.domain.EstadoReceta;
import com.qalikay.backend.recipe.domain.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Acceso a datos de recetas.
 *
 * Usa paginacion (Pageable) en consultas publicas para evitar
 * traer miles de recetas en una sola respuesta.
 */
@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {

    /**
     * Lista recetas con filtros opcionales:
     *  - estado:     filtra por estado (publico solo recibe PUBLICADA)
     *  - categoriaId: opcional
     *  - texto:      busqueda en titulo o descripcion (case insensitive)
     *  - precioMin / precioMax: rango de precio opcional
     *
     * Los parametros opcionales se envian como NULL para ignorar el filtro.
     */
    @Query("""
            SELECT r FROM Receta r
            WHERE r.estado = :estado
              AND (:categoriaId IS NULL OR r.categoria.id = :categoriaId)
              AND (:texto IS NULL OR LOWER(r.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
                                  OR LOWER(r.descripcionCorta) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:precioMin IS NULL OR r.precio >= :precioMin)
              AND (:precioMax IS NULL OR r.precio <= :precioMax)
            """)
    Page<Receta> buscar(
            @Param("estado") EstadoReceta estado,
            @Param("categoriaId") Long categoriaId,
            @Param("texto") String texto,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            Pageable pageable
    );

    /** Lista recetas creadas por un experto especifico (incluye todos los estados). */
    Page<Receta> findByExpertoId(Long expertoId, Pageable pageable);
}

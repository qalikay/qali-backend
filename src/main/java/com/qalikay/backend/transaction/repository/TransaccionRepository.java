package com.qalikay.backend.transaction.repository;

import com.qalikay.backend.transaction.domain.EstadoTransaccion;
import com.qalikay.backend.transaction.domain.TipoItem;
import com.qalikay.backend.transaction.domain.Transaccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    Page<Transaccion> findByClienteId(Long clienteId, Pageable pageable);

    /**
     * Verifica si un cliente compro y pago un item especifico.
     * Util para autorizar acceso a recetas pagas y para reseñas.
     */
    @Query("""
            SELECT COUNT(d) > 0 FROM TransaccionDetalle d
            WHERE d.transaccion.cliente.id = :clienteId
              AND d.transaccion.estado = :estadoPagada
              AND d.tipo = :tipo
              AND d.refId = :refId
            """)
    boolean clienteComproItem(
            @Param("clienteId") Long clienteId,
            @Param("tipo") TipoItem tipo,
            @Param("refId") Long refId,
            @Param("estadoPagada") EstadoTransaccion estadoPagada
    );
}

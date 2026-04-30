package com.qalikay.backend.transaction.domain;

import com.qalikay.backend.shared.domain.BaseEntity;
import com.qalikay.backend.user.domain.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cabecera de transaccion comercial.
 *
 * Una transaccion es una compra completa que el cliente realiza,
 * y puede contener varios items (recetas y/o insumos) a traves de
 * TransaccionDetalle.
 *
 * Mantiene el total y el estado del pago. Cuando estado es PAGADA,
 * se marca fechaPago y se generan los efectos comerciales (reduccion
 * de stock, habilitacion de receta, etc.).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "transaccion",
        indexes = {
                @Index(name = "idx_transaccion_cliente", columnList = "id_cliente"),
                @Index(name = "idx_transaccion_estado", columnList = "estado")
        }
)
public class Transaccion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private EstadoTransaccion estado = EstadoTransaccion.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", length = 20)
    private MetodoPago metodoPago;

    @Column(name = "referencia_pago", length = 100)
    private String referenciaPago;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TransaccionDetalle> detalles = new ArrayList<>();

    public void marcarPagada(MetodoPago metodo, String referencia) {
        this.estado = EstadoTransaccion.PAGADA;
        this.metodoPago = metodo;
        this.referenciaPago = referencia;
        this.fechaPago = LocalDateTime.now();
    }
}

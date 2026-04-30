package com.qalikay.backend.product.domain;

import com.qalikay.backend.recipe.domain.Categoria;
import com.qalikay.backend.shared.domain.BaseEntity;
import com.qalikay.backend.user.domain.Usuario;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Insumo natural ofrecido por un EXPERTO en la plataforma.
 *
 * A diferencia de la receta (que es informacion), el insumo es un
 * producto fisico (hojas de muna, miel de abeja, raiz de jengibre, etc.)
 * que el experto vende y debe enviar al cliente.
 *
 * Tiene control de stock con bloqueo optimista a traves del @Version
 * heredado de BaseEntity, esencial cuando varios clientes intentan
 * comprar el mismo insumo simultaneamente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "insumo",
        indexes = {
                @Index(name = "idx_insumo_estado_categoria", columnList = "estado,id_categoria"),
                @Index(name = "idx_insumo_experto", columnList = "id_experto")
        }
)
public class Insumo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion_corta", nullable = false, length = 300)
    private String descripcionCorta;

    @Column(name = "descripcion", length = 4000)
    private String descripcion;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "unidad_medida", length = 30)
    private String unidadMedida;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoInsumo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private EstadoInsumo estado = EstadoInsumo.DISPONIBLE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_experto", nullable = false)
    private Usuario experto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    /** Reduce el stock al concretar una compra. Marca AGOTADO si llega a 0. */
    public void reducirStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (this.stock < cantidad) {
            throw new IllegalStateException("Stock insuficiente para el insumo " + nombre);
        }
        this.stock -= cantidad;
        if (this.stock == 0) {
            this.estado = EstadoInsumo.AGOTADO;
        }
    }

    /** Devuelve stock al cancelar una compra. Reactiva el estado si estaba AGOTADO. */
    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.stock += cantidad;
        if (this.estado == EstadoInsumo.AGOTADO) {
            this.estado = EstadoInsumo.DISPONIBLE;
        }
    }
}

package com.qalikay.backend.review.domain;

import com.qalikay.backend.shared.domain.BaseEntity;
import com.qalikay.backend.transaction.domain.TipoItem;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Resena dejada por un cliente sobre una receta o insumo que compro.
 *
 * Constraint UNIQUE (clienteId, tipo, refId) para evitar que un mismo
 * cliente publique varias resenas del mismo producto. Si quiere
 * cambiar su opinion, debe editarla.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "resena",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_resena_unica", columnNames = {"id_cliente", "tipo", "ref_id"})
        },
        indexes = {
                @Index(name = "idx_resena_item", columnList = "tipo,ref_id")
        }
)
public class Resena extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 10)
    private TipoItem tipo;

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(name = "calificacion", nullable = false)
    private Integer calificacion;

    @Column(name = "comentario", length = 1000)
    private String comentario;
}

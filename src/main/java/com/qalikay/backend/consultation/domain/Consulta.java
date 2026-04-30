package com.qalikay.backend.consultation.domain;

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
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Consulta privada entre un cliente y un experto.
 *
 * El cliente la crea con un asunto y mensaje inicial; el experto la
 * acepta o rechaza. Mientras este ACEPTADA, ambos pueden intercambiar
 * mensajes mediante la entidad Mensaje.
 *
 * Modelar la consulta como cabecera + mensajes evita confundir el
 * "asunto" con cada respuesta y permite estados claros.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "consulta",
        indexes = {
                @Index(name = "idx_consulta_cliente", columnList = "id_cliente"),
                @Index(name = "idx_consulta_experto", columnList = "id_experto"),
                @Index(name = "idx_consulta_estado", columnList = "estado")
        }
)
public class Consulta extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Long id;

    @Column(name = "asunto", nullable = false, length = 200)
    private String asunto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private EstadoConsulta estado = EstadoConsulta.SOLICITADA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_experto", nullable = false)
    private Usuario experto;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<Mensaje> mensajes = new ArrayList<>();

    public boolean involucra(Long usuarioId) {
        return cliente.getId().equals(usuarioId) || experto.getId().equals(usuarioId);
    }

    public boolean estaAbierta() {
        return estado == EstadoConsulta.SOLICITADA || estado == EstadoConsulta.ACEPTADA;
    }
}

package com.qalikay.backend.recipe.domain;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Receta de medicina natural publicada por un EXPERTO en la plataforma.
 *
 * Reglas de negocio principales:
 *  - Solo un usuario con rol EXPERTO puede crearla.
 *  - Empieza en estado BORRADOR; pasa a PUBLICADA cuando el experto la activa.
 *  - Los clientes solo ven recetas en estado PUBLICADA.
 *  - El experto puede archivarla pero no eliminarla si tiene compras asociadas
 *    (para preservar historial). Esa regla la valida el RecipeService.
 *
 * Indice por (estado, categoria) para acelerar listado/filtros publicos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "receta",
        indexes = {
                @Index(name = "idx_receta_estado_categoria", columnList = "estado,id_categoria"),
                @Index(name = "idx_receta_experto", columnList = "id_experto")
        }
)
public class Receta extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descripcion_corta", nullable = false, length = 300)
    private String descripcionCorta;

    @Lob
    @Column(name = "ingredientes", nullable = false, columnDefinition = "TEXT")
    private String ingredientes;

    @Lob
    @Column(name = "preparacion", nullable = false, columnDefinition = "TEXT")
    private String preparacion;

    @Lob
    @Column(name = "modo_uso", columnDefinition = "TEXT")
    private String modoUso;

    @Lob
    @Column(name = "precauciones", columnDefinition = "TEXT")
    private String precauciones;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "duracion_preparacion_min")
    private Integer duracionPreparacionMin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private EstadoReceta estado = EstadoReceta.BORRADOR;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(name = "vistas", nullable = false)
    @Builder.Default
    private Long vistas = 0L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_experto", nullable = false)
    private Usuario experto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    /** Helper de dominio: marca la receta como publicada y registra la fecha. */
    public void publicar() {
        this.estado = EstadoReceta.PUBLICADA;
        this.fechaPublicacion = LocalDateTime.now();
    }

    /** Helper de dominio: la archiva y deja de ser visible al publico. */
    public void archivar() {
        this.estado = EstadoReceta.ARCHIVADA;
    }

    /** Incrementa el contador de vistas. */
    public void incrementarVistas() {
        this.vistas = (this.vistas == null ? 0L : this.vistas) + 1;
    }
}

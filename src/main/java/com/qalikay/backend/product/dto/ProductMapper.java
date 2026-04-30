package com.qalikay.backend.product.dto;

import com.qalikay.backend.product.domain.Insumo;
import com.qalikay.backend.user.domain.PerfilExperto;
import com.qalikay.backend.user.domain.Usuario;
import com.qalikay.backend.user.repository.PerfilExpertoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final PerfilExpertoRepository perfilExpertoRepository;

    public ProductResponse toResponse(Insumo insumo) {
        Usuario experto = insumo.getExperto();
        String specialty = perfilExpertoRepository.findByUsuarioId(experto.getId())
                .map(PerfilExperto::getEspecialidad)
                .map(esp -> esp.getNombre())
                .orElse(null);

        return ProductResponse.builder()
                .id(insumo.getId())
                .name(insumo.getNombre())
                .shortDescription(insumo.getDescripcionCorta())
                .description(insumo.getDescripcion())
                .imageUrl(insumo.getImagenUrl())
                .price(insumo.getPrecio())
                .stock(insumo.getStock())
                .unit(insumo.getUnidadMedida())
                .type(insumo.getTipo().name())
                .status(insumo.getEstado().name())
                .createdAt(insumo.getCreatedAt())
                .updatedAt(insumo.getUpdatedAt())
                .seller(ProductResponse.SellerDto.builder()
                        .id(experto.getId())
                        .firstName(experto.getNombre())
                        .lastName(experto.getApellido())
                        .specialty(specialty)
                        .build())
                .category(ProductResponse.CategoryDto.builder()
                        .id(insumo.getCategoria().getId())
                        .name(insumo.getCategoria().getNombre())
                        .icon(insumo.getCategoria().getIcono())
                        .build())
                .build();
    }

    public ProductSummary toSummary(Insumo insumo) {
        Usuario experto = insumo.getExperto();
        return ProductSummary.builder()
                .id(insumo.getId())
                .name(insumo.getNombre())
                .shortDescription(insumo.getDescripcionCorta())
                .imageUrl(insumo.getImagenUrl())
                .price(insumo.getPrecio())
                .stock(insumo.getStock())
                .type(insumo.getTipo().name())
                .unit(insumo.getUnidadMedida())
                .categoryName(insumo.getCategoria().getNombre())
                .sellerFullName(experto.getNombre() + " " + experto.getApellido())
                .build();
    }
}

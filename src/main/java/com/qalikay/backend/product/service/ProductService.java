package com.qalikay.backend.product.service;

import com.qalikay.backend.product.domain.EstadoInsumo;
import com.qalikay.backend.product.domain.Insumo;
import com.qalikay.backend.product.domain.TipoInsumo;
import com.qalikay.backend.product.dto.ProductMapper;
import com.qalikay.backend.product.dto.ProductRequest;
import com.qalikay.backend.product.dto.ProductResponse;
import com.qalikay.backend.product.dto.ProductSummary;
import com.qalikay.backend.product.repository.InsumoRepository;
import com.qalikay.backend.recipe.domain.Categoria;
import com.qalikay.backend.recipe.repository.CategoriaRepository;
import com.qalikay.backend.shared.exception.BusinessException;
import com.qalikay.backend.shared.exception.ResourceNotFoundException;
import com.qalikay.backend.user.domain.RolNombre;
import com.qalikay.backend.user.domain.Usuario;
import com.qalikay.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final InsumoRepository insumoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductMapper mapper;

    @Transactional
    public ProductResponse create(String sellerEmail, ProductRequest request) {
        Usuario experto = loadExperto(sellerEmail);
        Categoria categoria = loadCategoria(request.categoryId());
        TipoInsumo tipo = parseTipo(request.type());

        Insumo insumo = Insumo.builder()
                .nombre(request.name())
                .descripcionCorta(request.shortDescription())
                .descripcion(request.description())
                .imagenUrl(request.imageUrl())
                .precio(request.price())
                .stock(request.stock())
                .unidadMedida(request.unit())
                .tipo(tipo)
                .estado(request.stock() > 0 ? EstadoInsumo.DISPONIBLE : EstadoInsumo.AGOTADO)
                .experto(experto)
                .categoria(categoria)
                .build();

        Insumo guardado = insumoRepository.save(insumo);
        log.info("Insumo creado id={} por experto={}", guardado.getId(), sellerEmail);
        return mapper.toResponse(guardado);
    }

    @Transactional
    public ProductResponse update(String sellerEmail, Long productId, ProductRequest request) {
        Insumo insumo = loadInsumoOwned(productId, sellerEmail);
        Categoria categoria = loadCategoria(request.categoryId());
        TipoInsumo tipo = parseTipo(request.type());

        insumo.setNombre(request.name());
        insumo.setDescripcionCorta(request.shortDescription());
        insumo.setDescripcion(request.description());
        insumo.setImagenUrl(request.imageUrl());
        insumo.setPrecio(request.price());
        insumo.setStock(request.stock());
        insumo.setUnidadMedida(request.unit());
        insumo.setTipo(tipo);
        insumo.setCategoria(categoria);
        if (request.stock() == 0 && insumo.getEstado() == EstadoInsumo.DISPONIBLE) {
            insumo.setEstado(EstadoInsumo.AGOTADO);
        } else if (request.stock() > 0 && insumo.getEstado() == EstadoInsumo.AGOTADO) {
            insumo.setEstado(EstadoInsumo.DISPONIBLE);
        }
        return mapper.toResponse(insumo);
    }

    @Transactional
    public void delete(String sellerEmail, Long productId) {
        Insumo insumo = loadInsumoOwned(productId, sellerEmail);
        insumoRepository.delete(insumo);
        log.info("Insumo eliminado id={} por experto={}", productId, sellerEmail);
    }

    @Transactional
    public ProductResponse discontinue(String sellerEmail, Long productId) {
        Insumo insumo = loadInsumoOwned(productId, sellerEmail);
        insumo.setEstado(EstadoInsumo.DESCONTINUADO);
        return mapper.toResponse(insumo);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long productId) {
        Insumo insumo = insumoRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo", productId));
        if (insumo.getEstado() == EstadoInsumo.DESCONTINUADO) {
            throw new ResourceNotFoundException("Insumo", productId);
        }
        return mapper.toResponse(insumo);
    }

    @Transactional(readOnly = true)
    public Page<ProductSummary> search(
            Long categoryId,
            String type,
            String text,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        TipoInsumo tipo = type == null ? null : parseTipo(type);
        return insumoRepository
                .buscar(EstadoInsumo.DISPONIBLE, categoryId, tipo, text, minPrice, maxPrice, pageable)
                .map(mapper::toSummary);
    }

    @Transactional(readOnly = true)
    public Page<ProductSummary> listMyProducts(String sellerEmail, Pageable pageable) {
        Usuario experto = loadExperto(sellerEmail);
        return insumoRepository
                .findByExpertoId(experto.getId(), pageable)
                .map(mapper::toSummary);
    }

    private Insumo loadInsumoOwned(Long productId, String sellerEmail) {
        Insumo insumo = insumoRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo", productId));
        if (!insumo.getExperto().getCorreo().equalsIgnoreCase(sellerEmail)) {
            throw new BusinessException("No tienes permisos sobre este insumo", HttpStatus.FORBIDDEN);
        }
        return insumo;
    }

    private Usuario loadExperto(String email) {
        Usuario usuario = usuarioRepository.findByCorreoWithRoles(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + email));
        if (!usuario.tieneRol(RolNombre.EXPERTO)) {
            throw new BusinessException("Solo los expertos pueden gestionar insumos", HttpStatus.FORBIDDEN);
        }
        return usuario;
    }

    private Categoria loadCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }

    private TipoInsumo parseTipo(String value) {
        try {
            return TipoInsumo.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Tipo de insumo invalido: " + value);
        }
    }
}

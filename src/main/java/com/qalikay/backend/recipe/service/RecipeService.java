package com.qalikay.backend.recipe.service;

import com.qalikay.backend.recipe.domain.Categoria;
import com.qalikay.backend.recipe.domain.EstadoReceta;
import com.qalikay.backend.recipe.domain.Receta;
import com.qalikay.backend.recipe.dto.RecipeMapper;
import com.qalikay.backend.recipe.dto.RecipeRequest;
import com.qalikay.backend.recipe.dto.RecipeResponse;
import com.qalikay.backend.recipe.dto.RecipeSummary;
import com.qalikay.backend.recipe.repository.CategoriaRepository;
import com.qalikay.backend.recipe.repository.RecetaRepository;
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

/**
 * Logica de negocio de Recetas.
 *
 * Reglas centrales:
 *  - Solo un EXPERTO puede crear recetas.
 *  - Solo el dueno (o un ADMIN) puede editar/borrar/publicar.
 *  - El listado publico solo retorna recetas en estado PUBLICADA.
 *  - El detalle publico tambien lo limita; el dueno puede ver sus borradores.
 *
 * La autorizacion fina por rol se reforzara con @PreAuthorize en el controller.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecetaRepository recetaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecipeMapper mapper;

    @Transactional
    public RecipeResponse create(String authorEmail, RecipeRequest request) {
        Usuario experto = loadExperto(authorEmail);
        Categoria categoria = loadCategoria(request.categoryId());

        Receta receta = Receta.builder()
                .titulo(request.title())
                .descripcionCorta(request.shortDescription())
                .ingredientes(request.ingredients())
                .preparacion(request.preparation())
                .modoUso(request.usage())
                .precauciones(request.warnings())
                .imagenUrl(request.imageUrl())
                .precio(request.price())
                .duracionPreparacionMin(request.preparationMinutes())
                .estado(EstadoReceta.BORRADOR)
                .vistas(0L)
                .experto(experto)
                .categoria(categoria)
                .build();

        Receta guardada = recetaRepository.save(receta);
        log.info("Receta creada id={} por experto={}", guardada.getId(), authorEmail);
        return mapper.toResponse(guardada);
    }

    @Transactional
    public RecipeResponse update(String authorEmail, Long recipeId, RecipeRequest request) {
        Receta receta = loadRecetaOwned(recipeId, authorEmail);
        Categoria categoria = loadCategoria(request.categoryId());

        receta.setTitulo(request.title());
        receta.setDescripcionCorta(request.shortDescription());
        receta.setIngredientes(request.ingredients());
        receta.setPreparacion(request.preparation());
        receta.setModoUso(request.usage());
        receta.setPrecauciones(request.warnings());
        receta.setImagenUrl(request.imageUrl());
        receta.setPrecio(request.price());
        receta.setDuracionPreparacionMin(request.preparationMinutes());
        receta.setCategoria(categoria);

        Receta actualizada = recetaRepository.save(receta);
        log.info("Receta actualizada id={} por experto={}", actualizada.getId(), authorEmail);
        return mapper.toResponse(actualizada);
    }

    @Transactional
    public RecipeResponse publish(String authorEmail, Long recipeId) {
        Receta receta = loadRecetaOwned(recipeId, authorEmail);
        if (receta.getEstado() == EstadoReceta.PUBLICADA) {
            throw new BusinessException("La receta ya se encuentra publicada");
        }
        receta.publicar();
        log.info("Receta publicada id={} por experto={}", recipeId, authorEmail);
        return mapper.toResponse(receta);
    }

    @Transactional
    public RecipeResponse archive(String authorEmail, Long recipeId) {
        Receta receta = loadRecetaOwned(recipeId, authorEmail);
        receta.archivar();
        log.info("Receta archivada id={} por experto={}", recipeId, authorEmail);
        return mapper.toResponse(receta);
    }

    @Transactional
    public void delete(String authorEmail, Long recipeId) {
        Receta receta = loadRecetaOwned(recipeId, authorEmail);
        recetaRepository.delete(receta);
        log.info("Receta eliminada id={} por experto={}", recipeId, authorEmail);
    }

    @Transactional
    public RecipeResponse getPublishedById(Long recipeId) {
        Receta receta = recetaRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", recipeId));
        if (receta.getEstado() != EstadoReceta.PUBLICADA) {
            throw new ResourceNotFoundException("Receta", recipeId);
        }
        receta.incrementarVistas();
        return mapper.toResponse(receta);
    }

    @Transactional(readOnly = true)
    public RecipeResponse getOwnById(String authorEmail, Long recipeId) {
        Receta receta = loadRecetaOwned(recipeId, authorEmail);
        return mapper.toResponse(receta);
    }

    @Transactional(readOnly = true)
    public Page<RecipeSummary> searchPublished(
            Long categoryId,
            String text,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        return recetaRepository
                .buscar(EstadoReceta.PUBLICADA, categoryId, text, minPrice, maxPrice, pageable)
                .map(mapper::toSummary);
    }

    @Transactional(readOnly = true)
    public Page<RecipeSummary> listMyRecipes(String authorEmail, Pageable pageable) {
        Usuario experto = loadExperto(authorEmail);
        return recetaRepository
                .findByExpertoId(experto.getId(), pageable)
                .map(mapper::toSummary);
    }

    private Receta loadRecetaOwned(Long recipeId, String authorEmail) {
        Receta receta = recetaRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", recipeId));
        if (!receta.getExperto().getCorreo().equalsIgnoreCase(authorEmail)) {
            throw new BusinessException("No tienes permisos sobre esta receta",
                    HttpStatus.FORBIDDEN);
        }
        return receta;
    }

    private Usuario loadExperto(String email) {
        Usuario usuario = usuarioRepository.findByCorreoWithRoles(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + email));
        if (!usuario.tieneRol(RolNombre.EXPERTO)) {
            throw new BusinessException("Solo los expertos pueden gestionar recetas",
                    HttpStatus.FORBIDDEN);
        }
        return usuario;
    }

    private Categoria loadCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }
}

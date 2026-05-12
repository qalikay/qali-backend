package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.CrearRecetaDTO;
import com.qalikay.backend.dtos.RecetaDTO;
import com.qalikay.backend.entities.Receta;
import com.qalikay.backend.services.ExpertoService;
import com.qalikay.backend.services.RecetaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoints de Recetas.
 *  - /api/recetas         -> publico, solo muestra estado=PUBLICADA
 *  - /api/experto/recetas -> ROLE_EXPERTO, ve y administra solo SUS recetas
 */
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private ExpertoService expertoService;

    @Autowired
    private ModelMapper modelMapper;

    // ----------------- ENDPOINTS PUBLICOS -----------------

    // GET /api/recetas[?categoriaId=&q=]   Filtros opcionales por categoria o por titulo.
    @GetMapping("/recetas")
    public List<RecetaDTO> listar(@RequestParam(required = false) Long categoriaId,
                                  @RequestParam(required = false) String q) {
        List<Receta> recetas;
        if (q != null && !q.isBlank()) {
            recetas = recetaService.buscarPublicadasPorTitulo(q);          // Filtro por busqueda
        } else if (categoriaId != null) {
            recetas = recetaService.listarPublicadasPorCategoria(categoriaId);
        } else {
            recetas = recetaService.listarPublicadas();                    // Por defecto: todas las PUBLICADA
        }
        return recetas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // GET /api/recetas/{id} -> solo si esta PUBLICADA, sino 404
    @GetMapping("/recetas/{id}")
    public ResponseEntity<RecetaDTO> buscarPorId(@PathVariable Long id) {
        Receta r = recetaService.buscarPorId(id);
        if (r == null || !"PUBLICADA".equals(r.getEstado())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(r));
    }

    // ----------------- ENDPOINTS DEL EXPERTO -----------------

    // GET /api/experto/recetas -> recetas del experto autenticado (todas, sin importar estado)
    // @AuthenticationPrincipal: Spring inyecta el UserDetails que cargo el JwtRequestFilter
    @GetMapping("/experto/recetas")
    @PreAuthorize("hasRole('EXPERTO')")
    public List<RecetaDTO> misRecetas(@AuthenticationPrincipal UserDetails userDetails) {
        var experto = expertoService.buscarPorUsername(userDetails.getUsername());
        if (experto == null) return List.of();
        return recetaService.listarPorExperto(experto.getId()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // GET /api/experto/recetas/{id} -> el experto solo puede ver su propia receta (403 si no)
    @GetMapping("/experto/recetas/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> miReceta(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        Receta r = recetaService.buscarPorId(id);
        if (r == null) return ResponseEntity.notFound().build();
        // Verifica que la receta sea del experto autenticado (defensa adicional al @PreAuthorize)
        if (r.getExperto() == null || r.getExperto().getUser() == null
                || !r.getExperto().getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(toDTO(r));
    }

    // POST /api/experto/recetas -> crea receta vinculada al experto autenticado (estado = BORRADOR)
    @PostMapping("/experto/recetas")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> crear(@RequestBody CrearRecetaDTO dto,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Receta creada = recetaService.crearComoExperto(dto, userDetails.getUsername());
        return new ResponseEntity<>(toDTO(creada), HttpStatus.CREATED);
    }

    // PUT /api/experto/recetas/{id} -> actualiza una receta (el service valida la propiedad)
    @PutMapping("/experto/recetas/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> modificar(@PathVariable Long id,
                                               @RequestBody CrearRecetaDTO dto,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        Receta mod = recetaService.modificarComoExperto(id, dto, userDetails.getUsername());
        return ResponseEntity.ok(toDTO(mod));
    }

    // POST /api/experto/recetas/{id}/publicar -> cambia estado BORRADOR -> PUBLICADA
    @PostMapping("/experto/recetas/{id}/publicar")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> publicar(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(toDTO(recetaService.publicar(id, userDetails.getUsername())));
    }

    // POST /api/experto/recetas/{id}/archivar -> vuelve a estado BORRADOR (oculta del publico)
    @PostMapping("/experto/recetas/{id}/archivar")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> archivar(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(toDTO(recetaService.archivar(id, userDetails.getUsername())));
    }

    @DeleteMapping("/experto/recetas/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        recetaService.eliminar(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // ----------------- helpers -----------------

    // Convierte Receta -> RecetaDTO y agrega el username del experto (no lo mapea ModelMapper)
    private RecetaDTO toDTO(Receta r) {
        RecetaDTO dto = modelMapper.map(r, RecetaDTO.class);
        if (r.getExperto() != null && r.getExperto().getUser() != null && dto.getExperto() != null) {
            dto.getExperto().setUsername(r.getExperto().getUser().getUsername());
        }
        return dto;
    }
}

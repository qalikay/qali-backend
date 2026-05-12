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

    @GetMapping("/recetas")
    public List<RecetaDTO> listar(@RequestParam(required = false) Long categoriaId,
                                  @RequestParam(required = false) String q) {
        List<Receta> recetas;
        if (q != null && !q.isBlank()) {
            recetas = recetaService.buscarPublicadasPorTitulo(q);
        } else if (categoriaId != null) {
            recetas = recetaService.listarPublicadasPorCategoria(categoriaId);
        } else {
            recetas = recetaService.listarPublicadas();
        }
        return recetas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/recetas/{id}")
    public ResponseEntity<RecetaDTO> buscarPorId(@PathVariable Long id) {
        Receta r = recetaService.buscarPorId(id);
        if (r == null || !"PUBLICADA".equals(r.getEstado())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(r));
    }

    // ----------------- ENDPOINTS DEL EXPERTO -----------------

    @GetMapping("/experto/recetas")
    @PreAuthorize("hasRole('EXPERTO')")
    public List<RecetaDTO> misRecetas(@AuthenticationPrincipal UserDetails userDetails) {
        var experto = expertoService.buscarPorUsername(userDetails.getUsername());
        if (experto == null) return List.of();
        return recetaService.listarPorExperto(experto.getId()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/experto/recetas/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> miReceta(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        Receta r = recetaService.buscarPorId(id);
        if (r == null) return ResponseEntity.notFound().build();
        if (r.getExperto() == null || r.getExperto().getUser() == null
                || !r.getExperto().getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(toDTO(r));
    }

    @PostMapping("/experto/recetas")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> crear(@RequestBody CrearRecetaDTO dto,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Receta creada = recetaService.crearComoExperto(dto, userDetails.getUsername());
        return new ResponseEntity<>(toDTO(creada), HttpStatus.CREATED);
    }

    @PutMapping("/experto/recetas/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> modificar(@PathVariable Long id,
                                               @RequestBody CrearRecetaDTO dto,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        Receta mod = recetaService.modificarComoExperto(id, dto, userDetails.getUsername());
        return ResponseEntity.ok(toDTO(mod));
    }

    @PostMapping("/experto/recetas/{id}/publicar")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<RecetaDTO> publicar(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(toDTO(recetaService.publicar(id, userDetails.getUsername())));
    }

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

    private RecetaDTO toDTO(Receta r) {
        RecetaDTO dto = modelMapper.map(r, RecetaDTO.class);
        if (r.getExperto() != null && r.getExperto().getUser() != null && dto.getExperto() != null) {
            dto.getExperto().setUsername(r.getExperto().getUser().getUsername());
        }
        return dto;
    }
}

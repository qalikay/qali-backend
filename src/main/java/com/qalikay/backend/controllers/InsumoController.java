package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.CrearInsumoDTO;
import com.qalikay.backend.dtos.InsumoDTO;
import com.qalikay.backend.entities.Insumo;
import com.qalikay.backend.services.ExpertoService;
import com.qalikay.backend.services.InsumoService;
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
 * Endpoints de Insumos (productos).
 *  - GET son publicos con filtros por categoria, tipo y nombre
 *  - CRUD bajo /api/experto/insumos solo ROLE_EXPERTO
 */
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class InsumoController {

    @Autowired private InsumoService insumoService;
    @Autowired private ExpertoService expertoService;
    @Autowired private ModelMapper modelMapper;

    // GET /api/insumos[?categoriaId=&tipo=&q=]  Filtros opcionales (solo uno se aplica por request)
    @GetMapping("/insumos")
    public List<InsumoDTO> listar(@RequestParam(required = false) Long categoriaId,
                                  @RequestParam(required = false) String tipo,
                                  @RequestParam(required = false) String q) {
        List<Insumo> lista;
        if (q != null && !q.isBlank()) {
            lista = insumoService.buscarPorNombre(q);
        } else if (categoriaId != null) {
            lista = insumoService.listarPorCategoria(categoriaId);
        } else if (tipo != null && !tipo.isBlank()) {
            lista = insumoService.listarPorTipo(tipo);
        } else {
            lista = insumoService.listar();
        }
        return lista.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // GET /api/insumos/{id} -> detalle publico
    @GetMapping("/insumos/{id}")
    public ResponseEntity<InsumoDTO> buscarPorId(@PathVariable Long id) {
        Insumo i = insumoService.buscarPorId(id);
        if (i == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(i));
    }

    // ----------------- ENDPOINTS DEL EXPERTO -----------------

    // GET /api/experto/insumos -> insumos que vende el experto autenticado
    @GetMapping("/experto/insumos")
    @PreAuthorize("hasRole('EXPERTO')")
    public List<InsumoDTO> misInsumos(@AuthenticationPrincipal UserDetails userDetails) {
        var experto = expertoService.buscarPorUsername(userDetails.getUsername());
        if (experto == null) return List.of();
        return insumoService.listarPorExperto(experto.getId()).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @PostMapping("/experto/insumos")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<InsumoDTO> crear(@RequestBody CrearInsumoDTO dto,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Insumo creado = insumoService.crearComoExperto(dto, userDetails.getUsername());
        return new ResponseEntity<>(toDTO(creado), HttpStatus.CREATED);
    }

    @PutMapping("/experto/insumos/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<InsumoDTO> modificar(@PathVariable Long id,
                                               @RequestBody CrearInsumoDTO dto,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        Insumo mod = insumoService.modificarComoExperto(id, dto, userDetails.getUsername());
        return ResponseEntity.ok(toDTO(mod));
    }

    @DeleteMapping("/experto/insumos/{id}")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        insumoService.eliminar(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // Mapeo manual del username (no lo cubre ModelMapper porque va anidado)
    private InsumoDTO toDTO(Insumo i) {
        InsumoDTO dto = modelMapper.map(i, InsumoDTO.class);
        if (i.getExperto() != null && i.getExperto().getUser() != null && dto.getExperto() != null) {
            dto.getExperto().setUsername(i.getExperto().getUser().getUsername());
        }
        return dto;
    }
}

package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.ExpertoDTO;
import com.qalikay.backend.entities.Experto;
import com.qalikay.backend.services.ExpertoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoints de Expertos.
 *  - /expertos         publico (lista + detalle)
 *  - /experto/me       ROLE_EXPERTO (consulta/edita su propio perfil)
 */
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class  ExpertoController {

    @Autowired
    private ExpertoService expertoService;

    @Autowired
    private ModelMapper modelMapper;

    // GET /api/expertos[?especialidadId=] -> directorio publico de expertos
    @GetMapping("/expertos")
    public List<ExpertoDTO> listar(@RequestParam(required = false) Long especialidadId) {
        List<Experto> expertos = especialidadId == null
                ? expertoService.listar()
                : expertoService.listarPorEspecialidad(especialidadId);
        return expertos.stream()
                .map(e -> {
                    ExpertoDTO dto = modelMapper.map(e, ExpertoDTO.class);
                    if (e.getUser() != null) dto.setUsername(e.getUser().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/expertos/{id}")
    public ResponseEntity<ExpertoDTO> buscarPorId(@PathVariable Long id) {
        Experto e = expertoService.buscarPorId(id);
        if (e == null) return ResponseEntity.notFound().build();
        ExpertoDTO dto = modelMapper.map(e, ExpertoDTO.class);
        if (e.getUser() != null) dto.setUsername(e.getUser().getUsername());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/experto/me")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<ExpertoDTO> miPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        Experto e = expertoService.buscarPorUsername(userDetails.getUsername());
        if (e == null) return ResponseEntity.notFound().build();
        ExpertoDTO dto = modelMapper.map(e, ExpertoDTO.class);
        dto.setUsername(e.getUser().getUsername());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/experto/me")
    @PreAuthorize("hasRole('EXPERTO')")
    public ResponseEntity<ExpertoDTO> actualizarMiPerfil(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody Experto cambios) {
        Experto experto = expertoService.buscarPorUsername(userDetails.getUsername());
        if (experto == null) return ResponseEntity.notFound().build();
        experto.setNombres(cambios.getNombres() != null ? cambios.getNombres() : experto.getNombres());
        experto.setApellidos(cambios.getApellidos() != null ? cambios.getApellidos() : experto.getApellidos());
        experto.setTelefono(cambios.getTelefono() != null ? cambios.getTelefono() : experto.getTelefono());
        experto.setTrayectoria(cambios.getTrayectoria() != null ? cambios.getTrayectoria() : experto.getTrayectoria());
        if (cambios.getAnosExperiencia() != null) experto.setAnosExperiencia(cambios.getAnosExperiencia());
        Experto mod = expertoService.modificar(experto);
        ExpertoDTO dto = modelMapper.map(mod, ExpertoDTO.class);
        dto.setUsername(mod.getUser().getUsername());
        return ResponseEntity.ok(dto);
    }
}

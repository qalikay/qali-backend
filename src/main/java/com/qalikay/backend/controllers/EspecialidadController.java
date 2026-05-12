package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.EspecialidadDTO;
import com.qalikay.backend.entities.Especialidad;
import com.qalikay.backend.services.EspecialidadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/especialidades")
    public List<EspecialidadDTO> listar() {
        return especialidadService.listar().stream()
                .map(e -> modelMapper.map(e, EspecialidadDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/especialidades/{id}")
    public ResponseEntity<EspecialidadDTO> buscarPorId(@PathVariable Long id) {
        Especialidad e = especialidadService.buscarPorId(id);
        if (e == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(e, EspecialidadDTO.class));
    }

    @PostMapping("/especialidad")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecialidadDTO> crear(@RequestBody Especialidad especialidad) {
        Especialidad creada = especialidadService.insertar(especialidad);
        return new ResponseEntity<>(modelMapper.map(creada, EspecialidadDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/especialidad")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecialidadDTO> actualizar(@RequestBody Especialidad especialidad) {
        Especialidad mod = especialidadService.modificar(especialidad);
        if (mod == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(mod, EspecialidadDTO.class));
    }

    @DeleteMapping("/especialidad/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

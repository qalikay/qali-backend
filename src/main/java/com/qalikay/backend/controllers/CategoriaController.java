package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.CategoriaDTO;
import com.qalikay.backend.entities.Categoria;
import com.qalikay.backend.services.CategoriaService;
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
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/categorias")
    public List<CategoriaDTO> listar() {
        return categoriaService.listar().stream()
                .map(c -> modelMapper.map(c, CategoriaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Long id) {
        Categoria c = categoriaService.buscarPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(c, CategoriaDTO.class));
    }

    @PostMapping("/categoria")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTO> crear(@RequestBody Categoria categoria) {
        Categoria creada = categoriaService.insertar(categoria);
        return new ResponseEntity<>(modelMapper.map(creada, CategoriaDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/categoria")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTO> actualizar(@RequestBody Categoria categoria) {
        Categoria mod = categoriaService.modificar(categoria);
        if (mod == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(mod, CategoriaDTO.class));
    }

    @DeleteMapping("/categoria/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

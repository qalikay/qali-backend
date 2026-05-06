package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.CrearResenaDTO;
import com.qalikay.backend.dtos.ResenaDTO;
import com.qalikay.backend.entities.Resena;
import com.qalikay.backend.services.ResenaService;
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
public class ResenaController {

    @Autowired private ResenaService resenaService;
    @Autowired private ModelMapper modelMapper;

    @GetMapping("/resenas")
    public List<ResenaDTO> listarPorItem(@RequestParam String tipoItem,
                                         @RequestParam Long itemId) {
        return resenaService.listarPorItem(tipoItem, itemId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/cliente/resenas")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ResenaDTO> misResenas(@AuthenticationPrincipal UserDetails userDetails) {
        return resenaService.listarMisResenas(userDetails.getUsername()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/resenas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ResenaDTO> crear(@RequestBody CrearResenaDTO dto,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Resena r = resenaService.crearComoCliente(dto, userDetails.getUsername());
        return new ResponseEntity<>(toDTO(r), HttpStatus.CREATED);
    }

    @DeleteMapping("/resenas/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        resenaService.eliminar(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    private ResenaDTO toDTO(Resena r) {
        ResenaDTO dto = modelMapper.map(r, ResenaDTO.class);
        if (r.getCliente() != null && r.getCliente().getUser() != null && dto.getCliente() != null) {
            dto.getCliente().setUsername(r.getCliente().getUser().getUsername());
        }
        return dto;
    }
}

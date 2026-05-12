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

/**
 * Endpoints de Resenas (estrellas + comentario de un cliente sobre receta/insumo/experto).
 *  - GET publico filtrado por tipoItem + itemId
 *  - POST / DELETE solo ROLE_CLIENTE
 */
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class ResenaController {

    @Autowired private ResenaService resenaService;
    @Autowired private ModelMapper modelMapper;

    // GET /api/resenas?tipoItem=RECETA&itemId=1 -> resenas de un item especifico
    @GetMapping("/resenas")
    public List<ResenaDTO> listarPorItem(@RequestParam String tipoItem,
                                         @RequestParam Long itemId) {
        return resenaService.listarPorItem(tipoItem, itemId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // GET /api/cliente/resenas -> resenas que dejo el cliente autenticado
    @GetMapping("/cliente/resenas")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ResenaDTO> misResenas(@AuthenticationPrincipal UserDetails userDetails) {
        return resenaService.listarMisResenas(userDetails.getUsername()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // POST /api/resenas -> el cliente publica una resena nueva
    @PostMapping("/resenas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ResenaDTO> crear(@RequestBody CrearResenaDTO dto,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Resena r = resenaService.crearComoCliente(dto, userDetails.getUsername());
        return new ResponseEntity<>(toDTO(r), HttpStatus.CREATED);
    }

    // DELETE /api/resenas/{id} -> el cliente borra su propia resena (el service valida la propiedad)
    @DeleteMapping("/resenas/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        resenaService.eliminar(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // Helper: agrega el username del autor al DTO (no lo mapea ModelMapper)
    private ResenaDTO toDTO(Resena r) {
        ResenaDTO dto = modelMapper.map(r, ResenaDTO.class);
        if (r.getCliente() != null && r.getCliente().getUser() != null && dto.getCliente() != null) {
            dto.getCliente().setUsername(r.getCliente().getUser().getUsername());
        }
        return dto;
    }
}

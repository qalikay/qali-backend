package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.CrearOrdenDTO;
import com.qalikay.backend.dtos.OrdenDTO;
import com.qalikay.backend.entities.Orden;
import com.qalikay.backend.services.OrdenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Endpoints de Ordenes (compras).
 *  - Crear / listar lo propio: ROLE_CLIENTE
 *  - Cambiar estado: ROLE_ADMIN
 */
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class OrdenController {

    @Autowired private OrdenService ordenService;
    @Autowired private ModelMapper modelMapper;

    // POST /api/ordenes -> el service calcula totales y guarda la orden + detalles
    @PostMapping("/ordenes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<OrdenDTO> crear(@RequestBody CrearOrdenDTO dto,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        Orden o = ordenService.crearComoCliente(dto, userDetails.getUsername());
        return new ResponseEntity<>(modelMapper.map(o, OrdenDTO.class), HttpStatus.CREATED);
    }

    // GET /api/cliente/ordenes -> historial de compras del cliente autenticado
    @GetMapping("/cliente/ordenes")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<OrdenDTO> misOrdenes(@AuthenticationPrincipal UserDetails userDetails) {
        return ordenService.listarMisOrdenes(userDetails.getUsername()).stream()
                .map(o -> modelMapper.map(o, OrdenDTO.class))
                .collect(Collectors.toList());
    }

    // GET /api/ordenes/{id} -> cualquier autenticado puede ver el detalle si conoce el id
    @GetMapping("/ordenes/{id}")
    public ResponseEntity<OrdenDTO> buscarPorId(@PathVariable Long id) {
        Orden o = ordenService.buscarPorId(id);
        if (o == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(o, OrdenDTO.class));
    }

    // POST /api/ordenes/{id}/estado -> ADMIN marca la orden como PAGADA / CANCELADA
    @PostMapping("/ordenes/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdenDTO> cambiarEstado(@PathVariable Long id,
                                                  @RequestBody Map<String, String> body) {
        String estado = body.getOrDefault("estado", "PENDIENTE");
        Orden o = ordenService.cambiarEstado(id, estado);
        return ResponseEntity.ok(modelMapper.map(o, OrdenDTO.class));
    }
}

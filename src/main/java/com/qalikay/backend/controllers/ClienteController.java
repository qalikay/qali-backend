package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.ClienteDTO;
import com.qalikay.backend.entities.Cliente;
import com.qalikay.backend.services.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClienteDTO> listar() {
        return clienteService.listar().stream()
                .map(c -> {
                    ClienteDTO dto = modelMapper.map(c, ClienteDTO.class);
                    if (c.getUser() != null) dto.setUsername(c.getUser().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/cliente/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteDTO> miPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = clienteService.buscarPorUsername(userDetails.getUsername());
        if (cliente == null) return ResponseEntity.notFound().build();
        ClienteDTO dto = modelMapper.map(cliente, ClienteDTO.class);
        dto.setUsername(cliente.getUser().getUsername());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/cliente/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteDTO> actualizarMiPerfil(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody Cliente cambios) {
        Cliente cliente = clienteService.buscarPorUsername(userDetails.getUsername());
        if (cliente == null) return ResponseEntity.notFound().build();
        cliente.setNombres(cambios.getNombres() != null ? cambios.getNombres() : cliente.getNombres());
        cliente.setApellidos(cambios.getApellidos() != null ? cambios.getApellidos() : cliente.getApellidos());
        cliente.setTelefono(cambios.getTelefono() != null ? cambios.getTelefono() : cliente.getTelefono());
        Cliente mod = clienteService.modificar(cliente);
        ClienteDTO dto = modelMapper.map(mod, ClienteDTO.class);
        dto.setUsername(mod.getUser().getUsername());
        return ResponseEntity.ok(dto);
    }
}

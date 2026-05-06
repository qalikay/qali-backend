package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.ConsultaDTO;
import com.qalikay.backend.dtos.CrearConsultaDTO;
import com.qalikay.backend.dtos.MensajeDTO;
import com.qalikay.backend.entities.Consulta;
import com.qalikay.backend.entities.Mensaje;
import com.qalikay.backend.services.ConsultaService;
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

@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class ConsultaController {

    @Autowired private ConsultaService consultaService;
    @Autowired private ModelMapper modelMapper;

    @GetMapping("/cliente/consultas")
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ConsultaDTO> misConsultasCliente(@AuthenticationPrincipal UserDetails userDetails) {
        return consultaService.listarMisConsultasComoCliente(userDetails.getUsername()).stream()
                .map(c -> modelMapper.map(c, ConsultaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/experto/consultas")
    @PreAuthorize("hasRole('EXPERTO')")
    public List<ConsultaDTO> misConsultasExperto(@AuthenticationPrincipal UserDetails userDetails) {
        return consultaService.listarMisConsultasComoExperto(userDetails.getUsername()).stream()
                .map(c -> modelMapper.map(c, ConsultaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/consultas/{id}")
    public ResponseEntity<ConsultaDTO> buscarPorId(@PathVariable Long id) {
        Consulta c = consultaService.buscarPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(c, ConsultaDTO.class));
    }

    @PostMapping("/consultas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ConsultaDTO> crear(@RequestBody CrearConsultaDTO dto,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Consulta c = consultaService.crearComoCliente(dto, userDetails.getUsername());
        return new ResponseEntity<>(modelMapper.map(c, ConsultaDTO.class), HttpStatus.CREATED);
    }

    @PostMapping("/consultas/{id}/mensajes")
    public ResponseEntity<MensajeDTO> agregarMensaje(@PathVariable Long id,
                                                     @RequestBody Map<String, String> body,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        String contenido = body.getOrDefault("contenido", "");
        Mensaje m = consultaService.agregarMensaje(id, contenido, userDetails.getUsername());
        return new ResponseEntity<>(modelMapper.map(m, MensajeDTO.class), HttpStatus.CREATED);
    }

    @PostMapping("/consultas/{id}/cerrar")
    public ResponseEntity<ConsultaDTO> cerrar(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        Consulta c = consultaService.cerrar(id, userDetails.getUsername());
        return ResponseEntity.ok(modelMapper.map(c, ConsultaDTO.class));
    }
}

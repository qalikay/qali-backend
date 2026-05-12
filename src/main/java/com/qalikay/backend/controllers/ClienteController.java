package com.qalikay.backend.controllers;

import com.qalikay.backend.dtos.ClienteDTO;
import com.qalikay.backend.entities.Categoria;
import com.qalikay.backend.entities.Cliente;
import com.qalikay.backend.services.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoints del perfil de Cliente.
 *  - /cliente/me      -> ROLE_CLIENTE consulta/edita SU propio perfil
 *  - /clientes         -> ROLE_ADMIN administra la lista de clientes
 */
@RestController
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ModelMapper modelMapper;

    // GET /api/clientes -> listado global (solo ADMIN)
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

    // GET /api/cliente/me -> perfil del cliente autenticado (sin id en URL, lo saca del JWT)
    @GetMapping("/cliente/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteDTO> miPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        Cliente cliente = clienteService.buscarPorUsername(userDetails.getUsername());
        if (cliente == null) return ResponseEntity.notFound().build();
        ClienteDTO dto = modelMapper.map(cliente, ClienteDTO.class);
        dto.setUsername(cliente.getUser().getUsername());
        return ResponseEntity.ok(dto);
    }

    // PUT /api/cliente/me -> actualizacion parcial: solo cambia los campos no nulos del body
    @PutMapping("/cliente/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteDTO> actualizarMiPerfil(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody Cliente cambios) {
        Cliente cliente = clienteService.buscarPorUsername(userDetails.getUsername());
        if (cliente == null) return ResponseEntity.notFound().build();
        // Patron de update parcial: si el campo viene null, mantenemos el valor actual
        cliente.setNombres(cambios.getNombres() != null ? cambios.getNombres() : cliente.getNombres());
        cliente.setApellidos(cambios.getApellidos() != null ? cambios.getApellidos() : cliente.getApellidos());
        cliente.setTelefono(cambios.getTelefono() != null ? cambios.getTelefono() : cliente.getTelefono());
        Cliente mod = clienteService.modificar(cliente);
        ClienteDTO dto = modelMapper.map(mod, ClienteDTO.class);
        dto.setUsername(mod.getUser().getUsername());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> crear(@RequestBody ClienteDTO clientedto) {

        Cliente cliente = new Cliente();

        cliente.setNombres(clientedto.getNombres());
        cliente.setApellidos(clientedto.getApellidos());
        cliente.setTelefono(clientedto.getTelefono());

        Cliente cli = clienteService.buscarPorUsername(clientedto.getUsername());

        if (cli == null) {
            return ResponseEntity.badRequest().build();
        }

        cliente.setUser(cli.getUser());

        Cliente creado = clienteService.insertar(cliente);

        ClienteDTO response = modelMapper.map(creado, ClienteDTO.class);
        response.setUsername(creado.getUser().getUsername());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

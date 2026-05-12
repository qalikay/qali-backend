package com.qalikay.backend.security.controllers;

import com.qalikay.backend.entities.Cliente;
import com.qalikay.backend.entities.Especialidad;
import com.qalikay.backend.entities.Experto;
import com.qalikay.backend.repositories.ClienteRepositorio;
import com.qalikay.backend.repositories.EspecialidadRepositorio;
import com.qalikay.backend.repositories.ExpertoRepositorio;
import com.qalikay.backend.security.dtos.AuthRequestDTO;
import com.qalikay.backend.security.dtos.AuthResponseDTO;
import com.qalikay.backend.security.dtos.RegistroClienteDTO;
import com.qalikay.backend.security.dtos.RegistroExpertoDTO;
import com.qalikay.backend.security.entities.Role;
import com.qalikay.backend.security.entities.User;
import com.qalikay.backend.security.repositories.RoleRepository;
import com.qalikay.backend.security.repositories.UserRepository;
import com.qalikay.backend.security.services.CustomUserDetailsService;
import com.qalikay.backend.security.services.UserService;
import com.qalikay.backend.security.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Endpoints publicos de autenticacion y registro.
 *  - POST /api/authenticate    -> valida usuario+password, devuelve JWT
 *  - POST /api/registro/cliente -> alta de cliente
 *  - POST /api/registro/experto -> alta de experto
 */
@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClienteRepositorio clienteRepositorio;
    private final ExpertoRepositorio expertoRepositorio;
    private final EspecialidadRepositorio especialidadRepositorio;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService,
                          UserService userService,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          ClienteRepositorio clienteRepositorio,
                          ExpertoRepositorio expertoRepositorio,
                          EspecialidadRepositorio especialidadRepositorio,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.clienteRepositorio = clienteRepositorio;
        this.expertoRepositorio = expertoRepositorio;
        this.especialidadRepositorio = especialidadRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    // POST /api/authenticate -> recibe {username,password} y devuelve {jwt, username, roles}
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO authRequest) {
        // 1) Spring valida credenciales contra la BD (lanza excepcion si son incorrectas)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        // 2) Carga el UserDetails (username + roles)
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        // 3) Genera el JWT firmado
        final String token = jwtUtil.generateToken(userDetails);

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Tambien lo devolvemos en el header para clientes que lo lean ahi
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + token);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setJwt(token);
        response.setUsername(userDetails.getUsername());
        response.setRoles(roles);

        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }

    // POST /api/registro/cliente -> crea User + Cliente y le asigna ROLE_CLIENTE
    @PostMapping("/registro/cliente")
    public ResponseEntity<?> registrarCliente(@RequestBody RegistroClienteDTO dto) {
        if (userService.existePorUsername(dto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }
        // Busca el rol; si no existe lo crea (defensivo)
        Role rol = roleRepository.findByName("ROLE_CLIENTE")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_CLIENTE")));

        // 1) Guardar credenciales (password siempre hasheada con BCrypt)
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(rol);
        user.setRoles(roles);
        user = userRepository.save(user);

        // 2) Guardar perfil de cliente asociado al user recien creado
        Cliente cliente = new Cliente();
        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setTelefono(dto.getTelefono());
        cliente.setUser(user);
        clienteRepositorio.save(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente registrado correctamente");
    }

    // POST /api/registro/experto -> crea User + Experto y le asigna ROLE_EXPERTO
    @PostMapping("/registro/experto")
    public ResponseEntity<?> registrarExperto(@RequestBody RegistroExpertoDTO dto) {
        if (userService.existePorUsername(dto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }
        Role rol = roleRepository.findByName("ROLE_EXPERTO")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_EXPERTO")));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(rol);
        user.setRoles(roles);
        user = userRepository.save(user);

        // Carga la especialidad si vino el id (puede registrarse sin especialidad)
        Especialidad especialidad = null;
        if (dto.getEspecialidadId() != null) {
            especialidad = especialidadRepositorio.findById(dto.getEspecialidadId()).orElse(null);
        }

        Experto experto = new Experto();
        experto.setNombres(dto.getNombres());
        experto.setApellidos(dto.getApellidos());
        experto.setTelefono(dto.getTelefono());
        experto.setTrayectoria(dto.getTrayectoria());
        experto.setAnosExperiencia(dto.getAnosExperiencia());
        experto.setEspecialidad(especialidad);
        experto.setUser(user);
        expertoRepositorio.save(experto);

        return ResponseEntity.status(HttpStatus.CREATED).body("Experto registrado correctamente");
    }
}

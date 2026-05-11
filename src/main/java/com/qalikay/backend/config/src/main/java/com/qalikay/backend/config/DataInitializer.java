package com.qalikay.backend.config;

import com.qalikay.backend.entities.Categoria;
import com.qalikay.backend.entities.Cliente;
import com.qalikay.backend.entities.Especialidad;
import com.qalikay.backend.entities.Experto;
import com.qalikay.backend.repositories.CategoriaRepositorio;
import com.qalikay.backend.repositories.ClienteRepositorio;
import com.qalikay.backend.repositories.EspecialidadRepositorio;
import com.qalikay.backend.repositories.ExpertoRepositorio;
import com.qalikay.backend.security.entities.Role;
import com.qalikay.backend.security.entities.User;
import com.qalikay.backend.security.repositories.RoleRepository;
import com.qalikay.backend.security.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ClienteRepositorio clienteRepositorio;
    private final ExpertoRepositorio expertoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final EspecialidadRepositorio especialidadRepositorio;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           ClienteRepositorio clienteRepositorio,
                           ExpertoRepositorio expertoRepositorio,
                           CategoriaRepositorio categoriaRepositorio,
                           EspecialidadRepositorio especialidadRepositorio,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.clienteRepositorio = clienteRepositorio;
        this.expertoRepositorio = expertoRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.especialidadRepositorio = especialidadRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role adminRole = obtenerOCrearRol("ROLE_ADMIN");
        Role clienteRole = obtenerOCrearRol("ROLE_CLIENTE");
        Role expertoRole = obtenerOCrearRol("ROLE_EXPERTO");

        crearCategorias();
        Especialidad especialidad = obtenerOCrearEspecialidad();

        crearAdmin(adminRole);
        crearClienteDemo(clienteRole);
        crearExpertoDemo(expertoRole, especialidad);
    }

    private Role obtenerOCrearRol(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(null, name)));
    }

    private void crearCategorias() {
        crearCategoria("Infusiones", "Preparaciones naturales para consumo diario.");
        crearCategoria("Pomadas", "Productos topicos elaborados con insumos naturales.");
        crearCategoria("Jarabes", "Preparaciones naturales para malestares comunes.");
    }

    private void crearCategoria(String nombre, String descripcion) {
        if (!categoriaRepositorio.existsByNombre(nombre)) {
            categoriaRepositorio.save(new Categoria(null, nombre, descripcion));
        }
    }

    private Especialidad obtenerOCrearEspecialidad() {
        String nombre = "Medicina natural andina";

        if (!especialidadRepositorio.existsByNombre(nombre)) {
            return especialidadRepositorio.save(new Especialidad(
                    null,
                    nombre,
                    "Uso tradicional de plantas medicinales y saberes ancestrales."
            ));
        }

        return especialidadRepositorio.findAll()
                .stream()
                .filter(e -> nombre.equals(e.getNombre()))
                .findFirst()
                .orElseThrow();
    }

    private void crearAdmin(Role role) {
        if (!userRepository.existsByUsername("admin")) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRoles(Set.of(role));
            userRepository.save(user);
        }
    }

    private void crearClienteDemo(Role role) {
        if (!userRepository.existsByUsername("cliente")) {
            User user = new User();
            user.setUsername("cliente");
            user.setPassword(passwordEncoder.encode("cliente123"));
            user.setRoles(Set.of(role));
            user = userRepository.save(user);

            Cliente cliente = new Cliente();
            cliente.setNombres("Cliente");
            cliente.setApellidos("Demo");
            cliente.setTelefono("999888777");
            cliente.setUser(user);
            clienteRepositorio.save(cliente);
        }
    }

    private void crearExpertoDemo(Role role, Especialidad especialidad) {
        if (!userRepository.existsByUsername("experto")) {
            User user = new User();
            user.setUsername("experto");
            user.setPassword(passwordEncoder.encode("experto123"));
            user.setRoles(Set.of(role));
            user = userRepository.save(user);

            Experto experto = new Experto();
            experto.setNombres("Experto");
            experto.setApellidos("Demo");
            experto.setTelefono("988777666");
            experto.setTrayectoria("Especialista en medicina natural andina.");
            experto.setAnosExperiencia(5);
            experto.setEspecialidad(especialidad);
            experto.setUser(user);
            expertoRepositorio.save(experto);
        }
    }
}


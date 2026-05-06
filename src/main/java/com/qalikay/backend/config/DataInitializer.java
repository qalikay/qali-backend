package com.qalikay.backend.config;

import com.qalikay.backend.entities.Categoria;
import com.qalikay.backend.entities.Cliente;
import com.qalikay.backend.entities.Especialidad;
import com.qalikay.backend.entities.Experto;
import com.qalikay.backend.entities.Receta;
import com.qalikay.backend.entities.Insumo;
import com.qalikay.backend.repositories.CategoriaRepositorio;
import com.qalikay.backend.repositories.ClienteRepositorio;
import com.qalikay.backend.repositories.EspecialidadRepositorio;
import com.qalikay.backend.repositories.ExpertoRepositorio;
import com.qalikay.backend.repositories.RecetaRepositorio;
import com.qalikay.backend.repositories.InsumoRepositorio;
import com.qalikay.backend.security.entities.Role;
import com.qalikay.backend.security.entities.User;
import com.qalikay.backend.security.repositories.RoleRepository;
import com.qalikay.backend.security.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Carga datos iniciales: roles base, categorias, especialidades y un usuario admin demo.
 */
@Component
public class DataInitializer {

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoriaRepositorio categoriaRepositorio;
    @Autowired private EspecialidadRepositorio especialidadRepositorio;
    @Autowired private ClienteRepositorio clienteRepositorio;
    @Autowired private ExpertoRepositorio expertoRepositorio;
    @Autowired private RecetaRepositorio recetaRepositorio;
    @Autowired private InsumoRepositorio insumoRepositorio;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // --- Roles ---
        Role rolAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));
        Role rolCliente = roleRepository.findByName("ROLE_CLIENTE")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_CLIENTE")));
        Role rolExperto = roleRepository.findByName("ROLE_EXPERTO")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_EXPERTO")));

        // --- Categorias ---
        if (categoriaRepositorio.count() == 0) {
            categoriaRepositorio.save(new Categoria(null, "Digestivo", "Recetas y plantas para el sistema digestivo"));
            categoriaRepositorio.save(new Categoria(null, "Respiratorio", "Recetas y plantas para vias respiratorias"));
            categoriaRepositorio.save(new Categoria(null, "Relajante", "Plantas y recetas para relajacion y sueno"));
            categoriaRepositorio.save(new Categoria(null, "Energizante", "Plantas con propiedades energizantes"));
            categoriaRepositorio.save(new Categoria(null, "Antiinflamatorio", "Plantas con propiedades antiinflamatorias"));
            categoriaRepositorio.save(new Categoria(null, "Inmunidad", "Recetas para reforzar el sistema inmune"));
        }

        // --- Especialidades ---
        if (especialidadRepositorio.count() == 0) {
            especialidadRepositorio.save(new Especialidad(null, "Herbolaria Andina", "Conocimiento de plantas andinas"));
            especialidadRepositorio.save(new Especialidad(null, "Aromaterapia", "Uso terapeutico de aceites esenciales"));
            especialidadRepositorio.save(new Especialidad(null, "Nutricion Natural", "Alimentacion y plantas medicinales"));
            especialidadRepositorio.save(new Especialidad(null, "Medicina Tradicional", "Practicas ancestrales andinas"));
        }

        // --- Usuarios demo ---
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            Set<Role> roles = new HashSet<>();
            roles.add(rolAdmin);
            admin.setRoles(roles);
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("cliente")) {
            User u = new User();
            u.setUsername("cliente");
            u.setPassword(passwordEncoder.encode("cliente123"));
            Set<Role> roles = new HashSet<>();
            roles.add(rolCliente);
            u.setRoles(roles);
            u = userRepository.save(u);

            Cliente c = new Cliente();
            c.setNombres("Maria");
            c.setApellidos("Quispe");
            c.setTelefono("+51999111222");
            c.setUser(u);
            clienteRepositorio.save(c);
        }

        if (!userRepository.existsByUsername("experto")) {
            User u = new User();
            u.setUsername("experto");
            u.setPassword(passwordEncoder.encode("experto123"));
            Set<Role> roles = new HashSet<>();
            roles.add(rolExperto);
            u.setRoles(roles);
            u = userRepository.save(u);

            Experto e = new Experto();
            e.setNombres("Jose");
            e.setApellidos("Mamani");
            e.setTelefono("+51999333444");
            e.setTrayectoria("15 anos como herbolario en la sierra de Cusco");
            e.setAnosExperiencia(15);
            e.setEspecialidad(especialidadRepositorio.findAll().stream().findFirst().orElse(null));
            e.setUser(u);
            expertoRepositorio.save(e);

            // Recetas demo PUBLICADAS
            if (recetaRepositorio.count() == 0) {
                Categoria digestivo = categoriaRepositorio.findAll().stream()
                        .filter(c -> c.getNombre().equals("Digestivo")).findFirst().orElse(null);
                Categoria relajante = categoriaRepositorio.findAll().stream()
                        .filter(c -> c.getNombre().equals("Relajante")).findFirst().orElse(null);

                Receta r1 = new Receta();
                r1.setTitulo("Infusion de muna para la digestion");
                r1.setDescripcion("Infusion tradicional andina para malestares estomacales");
                r1.setIngredientes("- 1 cucharadita de hojas secas de muna\n- 250 ml de agua hirviendo\n- Miel al gusto");
                r1.setPreparacion("1. Calentar el agua hasta hervir\n2. Agregar las hojas de muna\n3. Dejar reposar 5 minutos\n4. Colar y endulzar con miel");
                r1.setAdvertencias("No consumir durante el embarazo");
                r1.setMinutosPreparacion(10);
                r1.setPrecio(0d);
                r1.setEstado("PUBLICADA");
                r1.setCategoria(digestivo);
                r1.setExperto(e);
                recetaRepositorio.save(r1);

                Receta r2 = new Receta();
                r2.setTitulo("Te de manzanilla con valeriana");
                r2.setDescripcion("Para conciliar el sueno de forma natural");
                r2.setIngredientes("- 1 cucharadita de manzanilla\n- 1/2 cucharadita de valeriana\n- 250 ml de agua\n- Miel");
                r2.setPreparacion("1. Hervir el agua\n2. Agregar manzanilla y valeriana\n3. Reposar 7 minutos\n4. Colar y endulzar");
                r2.setAdvertencias("No conducir despues de tomar");
                r2.setMinutosPreparacion(12);
                r2.setPrecio(5.5);
                r2.setEstado("PUBLICADA");
                r2.setCategoria(relajante);
                r2.setExperto(e);
                recetaRepositorio.save(r2);
            }

            // Insumos demo
            if (insumoRepositorio.count() == 0) {
                Categoria digestivo = categoriaRepositorio.findAll().stream()
                        .filter(c -> c.getNombre().equals("Digestivo")).findFirst().orElse(null);
                Categoria relajante = categoriaRepositorio.findAll().stream()
                        .filter(c -> c.getNombre().equals("Relajante")).findFirst().orElse(null);

                Insumo i1 = new Insumo();
                i1.setNombre("Hojas secas de muna");
                i1.setDescripcion("Muna recolectada en la sierra de Cusco, secada al sol");
                i1.setPrecio(8.0);
                i1.setStock(50);
                i1.setUnidad("paquete 50g");
                i1.setTipo("HIERBA");
                i1.setEstado("DISPONIBLE");
                i1.setCategoria(digestivo);
                i1.setExperto(e);
                insumoRepositorio.save(i1);

                Insumo i2 = new Insumo();
                i2.setNombre("Aceite esencial de eucalipto");
                i2.setDescripcion("100% puro, ideal para vaporizaciones");
                i2.setPrecio(25.0);
                i2.setStock(20);
                i2.setUnidad("frasco 30ml");
                i2.setTipo("ACEITE");
                i2.setEstado("DISPONIBLE");
                i2.setCategoria(relajante);
                i2.setExperto(e);
                insumoRepositorio.save(i2);
            }
        }
    }
}

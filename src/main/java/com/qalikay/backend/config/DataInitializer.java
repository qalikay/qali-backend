package com.qalikay.backend.config;

import com.qalikay.backend.recipe.domain.Categoria;
import com.qalikay.backend.recipe.repository.CategoriaRepository;
import com.qalikay.backend.user.domain.Especialidad;
import com.qalikay.backend.user.domain.Rol;
import com.qalikay.backend.user.domain.RolNombre;
import com.qalikay.backend.user.repository.EspecialidadRepository;
import com.qalikay.backend.user.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Carga datos iniciales en la BD al arrancar la aplicacion.
 *
 * Solo carga catalogos basicos (roles y especialidades) si no existen.
 * Es seguro ejecutarlo varias veces: usa existsByNombre antes de insertar.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final EspecialidadRepository especialidadRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("===> Inicializando datos base de QaliKay...");
        cargarRoles();
        cargarEspecialidades();
        cargarCategorias();
        log.info("===> Datos base inicializados correctamente.");
    }

    private void cargarRoles() {
        crearRolSiNoExiste(RolNombre.CLIENTE, "Usuario consumidor que busca y compra recetas");
        crearRolSiNoExiste(RolNombre.EXPERTO, "Usuario que publica recetas y vende insumos naturales");
        crearRolSiNoExiste(RolNombre.ADMIN, "Administrador de la plataforma");
    }

    private void crearRolSiNoExiste(RolNombre nombre, String descripcion) {
        if (!rolRepository.existsByNombre(nombre)) {
            rolRepository.save(Rol.builder()
                    .nombre(nombre)
                    .descripcion(descripcion)
                    .build());
            log.info("  + Rol creado: {}", nombre);
        }
    }

    private void cargarEspecialidades() {
        List<String[]> especialidades = List.of(
                new String[]{"Herbolaria Andina", "Uso medicinal de plantas y hierbas de los Andes peruanos"},
                new String[]{"Medicina Tradicional Amazonica", "Conocimiento ancestral de plantas medicinales de la selva"},
                new String[]{"Fitoterapia", "Tratamiento basado en extractos y preparados de plantas"},
                new String[]{"Aromaterapia", "Uso terapeutico de aceites esenciales"},
                new String[]{"Naturopatia", "Enfoque holistico de salud usando recursos naturales"},
                new String[]{"Apiterapia", "Uso medicinal de productos de las abejas (miel, propoleo, polen)"}
        );

        for (String[] esp : especialidades) {
            if (!especialidadRepository.existsByNombre(esp[0])) {
                especialidadRepository.save(Especialidad.builder()
                        .nombre(esp[0])
                        .descripcion(esp[1])
                        .build());
                log.info("  + Especialidad creada: {}", esp[0]);
            }
        }
    }

    private void cargarCategorias() {
        List<String[]> categorias = List.of(
                new String[]{"DIGESTIVO",     "Recetas e insumos para el sistema digestivo (gastritis, colon, estrenimiento)", "leaf"},
                new String[]{"RESPIRATORIO",  "Para tos, gripe, asma, bronquios y vias respiratorias",                          "wind"},
                new String[]{"CIRCULATORIO",  "Para presion, varices, colesterol y circulacion sanguinea",                       "heart"},
                new String[]{"DERMATOLOGICO", "Para piel, acne, manchas, heridas y problemas dermatologicos",                    "sparkle"},
                new String[]{"RELAJANTE",     "Para ansiedad, estres, insomnio y relajacion",                                    "moon"},
                new String[]{"ENERGIZANTE",   "Para fatiga, cansancio y aumentar energia natural",                               "sun"},
                new String[]{"INMUNOLOGICO",  "Para fortalecer las defensas y prevenir enfermedades",                            "shield"},
                new String[]{"ANALGESICO",    "Para dolores musculares, articulares, migrana y dolor en general",                "pill"},
                new String[]{"FEMENINO",      "Salud femenina: ciclos, menopausia, fertilidad",                                  "flower"},
                new String[]{"OTROS",         "Otras categorias no clasificadas",                                                "more"}
        );

        for (String[] cat : categorias) {
            if (!categoriaRepository.existsByNombre(cat[0])) {
                categoriaRepository.save(Categoria.builder()
                        .nombre(cat[0])
                        .descripcion(cat[1])
                        .icono(cat[2])
                        .build());
                log.info("  + Categoria creada: {}", cat[0]);
            }
        }
    }
}

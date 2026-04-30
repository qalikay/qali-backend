package com.qalikay.backend.user.controller;

import com.qalikay.backend.user.domain.Especialidad;
import com.qalikay.backend.user.domain.Rol;
import com.qalikay.backend.user.repository.EspecialidadRepository;
import com.qalikay.backend.user.repository.RolRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints publicos de catalogos del sistema.
 *
 * Sirven para que el frontend popule selects/dropdowns
 * con datos de referencia (roles, especialidades).
 */
@RestController
@RequestMapping("/catalogs")
@RequiredArgsConstructor
@Tag(name = "Catalogos", description = "Listas de referencia: roles, especialidades, etc.")
public class CatalogoController {

    private final RolRepository rolRepository;
    private final EspecialidadRepository especialidadRepository;

    @GetMapping("/roles")
    @Operation(
            summary = "Listar roles disponibles",
            description = "Devuelve los roles que un usuario puede tener al registrarse: CLIENTE, EXPERTO, ADMIN."
    )
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @GetMapping("/especialidades")
    @Operation(
            summary = "Listar especialidades de expertos",
            description = "Devuelve el catalogo de especialidades disponibles para los expertos: " +
                    "Herbolaria Andina, Fitoterapia, Aromaterapia, etc."
    )
    public List<Especialidad> listarEspecialidades() {
        return especialidadRepository.findAll();
    }
}

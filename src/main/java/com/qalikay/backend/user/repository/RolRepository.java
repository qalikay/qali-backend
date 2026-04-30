package com.qalikay.backend.user.repository;

import com.qalikay.backend.user.domain.Rol;
import com.qalikay.backend.user.domain.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para acceder a la tabla rol.
 *
 * JpaRepository nos provee automaticamente:
 * - findAll(), findById(id), save(entity), deleteById(id), count(), etc.
 *
 * Solo definimos metodos personalizados aqui.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    /** Busca un rol por su nombre (CLIENTE, EXPERTO, ADMIN). */
    Optional<Rol> findByNombre(RolNombre nombre);

    /** Verifica si existe un rol con ese nombre. */
    boolean existsByNombre(RolNombre nombre);
}

package com.qalikay.backend.user.repository;

import com.qalikay.backend.user.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /** Busca un usuario por su correo electronico. Usado en login y registro. */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Busca un usuario y carga sus roles en la misma query.
     * Evita el problema N+1 y LazyInitializationException en autenticacion.
     */
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.correo = :correo")
    Optional<Usuario> findByCorreoWithRoles(@Param("correo") String correo);

    /** Verifica si ya existe un usuario con ese correo (validacion de registro). */
    boolean existsByCorreo(String correo);
}

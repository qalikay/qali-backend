package com.qalikay.backend.repositories;

import com.qalikay.backend.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUserUsername(String username);
}

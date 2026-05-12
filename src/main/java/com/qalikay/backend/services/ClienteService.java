package com.qalikay.backend.services;

import com.qalikay.backend.entities.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> listar();
    Cliente buscarPorId(Long id);
    Cliente buscarPorUsername(String username);
    Cliente insertar(Cliente cliente);
    Cliente modificar(Cliente cliente);
    void eliminar(Long id);
}

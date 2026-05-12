package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.entities.Cliente;
import com.qalikay.backend.repositories.ClienteRepositorio;
import com.qalikay.backend.services.ClienteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Override
    public List<Cliente> listar() {
        return clienteRepositorio.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepositorio.findById(id).orElse(null);
    }

    @Override
    public Cliente buscarPorUsername(String username) {
        return clienteRepositorio.findByUserUsername(username).orElse(null);
    }

    @Transactional
    @Override
    public Cliente insertar(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    @Transactional
    @Override
    public Cliente modificar(Cliente cliente) {
        if (clienteRepositorio.findById(cliente.getId()).isPresent()) {
            return clienteRepositorio.save(cliente);
        }
        return null;
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        if (clienteRepositorio.existsById(id)) {
            clienteRepositorio.deleteById(id);
        }
    }
}

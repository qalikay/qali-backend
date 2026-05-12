package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.dtos.CrearResenaDTO;
import com.qalikay.backend.entities.Cliente;
import com.qalikay.backend.entities.Resena;
import com.qalikay.backend.repositories.ClienteRepositorio;
import com.qalikay.backend.repositories.ResenaRepositorio;
import com.qalikay.backend.services.ResenaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResenaServiceImpl implements ResenaService {

    @Autowired
    private ResenaRepositorio resenaRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Override
    public List<Resena> listarPorItem(String tipoItem, Long itemId) {
        return resenaRepositorio.findByTipoItemAndItemId(tipoItem, itemId);
    }

    @Override
    public List<Resena> listarMisResenas(String username) {
        Cliente cliente = clienteRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        return resenaRepositorio.findByClienteId(cliente.getId());
    }

    @Transactional
    @Override
    public Resena crearComoCliente(CrearResenaDTO dto, String username) {
        Cliente cliente = clienteRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        if (dto.getCalificacion() == null || dto.getCalificacion() < 1 || dto.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificacion debe ser entre 1 y 5");
        }

        Resena resena = new Resena();
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setTipoItem(dto.getTipoItem());
        resena.setItemId(dto.getItemId());
        resena.setCliente(cliente);
        resena.setFechaCreacion(LocalDateTime.now());
        return resenaRepositorio.save(resena);
    }

    @Transactional
    @Override
    public void eliminar(Long id, String username) {
        Resena resena = resenaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resena no encontrada: " + id));
        if (resena.getCliente() == null
                || resena.getCliente().getUser() == null
                || !resena.getCliente().getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Esta resena no le pertenece");
        }
        resenaRepositorio.delete(resena);
    }
}

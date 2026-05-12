package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.dtos.CrearInsumoDTO;
import com.qalikay.backend.entities.Categoria;
import com.qalikay.backend.entities.Experto;
import com.qalikay.backend.entities.Insumo;
import com.qalikay.backend.repositories.CategoriaRepositorio;
import com.qalikay.backend.repositories.ExpertoRepositorio;
import com.qalikay.backend.repositories.InsumoRepositorio;
import com.qalikay.backend.services.InsumoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsumoServiceImpl implements InsumoService {

    @Autowired
    private InsumoRepositorio insumoRepositorio;

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Autowired
    private ExpertoRepositorio expertoRepositorio;

    @Override
    public List<Insumo> listar() {
        return insumoRepositorio.findAll();
    }

    @Override
    public List<Insumo> listarPorCategoria(Long categoriaId) {
        return insumoRepositorio.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Insumo> listarPorTipo(String tipo) {
        return insumoRepositorio.findByTipo(tipo);
    }

    @Override
    public List<Insumo> buscarPorNombre(String nombre) {
        return insumoRepositorio.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Insumo> listarPorExperto(Long expertoId) {
        return insumoRepositorio.findByExpertoId(expertoId);
    }

    @Override
    public Insumo buscarPorId(Long id) {
        return insumoRepositorio.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Insumo crearComoExperto(CrearInsumoDTO dto, String username) {
        Experto experto = expertoRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado"));

        Insumo insumo = new Insumo();
        copiarDatos(insumo, dto);
        insumo.setExperto(experto);
        insumo.setEstado(dto.getStock() != null && dto.getStock() > 0 ? "DISPONIBLE" : "AGOTADO");
        return insumoRepositorio.save(insumo);
    }

    @Transactional
    @Override
    public Insumo modificarComoExperto(Long id, CrearInsumoDTO dto, String username) {
        Insumo insumo = buscarYValidarPropietario(id, username);
        copiarDatos(insumo, dto);
        insumo.setEstado(dto.getStock() != null && dto.getStock() > 0 ? "DISPONIBLE" : "AGOTADO");
        return insumoRepositorio.save(insumo);
    }

    @Transactional
    @Override
    public void eliminar(Long id, String username) {
        Insumo insumo = buscarYValidarPropietario(id, username);
        insumoRepositorio.delete(insumo);
    }

    private void copiarDatos(Insumo insumo, CrearInsumoDTO dto) {
        insumo.setNombre(dto.getNombre());
        insumo.setDescripcion(dto.getDescripcion());
        insumo.setPrecio(dto.getPrecio());
        insumo.setStock(dto.getStock());
        insumo.setUnidad(dto.getUnidad());
        insumo.setTipo(dto.getTipo());
        insumo.setImagenUrl(dto.getImagenUrl());
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepositorio.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada: " + dto.getCategoriaId()));
            insumo.setCategoria(categoria);
        }
    }

    private Insumo buscarYValidarPropietario(Long id, String username) {
        Insumo insumo = insumoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado: " + id));
        if (insumo.getExperto() == null
                || insumo.getExperto().getUser() == null
                || !insumo.getExperto().getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Este insumo no pertenece al experto autenticado");
        }
        return insumo;
    }
}

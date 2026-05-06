package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.entities.Categoria;
import com.qalikay.backend.repositories.CategoriaRepositorio;
import com.qalikay.backend.services.CategoriaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Override
    public List<Categoria> listar() {
        return categoriaRepositorio.findAll();
    }

    @Override
    public Categoria buscarPorId(Long id) {
        return categoriaRepositorio.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Categoria insertar(Categoria categoria) {
        return categoriaRepositorio.save(categoria);
    }

    @Transactional
    @Override
    public Categoria modificar(Categoria categoria) {
        if (categoriaRepositorio.findById(categoria.getId()).isPresent()) {
            return categoriaRepositorio.save(categoria);
        }
        return null;
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        if (categoriaRepositorio.existsById(id)) {
            categoriaRepositorio.deleteById(id);
        }
    }
}

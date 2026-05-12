package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.entities.Experto;
import com.qalikay.backend.repositories.ExpertoRepositorio;
import com.qalikay.backend.services.ExpertoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpertoServiceImpl implements ExpertoService {

    @Autowired
    private ExpertoRepositorio expertoRepositorio;

    @Override
    public List<Experto> listar() {
        return expertoRepositorio.findAll();
    }

    @Override
    public Experto buscarPorId(Long id) {
        return expertoRepositorio.findById(id).orElse(null);
    }

    @Override
    public Experto buscarPorUsername(String username) {
        return expertoRepositorio.findByUserUsername(username).orElse(null);
    }

    @Override
    public List<Experto> listarPorEspecialidad(Long especialidadId) {
        return expertoRepositorio.findByEspecialidadId(especialidadId);
    }

    @Transactional
    @Override
    public Experto insertar(Experto experto) {
        return expertoRepositorio.save(experto);
    }

    @Transactional
    @Override
    public Experto modificar(Experto experto) {
        if (expertoRepositorio.findById(experto.getId()).isPresent()) {
            return expertoRepositorio.save(experto);
        }
        return null;
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        if (expertoRepositorio.existsById(id)) {
            expertoRepositorio.deleteById(id);
        }
    }
}

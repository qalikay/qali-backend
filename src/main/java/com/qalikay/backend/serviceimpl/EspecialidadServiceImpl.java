package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.entities.Especialidad;
import com.qalikay.backend.repositories.EspecialidadRepositorio;
import com.qalikay.backend.services.EspecialidadService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {

    @Autowired
    private EspecialidadRepositorio especialidadRepositorio;

    @Override
    public List<Especialidad> listar() {
        return especialidadRepositorio.findAll();
    }

    @Override
    public Especialidad buscarPorId(Long id) {
        return especialidadRepositorio.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Especialidad insertar(Especialidad especialidad) {
        return especialidadRepositorio.save(especialidad);
    }

    @Transactional
    @Override
    public Especialidad modificar(Especialidad especialidad) {
        if (especialidadRepositorio.findById(especialidad.getId()).isPresent()) {
            return especialidadRepositorio.save(especialidad);
        }
        return null;
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        if (especialidadRepositorio.existsById(id)) {
            especialidadRepositorio.deleteById(id);
        }
    }
}

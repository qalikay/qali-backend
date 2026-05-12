package com.qalikay.backend.services;

import com.qalikay.backend.entities.Especialidad;

import java.util.List;

public interface EspecialidadService {
    List<Especialidad> listar();
    Especialidad buscarPorId(Long id);
    Especialidad insertar(Especialidad especialidad);
    Especialidad modificar(Especialidad especialidad);
    void eliminar(Long id);
}

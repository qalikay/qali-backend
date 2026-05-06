package com.qalikay.backend.services;

import com.qalikay.backend.entities.Experto;

import java.util.List;

public interface ExpertoService {
    List<Experto> listar();
    Experto buscarPorId(Long id);
    Experto buscarPorUsername(String username);
    List<Experto> listarPorEspecialidad(Long especialidadId);
    Experto insertar(Experto experto);
    Experto modificar(Experto experto);
    void eliminar(Long id);
}

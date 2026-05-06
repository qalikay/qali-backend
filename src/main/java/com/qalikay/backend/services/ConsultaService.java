package com.qalikay.backend.services;

import com.qalikay.backend.dtos.CrearConsultaDTO;
import com.qalikay.backend.entities.Consulta;
import com.qalikay.backend.entities.Mensaje;

import java.util.List;

public interface ConsultaService {
    List<Consulta> listarMisConsultasComoCliente(String username);
    List<Consulta> listarMisConsultasComoExperto(String username);
    Consulta buscarPorId(Long id);
    Consulta crearComoCliente(CrearConsultaDTO dto, String username);
    Mensaje agregarMensaje(Long consultaId, String contenido, String username);
    Consulta cerrar(Long id, String username);
}

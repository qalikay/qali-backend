package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.dtos.CrearConsultaDTO;
import com.qalikay.backend.entities.Cliente;
import com.qalikay.backend.entities.Consulta;
import com.qalikay.backend.entities.Experto;
import com.qalikay.backend.entities.Mensaje;
import com.qalikay.backend.repositories.ClienteRepositorio;
import com.qalikay.backend.repositories.ConsultaRepositorio;
import com.qalikay.backend.repositories.ExpertoRepositorio;
import com.qalikay.backend.repositories.MensajeRepositorio;
import com.qalikay.backend.services.ConsultaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaServiceImpl implements ConsultaService {

    @Autowired
    private ConsultaRepositorio consultaRepositorio;

    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ExpertoRepositorio expertoRepositorio;

    @Override
    public List<Consulta> listarMisConsultasComoCliente(String username) {
        Cliente cliente = clienteRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        return consultaRepositorio.findByClienteId(cliente.getId());
    }

    @Override
    public List<Consulta> listarMisConsultasComoExperto(String username) {
        Experto experto = expertoRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado"));
        return consultaRepositorio.findByExpertoId(experto.getId());
    }

    @Override
    public Consulta buscarPorId(Long id) {
        return consultaRepositorio.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Consulta crearComoCliente(CrearConsultaDTO dto, String username) {
        Cliente cliente = clienteRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Experto experto = expertoRepositorio.findById(dto.getExpertoId())
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado: " + dto.getExpertoId()));

        Consulta consulta = new Consulta();
        consulta.setAsunto(dto.getAsunto());
        consulta.setEstado("ABIERTA");
        consulta.setCliente(cliente);
        consulta.setExperto(experto);
        consulta.setFechaCreacion(LocalDateTime.now());
        consulta = consultaRepositorio.save(consulta);

        if (dto.getMensajeInicial() != null && !dto.getMensajeInicial().isBlank()) {
            Mensaje msg = new Mensaje();
            msg.setContenido(dto.getMensajeInicial());
            msg.setRemitente("CLIENTE");
            msg.setFechaEnvio(LocalDateTime.now());
            msg.setConsulta(consulta);
            mensajeRepositorio.save(msg);
        }

        return consultaRepositorio.findById(consulta.getId()).orElse(consulta);
    }

    @Transactional
    @Override
    public Mensaje agregarMensaje(Long consultaId, String contenido, String username) {
        Consulta consulta = consultaRepositorio.findById(consultaId)
                .orElseThrow(() -> new IllegalArgumentException("Consulta no encontrada: " + consultaId));

        String remitente;
        if (consulta.getCliente() != null && consulta.getCliente().getUser() != null
                && consulta.getCliente().getUser().getUsername().equals(username)) {
            remitente = "CLIENTE";
        } else if (consulta.getExperto() != null && consulta.getExperto().getUser() != null
                && consulta.getExperto().getUser().getUsername().equals(username)) {
            remitente = "EXPERTO";
        } else {
            throw new IllegalArgumentException("No participa en esta consulta");
        }

        Mensaje msg = new Mensaje();
        msg.setContenido(contenido);
        msg.setRemitente(remitente);
        msg.setFechaEnvio(LocalDateTime.now());
        msg.setConsulta(consulta);
        return mensajeRepositorio.save(msg);
    }

    @Transactional
    @Override
    public Consulta cerrar(Long id, String username) {
        Consulta consulta = consultaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta no encontrada: " + id));
        consulta.setEstado("CERRADA");
        return consultaRepositorio.save(consulta);
    }
}

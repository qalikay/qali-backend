package com.qalikay.backend.consultation.service;

import com.qalikay.backend.consultation.domain.Consulta;
import com.qalikay.backend.consultation.domain.EstadoConsulta;
import com.qalikay.backend.consultation.domain.Mensaje;
import com.qalikay.backend.consultation.dto.ConsultationCreateRequest;
import com.qalikay.backend.consultation.dto.ConsultationMapper;
import com.qalikay.backend.consultation.dto.ConsultationResponse;
import com.qalikay.backend.consultation.dto.ConsultationSummary;
import com.qalikay.backend.consultation.dto.MessageRequest;
import com.qalikay.backend.consultation.repository.ConsultaRepository;
import com.qalikay.backend.consultation.repository.MensajeRepository;
import com.qalikay.backend.shared.exception.BusinessException;
import com.qalikay.backend.shared.exception.ResourceNotFoundException;
import com.qalikay.backend.user.domain.RolNombre;
import com.qalikay.backend.user.domain.Usuario;
import com.qalikay.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultaRepository consultaRepository;
    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConsultationMapper mapper;

    @Transactional
    public ConsultationResponse create(String clientEmail, ConsultationCreateRequest request) {
        Usuario cliente = loadUser(clientEmail);
        Usuario experto = usuarioRepository.findById(request.expertId())
                .orElseThrow(() -> new ResourceNotFoundException("Experto", request.expertId()));
        if (!experto.tieneRol(RolNombre.EXPERTO)) {
            throw new BusinessException("El usuario destino no es un experto");
        }
        if (cliente.getId().equals(experto.getId())) {
            throw new BusinessException("No puedes consultarte a ti mismo");
        }

        Consulta consulta = Consulta.builder()
                .cliente(cliente)
                .experto(experto)
                .asunto(request.subject())
                .estado(EstadoConsulta.SOLICITADA)
                .build();

        Mensaje primero = Mensaje.builder()
                .consulta(consulta)
                .emisor(cliente)
                .contenido(request.initialMessage())
                .leido(false)
                .build();
        consulta.getMensajes().add(primero);

        Consulta guardada = consultaRepository.save(consulta);
        log.info("Consulta creada id={} cliente={} experto={}",
                guardada.getId(), clientEmail, experto.getCorreo());
        return mapper.toResponse(guardada);
    }

    @Transactional
    public ConsultationResponse accept(String userEmail, Long consultationId) {
        Consulta consulta = loadParticipating(consultationId, userEmail);
        if (!consulta.getExperto().getCorreo().equalsIgnoreCase(userEmail)) {
            throw new BusinessException("Solo el experto puede aceptar la consulta", HttpStatus.FORBIDDEN);
        }
        if (consulta.getEstado() != EstadoConsulta.SOLICITADA) {
            throw new BusinessException("La consulta no esta en estado SOLICITADA");
        }
        consulta.setEstado(EstadoConsulta.ACEPTADA);
        return mapper.toResponse(consulta);
    }

    @Transactional
    public ConsultationResponse reject(String userEmail, Long consultationId) {
        Consulta consulta = loadParticipating(consultationId, userEmail);
        if (!consulta.getExperto().getCorreo().equalsIgnoreCase(userEmail)) {
            throw new BusinessException("Solo el experto puede rechazar la consulta", HttpStatus.FORBIDDEN);
        }
        if (consulta.getEstado() != EstadoConsulta.SOLICITADA) {
            throw new BusinessException("La consulta no esta en estado SOLICITADA");
        }
        consulta.setEstado(EstadoConsulta.RECHAZADA);
        return mapper.toResponse(consulta);
    }

    @Transactional
    public ConsultationResponse complete(String userEmail, Long consultationId) {
        Consulta consulta = loadParticipating(consultationId, userEmail);
        if (consulta.getEstado() != EstadoConsulta.ACEPTADA) {
            throw new BusinessException("Solo se pueden completar consultas ACEPTADAS");
        }
        consulta.setEstado(EstadoConsulta.COMPLETADA);
        return mapper.toResponse(consulta);
    }

    @Transactional
    public ConsultationResponse cancel(String userEmail, Long consultationId) {
        Consulta consulta = loadParticipating(consultationId, userEmail);
        if (!consulta.estaAbierta()) {
            throw new BusinessException("Solo se pueden cancelar consultas abiertas");
        }
        consulta.setEstado(EstadoConsulta.CANCELADA);
        return mapper.toResponse(consulta);
    }

    @Transactional
    public ConsultationResponse sendMessage(String userEmail, Long consultationId, MessageRequest request) {
        Consulta consulta = loadParticipating(consultationId, userEmail);
        if (consulta.getEstado() != EstadoConsulta.ACEPTADA) {
            throw new BusinessException("Solo se pueden enviar mensajes en consultas ACEPTADAS");
        }
        Usuario emisor = consulta.getCliente().getCorreo().equalsIgnoreCase(userEmail)
                ? consulta.getCliente()
                : consulta.getExperto();

        Mensaje mensaje = Mensaje.builder()
                .consulta(consulta)
                .emisor(emisor)
                .contenido(request.content())
                .leido(false)
                .build();
        consulta.getMensajes().add(mensaje);
        mensajeRepository.save(mensaje);
        return mapper.toResponse(consulta);
    }

    @Transactional(readOnly = true)
    public ConsultationResponse getById(String userEmail, Long consultationId) {
        Consulta consulta = loadParticipating(consultationId, userEmail);
        return mapper.toResponse(consulta);
    }

    @Transactional(readOnly = true)
    public Page<ConsultationSummary> listAsClient(String userEmail, Pageable pageable) {
        Usuario user = loadUser(userEmail);
        return consultaRepository.findByClienteId(user.getId(), pageable)
                .map(c -> mapper.toSummary(c, user.getId()));
    }

    @Transactional(readOnly = true)
    public Page<ConsultationSummary> listAsExpert(String userEmail, Pageable pageable) {
        Usuario user = loadUser(userEmail);
        return consultaRepository.findByExpertoId(user.getId(), pageable)
                .map(c -> mapper.toSummary(c, user.getId()));
    }

    private Consulta loadParticipating(Long consultationId, String userEmail) {
        Usuario user = loadUser(userEmail);
        Consulta consulta = consultaRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta", consultationId));
        if (!consulta.involucra(user.getId())) {
            throw new BusinessException("No participas en esta consulta", HttpStatus.FORBIDDEN);
        }
        return consulta;
    }

    private Usuario loadUser(String email) {
        return usuarioRepository.findByCorreoWithRoles(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + email));
    }
}

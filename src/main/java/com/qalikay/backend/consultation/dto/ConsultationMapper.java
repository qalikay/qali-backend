package com.qalikay.backend.consultation.dto;

import com.qalikay.backend.consultation.domain.Consulta;
import com.qalikay.backend.consultation.domain.Mensaje;
import com.qalikay.backend.user.domain.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ConsultationMapper {

    public ConsultationResponse toResponse(Consulta consulta) {
        return ConsultationResponse.builder()
                .id(consulta.getId())
                .subject(consulta.getAsunto())
                .status(consulta.getEstado().name())
                .client(toParticipant(consulta.getCliente()))
                .expert(toParticipant(consulta.getExperto()))
                .messages(consulta.getMensajes().stream().map(this::toMessage).toList())
                .createdAt(consulta.getCreatedAt())
                .updatedAt(consulta.getUpdatedAt())
                .build();
    }

    public ConsultationSummary toSummary(Consulta consulta, Long viewerId) {
        Usuario other = consulta.getCliente().getId().equals(viewerId)
                ? consulta.getExperto()
                : consulta.getCliente();
        return ConsultationSummary.builder()
                .id(consulta.getId())
                .subject(consulta.getAsunto())
                .status(consulta.getEstado().name())
                .otherPartyFullName(other.getNombre() + " " + other.getApellido())
                .messageCount(consulta.getMensajes().size())
                .updatedAt(consulta.getUpdatedAt())
                .build();
    }

    public ConsultationResponse.MessageDto toMessage(Mensaje m) {
        return ConsultationResponse.MessageDto.builder()
                .id(m.getId())
                .content(m.getContenido())
                .sender(toParticipant(m.getEmisor()))
                .read(m.getLeido())
                .sentAt(m.getCreatedAt())
                .build();
    }

    private ConsultationResponse.ParticipantDto toParticipant(Usuario u) {
        return ConsultationResponse.ParticipantDto.builder()
                .id(u.getId())
                .firstName(u.getNombre())
                .lastName(u.getApellido())
                .build();
    }
}

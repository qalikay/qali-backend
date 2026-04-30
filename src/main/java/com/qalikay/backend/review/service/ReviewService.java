package com.qalikay.backend.review.service;

import com.qalikay.backend.review.domain.Resena;
import com.qalikay.backend.review.dto.ReviewRequest;
import com.qalikay.backend.review.dto.ReviewResponse;
import com.qalikay.backend.review.dto.ReviewSummary;
import com.qalikay.backend.review.repository.ResenaRepository;
import com.qalikay.backend.shared.exception.BusinessException;
import com.qalikay.backend.shared.exception.DuplicateResourceException;
import com.qalikay.backend.shared.exception.ResourceNotFoundException;
import com.qalikay.backend.transaction.domain.EstadoTransaccion;
import com.qalikay.backend.transaction.domain.TipoItem;
import com.qalikay.backend.transaction.repository.TransaccionRepository;
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
public class ReviewService {

    private final ResenaRepository resenaRepository;
    private final TransaccionRepository transaccionRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ReviewResponse createOrUpdate(String clientEmail, String type, Long refId, ReviewRequest request) {
        TipoItem tipo = parseTipo(type);
        Usuario cliente = usuarioRepository.findByCorreo(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + clientEmail));

        boolean compro = transaccionRepository
                .clienteComproItem(cliente.getId(), tipo, refId, EstadoTransaccion.PAGADA);
        if (!compro) {
            throw new BusinessException(
                    "Solo puedes resenar items que hayas comprado",
                    HttpStatus.FORBIDDEN
            );
        }

        Resena resena = resenaRepository
                .findByClienteIdAndTipoAndRefId(cliente.getId(), tipo, refId)
                .map(existing -> {
                    existing.setCalificacion(request.rating());
                    existing.setComentario(request.comment());
                    return existing;
                })
                .orElseGet(() -> Resena.builder()
                        .cliente(cliente)
                        .tipo(tipo)
                        .refId(refId)
                        .calificacion(request.rating())
                        .comentario(request.comment())
                        .build());

        try {
            Resena guardada = resenaRepository.save(resena);
            log.info("Resena guardada cliente={} tipo={} refId={} rating={}",
                    clientEmail, tipo, refId, request.rating());
            return toResponse(guardada);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new DuplicateResourceException("Ya existe una resena tuya para este item");
        }
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> listForItem(String type, Long refId, Pageable pageable) {
        TipoItem tipo = parseTipo(type);
        return resenaRepository.findByTipoAndRefId(tipo, refId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ReviewSummary summaryForItem(String type, Long refId) {
        TipoItem tipo = parseTipo(type);
        Double avg = resenaRepository.promedioCalificacion(tipo, refId);
        long count = resenaRepository.countByTipoAndRefId(tipo, refId);
        return ReviewSummary.builder()
                .averageRating(avg == null ? 0.0 : Math.round(avg * 10.0) / 10.0)
                .totalReviews(count)
                .build();
    }

    private ReviewResponse toResponse(Resena r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .rating(r.getCalificacion())
                .comment(r.getComentario())
                .authorFullName(r.getCliente().getNombre() + " " + r.getCliente().getApellido())
                .itemType(r.getTipo().name())
                .itemRefId(r.getRefId())
                .createdAt(r.getCreatedAt())
                .build();
    }

    private TipoItem parseTipo(String value) {
        try {
            return TipoItem.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Tipo de item invalido: " + value);
        }
    }
}

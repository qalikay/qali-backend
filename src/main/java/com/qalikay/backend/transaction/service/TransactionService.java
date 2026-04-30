package com.qalikay.backend.transaction.service;

import com.qalikay.backend.product.domain.EstadoInsumo;
import com.qalikay.backend.product.domain.Insumo;
import com.qalikay.backend.product.repository.InsumoRepository;
import com.qalikay.backend.recipe.domain.EstadoReceta;
import com.qalikay.backend.recipe.domain.Receta;
import com.qalikay.backend.recipe.repository.RecetaRepository;
import com.qalikay.backend.shared.exception.BusinessException;
import com.qalikay.backend.shared.exception.ResourceNotFoundException;
import com.qalikay.backend.transaction.domain.EstadoTransaccion;
import com.qalikay.backend.transaction.domain.MetodoPago;
import com.qalikay.backend.transaction.domain.TipoItem;
import com.qalikay.backend.transaction.domain.Transaccion;
import com.qalikay.backend.transaction.domain.TransaccionDetalle;
import com.qalikay.backend.transaction.dto.PurchaseRequest;
import com.qalikay.backend.transaction.dto.TransactionResponse;
import com.qalikay.backend.transaction.repository.TransaccionRepository;
import com.qalikay.backend.user.domain.Usuario;
import com.qalikay.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que orquesta la compra de recetas e insumos.
 *
 * En esta version simulamos el pago: el cliente envia el metodo de pago
 * y la transaccion se marca PAGADA inmediatamente. En un proyecto real
 * integrariamos con una pasarela (Culqi, Mercado Pago, Stripe, etc.).
 *
 * Reglas:
 *  - Solo CLIENTES pueden comprar.
 *  - Las RECETAS deben estar PUBLICADAS.
 *  - Los INSUMOS deben estar DISPONIBLES y tener stock suficiente.
 *  - El stock de insumos se descuenta atomicamente con la transaccion.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransaccionRepository transaccionRepository;
    private final RecetaRepository recetaRepository;
    private final InsumoRepository insumoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public TransactionResponse purchase(String clientEmail, PurchaseRequest request) {
        Usuario cliente = usuarioRepository.findByCorreoWithRoles(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + clientEmail));

        Transaccion transaccion = Transaccion.builder()
                .cliente(cliente)
                .total(BigDecimal.ZERO)
                .estado(EstadoTransaccion.PENDIENTE)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        List<TransaccionDetalle> detalles = new ArrayList<>();

        for (PurchaseRequest.Item item : request.items()) {
            TipoItem tipo = parseTipo(item.type());
            TransaccionDetalle detalle = procesarItem(tipo, item.refId(), item.quantity(), transaccion);
            detalles.add(detalle);
            total = total.add(detalle.getSubtotal());
        }

        transaccion.setDetalles(detalles);
        transaccion.setTotal(total);
        transaccion.marcarPagada(parseMetodo(request.paymentMethod()), request.paymentReference());

        Transaccion guardada = transaccionRepository.save(transaccion);
        log.info("Transaccion completada id={} total={} cliente={}", guardada.getId(), total, clientEmail);
        return toResponse(guardada);
    }

    private TransaccionDetalle procesarItem(TipoItem tipo, Long refId, Integer cantidad, Transaccion transaccion) {
        if (tipo == TipoItem.RECETA) {
            return procesarReceta(refId, cantidad, transaccion);
        } else {
            return procesarInsumo(refId, cantidad, transaccion);
        }
    }

    private TransaccionDetalle procesarReceta(Long refId, Integer cantidad, Transaccion transaccion) {
        if (cantidad != 1) {
            throw new BusinessException("La cantidad para una receta debe ser 1");
        }
        Receta receta = recetaRepository.findById(refId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta", refId));
        if (receta.getEstado() != EstadoReceta.PUBLICADA) {
            throw new BusinessException("La receta " + refId + " no esta disponible");
        }
        BigDecimal subtotal = receta.getPrecio();
        return TransaccionDetalle.builder()
                .transaccion(transaccion)
                .tipo(TipoItem.RECETA)
                .refId(receta.getId())
                .nombreItem(receta.getTitulo())
                .cantidad(1)
                .precioUnitario(receta.getPrecio())
                .subtotal(subtotal)
                .build();
    }

    private TransaccionDetalle procesarInsumo(Long refId, Integer cantidad, Transaccion transaccion) {
        Insumo insumo = insumoRepository.findByIdForUpdate(refId)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo", refId));
        if (insumo.getEstado() != EstadoInsumo.DISPONIBLE) {
            throw new BusinessException("El insumo " + insumo.getNombre() + " no esta disponible");
        }
        if (insumo.getStock() < cantidad) {
            throw new BusinessException("Stock insuficiente para " + insumo.getNombre()
                    + " (disponibles: " + insumo.getStock() + ")");
        }
        insumo.reducirStock(cantidad);
        BigDecimal subtotal = insumo.getPrecio().multiply(BigDecimal.valueOf(cantidad));
        return TransaccionDetalle.builder()
                .transaccion(transaccion)
                .tipo(TipoItem.INSUMO)
                .refId(insumo.getId())
                .nombreItem(insumo.getNombre())
                .cantidad(cantidad)
                .precioUnitario(insumo.getPrecio())
                .subtotal(subtotal)
                .build();
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(String clientEmail, Long id) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaccion", id));
        if (!transaccion.getCliente().getCorreo().equalsIgnoreCase(clientEmail)) {
            throw new BusinessException("No tienes acceso a esta transaccion",
                    org.springframework.http.HttpStatus.FORBIDDEN);
        }
        return toResponse(transaccion);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> listMyPurchases(String clientEmail, Pageable pageable) {
        Usuario cliente = usuarioRepository.findByCorreo(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + clientEmail));
        return transaccionRepository.findByClienteId(cliente.getId(), pageable)
                .map(this::toResponse);
    }

    private TransactionResponse toResponse(Transaccion t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .total(t.getTotal())
                .status(t.getEstado().name())
                .paymentMethod(t.getMetodoPago() == null ? null : t.getMetodoPago().name())
                .paymentReference(t.getReferenciaPago())
                .paidAt(t.getFechaPago())
                .createdAt(t.getCreatedAt())
                .items(t.getDetalles().stream().map(d ->
                        TransactionResponse.ItemDto.builder()
                                .type(d.getTipo().name())
                                .refId(d.getRefId())
                                .name(d.getNombreItem())
                                .quantity(d.getCantidad())
                                .unitPrice(d.getPrecioUnitario())
                                .subtotal(d.getSubtotal())
                                .build()
                ).toList())
                .build();
    }

    private TipoItem parseTipo(String value) {
        try {
            return TipoItem.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Tipo de item invalido: " + value);
        }
    }

    private MetodoPago parseMetodo(String value) {
        try {
            return MetodoPago.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Metodo de pago invalido: " + value);
        }
    }
}

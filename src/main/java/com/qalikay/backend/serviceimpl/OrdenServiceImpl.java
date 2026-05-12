package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.dtos.CrearOrdenDTO;
import com.qalikay.backend.dtos.DetalleOrdenDTO;
import com.qalikay.backend.entities.*;
import com.qalikay.backend.repositories.*;
import com.qalikay.backend.services.OrdenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenServiceImpl implements OrdenService {

    @Autowired
    private OrdenRepositorio ordenRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private RecetaRepositorio recetaRepositorio;

    @Autowired
    private InsumoRepositorio insumoRepositorio;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Orden crearComoCliente(CrearOrdenDTO dto, String username) {
        Cliente cliente = clienteRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setFecha(LocalDateTime.now());
        orden.setEstado("PENDIENTE");
        orden.setMetodoPago(dto.getMetodoPago());

        double total = 0d;

        if (dto.getDetalles() != null) {
            for (DetalleOrdenDTO detDTO : dto.getDetalles()) {
                DetalleOrden det = new DetalleOrden();
                det.setTipoItem(detDTO.getTipoItem());
                det.setItemId(detDTO.getItemId());
                det.setCantidad(detDTO.getCantidad() == null ? 1 : detDTO.getCantidad());

                double precio = 0d;
                String descripcion = detDTO.getDescripcion();

                if ("RECETA".equalsIgnoreCase(detDTO.getTipoItem())) {
                    Receta r = recetaRepositorio.findById(detDTO.getItemId())
                            .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada: " + detDTO.getItemId()));
                    precio = r.getPrecio() == null ? 0d : r.getPrecio();
                    descripcion = r.getTitulo();
                } else if ("INSUMO".equalsIgnoreCase(detDTO.getTipoItem())) {
                    Insumo i = insumoRepositorio.findById(detDTO.getItemId())
                            .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado: " + detDTO.getItemId()));
                    if (i.getStock() == null || i.getStock() < det.getCantidad()) {
                        throw new IllegalArgumentException("Stock insuficiente para insumo: " + i.getNombre());
                    }
                    i.setStock(i.getStock() - det.getCantidad());
                    if (i.getStock() == 0) {
                        i.setEstado("AGOTADO");
                    }
                    insumoRepositorio.save(i);
                    precio = i.getPrecio() == null ? 0d : i.getPrecio();
                    descripcion = i.getNombre();
                } else {
                    throw new IllegalArgumentException("tipoItem invalido: " + detDTO.getTipoItem());
                }

                det.setPrecioUnitario(precio);
                det.setDescripcion(descripcion);
                det.setSubtotal(precio * det.getCantidad());
                total += det.getSubtotal();
                det.setOrden(orden);
                orden.getDetalles().add(det);
            }
        }

        orden.setTotal(total);
        return ordenRepositorio.save(orden);
    }

    @Override
    public List<Orden> listarMisOrdenes(String username) {
        Cliente cliente = clienteRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        return ordenRepositorio.findByClienteIdOrderByFechaDesc(cliente.getId());
    }

    @Override
    public Orden buscarPorId(Long id) {
        return ordenRepositorio.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Orden cambiarEstado(Long id, String estado) {
        Orden orden = ordenRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + id));
        orden.setEstado(estado);
        return ordenRepositorio.save(orden);
    }
}

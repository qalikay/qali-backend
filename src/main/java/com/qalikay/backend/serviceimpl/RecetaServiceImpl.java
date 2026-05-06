package com.qalikay.backend.serviceimpl;

import com.qalikay.backend.dtos.CrearRecetaDTO;
import com.qalikay.backend.entities.Categoria;
import com.qalikay.backend.entities.Experto;
import com.qalikay.backend.entities.Receta;
import com.qalikay.backend.repositories.CategoriaRepositorio;
import com.qalikay.backend.repositories.ExpertoRepositorio;
import com.qalikay.backend.repositories.RecetaRepositorio;
import com.qalikay.backend.services.RecetaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private RecetaRepositorio recetaRepositorio;

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Autowired
    private ExpertoRepositorio expertoRepositorio;

    @Override
    public List<Receta> listarPublicadas() {
        return recetaRepositorio.findByEstado("PUBLICADA");
    }

    @Override
    public List<Receta> listarPublicadasPorCategoria(Long categoriaId) {
        return recetaRepositorio.findByEstadoAndCategoriaId("PUBLICADA", categoriaId);
    }

    @Override
    public List<Receta> buscarPublicadasPorTitulo(String titulo) {
        return recetaRepositorio.findByEstadoAndTituloContainingIgnoreCase("PUBLICADA", titulo);
    }

    @Override
    public List<Receta> listarTodas() {
        return recetaRepositorio.findAll();
    }

    @Override
    public List<Receta> listarPorExperto(Long expertoId) {
        return recetaRepositorio.findByExpertoId(expertoId);
    }

    @Override
    public Receta buscarPorId(Long id) {
        return recetaRepositorio.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Receta crearComoExperto(CrearRecetaDTO dto, String username) {
        Experto experto = expertoRepositorio.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Experto no encontrado"));

        Receta receta = new Receta();
        copiarDatos(receta, dto);
        receta.setExperto(experto);
        receta.setEstado("BORRADOR");
        receta.setFechaCreacion(LocalDate.now());
        return recetaRepositorio.save(receta);
    }

    @Transactional
    @Override
    public Receta modificarComoExperto(Long id, CrearRecetaDTO dto, String username) {
        Receta receta = buscarYValidarPropietario(id, username);
        copiarDatos(receta, dto);
        return recetaRepositorio.save(receta);
    }

    @Transactional
    @Override
    public Receta publicar(Long id, String username) {
        Receta receta = buscarYValidarPropietario(id, username);
        receta.setEstado("PUBLICADA");
        return recetaRepositorio.save(receta);
    }

    @Transactional
    @Override
    public Receta archivar(Long id, String username) {
        Receta receta = buscarYValidarPropietario(id, username);
        receta.setEstado("BORRADOR");
        return recetaRepositorio.save(receta);
    }

    @Transactional
    @Override
    public void eliminar(Long id, String username) {
        Receta receta = buscarYValidarPropietario(id, username);
        recetaRepositorio.delete(receta);
    }

    // ---- helpers ----

    private void copiarDatos(Receta receta, CrearRecetaDTO dto) {
        receta.setTitulo(dto.getTitulo());
        receta.setDescripcion(dto.getDescripcion());
        receta.setIngredientes(dto.getIngredientes());
        receta.setPreparacion(dto.getPreparacion());
        receta.setAdvertencias(dto.getAdvertencias());
        receta.setMinutosPreparacion(dto.getMinutosPreparacion());
        receta.setPrecio(dto.getPrecio());
        receta.setImagenUrl(dto.getImagenUrl());
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepositorio.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada: " + dto.getCategoriaId()));
            receta.setCategoria(categoria);
        }
    }

    private Receta buscarYValidarPropietario(Long id, String username) {
        Receta receta = recetaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada: " + id));
        if (receta.getExperto() == null
                || receta.getExperto().getUser() == null
                || !receta.getExperto().getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Esta receta no pertenece al experto autenticado");
        }
        return receta;
    }
}

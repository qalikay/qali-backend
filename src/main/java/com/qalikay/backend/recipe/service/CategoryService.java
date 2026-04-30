package com.qalikay.backend.recipe.service;

import com.qalikay.backend.recipe.dto.CategoryResponse;
import com.qalikay.backend.recipe.dto.RecipeMapper;
import com.qalikay.backend.recipe.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoriaRepository categoriaRepository;
    private final RecipeMapper mapper;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoriaRepository.findAll(Sort.by(Sort.Direction.ASC, "nombre"))
                .stream()
                .map(mapper::toCategoryResponse)
                .toList();
    }
}

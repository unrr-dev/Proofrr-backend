package com.proofrr.proofrr.category;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Validated @RequestBody CategoryRequest request,
                                                           UriComponentsBuilder uriBuilder) {
        Category category = new Category(request.getName(), request.getUserId());
        Category saved = categoryRepository.save(category);
        URI location = uriBuilder.path("/api/categories/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(new CategoryResponse(saved.getId(), saved.getName(), saved.getUserId()));
    }

    @GetMapping
    public List<CategoryResponse> listCategories(@RequestParam("userId") Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName(), category.getUserId()))
                .collect(Collectors.toList());
    }
}

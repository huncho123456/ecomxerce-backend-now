package com.ecommerce.app.controller;

import com.ecommerce.app.entity.ProductEntity;
import com.ecommerce.app.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class productController {

    private final ProductRepository repo;

    @GetMapping
    public List<ProductEntity> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ProductEntity get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PostMapping("/create")
    public ProductEntity add(@RequestBody ProductEntity product) {
        return repo.save(product);
    }

    @PutMapping("/{id}")
    public ProductEntity update(@PathVariable Long id, @RequestBody ProductEntity updated) {
        ProductEntity existing = repo.findById(id).orElseThrow();
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setStock(updated.getStock());
        existing.setImageUrl(updated.getImageUrl());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}

package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private long nextId = 1;

    public List<Product> findAll() {
        return products;
    }

    public Product findById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void create(Product p) {
        p.setId(nextId++);
        products.add(p);
    }

    public void update(Long id, Product p) {
        Product old = findById(id);
        if (old == null) return;

        p.setId(id);

        int idx = products.indexOf(old);
        if (idx >= 0) products.set(idx, p);
    }

    public void delete(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }
}

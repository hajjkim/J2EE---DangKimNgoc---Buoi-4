package com.example.demo.service;

import com.example.demo.model.Category;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryService {

    private final Map<Long, Category> store = new LinkedHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    public CategoryService() {
        // dữ liệu mặc định theo đề
        create(new Category(null, "Điện thoại"));
        create(new Category(null, "Laptop"));
    }

    public List<Category> findAll() {
        return new ArrayList<>(store.values());
    }

    public Category findById(Long id) {
        return store.get(id);
    }

    public void create(Category c) {
        long id = seq.incrementAndGet();
        c.setId(id);
        store.put(id, c);
    }

    public void update(Long id, Category c) {
        c.setId(id);
        store.put(id, c);
    }

    public void delete(Long id) {
        store.remove(id);
    }
}

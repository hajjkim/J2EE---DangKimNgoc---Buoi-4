package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;

public class Category {

    private Long id;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    public Category() {}

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}

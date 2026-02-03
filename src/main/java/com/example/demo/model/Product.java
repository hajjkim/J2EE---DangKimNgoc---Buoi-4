package com.example.demo.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Product {
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 1, message = "Giá sản phẩm phải >= 1")
    @Max(value = 9999999, message = "Giá sản phẩm phải <= 9,999,999")
    private Integer price;

    // ✅ số lượng
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải >= 0")
    private Integer quantity;

    // ✅ mô tả (có thể để trống)
    private String description;

    private String imageName;
    private Category category;

    public Product() {}

    // getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public String getDescription() { return description; }
    public String getImageName() { return imageName; }
    public Category getCategory() { return category; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(Integer price) { this.price = price; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setDescription(String description) { this.description = description; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public void setCategory(Category category) { this.category = category; }
}

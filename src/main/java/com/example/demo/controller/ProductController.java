package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // ===== LIST =====
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    // ===== CREATE FORM =====
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "products/form";
    }

    // ===== CREATE =====
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         Model model) throws IOException {

        validateImageFileName(imageFile, result);

        // bắt buộc chọn category
        if (categoryId == null) {
            result.rejectValue("category", "category.required", "Vui lòng chọn danh mục");
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "products/form";
        }

        product.setCategory(categoryService.findById(categoryId));

        String savedName = saveImage(imageFile);
        if (savedName != null) {
            product.setImageName(savedName);
        }

        productService.create(product);
        return "redirect:/products";
    }

    // ===== EDIT FORM =====
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product p = productService.findById(id);
        if (p == null) return "redirect:/products";

        model.addAttribute("product", p);
        model.addAttribute("categories", categoryService.findAll());
        return "products/form";
    }

    // ===== UPDATE =====
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         Model model) throws IOException {

        Product old = productService.findById(id);
        if (old == null) return "redirect:/products";

        validateImageFileName(imageFile, result);

        if (categoryId == null) {
            result.rejectValue("category", "category.required", "Vui lòng chọn danh mục");
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "products/form";
        }

        product.setCategory(categoryService.findById(categoryId));

        // giữ ảnh cũ nếu không chọn ảnh mới
        String savedName = saveImage(imageFile);
        if (savedName != null) {
            product.setImageName(savedName);
        } else {
            product.setImageName(old.getImageName());
        }

        productService.update(id, product);
        return "redirect:/products";
    }

    // ===== DELETE =====
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }

    // ===================== helpers =====================

    private void validateImageFileName(MultipartFile file, BindingResult result) {
        if (file == null || file.isEmpty()) return;

        String original = file.getOriginalFilename();
        if (original != null && original.length() > 200) {
            result.addError(new FieldError("product", "imageName", "Tên hình ảnh không quá 200 kí tự"));
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) return null;

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        String original = Objects.requireNonNull(file.getOriginalFilename());
        String ext = "";

        int dot = original.lastIndexOf(".");
        if (dot >= 0 && dot < original.length() - 1) {
            ext = original.substring(dot);
        }

        String filename = UUID.randomUUID() + ext;
        Path target = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }
}

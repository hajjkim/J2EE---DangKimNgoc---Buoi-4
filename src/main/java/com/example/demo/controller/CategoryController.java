package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", service.findAll());
        return "categories/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Category category,
                         BindingResult result) {
        if (result.hasErrors()) return "categories/form";
        service.create(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category c = service.findById(id);
        if (c == null) return "redirect:/categories";
        model.addAttribute("category", c);
        return "categories/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Category category,
                         BindingResult result) {
        if (result.hasErrors()) return "categories/form";
        service.update(id, category);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/categories";
    }
}

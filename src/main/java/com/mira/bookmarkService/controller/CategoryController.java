package com.mira.bookmarkService.controller;

import com.mira.bookmarkService.model.CategoryTagRequestDto;
import com.mira.bookmarkService.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public void createCategoryAndTags(@RequestBody CategoryTagRequestDto dto) {
        categoryService.createCategoryAndTags(dto);
    }
}

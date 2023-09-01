package com.mira.bookmarkService.controller;

import com.mira.bookmarkService.model.CategoryTagRequestDto;
import com.mira.bookmarkService.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categories")
public class CategoryController {

    private final BookmarkService bookmarkService;

    public CategoryController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping
    public ResponseEntity<String> createCategoryAndTags(@RequestBody CategoryTagRequestDto dto) {
        bookmarkService.createCategoryAndTags(dto);
        return ResponseEntity.ok("Category and tags created successfully");
    }
}

package com.mira.bookmarkService.service;

import com.mira.bookmarkService.model.Category;
import com.mira.bookmarkService.model.CategoryTagRequestDto;
import com.mira.bookmarkService.model.Tag;
import com.mira.bookmarkService.repository.CategoryRepository;
import com.mira.bookmarkService.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    public CategoryService(CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    //7. Add Category along with the tags
    public void createCategoryAndTags(CategoryTagRequestDto categoryTagRequestDto){
        Category newCategory = new Category();
        newCategory.setCategoryName(categoryTagRequestDto.getCategoryName());
        Category savedCategory = categoryRepository.save(newCategory);

        for (String tagName : categoryTagRequestDto.getTagNames()) {
            Tag newTag = new Tag();
            newTag.setTagName(tagName);
            newTag.setCategory(savedCategory);
            tagRepository.save(newTag);
        }
    }
}

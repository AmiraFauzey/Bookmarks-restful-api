package com.mira.bookmarkService.repository;

import com.mira.bookmarkService.model.Bookmark;
import com.mira.bookmarkService.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
    Category findByCategoryId(Integer categoryId);
}

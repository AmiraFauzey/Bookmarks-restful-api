package com.mira.bookmarkService.repository;


import com.mira.bookmarkService.model.Category;
import com.mira.bookmarkService.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findBytagName(String tagName);
    Tag findByTagNameAndCategory(String tagName, Category category);

}

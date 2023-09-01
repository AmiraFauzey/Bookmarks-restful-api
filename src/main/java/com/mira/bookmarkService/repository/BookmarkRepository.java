package com.mira.bookmarkService.repository;


import com.mira.bookmarkService.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer>{
    Bookmark findByBookmarkId(Integer bookmarkId);
}

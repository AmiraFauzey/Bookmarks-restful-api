package com.mira.bookmarkService.controller;

import com.mira.bookmarkService.exceptionHandler.ResourceNotFoundException;
import com.mira.bookmarkService.model.Bookmark;
import com.mira.bookmarkService.model.BookmarkPage;
import com.mira.bookmarkService.model.BookmarkSearchCriteria;
import com.mira.bookmarkService.model.Tag;
import com.mira.bookmarkService.repository.BookmarkRepository;
import com.mira.bookmarkService.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookmarks")
public class BookmarkController {

    //dependency Injection
    private final BookmarkService bookmarkService;

    //constructor injection
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    //1. Create New Bookmark
    @PostMapping
    public Bookmark createBookmark(@RequestBody Bookmark bookmark) {
        return bookmarkService.createBookmark(bookmark);
    }

    //2. Get Bookmark By Id
    @GetMapping("{bookmarkId}")
    public Bookmark getBookmarkById(@PathVariable Integer bookmarkId) {
            Bookmark bookmark = bookmarkService.findByBookmarkId(bookmarkId);
            return bookmark;
    }

    //3. Update the Bookmark
    @PutMapping("{bookmarkId}")
    public Bookmark updateBookmark(
            @PathVariable Integer bookmarkId,
            @RequestBody Bookmark updatedBookmark) throws Exception {

            Bookmark updated = bookmarkService.updateBookmark(bookmarkId, updatedBookmark);
            return updated;
    }

    //4. Delete the Bookmark
    @DeleteMapping("{bookmarkId}")
    public void deleteBookmark(@PathVariable Integer bookmarkId) {
        Bookmark existingBookmark = bookmarkService.findByBookmarkId(bookmarkId);

        if (existingBookmark != null) {
            bookmarkService.deleteBookmark(bookmarkId);
        }
    }

    //5. Get All Bookmarks
    @GetMapping
    public List<Bookmark> getAllBookmarks() {
        return bookmarkService.getAllBookmarks();
    }

    //6. Filter, Sorting, Pagination bookmarks
    @GetMapping("searchResult")
    public Page<Bookmark> getBookmarksResult(
            BookmarkPage bookmarkPage,
            BookmarkSearchCriteria bookmarkSearchCriteria){
        return bookmarkService.getBookmarks(bookmarkPage,bookmarkSearchCriteria);
    }

}

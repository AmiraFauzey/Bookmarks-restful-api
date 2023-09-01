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

    private final BookmarkService bookmarkService;

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
    public ResponseEntity<Bookmark> getBookmarkById(@PathVariable Integer bookmarkId) {
        try {
            Bookmark bookmark = bookmarkService.findByBookmarkId(bookmarkId);
            return ResponseEntity.ok(bookmark);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Not found Bookmark with id = " + bookmarkId);
        }
    }

    //3. Update the Bookmark
    @PutMapping("/update/{bookmarkId}")
    public ResponseEntity<Bookmark> updateBookmark(
            @PathVariable Integer bookmarkId,
            @RequestBody Bookmark updatedBookmark,
            @RequestParam Integer categoryId,
            @RequestParam List<Integer> tagIds) throws Exception {
        try {
            Bookmark updated = bookmarkService.updateBookmark(bookmarkId, updatedBookmark, categoryId, tagIds);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //4. Delete the Bookmark
    @DeleteMapping("{bookmarkId}")
    public ResponseEntity<String> deleteBookmark(@PathVariable Integer bookmarkId) {
        Bookmark existingBookmark = bookmarkService.findByBookmarkId(bookmarkId);

        if (existingBookmark != null) {
            bookmarkService.deleteBookmark(bookmarkId);
            return ResponseEntity.ok("Bookmark deleted successfully");
        } else {
            throw new ResourceNotFoundException("Not found Bookmark with id = " + bookmarkId);
        }
    }

    //5. Get All Bookmarks
    @GetMapping
    public List<Bookmark> getAllBookmarks() {
        return bookmarkService.getAllBookmarks();
    }

    //6. Filter, Sorting, Pagination bookmarks
    @GetMapping("searchResult")
    public ResponseEntity<Page<Bookmark>> getBookmarksResult(
            BookmarkPage bookmarkPage,
            BookmarkSearchCriteria bookmarkSearchCriteria){
        return new ResponseEntity<>(bookmarkService.getBookmarks(bookmarkPage,bookmarkSearchCriteria),
                HttpStatus.OK);
    }

}

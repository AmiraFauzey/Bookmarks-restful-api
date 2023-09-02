package com.mira.bookmarkService.service;

import com.mira.bookmarkService.exceptionHandler.ResourceNotFoundException;
import com.mira.bookmarkService.model.*;
import com.mira.bookmarkService.repository.BookmarkCriteriaRepository;
import com.mira.bookmarkService.repository.BookmarkRepository;
import com.mira.bookmarkService.repository.CategoryRepository;
import com.mira.bookmarkService.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookmarkService.class);

    private final BookmarkRepository bookmarkRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    private final BookmarkCriteriaRepository bookmarkCriteriaRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository,
                           CategoryRepository categoryRepository,
                           TagRepository tagRepository,
                           BookmarkCriteriaRepository bookmarkCriteriaRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.bookmarkCriteriaRepository = bookmarkCriteriaRepository;
    }

    //1. Create bookmark
    @Transactional
    public Bookmark createBookmark(Bookmark bookmark){
        // Create a new bookmark
        Bookmark savebookmark = new Bookmark();
        savebookmark.setBookmarkUrl(bookmark.getBookmarkUrl());
        savebookmark.setBookmarkTitle(bookmark.getBookmarkTitle());
        savebookmark.setDescription(bookmark.getDescription());
        savebookmark.setNote(bookmark.getNote());

        // Retrieve the category object
        Integer categoryId = bookmark.getCategory().getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        savebookmark.setCategory(category);

        // Retrieve the tag objects and set them
        List<Tag> tags = tagRepository.findAllById(bookmark.getTags().stream()
                .map(Tag::getTagId)
                .collect(Collectors.toList()));
        savebookmark.setTags(tags);

        // Save the bookmark to the database
        return bookmarkRepository.save(savebookmark);
    }

    //2. Find bookmark by Id
    public Bookmark findByBookmarkId(Integer bookmarkId){
        Bookmark getbookmarkId = bookmarkRepository.findByBookmarkId(bookmarkId);
        LOGGER.error("Before Check Bookmark Id");
        LOGGER.error("Result: {}", getbookmarkId);
        if(getbookmarkId == null){
            throw new ResourceNotFoundException("Not found Bookmark with id = " + bookmarkId);
        }
        return bookmarkRepository.findByBookmarkId(bookmarkId);
    }

    //3. Update bookmark
    @Transactional
    public Bookmark updateBookmark(Integer bookmarkId, Bookmark bookmark, Integer categoryId, List<Integer> tagIds){
        // Retrieve the existing bookmark by its ID
        Bookmark existingBookmark = findByBookmarkId(bookmarkId);

        if (existingBookmark == null) {
            throw new ResourceNotFoundException("Bookmark not found with id: " + bookmarkId);
        }

        // Update the properties of the existing bookmark with the new values
        existingBookmark.setBookmarkUrl(bookmark.getBookmarkUrl());
        existingBookmark.setBookmarkTitle(bookmark.getBookmarkTitle());
        existingBookmark.setDescription(bookmark.getDescription());
        existingBookmark.setNote(bookmark.getNote());

        // Retrieve the existing category entity by its ID
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow();
        if (existingCategory == null) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        existingBookmark.setCategory(existingCategory);

        // Retrieve the existing tag entities by their IDs
        List<Tag> existingTags = tagRepository.findAllById(tagIds);
        // Ensure all requested tag IDs exist
        if (existingTags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("One or more tags not found.");
        }
        existingBookmark.setTags(existingTags);

        // Save the updated bookmark
        return bookmarkRepository.save(existingBookmark);
    }

    //4. Delete bookmark
    public void deleteBookmark(Integer bookmarkId){
        Bookmark bookmark = findByBookmarkId(bookmarkId);

        if(bookmark != null){
            bookmarkRepository.delete(bookmark);
        }else{
            throw new ResourceNotFoundException("Not found Bookmark with id = " + bookmarkId);
        }
    }

    //5. Get all bookmarks data
    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    //6. Search, sort, pagination bookmark
    public Page<Bookmark> getBookmarks(
            BookmarkPage bookmarkPage,
            BookmarkSearchCriteria bookmarkSearchCriteria){
        return bookmarkCriteriaRepository.findAllWithFilters(bookmarkPage,bookmarkSearchCriteria);
    }
}

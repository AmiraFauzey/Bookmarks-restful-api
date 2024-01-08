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
    public Bookmark createBookmark(Bookmark bookmark) {
        try {
            // Check if the category exists or create a new one
            Category existingCategory = categoryRepository.findByCategoryName(bookmark.getCategory().getCategoryName());
            if (existingCategory == null) {
                // Category does not exist, create a new one
                existingCategory = categoryRepository.save(bookmark.getCategory());
            }

            // Set the category for the bookmark
            bookmark.setCategory(existingCategory);

            //check if the tags that we input already exist or not
            List<Tag> existingTags = new ArrayList<>();
            for (Tag tag : bookmark.getTags()) {
                // Ensure the tag has a reference to the correct category
                tag.setCategory(existingCategory);

                Tag existingTag = tagRepository.findByTagNameAndCategory(tag.getTagName(), existingCategory);
                if (existingTag != null) {
                    existingTags.add(existingTag);
                } else {
                    // Tag does not exist, create a new one
                    Tag newTag = tagRepository.save(tag);
                    existingTags.add(newTag);
                }
            }
            bookmark.setTags(existingTags);

            // Save the bookmark to the database
            return bookmarkRepository.save(bookmark);
        } catch (Exception e) {
            // Handle exceptions, log, or rethrow as needed
            throw new RuntimeException("Error creating bookmark", e);
        }
    }

    //2. Find bookmark by Id
    public Bookmark findByBookmarkId(Integer bookmarkId) {
        Bookmark getbookmarkId = bookmarkRepository.findByBookmarkId(bookmarkId);
        LOGGER.error("Before Check Bookmark Id");
        LOGGER.error("Result: {}", getbookmarkId);
        if (getbookmarkId == null) {
            throw new ResourceNotFoundException("Not found Bookmark with id = " + bookmarkId);
        }
        return bookmarkRepository.findByBookmarkId(bookmarkId);
    }

    //3. Update bookmark
    @Transactional
    public Bookmark updateBookmark(Integer bookmarkId, Bookmark bookmark) {

        try {
        // Retrieve the existing bookmark by its ID
        Bookmark existingBookmark = findByBookmarkId(bookmarkId);

        if(existingBookmark != null){
              existingBookmark.setBookmarkTitle(bookmark.getBookmarkTitle());
              existingBookmark.setBookmarkUrl(bookmark.getBookmarkUrl());
              existingBookmark.setDescription(bookmark.getDescription());
              existingBookmark.setNote(bookmark.getNote());

            // retrieve the existing category
            Category updatedCategory = existingBookmark.getCategory();
            if (updatedCategory != null) {
                Category existingCategory = categoryRepository.findByCategoryName(updatedCategory.getCategoryName());
                if (existingCategory == null) {
                    // Category does not exist, create a new one
                    // Category does not exist, create a new one and set it to the existing bookmark
                    existingCategory = categoryRepository.save(bookmark.getCategory());
                    existingBookmark.setCategory(existingCategory);
                }else{
                    //if the category already exist we still want to save the new value into the table
                    // Category already exists, update the existingBookmark with the existing category
                    existingCategory.setCategoryName(bookmark.getCategory().getCategoryName());
                    existingBookmark.setCategory(existingCategory);
                }
            }

            /**
            // Check and update the tags
            List<Tag> updatedTags = existingBookmark.getTags();
            if (updatedTags != null && !updatedTags.isEmpty()) {
                List<Tag> newTags = new ArrayList<>();
                for (Tag tag : updatedTags) {
                    // Ensure the tag has a reference to the correct category
                    tag.setCategory(existingBookmark.getCategory());

                    Tag existingTag = tagRepository.findByTagNameAndCategory(tag.getTagName(), existingBookmark.getCategory());
                    if (existingTag != null) {
                        // Update the existing tag with the new values from the incoming bookmark
                        // Update the existing tag with the new values from the incoming bookmark
                        existingTag.setTagName(bookmark.getTags().get(0).getTagName()); // Update other properties as needed
                        newTags.add(existingTag);
                    }else{
                        // Tag does not exist, create a new one and save it
                        Tag newTag = new Tag();
                        newTag.setTagName(bookmark.getTags().get(0).getTagName()); // Set other properties as needed
                        newTag.setCategory(existingBookmark.getCategory());
                        newTags.add(newTag);
                    }
                }
                // Save the updated or new tags
                newTags = tagRepository.saveAll(newTags);
                // Set the updated tags list to existingBookmark
                existingBookmark.setTags(newTags);
            }
            **/

            // Save the updated bookmark
            return bookmarkRepository.save(existingBookmark);
        }else{
            throw new ResourceNotFoundException("Bookmark not found with id: " + bookmarkId);
        }
        } catch (Exception e) {
            // Handle exceptions, log, or rethrow as needed
            throw new RuntimeException("Error updating bookmark", e);
        }
        // Update the properties of the existing bookmark with the new values
    }

    //4. Delete bookmark
    public void deleteBookmark(Integer bookmarkId) {
        Bookmark bookmark = findByBookmarkId(bookmarkId);

        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
        } else {
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
            BookmarkSearchCriteria bookmarkSearchCriteria) {
        return bookmarkCriteriaRepository.findAllWithFilters(bookmarkPage, bookmarkSearchCriteria);
    }
}

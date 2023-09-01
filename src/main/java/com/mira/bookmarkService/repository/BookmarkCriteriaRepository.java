package com.mira.bookmarkService.repository;

import com.mira.bookmarkService.model.Bookmark;
import com.mira.bookmarkService.model.BookmarkPage;
import com.mira.bookmarkService.model.BookmarkSearchCriteria;
import com.mira.bookmarkService.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookmarkCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public BookmarkCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Bookmark> findAllWithFilters(BookmarkPage bookmarkPage,
                                             BookmarkSearchCriteria bookmarkSearchCriteria){
        CriteriaQuery<Bookmark> criteriaQuery = criteriaBuilder.createQuery(Bookmark.class);
        Root<Bookmark> bookmarkRoot = criteriaQuery.from(Bookmark.class);
        Predicate predicate = getPredicate(bookmarkSearchCriteria,bookmarkRoot);
        criteriaQuery.where(predicate);
        setOrder(bookmarkPage, criteriaQuery, bookmarkRoot);
        TypedQuery<Bookmark> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(bookmarkPage.getPageNumber() * bookmarkPage.getPageSize());
        typedQuery.setMaxResults(bookmarkPage.getPageSize());

        Pageable pageable = getPageable(bookmarkPage);
        long bookmarksCount = getBookmarksCount(bookmarkSearchCriteria);

        return new PageImpl<>(typedQuery.getResultList(), pageable, bookmarksCount);
    }

    private Predicate getPredicate(BookmarkSearchCriteria bookmarkSearchCriteria,
                                   Root<Bookmark> bookmarkRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(bookmarkSearchCriteria.getBookmarkTitle())){
            predicates.add(
                    criteriaBuilder.like(bookmarkRoot.get("bookmarkTitle"),
                            "%" + bookmarkSearchCriteria.getBookmarkTitle() + "%")
            );
        }
        if (Objects.nonNull(bookmarkSearchCriteria.getCategoryName())) {
            // Join with Category entity
            Join<Bookmark, Category> categoryJoin = bookmarkRoot.join("category");
            predicates.add(
                    criteriaBuilder.like(categoryJoin.get("categoryName"),
                            "%" + bookmarkSearchCriteria.getCategoryName() + "%")
            );
        }
        if(Objects.nonNull(bookmarkSearchCriteria.getCreatedDateFrom())){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(bookmarkSearchCriteria.getCreatedDateFrom());
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            Date date = calendar.getTime();
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(bookmarkRoot.get("createdDate"),date)
            );
        }
        if(Objects.nonNull(bookmarkSearchCriteria.getCreatedDateTo())){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(bookmarkSearchCriteria.getCreatedDateTo());
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.SECOND,59);
            Date date = calendar.getTime();
            predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(bookmarkRoot.get("createdDate"),date)
            );
        }
        return criteriaBuilder.and(predicates.toArray(new  Predicate[0]));
    }

    private void setOrder(BookmarkPage bookmarkPage,
                          CriteriaQuery<Bookmark> criteriaQuery,
                          Root<Bookmark> bookmarkRoot) {
        if(bookmarkPage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(bookmarkRoot.get(bookmarkPage.getSortBy())));
        }else{
            criteriaQuery.orderBy(criteriaBuilder.desc(bookmarkRoot.get(bookmarkPage.getSortBy())));
        }
    }

    private Pageable getPageable(BookmarkPage bookmarkPage) {
        Sort sort = Sort.by(bookmarkPage.getSortDirection(), bookmarkPage.getSortBy());
        return PageRequest.of(bookmarkPage.getPageNumber(), bookmarkPage.getPageSize(), sort);
    }

    private long getBookmarksCount(BookmarkSearchCriteria bookmarkSearchCriteria) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Bookmark> countRoot = countQuery.from(Bookmark.class);
        Predicate predicate = getPredicate(bookmarkSearchCriteria, countRoot);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}

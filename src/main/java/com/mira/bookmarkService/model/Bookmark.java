package com.mira.bookmarkService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name="BOOKMARK")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARK_ID")
    private Integer bookmarkId;

    @Column(name = "BOOKMARK_TITLE")
    private String bookmarkTitle;

    @Column(name = "BOOKMARK_URL")
    private String bookmarkUrl;

    @Column(name = "BOOKMARK_DESCRIPTION")
    private String description;

    @Column(name = "BOOKMARK_NOTE")
    private String note;

    @CreationTimestamp
    @Column(name = "CREATED_DATE", updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "BOOKMARK_TAG",
            joinColumns = @JoinColumn(name = "BOOKMARK_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private List<Tag> tags;
}

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

    //This strategy relies on the auto-increment functionality
    // provided by the database to generate unique identifier values automatically
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

    //FetchType enum is used to specify the strategy for fetching data from the database
    //There are two values of FetchType: LAZY and EAGER
    /*
    * The EAGER fetch type specifies that the data should be fetched eagerly,
    * which means that the data is fetched when the parent entity is fetched.
    * This can be more efficient in cases where the data is needed immediately,
    * because it avoids the overhead of fetching the data later.
    *
    * FetchType. LAZY tells Hibernate to only fetch the related entities from the database
    * when you use the relationship
    * */

    //@JoinTable will use a separate table to hold the relationship between BOOKMARK and TAG.
    //BOOKMARK_TAG will hold the association between A and B
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "BOOKMARK_TAG",
            joinColumns = @JoinColumn(name = "BOOKMARK_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private List<Tag> tags;
}

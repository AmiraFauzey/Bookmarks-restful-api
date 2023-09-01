package com.mira.bookmarkService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name="TAG")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Integer tagId;

    @Column(name = "TAG_NAME")
    private String tagName;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
}

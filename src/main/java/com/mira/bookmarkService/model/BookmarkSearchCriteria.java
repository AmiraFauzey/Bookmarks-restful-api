package com.mira.bookmarkService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkSearchCriteria {

    private String bookmarkTitle;
    private String categoryName;
    private Timestamp createdDateFrom;
    private Timestamp createdDateTo;

}

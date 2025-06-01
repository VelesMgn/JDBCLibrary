package com.store.jdbclibrary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Data
public class Book {
    private Long id;
    private String title;
    private String author;
    private Year publicationYear;
}
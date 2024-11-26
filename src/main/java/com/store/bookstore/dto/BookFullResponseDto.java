package com.store.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
public class BookFullResponseDto {
    private UUID id;
    private String title;
    private String description;
    private Integer pageCount;
    private AuthorResponseDto author;
}

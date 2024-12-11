package com.store.bookstore.dto.book.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateRequestDto {
    private String title;
    private String description;
    private Integer pageCount;
    private String authorId;
}

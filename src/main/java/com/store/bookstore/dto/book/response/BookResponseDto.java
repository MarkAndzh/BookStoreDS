package com.store.bookstore.dto.book.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private String id;
    private String title;
    private String description;
    private Integer pageCount;
}

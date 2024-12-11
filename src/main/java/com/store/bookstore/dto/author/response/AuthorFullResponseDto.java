package com.store.bookstore.dto.author.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.store.bookstore.dto.book.response.BookResponseDto;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorFullResponseDto {
    private String id;
    private String name;
    private String surname;
    private List<BookResponseDto> books;
}

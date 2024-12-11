package com.store.bookstore.dto.author.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.store.bookstore.dto.book.request.BookCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreateRequestDto {
    private String id;
    private String name;
    private String surname;
    private List<BookCreateRequestDto> books;
}

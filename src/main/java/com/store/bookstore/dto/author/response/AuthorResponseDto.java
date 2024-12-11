package com.store.bookstore.dto.author.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponseDto {
    private String id;
    private String name;
    private String surname;
}

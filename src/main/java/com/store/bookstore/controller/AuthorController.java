package com.store.bookstore.controller;

import com.store.bookstore.dto.AuthorFullResponseDto;
import com.store.bookstore.dto.AuthorRequestDto;
import com.store.bookstore.dto.AuthorResponseDto;
import com.store.bookstore.repository.AuthorRepository;
import com.store.bookstore.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthor(@PathVariable String id) {
        AuthorResponseDto authorResponseDto =  authorService.getAuthorWithoutBooks(id);

        return ResponseEntity.ok(authorResponseDto);
    }

    @GetMapping("/author/full/{id}")
    public ResponseEntity<AuthorFullResponseDto> getAuthorFullInfo(@PathVariable String id) {
        AuthorFullResponseDto authorFullResponseDto = authorService.getAuthorWithBooks(id);

        return ResponseEntity.ok(authorFullResponseDto);
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorResponseDto>> getAuthors() {
        List<AuthorResponseDto> authorResponseDtos = authorService.getAuthorsWithoutBooks();

        return ResponseEntity.ok(authorResponseDtos);
    }

    @GetMapping("/authors/full")
    public ResponseEntity<List<AuthorFullResponseDto>> getFullAuthors() {
        List<AuthorFullResponseDto> authorFullResponseDtos = authorService.getAuthorsWithBooks();

        return ResponseEntity.ok(authorFullResponseDtos);
    }

    @PostMapping("/author")
    public ResponseEntity<String> createAuthor(@RequestBody AuthorRequestDto author) {
        authorService.createAuthorWithBooks(author);

        return ResponseEntity.ok("Author created");
    }
}

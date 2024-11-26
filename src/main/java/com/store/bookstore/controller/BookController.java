package com.store.bookstore.controller;

import com.store.bookstore.dto.BookFullResponseDto;
import com.store.bookstore.dto.BookRequestDto;
import com.store.bookstore.dto.BookResponseDto;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable String id) {
        Book book = bookRepository.findById(UUID.fromString(id)).orElse(null);

        BookResponseDto bookResponseDto = modelMapper.map(book, BookResponseDto.class);

        return ResponseEntity.ok(bookResponseDto);
    }

    @GetMapping("/book/full/{id}")
    public ResponseEntity<BookFullResponseDto> getFullBook(@PathVariable String id) {
        Book book = bookRepository.findById(UUID.fromString(id)).orElse(null);

        BookFullResponseDto bookResponseDto = modelMapper.map(book, BookFullResponseDto.class);

        return ResponseEntity.ok(bookResponseDto);
    }

//    @PostMapping
//    public ResponseEntity<Book> addBook(@RequestBody BookRequestDto bookRequestDto) {
//        Book book = Book.builder()
//                .title(bookRequestDto.getTitle())
//                .description(bookRequestDto.getDescription())
//                .author()
//                .build();
//        return ResponseEntity.ok(bookRepository.save(book));
//    }

}

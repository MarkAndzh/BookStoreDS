package com.store.bookstore.controller;

import com.store.bookstore.dto.book.request.BookCreateRequestDto;
import com.store.bookstore.dto.book.response.BookFullResponseDto;
import com.store.bookstore.dto.book.response.BookResponseDto;
import com.store.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable String id) {
        BookResponseDto bookResponseDto = bookService.getBookWithoutAuthors(id);

        return ResponseEntity.ok(bookResponseDto);
    }

    @GetMapping("/book/full/{id}")
    public ResponseEntity<BookFullResponseDto> getFullBook(@PathVariable String id) {
        BookFullResponseDto bookFullResponseDto = bookService.getBookWithAuthors(id);

        return ResponseEntity.ok(bookFullResponseDto);
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDto>> getBooks() {
        List<BookResponseDto> bookResponseDtos = bookService.getBooksWithoutAuthors();

        return ResponseEntity.ok(bookResponseDtos);
    }

    @GetMapping("/books/full")
    public ResponseEntity<List<BookFullResponseDto>> getFullBooks() {
        List<BookFullResponseDto> bookFullResponseDtos = bookService.getBooksWithAuthors();

        return ResponseEntity.ok(bookFullResponseDtos);
    }

    @PostMapping("/book")
    public ResponseEntity<String> addBook(@RequestBody BookCreateRequestDto bookRequestDto) {
        bookService.createBook(bookRequestDto);

        return ResponseEntity.ok("Book created");
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<String> updateBook(@PathVariable String id, @RequestBody BookCreateRequestDto bookRequestDto) {
        bookService.updateBook(id, bookRequestDto);

        return ResponseEntity.ok("Book updated");
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);

        return ResponseEntity.ok("Book deleted");
    }
}

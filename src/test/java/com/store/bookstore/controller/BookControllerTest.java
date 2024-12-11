package com.store.bookstore.controller;

import com.store.bookstore.dto.author.response.AuthorResponseDto;
import com.store.bookstore.dto.book.request.BookCreateRequestDto;
import com.store.bookstore.dto.book.response.BookFullResponseDto;
import com.store.bookstore.dto.book.response.BookResponseDto;
import com.store.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookResponseDto bookResponseDto;
    private BookFullResponseDto bookFullResponseDto;
    private BookCreateRequestDto bookCreateRequestDto;
    private String bookId;
    private UUID authorId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID().toString();
        authorId = UUID.randomUUID();
        bookResponseDto = new BookResponseDto(bookId, "Sample Title", "Sample Description", 300);
        bookFullResponseDto = new BookFullResponseDto(bookId, "Sample Title", "Sample Description", 300, new AuthorResponseDto(authorId.toString(), "John", "Doe"));
        bookCreateRequestDto = new BookCreateRequestDto("Sample Title", "Sample Description", 300, authorId.toString());
    }

    @Test
    void testGetBook_Success() {
        when(bookService.getBookWithoutAuthors(bookId)).thenReturn(bookResponseDto);

        ResponseEntity<BookResponseDto> response = bookController.getBook(bookId);

        verify(bookService, times(1)).getBookWithoutAuthors(bookId);
        assertEquals(bookResponseDto, response.getBody());
    }

    @Test
    void testGetBook_NotFound() {
        when(bookService.getBookWithoutAuthors(bookId)).thenThrow(new RuntimeException("Book not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookController.getBook(bookId);
        });

        assertEquals("Book not found", exception.getMessage());
        verify(bookService, times(1)).getBookWithoutAuthors(bookId);
    }

    @Test
    void testGetFullBook_Success() {
        when(bookService.getBookWithAuthors(bookId)).thenReturn(bookFullResponseDto);

        ResponseEntity<BookFullResponseDto> response = bookController.getFullBook(bookId);

        verify(bookService, times(1)).getBookWithAuthors(bookId);
        assertEquals(bookFullResponseDto, response.getBody());
    }

    @Test
    void testGetBooks_Success() {
        List<BookResponseDto> bookResponseDtos = Arrays.asList(bookResponseDto);
        when(bookService.getBooksWithoutAuthors()).thenReturn(bookResponseDtos);

        ResponseEntity<List<BookResponseDto>> response = bookController.getBooks();

        verify(bookService, times(1)).getBooksWithoutAuthors();
        assertEquals(bookResponseDtos, response.getBody());
    }

    @Test
    void testGetFullBooks_Success() {
        List<BookFullResponseDto> bookFullResponseDtos = Arrays.asList(bookFullResponseDto);
        when(bookService.getBooksWithAuthors()).thenReturn(bookFullResponseDtos);

        ResponseEntity<List<BookFullResponseDto>> response = bookController.getFullBooks();

        verify(bookService, times(1)).getBooksWithAuthors();
        assertEquals(bookFullResponseDtos, response.getBody());
    }

    @Test
    void testAddBook_Success() {
        doNothing().when(bookService).createBook(bookCreateRequestDto);

        ResponseEntity<String> response = bookController.addBook(bookCreateRequestDto);

        verify(bookService, times(1)).createBook(bookCreateRequestDto);
        assertEquals("Book created", response.getBody());
    }

    @Test
    void testUpdateBook_Success() {
        doNothing().when(bookService).updateBook(bookId, bookCreateRequestDto);

        ResponseEntity<String> response = bookController.updateBook(bookId, bookCreateRequestDto);

        verify(bookService, times(1)).updateBook(bookId, bookCreateRequestDto);
        assertEquals("Book updated", response.getBody());
    }

    @Test
    void testDeleteBook_Success() {
        doNothing().when(bookService).deleteBook(bookId);

        ResponseEntity<String> response = bookController.deleteBook(bookId);

        verify(bookService, times(1)).deleteBook(bookId);
        assertEquals("Book deleted", response.getBody());
    }
}

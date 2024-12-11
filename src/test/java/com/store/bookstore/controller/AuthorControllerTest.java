package com.store.bookstore.controller;

import com.store.bookstore.dto.author.response.AuthorFullResponseDto;
import com.store.bookstore.dto.author.request.AuthorCreateRequestDto;
import com.store.bookstore.dto.author.response.AuthorResponseDto;
import com.store.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private AuthorResponseDto authorResponseDto;
    private AuthorFullResponseDto authorFullResponseDto;
    private AuthorCreateRequestDto authorCreateRequestDto;
    private String authorId;

    @BeforeEach
    void setUp() {
        authorId = "12345";
        authorResponseDto = new AuthorResponseDto(authorId, "John", "Doe");
        authorFullResponseDto = new AuthorFullResponseDto(authorId, "John", "Doe", null);
        authorCreateRequestDto = new AuthorCreateRequestDto(authorId, "John", "Doe", null);
    }

    @Test
    void testGetAuthor_Success() {
        when(authorService.getAuthorWithoutBooks(authorId)).thenReturn(authorResponseDto);

        ResponseEntity<AuthorResponseDto> response = authorController.getAuthor(authorId);

        verify(authorService, times(1)).getAuthorWithoutBooks(authorId);
        assertEquals(authorResponseDto, response.getBody());
    }

    @Test
    void testGetAuthor_NotFound() {
        when(authorService.getAuthorWithoutBooks(authorId)).thenThrow(new RuntimeException("Author not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authorController.getAuthor(authorId);
        });

        assertEquals("Author not found", exception.getMessage());
        verify(authorService, times(1)).getAuthorWithoutBooks(authorId);
    }

    @Test
    void testGetAuthorFullInfo_Success() {
        when(authorService.getAuthorWithBooks(authorId)).thenReturn(authorFullResponseDto);

        ResponseEntity<AuthorFullResponseDto> response = authorController.getAuthorFullInfo(authorId);

        verify(authorService, times(1)).getAuthorWithBooks(authorId);
        assertEquals(authorFullResponseDto, response.getBody());
    }

    @Test
    void testGetAuthors_Success() {
        List<AuthorResponseDto> authorList = Arrays.asList(authorResponseDto);
        when(authorService.getAuthorsWithoutBooks()).thenReturn(authorList);

        ResponseEntity<List<AuthorResponseDto>> response = authorController.getAuthors();

        verify(authorService, times(1)).getAuthorsWithoutBooks();
        assertEquals(authorList, response.getBody());
    }

    @Test
    void testGetFullAuthors_Success() {
        List<AuthorFullResponseDto> fullAuthorList = Arrays.asList(authorFullResponseDto);
        when(authorService.getAuthorsWithBooks()).thenReturn(fullAuthorList);

        ResponseEntity<List<AuthorFullResponseDto>> response = authorController.getFullAuthors();

        verify(authorService, times(1)).getAuthorsWithBooks();
        assertEquals(fullAuthorList, response.getBody());
    }

    @Test
    void testCreateAuthor_Success() {
        when(authorService.createAuthor(authorCreateRequestDto)).thenReturn(authorFullResponseDto);

        ResponseEntity<AuthorFullResponseDto> response = authorController.createAuthor(authorCreateRequestDto);

        verify(authorService, times(1)).createAuthor(authorCreateRequestDto);
        assertEquals(authorFullResponseDto, response.getBody());
    }

    @Test
    void testUpdateAuthor_Success() {
        when(authorService.updateAuthor(authorId, authorCreateRequestDto)).thenReturn(authorFullResponseDto);

        ResponseEntity<AuthorFullResponseDto> response = authorController.updateAuthor(authorId, authorCreateRequestDto);

        verify(authorService, times(1)).updateAuthor(authorId, authorCreateRequestDto);
        assertEquals(authorFullResponseDto, response.getBody());
    }

    @Test
    void testDeleteAuthor_Success() {
        doNothing().when(authorService).deleteAuthor(authorId);

        ResponseEntity<String> response = authorController.deleteAuthor(authorId);

        verify(authorService, times(1)).deleteAuthor(authorId);
        assertEquals("Author deleted", response.getBody());
    }
}

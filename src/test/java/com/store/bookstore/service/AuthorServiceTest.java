package com.store.bookstore.service;

import com.store.bookstore.dto.author.request.AuthorCreateRequestDto;
import com.store.bookstore.dto.author.response.AuthorFullResponseDto;
import com.store.bookstore.dto.author.response.AuthorResponseDto;
import com.store.bookstore.dto.book.response.BookResponseDto;
import com.store.bookstore.exception.EntityAlreadyExistsException;
import com.store.bookstore.exception.EntityNotFoundException;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    private ModelMapper modelMapper = new ModelMapper();

    private AuthorService authorService;

    private Author authorWithoutBooks;
    private Author authorWithBooks;
    private UUID authorId;
    private UUID book1Id;
    private UUID book2Id;
    private List<Book> books;

    private AuthorCreateRequestDto authorCreateRequestDto;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository, modelMapper);

        book1Id = UUID.randomUUID();
        book2Id = UUID.randomUUID();
        Book book1 = new Book(book1Id, "Sample Title", "Sample Description", 300, null);
        Book book2 = new Book(book2Id, "Sample Title 2", "Sample Description 2", 400, null);
        books = Arrays.asList(book1, book2);

        authorId = UUID.randomUUID();
        authorWithoutBooks = new Author(authorId, "John", "Doe", null);
        authorWithBooks = new Author(authorId, "John", "Doe", books);

//        authorCreateRequestDto = new AuthorCreateRequestDto();
//        authorCreateRequestDto.setName("John");
//        authorCreateRequestDto.setSurname("Doe");
//        authorCreateRequestDto.setBooks(Collections.emptyList());

//        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAuthorWithoutBooks_Success() {
        AuthorResponseDto authorResponseDto = new AuthorResponseDto(authorId.toString(), "John", "Doe");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(authorWithoutBooks));

        AuthorResponseDto result = authorService.getAuthorWithoutBooks(authorId.toString());

        verify(authorRepository, times(1)).findById(authorId);
        assertEquals(authorResponseDto, result);
    }

    @Test
    void testGetAuthorWithoutBooks_NotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            authorService.getAuthorWithoutBooks(authorId.toString());
        });

        assertEquals("Author not found with ID: " + authorId, exception.getMessage());
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void testGetAuthorWithBooks_Success() {
        AuthorFullResponseDto authorFullResponseDto = new AuthorFullResponseDto(authorId.toString(), "John", "Doe", Arrays.asList(
                new BookResponseDto(book1Id.toString(), "Sample Title", "Sample Description", 300),
                new BookResponseDto(book2Id.toString(), "Sample Title 2", "Sample Description 2", 400)
        ));

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(authorWithBooks));

        AuthorFullResponseDto result = authorService.getAuthorWithBooks(authorId.toString());

        verify(authorRepository, times(1)).findById(authorId);
        assertEquals(authorFullResponseDto, result);
    }

    @Test
    void testGetAuthorWithBooks_NotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            authorService.getAuthorWithBooks(authorId.toString());
        });

        assertEquals("Author not found with ID: " + authorId, exception.getMessage());
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void testCreateAuthor_Success() {
        AuthorCreateRequestDto authorCreateRequestDto = new AuthorCreateRequestDto(UUID.randomUUID().toString(), "John", "Doe", Collections.emptyList());

        when(authorRepository.existsByNameAndSurname("John", "Doe")).thenReturn(false);

        authorService.createAuthor(authorCreateRequestDto);

        verify(authorRepository, times(1)).save(argThat(author ->
                author.getName().equals("John") &&
                        author.getSurname().equals("Doe") &&
                        (author.getBooks().isEmpty())
        ));
    }

    @Test
    void testCreateAuthor_AlreadyExists() {
        AuthorCreateRequestDto authorCreateRequestDto = new AuthorCreateRequestDto(UUID.randomUUID().toString(), "John", "Doe", Collections.emptyList());

        when(authorRepository.existsByNameAndSurname(any(), any())).thenReturn(true);

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> {
            authorService.createAuthor(authorCreateRequestDto);
        });

        assertEquals("Author already exists with name: John and surname: Doe", exception.getMessage());
        verify(authorRepository, times(0)).save(any());
    }

    @Test
    void testUpdateAuthor_Success() {
        AuthorCreateRequestDto authorCreateRequestDto = new AuthorCreateRequestDto(UUID.randomUUID().toString(), "John", "Doe", Collections.emptyList());

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(authorWithoutBooks));

        authorService.updateAuthor(authorId.toString(), authorCreateRequestDto);

        verify(authorRepository, times(1)).save(argThat(author ->
                author.getName().equals("John") &&
                        author.getSurname().equals("Doe") &&
                        (author.getBooks().isEmpty())
        ));
    }

    @Test
    void testUpdateAuthor_NotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            authorService.updateAuthor(authorId.toString(), authorCreateRequestDto);
        });

        assertEquals("Author not found with ID: " + authorId, exception.getMessage());
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(0)).save(any());
    }

    @Test
    void testDeleteAuthor_Success() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(authorWithBooks));

        authorService.deleteAuthor(authorId.toString());

        verify(authorRepository, times(1)).delete(authorWithBooks);
    }

    @Test
    void testDeleteAuthor_NotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            authorService.deleteAuthor(authorId.toString());
        });

        assertEquals("Author not found with ID: " + authorId, exception.getMessage());
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(0)).delete(any());
    }
}

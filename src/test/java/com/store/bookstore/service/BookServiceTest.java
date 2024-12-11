package com.store.bookstore.service;

import com.store.bookstore.dto.author.response.AuthorResponseDto;
import com.store.bookstore.dto.book.request.BookCreateRequestDto;
import com.store.bookstore.dto.book.response.BookFullResponseDto;
import com.store.bookstore.dto.book.response.BookResponseDto;
import com.store.bookstore.exception.EntityNotFoundException;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.AuthorRepository;
import com.store.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    private ModelMapper modelMapper = new ModelMapper();

    private BookService bookService;

    private Book bookWithoutAuthor;
    private Book bookWithAuthor;
    private UUID bookId;
    private UUID authorId;
    private Author author;

    private BookCreateRequestDto bookCreateRequestDto;

    @BeforeEach
    void setUp() {
        bookService = new BookService(authorRepository, modelMapper, bookRepository);

        authorId = UUID.randomUUID();
        author = new Author(authorId, "John", "Doe", Collections.emptyList());

        bookId = UUID.randomUUID();
        bookWithoutAuthor = new Book(bookId, "Sample Title", "Sample Description", 300, null);
        bookWithAuthor = new Book(bookId, "Sample Title", "Sample Description", 300, author);

        bookCreateRequestDto = new BookCreateRequestDto("Sample Title", "Sample Description", 300, authorId.toString());
    }

    @Test
    void testGetBookWithoutAuthors_Success() {
        BookResponseDto bookResponseDto = new BookResponseDto(bookId.toString(), "Sample Title", "Sample Description", 300);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookWithoutAuthor));

        BookResponseDto result = bookService.getBookWithoutAuthors(bookId.toString());

        verify(bookRepository, times(1)).findById(bookId);
        assertEquals(bookResponseDto, result);
    }

    @Test
    void testGetBookWithoutAuthors_NotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.getBookWithoutAuthors(bookId.toString());
        });

        assertEquals("Book not found with ID: " + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetBookWithAuthors_Success() {
        BookFullResponseDto bookFullResponseDto = new BookFullResponseDto(bookId.toString(), "Sample Title", "Sample Description", 300, new AuthorResponseDto(authorId.toString(), "John", "Doe"));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookWithAuthor));

        BookFullResponseDto result = bookService.getBookWithAuthors(bookId.toString());

        verify(bookRepository, times(1)).findById(bookId);
        assertEquals(bookFullResponseDto, result);
    }

    @Test
    void testGetBookWithAuthors_NotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.getBookWithAuthors(bookId.toString());
        });

        assertEquals("Book not found with ID: " + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetBooksWithoutAuthors() {
        List<Book> books = Arrays.asList(bookWithoutAuthor, new Book(UUID.randomUUID(), "Another Title", "Description", 200, null));
        List<BookResponseDto> bookResponseDtos = books.stream().map(book -> modelMapper.map(book, BookResponseDto.class)).toList();

        when(bookRepository.findAll()).thenReturn(books);

        List<BookResponseDto> result = bookService.getBooksWithoutAuthors();

        verify(bookRepository, times(1)).findAll();
        assertEquals(bookResponseDtos.size(), result.size());
    }

    @Test
    void testGetBooksWithoutAuthors_EmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<BookResponseDto> result = bookService.getBooksWithoutAuthors();

        verify(bookRepository, times(1)).findAll();
        assertNull(result);
    }

    @Test
    void testGetBooksWithAuthors() {
        List<Book> books = Arrays.asList(bookWithAuthor, new Book(UUID.randomUUID(), "Another Title", "Description", 200, author));
        List<BookFullResponseDto> bookFullResponseDtos = books.stream().map(book -> modelMapper.map(book, BookFullResponseDto.class)).toList();

        when(bookRepository.findAll()).thenReturn(books);

        List<BookFullResponseDto> result = bookService.getBooksWithAuthors();

        verify(bookRepository, times(1)).findAll();
        assertEquals(bookFullResponseDtos.size(), result.size());
    }

    @Test
    void testGetBooksWithAuthors_EmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<BookFullResponseDto> result = bookService.getBooksWithAuthors();

        verify(bookRepository, times(1)).findAll();
        assertNull(result);
    }

    @Test
    void testCreateBook_Success() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        bookService.createBook(bookCreateRequestDto);

        verify(bookRepository, times(1)).save(argThat(book ->
                book.getTitle().equals("Sample Title") &&
                        book.getDescription().equals("Sample Description") &&
                        book.getPageCount() == 300 &&
                        book.getAuthor().equals(author)
        ));
    }

    @Test
    void testCreateBook_AuthorNotFound() {
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.createBook(bookCreateRequestDto);
        });

        assertEquals("Can't create book without author. Author not found with ID: " + authorId, exception.getMessage());
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    void testUpdateBook_Success() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookWithAuthor));

        bookCreateRequestDto.setTitle("Updated Title");
        bookCreateRequestDto.setDescription("Updated Description");
        bookCreateRequestDto.setPageCount(350);

        bookService.updateBook(bookId.toString(), bookCreateRequestDto);

        verify(bookRepository, times(1)).save(argThat(book ->
                book.getTitle().equals("Updated Title") &&
                        book.getDescription().equals("Updated Description") &&
                        book.getPageCount() == 350
        ));
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.updateBook(bookId.toString(), bookCreateRequestDto);
        });

        assertEquals("Book not found with ID: " + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookWithAuthor));

        bookService.deleteBook(bookId.toString());

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteBook(bookId.toString());
        });

        assertEquals("Book not found with ID: " + bookId, exception.getMessage());
    }
}
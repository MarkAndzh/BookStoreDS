package com.store.bookstore;

import com.store.bookstore.controller.BookController;
import com.store.bookstore.dto.AuthorResponseDto;
import com.store.bookstore.dto.BookFullResponseDto;
import com.store.bookstore.dto.BookResponseDto;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.BookRepository;
import com.store.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(BookService.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private ModelMapper modelMapper;

    private Book book;
    private BookResponseDto bookResponseDto;
    private BookFullResponseDto bookFullResponseDto;
    private AuthorResponseDto authorResponseDto;

    @BeforeEach
    public void setup() {
        book = new Book(UUID.randomUUID(), "Sample Title", "Sample Description", 300, new Author(UUID.randomUUID(), "Name", "Surname", null));
        bookResponseDto = new BookResponseDto(book.getId(), book.getTitle(), book.getDescription(), book.getPageCount());

        authorResponseDto = new AuthorResponseDto(book.getAuthor().getId().toString(), book.getAuthor().getName(), book.getAuthor().getSurname());
        bookFullResponseDto = new BookFullResponseDto(book.getId(), book.getTitle(), book.getDescription(), book.getPageCount(), authorResponseDto);
    }

    @Test
    public void testGetBook() throws Exception {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(modelMapper.map(any(Book.class), any(Class.class))).thenReturn(bookResponseDto);

        mockMvc.perform(get("/book/{id}", book.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + book.getId() + "\",\"title\":\"Sample Title\",\"description\":\"Sample Description\",\"pageCount\":300}"));
    }

    @Test
    public void testGetFullBook() throws Exception {
        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(modelMapper.map(any(Book.class), any(Class.class))).thenReturn(bookFullResponseDto);

        mockMvc.perform(get("/book/full/{id}", book.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + book.getId() + "\",\"title\":\"Sample Title\",\"description\":\"Sample Description\",\"pageCount\":300,\"author\":{\"id\":\"" + book.getAuthor().getId() + "\",\"name\":\"Name\",\"surname\":\"Surname\"}}"));
    }
}
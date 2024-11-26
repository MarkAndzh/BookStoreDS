package com.store.bookstore;

import com.store.bookstore.controller.AuthorController;
import com.store.bookstore.dto.AuthorFullResponseDto;
import com.store.bookstore.dto.AuthorRequestDto;
import com.store.bookstore.dto.AuthorResponseDto;
import com.store.bookstore.dto.BookResponseDto;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.AuthorRepository;
import com.store.bookstore.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import(AuthorService.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private ModelMapper modelMapper;

    private Author author;
    private AuthorResponseDto authorResponseDto;
    private AuthorFullResponseDto authorFullResponseDto;
    private BookResponseDto bookResponseDto1;
    private BookResponseDto bookResponseDto2;

    @BeforeEach
    public void setup() {
        author = new Author(UUID.randomUUID(), "John", "Doe", new ArrayList<>());
        Book book1 = new Book(UUID.randomUUID(), "Sample Title", "Sample Description", 300, author);
        Book book2 = new Book(UUID.randomUUID(), "Sample Title 2", "Sample Description 2", 400, author);
        author.getBooks().add(book1);
        author.getBooks().add(book2);

        authorResponseDto = new AuthorResponseDto(author.getId().toString(), author.getName(), author.getSurname());
        bookResponseDto1 = new BookResponseDto(book1.getId(), book1.getTitle(), book1.getDescription(), book1.getPageCount());
        bookResponseDto2 = new BookResponseDto(book2.getId(), book2.getTitle(), book2.getDescription(), book2.getPageCount());
        authorFullResponseDto = new AuthorFullResponseDto(author.getId().toString(), author.getName(), author.getSurname(), List.of(bookResponseDto1, bookResponseDto2));
    }

    @Test
    @DisplayName("Get author")
    public void testGetAuthor() throws Exception {
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.of(author));
        when(modelMapper.map(any(Author.class), any(Class.class))).thenReturn(authorResponseDto);

        mockMvc.perform(get("/author/{id}", author.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + author.getId() + "\",\"name\":\"John\",\"surname\":\"Doe\"}"));
    }

    @Test
    @DisplayName("Get author with books catalog")
    public void testGetAuthorFullInfo() throws Exception {
        when(authorRepository.findById(any(UUID.class))).thenReturn(Optional.of(author));
        when(modelMapper.map(any(Author.class), any(Class.class))).thenReturn(authorFullResponseDto);

        mockMvc.perform(get("/author/full/{id}", author.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + author.getId() + "\",\"name\":\"John\",\"surname\":\"Doe\",\"books\":[{\"id\":\"" + bookResponseDto1.getId() + "\",\"title\":\"Sample Title\",\"description\":\"Sample Description\",\"pageCount\":300},{\"id\":\"" + bookResponseDto2.getId() + "\",\"title\":\"Sample Title 2\",\"description\":\"Sample Description 2\",\"pageCount\":400}]}"));
    }

    @Test
    @DisplayName("Get all authors")
    public void testGetAuthors() throws Exception {
        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(modelMapper.map(any(Author.class), any(Class.class))).thenReturn(authorResponseDto);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"" + author.getId() + "\",\"name\":\"John\",\"surname\":\"Doe\"}]"));
    }

    @Test
    @DisplayName("Get all authors with books catalog")
    public void testGetFullAuthors() throws Exception {
        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(modelMapper.map(any(Author.class), any(Class.class))).thenReturn(authorFullResponseDto);

        mockMvc.perform(get("/authors/full"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"" + author.getId() + "\",\"name\":\"John\",\"surname\":\"Doe\",\"books\":[{\"id\":\"" + bookResponseDto1.getId() + "\",\"title\":\"Sample Title\",\"description\":\"Sample Description\",\"pageCount\":300},{\"id\":\"" + bookResponseDto2.getId() + "\",\"title\":\"Sample Title 2\",\"description\":\"Sample Description 2\",\"pageCount\":400}]}]"));
    }

    @Test
    @DisplayName("Create author")
    public void testCreateAuthor() throws Exception {
        AuthorRequestDto authorRequestDto = new AuthorRequestDto(UUID.randomUUID(),"John", "Doe", Collections.emptyList());
        when(modelMapper.map(any(AuthorRequestDto.class), any(Class.class))).thenReturn(author);

        mockMvc.perform(post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"surname\":\"Doe\",\"books\":[]}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Author created"));
    }
}
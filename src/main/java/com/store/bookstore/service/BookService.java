package com.store.bookstore.service;

import com.store.bookstore.dto.book.request.BookCreateRequestDto;
import com.store.bookstore.dto.book.response.BookFullResponseDto;
import com.store.bookstore.dto.book.response.BookResponseDto;
import com.store.bookstore.exception.EntityNotFoundException;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.AuthorRepository;
import com.store.bookstore.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    private final BookRepository bookRepository;

    public BookResponseDto getBookWithoutAuthors(String id){
        Book book = bookRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));

        return modelMapper.map(book, BookResponseDto.class);
    }

    @Transactional
    public BookFullResponseDto getBookWithAuthors(String id){
        Book book = bookRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));

        return modelMapper.map(book, BookFullResponseDto.class);
    }

    public List<BookResponseDto> getBooksWithoutAuthors() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()){
            return null;
        }

        return books.stream().map((book) -> modelMapper.map(book, BookResponseDto.class)).collect(Collectors.toList());
    }

    @Transactional
    public List<BookFullResponseDto> getBooksWithAuthors() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()){
            return null;
        }

        return books.stream().map((book) -> modelMapper.map(book, BookFullResponseDto.class)).collect(Collectors.toList());
    }

    @Transactional
    public void createBook(BookCreateRequestDto bookRequestDto) {
        Author author = authorRepository
                .findById(UUID.fromString(bookRequestDto.getAuthorId()))
                .orElseThrow(() -> new EntityNotFoundException("Can't create book without author. Author not found with ID: " + bookRequestDto.getAuthorId()));

        Book book = new Book();

        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setPageCount(bookRequestDto.getPageCount());
        book.setAuthor(author);

        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(String id, BookCreateRequestDto bookRequestDto) {
        Book book = bookRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));

        book.setTitle(bookRequestDto.getTitle());
        book.setTitle("ABC");
        book.setDescription(bookRequestDto.getDescription());
        book.setPageCount(bookRequestDto.getPageCount());

        bookRepository.save(book);
    }

    public void deleteBook(String id) {
        Book book = bookRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));

        bookRepository.deleteById(UUID.fromString(id));
    }
}

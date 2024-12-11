package com.store.bookstore.service;

import com.store.bookstore.dto.author.response.AuthorFullResponseDto;
import com.store.bookstore.dto.author.request.AuthorCreateRequestDto;
import com.store.bookstore.dto.author.response.AuthorResponseDto;
import com.store.bookstore.exception.EntityAlreadyExistsException;
import com.store.bookstore.exception.EntityNotFoundException;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    public AuthorResponseDto getAuthorWithoutBooks(String id){
        Author author = authorRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + id));

        return modelMapper.map(author, AuthorResponseDto.class);
    }

    @Transactional
    public AuthorFullResponseDto getAuthorWithBooks(String id){
        Author author = authorRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + id));

        return modelMapper.map(author, AuthorFullResponseDto.class);
    }

    public List<AuthorResponseDto> getAuthorsWithoutBooks() {
        List<Author> authors = authorRepository
                .findAll();

        if (authors.isEmpty()){
            throw new EntityNotFoundException("Authors not found");
        }

        return authors.stream().map((author) -> {
            return modelMapper.map(author, AuthorResponseDto.class);
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<AuthorFullResponseDto> getAuthorsWithBooks() {
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()){
            throw new EntityNotFoundException("Authors not found");
        }

        return authors.stream().map((author) -> {
            return modelMapper.map(author, AuthorFullResponseDto.class);
        }).collect(Collectors.toList());
    }

    @Transactional
    public AuthorFullResponseDto createAuthor(AuthorCreateRequestDto authorDto) {
        Author authorEntity = modelMapper.map(authorDto, Author.class);
        if (authorRepository.existsByNameAndSurname(authorEntity.getName(), authorEntity.getSurname())) {
            throw new EntityAlreadyExistsException("Author already exists with name: " + authorEntity.getName() + " and surname: " + authorEntity.getSurname());
        }

        if (authorDto.getBooks() != null) {
            List<Book> books = authorDto.getBooks().stream().map((bookDto) ->  {
                Book book = modelMapper.map(bookDto, Book.class);
                book.setAuthor(authorEntity);
                return book;
            }).toList();

            authorEntity.setBooks(books);
        }

        Author savedAuthor = authorRepository.save(authorEntity);
        return modelMapper.map(savedAuthor, AuthorFullResponseDto.class);
    }

    @Transactional
    public AuthorFullResponseDto updateAuthor(String id, AuthorCreateRequestDto authorDto) {
        Author author = authorRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + id));

        author.setName(authorDto.getName());
        author.setSurname(authorDto.getSurname());

        if (authorDto.getBooks() != null) {
            List<Book> books = authorDto.getBooks().stream().map((bookDto) -> {
                Book book = modelMapper.map(bookDto, Book.class);
                book.setAuthor(author);
                return book;
            }).collect(Collectors.toList());
            author.setBooks(books);
        }

        Author updatedAuthor = authorRepository.save(author);
        return modelMapper.map(updatedAuthor, AuthorFullResponseDto.class);
    }

    public void deleteAuthor(String id) {
        Author author = authorRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + id));

        authorRepository.delete(author);
    }
}

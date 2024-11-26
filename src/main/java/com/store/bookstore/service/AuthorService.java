package com.store.bookstore.service;

import com.store.bookstore.dto.AuthorFullResponseDto;
import com.store.bookstore.dto.AuthorRequestDto;
import com.store.bookstore.dto.AuthorResponseDto;
import com.store.bookstore.dto.BookResponseDto;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    public AuthorResponseDto getAuthorWithoutBooks(String id){
        Optional<Author> authorOptional = authorRepository.findById(UUID.fromString(id));

        if (authorOptional.isEmpty()){
            return null;
        }

        Author author = authorOptional.get();
        return modelMapper.map(author, AuthorResponseDto.class);
    }

    @Transactional
    public AuthorFullResponseDto getAuthorWithBooks(String id){
        Optional<Author> authorOptional = authorRepository.findById(UUID.fromString(id));

        if(authorOptional.isEmpty()){
            return null;
        }

        Author author = authorOptional.get();
        return modelMapper.map(author, AuthorFullResponseDto.class);
    }

    public List<AuthorResponseDto> getAuthorsWithoutBooks() {
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()){
            return null;
        }

        return authors.stream().map((author) -> {
            return modelMapper.map(author, AuthorResponseDto.class);
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<AuthorFullResponseDto> getAuthorsWithBooks() {
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()){
            return null;
        }

        return authors.stream().map((author) -> {
            return modelMapper.map(author, AuthorFullResponseDto.class);
        }).collect(Collectors.toList());
    }

    public void createAuthorWithBooks(AuthorRequestDto author) {
        Author authorEntity = modelMapper.map(author, Author.class);
        List<Book> books = author.getBooks().stream().map((bookDto) ->  {
            Book book = modelMapper.map(bookDto, Book.class);
            book.setAuthor(authorEntity);
            return book;
        }).toList();

        authorEntity.setBooks(books);

        authorRepository.save(authorEntity);
    }
}

package com.store.bookstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
@Getter
public class Book {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String description;
    private Integer pageCount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference
    private Author author;
}

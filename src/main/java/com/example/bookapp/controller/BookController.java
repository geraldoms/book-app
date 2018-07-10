package com.example.bookapp.controller;

import com.example.bookapp.exception.BookNotFoundException;
import com.example.bookapp.model.Author;
import com.example.bookapp.model.Book;
import com.example.bookapp.repository.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    @GetMapping
    public List<Book> findAll() {

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {

        return repository.findById(id)
                         .orElseThrow(() -> new BookNotFoundException("id", id.toString()));
    }

    @GetMapping("/{id}/authors")
    public List<Author> findAllAuthorsByBook(@PathVariable Long id) {

        return new ArrayList<>(findOne(id).getAuthors());
    }

    @PostMapping
    public Book create(@Valid @RequestBody Book book) {

        return repository.save(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Book book = findOne(id);
        repository.delete(book);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody Book book) {

        Book dbBook = findOne(id);
        BeanUtils.copyProperties(book, dbBook);
        return repository.save(dbBook);
    }
}

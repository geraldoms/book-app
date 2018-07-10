package com.example.bookapp.controller;

import com.example.bookapp.exception.BookNotFoundException;
import com.example.bookapp.model.Author;
import com.example.bookapp.model.Book;
import com.example.bookapp.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookControllerTest {

    @InjectMocks
    BookController bookController;

    @Mock
    BookRepository repository;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {

        final List<Book> books = new ArrayList<>();
        books.add(new Book(2L, "Title Test", 1, 15.50, new Date(), new HashSet<>()));
        books.add(new Book(3L, "Title02 Test", 2, 18.50, new Date(), new HashSet<>()));

        when(repository.findAll()).thenReturn(books);

        final List<Book> response = bookController.findAll();
        verify(repository, times(1)).findAll();
        assertThat(response.size(), is(2));
        assertThat(response.get(0).getId(), is(2L));
        assertThat(response.get(0).getTitle(), is("Title Test"));
        assertThat(response.get(1).getId(), is(3L));
        assertThat(response.get(1).getTitle(), is("Title02 Test"));
    }

    @Test
    public void testFindOne() {

        final Book book = new Book(1L, "Title Test II", 3, 10.50, new Date(), new HashSet<>());
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        final Book result = bookController.findOne(1L);
        verify(repository, times(1)).findById(1L);
        assertThat(result, Matchers.notNullValue());
        assertThat(result.getId(), is(1L));
        assertThat(result.getTitle(), is("Title Test II"));
        assertThat(result.getVersion(), is(3));
    }

    @Test(expected = BookNotFoundException.class)
    public void testFindOneWithNotFoundException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());
        bookController.findOne(1L);
    }

    @Test
    public void testCreate() {

        final Book book = new Book(1L, "Title Test III", 5, 10.50, new Date(), new HashSet<>());
        when(repository.save(any(Book.class))).thenReturn(book);

        final Book result = bookController.create(book);
        verify(repository, times(1)).save(book);
        assertThat(result, Matchers.notNullValue());
        assertThat(result.getId(), is(1L));
        assertThat(result.getTitle(), is("Title Test III"));
        assertThat(result.getVersion(), is(5));
        assertThat(result.getPrice(), is(10.5));
    }

    @Test
    public void testUpdate() {

        final Book book = new Book(1L, "Title Test IIII", 8, 18.50, new Date(), new HashSet<>());

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenReturn(book);

        final Book result = bookController.update(1L, book);
        verify(repository, times(1)).save(book);
        assertThat(result, Matchers.notNullValue());
        assertThat(result.getId(), is(1L));
        assertThat(result.getTitle(), is("Title Test IIII"));
        assertThat(result.getVersion(), is(8));
        assertThat(result.getPrice(), is(18.5));
    }

    @Test
    public void testDelete() {

        final Book book = new Book(1L, "Title Test IIIII", 11, 15.50, new Date(), new HashSet<>());

        Mockito.doNothing().when(repository).deleteById(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        final ResponseEntity reponse = bookController.delete(1L);
        verify(repository, times(1)).delete(book);
        assertThat(reponse.getStatusCode().name(), is("OK"));
    }

    @Test
    public void testFindAllCoursesByStudent() {

        final Set<Author> authors = new HashSet<>();
        authors.add(new Author(1L, "Tom", "Jackson", new HashSet<>()));

        final Book book = new Book(3L, "Title Test IIIIII", 13, 55.50, new Date(), authors);

        when(repository.findById(3L)).thenReturn(Optional.of(book));

        final List<Author> response = bookController.findAllAuthorsByBook(3L);
        verify(repository, times(1)).findById(3L);
        assertThat(response, Matchers.notNullValue());
        assertThat(response.size(), is(1));
        assertThat(response.get(0).getId(), is(1L));
        assertThat(response.get(0).getFirstName(), is("Tom"));
        assertThat(response.get(0).getLastName(), is("Jackson"));
    }
}

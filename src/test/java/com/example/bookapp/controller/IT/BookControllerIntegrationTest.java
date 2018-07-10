package com.example.bookapp.controller.IT;

import com.example.bookapp.BookAppApplication;
import com.example.bookapp.model.Author;
import com.example.bookapp.model.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = { "test" })
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private URL baseURL;

    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void setUp() throws MalformedURLException {

        this.baseURL = new URL("http://localhost:" + port + "/api/v1/books");
    }

    @Test
    public void testFindAll() {

        final ResponseEntity<Book[]> response = restTemplate.getForEntity(this.baseURL.toString(), Book[].class);

        assertThat(response.getStatusCode().name(), is("OK"));

        final Book[] books = response.getBody();
        assertThat(books.length, is(2));
        assertThat(books[0].getTitle(), is("Clean Code: A Handbook of Agile Software Craftsmanship"));
        assertThat(books[1].getTitle(),
            is("The Phoenix Project: A Novel about IT, DevOps, and Helping Your Business Win"));
    }

    @Test
    public void testFindOne() {

        final ResponseEntity<Book> response = restTemplate.getForEntity(this.baseURL.toString() + "/1", Book.class);
        assertThat(response.getStatusCode().name(), is("OK"));

        final Book book = response.getBody();
        assertThat(book, notNullValue());
        assertThat(book.getTitle(), is("Clean Code: A Handbook of Agile Software Craftsmanship"));
    }

    @Test
    public void testFindAllAuthorsByBook() {

        final ResponseEntity<Author[]> response =
            restTemplate.getForEntity(this.baseURL.toString() + "/1/authors", Author[].class);
        assertThat(response.getStatusCode().name(), is("OK"));

        final Author[] authors = response.getBody();
        assertThat(authors.length, is(1));
        assertThat(authors[0].getFirstName(), is("Robert"));
        assertThat(authors[0].getLastName(), is("Martin"));
    }

    @Test
    public void testCreate() {

        final Set<Author> authors = new HashSet<>();
        authors.add(new Author(null, "Tom", "Jackson", new HashSet<>()));
        final Book book = new Book(null, "Title Test II", 3, 10.50, new Date(), authors);

        final ResponseEntity<Book> response = restTemplate.postForEntity(this.baseURL.toString(), book, Book.class);
        assertThat(response.getStatusCode().name(), is("OK"));

        final Book bookResp = response.getBody();
        assertThat(bookResp, notNullValue());
        assertThat(bookResp.getId(), notNullValue());
        assertThat(bookResp.getTitle(), is("Title Test II"));
        assertThat(bookResp.getVersion(), is(3));
        assertThat(bookResp.getAuthors().size(), is(1));

        final Author author = bookResp.getAuthors().iterator().next();
        assertThat(author.getId(), notNullValue());
        assertThat(author.getFirstName(), is("Tom"));
    }

    @Test
    public void testDelete() {

        final ResponseEntity<Void> response =
            restTemplate.exchange(this.baseURL.toString() + "/2", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode().name(), is("OK"));
    }

    @Test
    public void testUpdate() {

        final Set<Author> authors = new HashSet<>();
        authors.add(new Author(1L, "Robert II", "Martin II", new HashSet<>()));
        final Book book = new Book(1L, "Clean Code II", 2, 19.50, new Date(), authors);

        final HttpEntity<Book> requestUpdate = new HttpEntity<>(book);
        final ResponseEntity<Book> response =
            restTemplate.exchange(this.baseURL.toString() + "/1", HttpMethod.PUT, requestUpdate, Book.class);

        assertThat(response.getStatusCode().name(), is("OK"));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), is(1L));
        assertThat(response.getBody().getTitle(), is("Clean Code II"));
        assertThat(response.getBody().getAuthors().size(), is(1));

        final Author author = response.getBody().getAuthors().iterator().next();
        assertThat(author, notNullValue());
        assertThat(author.getId(), is(1L));
        assertThat(author.getFirstName(), is("Robert II"));
    }
}

package com.example.bookapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BOOK")
public class Book extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 250)
    private String title;

    @NotNull
    private int version;

    @NotNull
    private Double price;

    @Temporal(TemporalType.DATE)
    private Date publishingDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "AUTHOR_BOOK", joinColumns = { @JoinColumn(name = "BOOK_ID") }, inverseJoinColumns = {
        @JoinColumn(name = "AUTHOR_ID") })
    private Set<Author> authors = new HashSet<>();

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public int getVersion() {

        return version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public Double getPrice() {

        return price;
    }

    public void setPrice(Double price) {

        this.price = price;
    }

    public Date getPublishingDate() {

        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {

        this.publishingDate = publishingDate;
    }

    public Set<Author> getAuthors() {

        return authors;
    }

    public void setAuthors(Set<Author> authors) {

        this.authors = authors;
    }
}

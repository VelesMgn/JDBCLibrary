package com.store.jdbclibrary;

import com.store.jdbclibrary.model.Book;
import com.store.jdbclibrary.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void saveAndFindBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(Year.of(2023));

        Book saved = bookRepository.save(book);
        assertNotNull(saved.getId());

        Optional<Book> found = bookRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Book", found.get().getTitle());
    }

    @Test
    void updateBook() {
        Book book = new Book();
        book.setTitle("Original Title");
        book.setAuthor("Original Author");
        book.setPublicationYear(Year.of(2020));
        Book saved = bookRepository.save(book);

        saved.setTitle("Updated Title");
        bookRepository.save(saved);

        Optional<Book> updated = bookRepository.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals("Updated Title", updated.get().getTitle());
    }

    @Test
    void deleteBook() {
        Book book = new Book();
        book.setTitle("To Delete");
        book.setAuthor("Delete Author");
        book.setPublicationYear(Year.of(2021));
        Book saved = bookRepository.save(book);

        bookRepository.deleteById(saved.getId());

        Optional<Book> deleted = bookRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void findAllBooks() {
        bookRepository.save(Book.builder().title("Book 1").author("Author 1")
                .publicationYear(Year.of(2021)).build());
        bookRepository.save(Book.builder().title("Book 2").author("Author 2")
                .publicationYear(Year.of(2022)).build());

        List<Book> books = bookRepository.findAll();
        assertTrue(books.size() >= 2);
    }
}
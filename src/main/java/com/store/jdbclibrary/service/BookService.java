package com.store.jdbclibrary.service;

import com.store.jdbclibrary.model.Book;
import com.store.jdbclibrary.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.bookRepository.createTable();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        bookDetails.setId(id);
        return bookRepository.save(bookDetails);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

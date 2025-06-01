package com.store.jdbclibrary.repository;

import com.store.jdbclibrary.model.Book;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.sql.Statement;

@Repository
@AllArgsConstructor
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        return jdbcTemplate.query(sql, new BookRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Transactional
    public Book save(Book book) {
        if (book.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setInt(3, book.getPublicationYear().getValue());
                return ps;
            }, keyHolder);

            book.setId(keyHolder.getKey().longValue());
            return book;
        } else {
            String sql = "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublicationYear().getValue(),
                    book.getId());
            return book;
        }
    }

    @Transactional
    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Transactional
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "author VARCHAR(255) NOT NULL," +
                "publication_year INT NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        jdbcTemplate.execute(sql);
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setPublicationYear(Year.of(rs.getInt("publication_year")));
            return book;
        }
    }
}
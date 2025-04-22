package main.library;

import java.util.*;

public class Catalog {
    private final Map<String, Book> books = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public List<Book> listAvailable() {
        List<Book> list = new ArrayList<>();
        for (Book b : books.values()) {
            if (b.isAvailable()) list.add(b);
        }
        return list;
    }
}
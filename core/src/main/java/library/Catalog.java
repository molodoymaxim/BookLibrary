package library;

import java.util.*;
import java.util.stream.Collectors;

public class Catalog {
    private final Map<String, Book> books = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public List<Book> listAvailable() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Book> listAll() {
        return new ArrayList<>(books.values());
    }

    public List<Book> searchByTitle(String titlePart) {
        String lower = titlePart.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String authorPart) {
        String lower = authorPart.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}

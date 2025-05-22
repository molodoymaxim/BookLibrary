package library;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

    @Test
    void testAddAndFindByIsbn() {
        Catalog catalog = new Catalog();
        Book b = new Book("999", "X", "Y");
        catalog.addBook(b);
        Optional<Book> found = catalog.findByIsbn("999");
        assertTrue(found.isPresent());
        assertEquals(b, found.get());
        assertFalse(catalog.findByIsbn("notexists").isPresent());
    }

    @Test
    void testListAvailable() {
        Catalog catalog = new Catalog();
        Book b1 = new Book("1", "A", "A");
        Book b2 = new Book("2", "B", "B");
        b2.setAvailable(false);
        catalog.addBook(b1);
        catalog.addBook(b2);

        List<Book> avail = catalog.listAvailable();
        assertEquals(1, avail.size());
        assertEquals("1", avail.get(0).getIsbn());
    }

    @Test
    void testAddBookWithExistingISBN() {
        Catalog catalog = new Catalog();
        Book b1 = new Book("999", "X", "Y");
        Book b2 = new Book("999", "Z", "W"); // Same ISBN

        catalog.addBook(b1);
        catalog.addBook(b2); // Should overwrite the previous book

        Optional<Book> found = catalog.findByIsbn("999");
        assertTrue(found.isPresent());
        assertEquals(b2, found.get()); // Check if the second book overwrote the first
    }

    @Test
    void testListAvailableWithNoAvailableBooks() {
        Catalog catalog = new Catalog();
        Book b1 = new Book("1", "A", "A");
        Book b2 = new Book("2", "B", "B");
        b1.setAvailable(false);
        b2.setAvailable(false);

        catalog.addBook(b1);
        catalog.addBook(b2);

        List<Book> avail = catalog.listAvailable();
        assertTrue(avail.isEmpty());
    }

    @Test
    void testListAvailableWithEmptyCatalog() {
        Catalog catalog = new Catalog();

        List<Book> avail = catalog.listAvailable();
        assertTrue(avail.isEmpty());
    }

    @Test
    void testListAllWithEmptyCatalog() {
        Catalog catalog = new Catalog();

        List<Book> all = catalog.listAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void testSearchByTitleWithMatchingBooks() {
        Catalog catalog = new Catalog();
        Book b1 = new Book("1", "Java Programming", "Author1");
        Book b2 = new Book("2", "Python Programming", "Author2");

        catalog.addBook(b1);
        catalog.addBook(b2);

        List<Book> result = catalog.searchByTitle("Programming");
        assertEquals(2, result.size());
        assertTrue(result.contains(b1));
        assertTrue(result.contains(b2));
    }

    @Test
    void testSearchByTitleWithNoMatchingBooks() {
        Catalog catalog = new Catalog();
        Book b1 = new Book("1", "Java Programming", "Author1");

        catalog.addBook(b1);

        List<Book> result = catalog.searchByTitle("Ruby");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchByTitleWithEmptyCatalog() {
        Catalog catalog = new Catalog();

        List<Book> result = catalog.searchByTitle("Any");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchByAuthorWithMatchingBooks() {
        Catalog catalog = new Catalog();
        Book b1 = new Book("1", "Book1", "John Doe");
        Book b2 = new Book("2", "Book2", "Jane Smith");

        catalog.addBook(b1);
        catalog.addBook(b2);

        List<Book> result = catalog.searchByAuthor("Doe");
        assertEquals(1, result.size());
        assertEquals(b1, result.get(0));
    }

    @Test
    void testSearchByAuthorWithNoMatchingBooks() {
        Catalog catalog = new Catalog();
        Book b1 = new Book("1", "Book1", "John Doe");

        catalog.addBook(b1);

        List<Book> result = catalog.searchByAuthor("Smith");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchByAuthorWithEmptyCatalog() {
        Catalog catalog = new Catalog();

        List<Book> result = catalog.searchByAuthor("Any");
        assertTrue(result.isEmpty());
    }
}
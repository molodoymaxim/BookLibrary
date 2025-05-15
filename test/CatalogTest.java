package test;

import library.Catalog;
import library.Book;

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
}
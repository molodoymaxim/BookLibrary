package library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testConstructorAndGetters() {
        Book book = new Book("123", "Test Title", "Author Name");
        assertEquals("123", book.getIsbn());
        assertEquals("Test Title", book.getTitle());
        assertEquals("Author Name", book.getAuthor());
        assertTrue(book.isAvailable());
    }

    @Test
    void testAvailabilityToggle() {
        Book book = new Book("123", "T", "A");
        book.setAvailable(false);
        assertFalse(book.isAvailable());
        book.setAvailable(true);
        assertTrue(book.isAvailable());
    }

    @Test
    void testToString() {
        Book book = new Book("123", "T", "A");
        String s = book.toString();
        assertTrue(s.contains("123"));
        assertTrue(s.contains("T"));
        assertTrue(s.contains("A"));
        assertTrue(s.contains("available"));
    }
}

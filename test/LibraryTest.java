package test;

import library.Library;
import library.Book;
import library.Member;
import library.Loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private Library lib;
    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        lib = new Library();
        book = new Book("42", "Answer", "Author");
        member = new Member("M42", "Deep Thought");
        lib.addBook(book);
        lib.registerMember(member);
    }

    @Test
    void testSuccessfulBorrowAndReturn() {
        assertTrue(lib.borrowBook("42", "M42"));
        assertFalse(book.isAvailable());

        List<Loan> active = lib.getActiveLoans();
        assertEquals(1, active.size());
        assertEquals(book, active.get(0).getBook());

        assertTrue(lib.returnBook("42", "M42"));
        assertTrue(book.isAvailable());
        assertTrue(lib.getActiveLoans().isEmpty());
    }

    @Test
    void testBorrowFailures() {
        // Неправильный ISBN
        assertFalse(lib.borrowBook("no", "M42"));
        // Неправильный MemberId
        assertFalse(lib.borrowBook("42", "no"));
        // Уже выдана
        assertTrue(lib.borrowBook("42", "M42"));
        assertFalse(lib.borrowBook("42", "M42"));
    }

    @Test
    void testReturnFailures() {
        // ничего не выдавали
        assertFalse(lib.returnBook("42", "M42"));
        // выдаём, возвращаем, пробуем вернуть снова
        lib.borrowBook("42", "M42");
        assertTrue(lib.returnBook("42", "M42"));
        assertFalse(lib.returnBook("42", "M42"));
    }
}

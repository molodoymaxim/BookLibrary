package library;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void testLoanCreatesAndMarksUnavailable() {
        Book book = new Book("111", "B", "A");
        Member member = new Member("M", "N");
        Loan loan = new Loan(book, member);

        assertEquals(book, loan.getBook());
        assertEquals(member, loan.getMember());
        assertNotNull(loan.getLoanDate());
        assertNull(loan.getReturnDate());
        assertFalse(book.isAvailable());
    }

    @Test
    void testReturnBook() {
        Book book = new Book("111", "B", "A");
        Member member = new Member("M", "N");
        Loan loan = new Loan(book, member);

        loan.returnBook();
        assertNotNull(loan.getReturnDate());
        assertTrue(book.isAvailable());
        // Повторный вызов не должен изменить дату
        LocalDate firstReturn = loan.getReturnDate();
        loan.returnBook();
        assertEquals(firstReturn, loan.getReturnDate());
    }

    @Test
    void testToStringIncludesDates() {
        Book book = new Book("111", "B", "A");
        Member member = new Member("M", "N");
        Loan loan = new Loan(book, member);
        String s1 = loan.toString();
        assertTrue(s1.contains("loaned to"));

        loan.returnBook();
        String s2 = loan.toString();
        assertTrue(s2.contains("returned on"));
    }
}

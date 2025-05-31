package library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

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

//    @BeforeEach
//    void setUp() {
//        library = new Library();
//        Book book1 = new Book("123", "Книга 1", "Автор А");
//        book2 = new Book("456", "Книга 2", "Автор Б");
//        member1 = new Member("M001", "Алексей");
//        member2 = new Member("M002", "Мария");
//
//        // Добавляем начальные книги в каталог
//        library.addBook(book1);
//        library.addBook(book2);
//        library.registerMember(member1);
//        library.registerMember(member2);
//    }

    @Test
    void testOverdueLoans() {
        lib.borrowBook("42", "M42");

        // Получаем активный заем
        Loan loan = lib.getActiveLoans().get(0);

        // Имитируем возврат через 3 дня после срока
        LocalDate dueDate = loan.getDueDate();
        LocalDate returnDate = dueDate.plusDays(3);
        loan.returnBookOn(returnDate);

        List<Loan> overdue = lib.listOverdueLoans();
        assertEquals(1, overdue.size(), "Expected 1 overdue loan");
    }

    @Test
    void testFineCalculation() {
        lib.borrowBook("42", "M42");

        // Получаем активный заем
        Loan loan = lib.getActiveLoans().get(0);

        // Имитируем возврат через 3 дня после срока
        LocalDate dueDate = loan.getDueDate();
        LocalDate returnDate = dueDate.plusDays(3);
        loan.returnBookOn(returnDate);

        long fine = lib.calculateFine("42", "M42");
        assertEquals(3, fine); // Ожидаем штраф за 3 дня просрочки
    }

    @Test
    void testSetLoans_shouldReplaceExistingLoans() {
        lib.borrowBook("123", "M001");
        assertEquals(1, lib.getActiveLoans().size());

        List<Loan> newLoans = new ArrayList<>();
        newLoans.add(new Loan(book1, member1, 14));
        newLoans.add(new Loan(book2, member2, 7));

        Library Booklibrary;
        Booklibrary.setLoans(newLoans);

        List<Loan> currentLoans = lib.getActiveLoans();
        assertEquals(2, currentLoans.size());
        assertTrue(currentLoans.containsAll(newLoans));
    }
}

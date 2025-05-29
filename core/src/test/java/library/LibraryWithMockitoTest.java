package library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryWithMockitoTest {

    @Mock
    private Catalog catalog;

    @Mock
    private Book book;

    @Mock
    private Member member;

    @Mock
    private Loan loan;

    @InjectMocks
    private Library library;

    private final String isbn = "1234567890";
    private final String memberId = "M001";

    @BeforeEach
    void setUp() throws Exception {
        // Сброс мок-объектов перед каждым тестом
        reset(catalog, book, member, loan);
        // Регистрация члена для заполнения карты members
        lenient().when(member.getMemberId()).thenReturn(memberId);
        library.registerMember(member);
        // Очистка списка loans с использованием рефлексии
        setLoansField(new ArrayList<>());
    }

    // Вспомогательный метод для установки приватного поля loans с помощью рефлексии
    private void setLoansField(List<Loan> loans) throws Exception {
        Field loansField = Library.class.getDeclaredField("loans");
        loansField.setAccessible(true);
        loansField.set(library, loans);
    }

    @Test
    void testBorrowBook_Successful() {
        lenient().when(catalog.findByIsbn(isbn)).thenReturn(Optional.of(book));
        lenient().when(book.isAvailable()).thenReturn(true);
        lenient().when(book.getIsbn()).thenReturn(isbn);

        boolean result = library.borrowBook(isbn, memberId);

        assertTrue(result, "Займ должен быть успешным");
        verify(catalog).findByIsbn(isbn);
        verify(book).setAvailable(false);
    }

    @Test
    void testBorrowBook_BookNotAvailable() {
        lenient().when(catalog.findByIsbn(isbn)).thenReturn(Optional.of(book));
        lenient().when(book.isAvailable()).thenReturn(false);

        boolean result = library.borrowBook(isbn, memberId);

        assertFalse(result, "Займ должен провалиться, если книга недоступна");
        verify(catalog).findByIsbn(isbn);
        verify(book, never()).setAvailable(anyBoolean());
    }

    @Test
    void testBorrowBook_BookNotFound() {
        lenient().when(catalog.findByIsbn(isbn)).thenReturn(Optional.empty());

        boolean result = library.borrowBook(isbn, memberId);

        assertFalse(result, "Займ должен провалиться, если книга не найдена");
        verify(catalog).findByIsbn(isbn);
        verify(book, never()).setAvailable(anyBoolean());
    }

    @Test
    void testBorrowBook_MemberNotFound() {
        String unknownMemberId = "M002"; // Не зарегистрирован
        lenient().when(catalog.findByIsbn(isbn)).thenReturn(Optional.of(book));
        lenient().when(book.isAvailable()).thenReturn(true);

        boolean result = library.borrowBook(isbn, unknownMemberId);

        assertFalse(result, "Займ должен провалиться, если член не найден");
        verify(catalog).findByIsbn(isbn);
        verify(book, never()).setAvailable(anyBoolean());
    }

    @Test
    void testReturnBook_Successful() throws Exception {
        // Подготовка: Установка займа в список loans с помощью рефлексии
        lenient().when(loan.getBook()).thenReturn(book);
        lenient().when(loan.getMember()).thenReturn(member);
        lenient().when(loan.getReturnDate()).thenReturn(null);
        lenient().when(book.getIsbn()).thenReturn(isbn);
        lenient().when(member.getMemberId()).thenReturn(memberId);
        List<Loan> loans = new ArrayList<>();
        loans.add(loan);
        setLoansField(loans);

        boolean result = library.returnBook(isbn, memberId);

        assertTrue(result, "Возврат книги должен быть успешным");
        verify(loan).returnBook();
    }

    @Test
    void testReturnBook_LoanNotFound() {
        // Подготовка: Нет займов в списке
        lenient().when(catalog.findByIsbn(isbn)).thenReturn(Optional.empty());

        boolean result = library.returnBook(isbn, memberId);

        assertFalse(result, "Возврат книги должен провалиться, если соответствующий займ не найден");
        verify(loan, never()).returnBook();
    }

    @Test
    void testCalculateFine_OverdueLoan() throws Exception {
        // Подготовка: Установка займа в список loans с помощью рефлексии
        lenient().when(loan.getBook()).thenReturn(book);
        lenient().when(loan.getMember()).thenReturn(member);
        lenient().when(loan.getReturnDate()).thenReturn(null);
        lenient().when(book.getIsbn()).thenReturn(isbn);
        lenient().when(member.getMemberId()).thenReturn(memberId);
        lenient().when(loan.isOverdue()).thenReturn(true);
        lenient().when(loan.calculateFine(1L)).thenReturn(10L);
        List<Loan> loans = new ArrayList<>();
        loans.add(loan);
        setLoansField(loans);

        long fine = library.calculateFine(isbn, memberId);

        assertEquals(10L, fine, "Штраф должен быть рассчитан для просроченного займа");
        verify(loan).calculateFine(1L);
    }

    @Test
    void testCalculateFine_NotOverdue() throws Exception {
        // Подготовка: Установка займа в список loans с помощью рефлексии
        lenient().when(loan.getBook()).thenReturn(book);
        lenient().when(loan.getMember()).thenReturn(member);
        lenient().when(loan.getReturnDate()).thenReturn(null);
        lenient().when(book.getIsbn()).thenReturn(isbn);
        lenient().when(member.getMemberId()).thenReturn(memberId);
        lenient().when(loan.isOverdue()).thenReturn(false);
        lenient().when(loan.calculateFine(1L)).thenReturn(0L);
        List<Loan> loans = new ArrayList<>();
        loans.add(loan);
        setLoansField(loans);

        long fine = library.calculateFine(isbn, memberId);

        assertEquals(0L, fine, "Штраф должен быть 0 для непросроченного займа");
        verify(loan).calculateFine(1L);
    }

    @Test
    void testCalculateFine_NoMatchingLoan() {
        // Подготовка: Нет займов в списке
        long fine = library.calculateFine(isbn, memberId);

        assertEquals(0L, fine, "Штраф должен быть 0, если соответствующий займ не найден");
        verify(loan, never()).calculateFine(anyLong());
    }
}

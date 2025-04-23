// src/main/java/main/library/Loan.java
package main.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private final Book book;
    private final Member member;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    /** Конструктор с явным сроком займа (в днях) */
    public Loan(Book book, Member member, int loanDays) {
        this.book = book;
        this.member = member;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(loanDays);
        this.returnDate = null;
        book.setAvailable(false);
    }

    /** Перегрузка: срок займа по умолчанию — 14 дней */
    public Loan(Book book, Member member) {
        this(book, member, 14);
    }

    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void returnBook() {
        if (returnDate == null) {
            this.returnDate = LocalDate.now();
            book.setAvailable(true);
        }
    }

    public boolean isOverdue() {
        LocalDate check = (returnDate != null ? returnDate : LocalDate.now());
        return check.isAfter(dueDate);
    }

    public long calculateFine(long perDay) {
        if (!isOverdue()) return 0;
        LocalDate end = (returnDate != null ? returnDate : LocalDate.now());
        long daysLate = ChronoUnit.DAYS.between(dueDate, end);
        return daysLate * perDay;
    }

    @Override
    public String toString() {
        String base = String.format(
                "%s loaned to %s on %s, due %s",
                book.getTitle(), member.getName(), loanDate, dueDate
        );
        if (returnDate != null) {
            base += String.format(" (returned on %s)", returnDate);
            if (isOverdue()) {
                base += String.format(", fine=%d", calculateFine(1));
            }
        } else if (isOverdue()) {
            base += String.format(" (OVERDUE, fine so far=%d)", calculateFine(1));
        }
        return base;
    }
}

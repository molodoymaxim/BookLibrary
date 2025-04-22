package main.library;

import java.time.LocalDate;

public class Loan {
    private final Book book;
    private final Member member;
    private final LocalDate loanDate;
    private LocalDate returnDate;

    public Loan(Book book, Member member) {
        this.book = book;
        this.member = member;
        this.loanDate = LocalDate.now();
        this.returnDate = null;
        book.setAvailable(false);
    }

    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void returnBook() {
        if (returnDate == null) {
            this.returnDate = LocalDate.now();
            book.setAvailable(true);
        }
    }

    @Override
    public String toString() {
        return String.format("%s loaned to %s on %s%s",
                book.getTitle(),
                member.getName(),
                loanDate,
                (returnDate != null ? " (returned on " + returnDate + ")" : ""));
    }
}

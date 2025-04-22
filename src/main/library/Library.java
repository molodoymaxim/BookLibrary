package main.library;

import java.util.*;

public class Library {
    private final Catalog catalog = new Catalog();
    private final Map<String, Member> members = new HashMap<>();
    private final List<Loan> loans = new ArrayList<>();

    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
    }

    public void addBook(Book book) {
        catalog.addBook(book);
    }

    public boolean borrowBook(String isbn, String memberId) {
        Optional<Book> optBook = catalog.findByIsbn(isbn);
        Member member = members.get(memberId);
        if (optBook.isPresent() && member != null && optBook.get().isAvailable()) {
            loans.add(new Loan(optBook.get(), member));
            return true;
        }
        return false;
    }

    public boolean returnBook(String isbn, String memberId) {
        for (Loan loan : loans) {
            if (loan.getBook().getIsbn().equals(isbn)
                    && loan.getMember().getMemberId().equals(memberId)
                    && loan.getReturnDate() == null) {
                loan.returnBook();
                return true;
            }
        }
        return false;
    }

    public List<Loan> getActiveLoans() {
        List<Loan> active = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getReturnDate() == null) active.add(loan);
        }
        return active;
    }
}

package library;

import java.util.*;

public class Library {
    private final Catalog catalog;
    private final Map<String, Member> members = new HashMap<>();
    private final List<Loan> loans = new ArrayList<>();
    private final int defaultLoanDays = 14;
    private final long finePerDay = 1; // currency units

    /**
     * Конструктор по умолчанию — создаёт новую библиотеку с новым каталогом.
     */
    public Library() {
        this(new Catalog());
    }

    /**
     * Конструктор для тестирования — позволяет подставить мок-каталог.
     *
     * @param catalog каталог книг
     */
    public Library(Catalog catalog) {
        this.catalog = catalog;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
    }

    public boolean removeMember(String memberId) {
        return members.remove(memberId) != null;
    }

    public void addBook(Book book) {
        catalog.addBook(book);
    }

    public boolean borrowBook(String isbn, String memberId) {
        Optional<Book> opt = catalog.findByIsbn(isbn);
        Member m = members.get(memberId);
        if (opt.isPresent() && m != null && opt.get().isAvailable()) {
            loans.add(new Loan(opt.get(), m, defaultLoanDays));
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

    public List<Book> listAllBooks() {
        return catalog.listAll();
    }

    public List<Book> listAvailableBooks() {
        return catalog.listAvailable();
    }

    public List<Member> listMembers() {
        return new ArrayList<>(members.values());
    }

    public List<Loan> getActiveLoans() {
        List<Loan> res = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getReturnDate() == null) {
                res.add(l);
            }
        }
        return res;
    }

    public void setLoans(List<Loan> loans) {
        this.loans.clear();
        this.loans.addAll(loans);
    }

    public List<Loan> listOverdueLoans() {
        List<Loan> overdue = new ArrayList<>();
        for (Loan l : loans) {
            if (l.isOverdue()) {
                overdue.add(l);
            }
        }
        return overdue;
    }

    public long calculateFine(String isbn, String memberId) {
        for (Loan l : loans) {
            if (l.getBook().getIsbn().equals(isbn)
                    && l.getMember().getMemberId().equals(memberId)) {
                return l.calculateFine(finePerDay);
            }
        }
        return 0;
    }
}

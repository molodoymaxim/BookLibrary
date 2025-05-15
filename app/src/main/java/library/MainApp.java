package library;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Library Management System
 *
 * Проект демонстрирует простую библиотечную систему:
 *  - хранение и поиск книг (Book, Catalog)
 *  - регистрация пользователей (Member)
 *  - выдача/возврат книг (Loan, Library)
 *
 */

/**
 * Улучшения
 * Book: добавлены equals/hashCode.
 * Loan: теперь хранит срок возврата (dueDate), умеет проверять просрочку и вычислять штраф.
 * Catalog: методы для поиска по части названия и автора, а также полный список книг.
 * Library: новые методы: список всех книг, доступных книг, участников, просроченных займов, расчёт штрафа, удаление участника.
 */
public class MainApp {
    private static final Logger logger = LogManager.getLogger(MainApp.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Application is starting...");

        Library lib = new Library();

        logger.info("Registering library members...");
        lib.registerMember(new Member("M1", "Alice"));
        lib.registerMember(new Member("M2", "Bob"));

        logger.info("Adding books to the catalog...");
        lib.addBook(new Book("ISBN1", "Clean Code", "Robert Martin"));
        lib.addBook(new Book("ISBN2", "Effective Java", "Joshua Bloch"));
        lib.addBook(new Book("ISBN3", "Java Concurrency", "Brian Goetz"));

        logger.info("Demonstrating book search by title 'Java':");
        lib.listAllBooks().stream()
                .filter(b -> b.getTitle().contains("Java"))
                .forEach(System.out::println);

        logger.info("Borrowing book ISBN2 to member M1...");
        lib.borrowBook("ISBN2", "M1");

        // Artificial delay to get different dates
        Thread.sleep(2000);

        logger.info("Returning book ISBN2 from member M1:");
        if (lib.returnBook("ISBN2", "M1")) {
            logger.info("Book returned. Fine: {}", lib.calculateFine("ISBN2", "M1"));
        }

        logger.info("Listing overdue loans:");
        for (Loan l : lib.listOverdueLoans()) {
            logger.info("Overdue loan: {}", l);
        }

        logger.info("Listing all members:");
        lib.listMembers().forEach(System.out::println);

        logger.info("Listing all books:");
        lib.listAllBooks().forEach(System.out::println);

        logger.info("Application has finished.");
    }
}
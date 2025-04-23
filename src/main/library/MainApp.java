package main.library;

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
    public static void main(String[] args) throws InterruptedException {
        Library lib = new Library();

        // регистрация и заполнение
        lib.registerMember(new Member("M1", "Alice"));
        lib.registerMember(new Member("M2", "Bob"));

        lib.addBook(new Book("ISBN1", "Clean Code", "Robert Martin"));
        lib.addBook(new Book("ISBN2", "Effective Java", "Joshua Bloch"));
        lib.addBook(new Book("ISBN3", "Java Concurrency", "Brian Goetz"));

        // демонстрация поиска
        System.out.println("Search by title 'Java':");
        lib.listAllBooks().stream()
                .filter(b -> b.getTitle().contains("Java"))
                .forEach(System.out::println);

        // выдача и задержка (искусственно ждем 2 секунды чтобы не одинаковые даты)
        lib.borrowBook("ISBN2", "M1");
        Thread.sleep(2000);

        // возврат с просрочкой
        if (lib.returnBook("ISBN2", "M1")) {
            System.out.println("Returned with fine: " + lib.calculateFine("ISBN2", "M1"));
        }

        // список просроченных займов
        System.out.println("Overdue loans:");
        for (Loan l : lib.listOverdueLoans()) System.out.println(l);

        // список всех участников и книг
        System.out.println("Members:"); lib.listMembers().forEach(System.out::println);
        System.out.println("All Books:"); lib.listAllBooks().forEach(System.out::println);
    }
}
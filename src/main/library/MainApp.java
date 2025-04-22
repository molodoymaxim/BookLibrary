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

public class MainApp {
    public static void main(String[] args) {
        Library lib = new Library();

        // Добавляем книги
        lib.addBook(new Book("978-1", "1984", "George Orwell"));
        lib.addBook(new Book("978-2", "Brave New World", "Aldous Huxley"));

        // Регистрируем участников
        lib.registerMember(new Member("M001", "Иван Иванов"));
        lib.registerMember(new Member("M002", "Мария Петрова"));

        // Берём и возвращаем книги
        boolean ok1 = lib.borrowBook("978-1", "M001");
        System.out.println("Успешно выдана книга: " + ok1);

        boolean ok2 = lib.returnBook("978-1", "M001");
        System.out.println("Книга возвращена: " + ok2);

        // Список активных займов
        System.out.println("Активные займы:");
        for (Loan loan : lib.getActiveLoans()) {
            System.out.println(" - " + loan);
        }
    }
}
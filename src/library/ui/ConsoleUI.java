package library.ui;

import library.algorithms.SortingAlgorithms;
import library.model.Book;
import library.model.LibraryManager;

import java.util.List;
import java.util.Scanner;

/**
 * Console User Interface for the Library Management System.
 * Provides a menu-driven interaction for all system features.
 */
public class ConsoleUI {

    private final LibraryManager library = new LibraryManager();
    private final Scanner        scanner = new Scanner(System.in);

    // ── ANSI colours ──────────────────────────────────────────────────────
    private static final String RESET  = "\033[0m";
    private static final String CYAN   = "\033[1;36m";
    private static final String GREEN  = "\033[1;32m";
    private static final String YELLOW = "\033[1;33m";
    private static final String RED    = "\033[1;31m";
    private static final String BLUE   = "\033[1;34m";

    // ══════════════════════════════════════════════════════════════════════
    // ENTRY POINT
    // ══════════════════════════════════════════════════════════════════════

    public void run() {
        loadSampleData();
        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1  -> menuBookManagement();
                case 2  -> menuSearch();
                case 3  -> menuSort();
                case 4  -> menuBorrowBook();
                case 5  -> menuViewHistory();
                case 6  -> menuViewActivities();
                case 7  -> menuViewAllBooks();
                case 0  -> { running = false; printGoodbye(); }
                default -> print(RED + "Invalid option. Please try again." + RESET);
            }
        }
        scanner.close();
    }

    // ══════════════════════════════════════════════════════════════════════
    // MAIN MENU
    // ══════════════════════════════════════════════════════════════════════

    private void printMainMenu() {
        System.out.println();
        System.out.println(CYAN + "╔══════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║       LIBRARY MANAGEMENT SYSTEM              ║" + RESET);
        System.out.println(CYAN + "╠══════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + "║" + RESET + "  1. Book Management  (add/remove/update)      " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "  2. Search Books     (linear / binary)        " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "  3. Sort Books       (bubble/selection/quick) " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "  4. Borrow a Book                             " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "  5. View Borrowing History                    " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "  6. View Recent Activities (Stack)            " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "  7. View All Books                            " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "  0. Exit                                      " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════╝" + RESET);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 1. BOOK MANAGEMENT
    // ══════════════════════════════════════════════════════════════════════

    private void menuBookManagement() {
        System.out.println(YELLOW + "\n── Book Management ──────────────────────────────" + RESET);
        System.out.println("  1. Add a new book");
        System.out.println("  2. Remove a book");
        System.out.println("  3. Update a book");
        System.out.println("  0. Back");

        int choice = readInt("Choice: ");
        switch (choice) {
            case 1 -> addBook();
            case 2 -> removeBook();
            case 3 -> updateBook();
            case 0 -> { /* back */ }
        }
    }

    private void addBook() {
        System.out.println(GREEN + "\nAdd New Book" + RESET);
        String title  = readString("Title       : ");
        String author = readString("Author      : ");
        String isbn   = readString("ISBN        : ");
        int    year   = readInt   ("Pub. Year   : ");
        String genre  = readString("Genre       : ");

        Book book = new Book(title, author, isbn, year, genre);
        if (library.addBook(book)) {
            print(GREEN + "✔  Book added successfully." + RESET);
        } else {
            print(RED + "✘  A book with this ISBN already exists." + RESET);
        }
    }

    private void removeBook() {
        String isbn = readString("Enter ISBN of the book to remove: ");
        if (library.removeBook(isbn)) {
            print(GREEN + "✔  Book removed." + RESET);
        } else {
            print(RED + "✘  No book found with that ISBN." + RESET);
        }
    }

    private void updateBook() {
        String isbn = readString("Enter ISBN of the book to update: ");
        System.out.println("(Leave blank to keep the current value)");
        String title  = readString("New Title      : ");
        String author = readString("New Author     : ");
        String yearS  = readString("New Year (0 to skip): ");
        String genre  = readString("New Genre      : ");

        int year = 0;
        try { year = Integer.parseInt(yearS.trim()); } catch (NumberFormatException ignored) {}

        if (library.updateBook(isbn, title, author, year, genre)) {
            print(GREEN + "✔  Book updated." + RESET);
        } else {
            print(RED + "✘  No book found with that ISBN." + RESET);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 2. SEARCH
    // ══════════════════════════════════════════════════════════════════════

    private void menuSearch() {
        System.out.println(YELLOW + "\n── Search Books ─────────────────────────────────" + RESET);
        System.out.println("  Algorithm:   1. Linear Search    2. Binary Search");
        System.out.println("  Field:       A. Title   B. Author   C. ISBN   D. Keyword (linear only)");

        int    algo  = readInt   ("Algorithm (1/2)     : ");
        String field = readString("Field (A/B/C/D)     : ").trim().toUpperCase();
        String query = readString("Search query        : ");

        long start = System.nanoTime();

        if (algo == 1) {
            // ── Linear Search ─────────────────────────────────────────────
            switch (field) {
                case "A" -> printBooks(library.linearSearchByTitle(query),  "Linear by Title");
                case "B" -> printBooks(library.linearSearchByAuthor(query), "Linear by Author");
                case "C" -> {
                    Book b = library.linearSearchByISBN(query);
                    printBooks(b == null ? List.of() : List.of(b), "Linear by ISBN");
                }
                case "D" -> printBooks(library.linearSearchByKeyword(query), "Linear by Keyword");
                default  -> print(RED + "Unknown field." + RESET);
            }
        } else {
            // ── Binary Search ─────────────────────────────────────────────
            print(YELLOW + "Note: Binary search requires sorting first. Sorting automatically..." + RESET);
            Book result = switch (field) {
                case "A" -> library.binarySearchByTitle(query);
                case "B" -> library.binarySearchByAuthor(query);
                case "C" -> library.binarySearchByISBN(query);
                default  -> null;
            };
            if (result != null) {
                print("\n" + result);
            } else {
                print(RED + "  No match found." + RESET);
            }
        }

        long elapsed = (System.nanoTime() - start) / 1_000_000;
        print(BLUE + "Search completed in " + elapsed + " ms." + RESET);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 3. SORT
    // ══════════════════════════════════════════════════════════════════════

    private void menuSort() {
        System.out.println(YELLOW + "\n── Sort Books ───────────────────────────────────" + RESET);
        System.out.println("  Algorithm:  1. Bubble Sort   2. Selection Sort   3. Quick Sort");
        System.out.println("  Criterion:  1. Title         2. Author            3. Year");

        int algo = readInt("Algorithm (1/2/3): ");
        int crit = readInt("Criterion (1/2/3): ");

        if (algo < 1 || algo > 3 || crit < 1 || crit > 3) {
            print(RED + "Invalid input." + RESET); return;
        }

        long start = System.nanoTime();
        library.sort(algo, crit);
        long elapsed = (System.nanoTime() - start) / 1_000_000;

        print(GREEN + "✔  Sorted successfully in " + elapsed + " ms." + RESET);
        menuViewAllBooks();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 4. BORROW
    // ══════════════════════════════════════════════════════════════════════

    private void menuBorrowBook() {
        System.out.println(YELLOW + "\n── Borrow a Book ────────────────────────────────" + RESET);
        String isbn     = readString("ISBN of the book    : ");
        String borrower = readString("Your name           : ");

        if (library.borrowBook(isbn, borrower)) {
            print(GREEN + "✔  Book borrowed successfully by " + borrower + "." + RESET);
        } else {
            print(RED + "✘  No book found with ISBN " + isbn + "." + RESET);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 5. BORROWING HISTORY
    // ══════════════════════════════════════════════════════════════════════

    private void menuViewHistory() {
        String isbn = readString("Enter ISBN: ");
        List<String> history = library.getBorrowingHistory(isbn);

        if (history.isEmpty()) {
            print(YELLOW + "  No borrowing history for this ISBN." + RESET);
        } else {
            System.out.println(CYAN + "\n── Borrowing History (" + isbn + ") ─────────────────" + RESET);
            int i = 1;
            for (String entry : history) {
                System.out.printf("  %2d. %s%n", i++, entry);
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 6. RECENT ACTIVITIES (Stack)
    // ══════════════════════════════════════════════════════════════════════

    private void menuViewActivities() {
        List<String> activities = library.getRecentActivities();
        System.out.println(CYAN + "\n── Recent Activities (newest first — Stack view) ──" + RESET);
        if (activities.isEmpty()) {
            print("  No activities recorded yet.");
        } else {
            for (String a : activities) {
                System.out.println("  • " + a);
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 7. VIEW ALL BOOKS
    // ══════════════════════════════════════════════════════════════════════

    private void menuViewAllBooks() {
        Book[] all = library.getActiveBooks();
        System.out.println(CYAN +
            "\n── All Books (" + library.getCount() + " total) ────────────────────────────────" + RESET);
        if (all.length == 0) {
            print(YELLOW + "  The collection is empty." + RESET);
            return;
        }
        System.out.printf("  %-4s %-35s %-20s %-15s %-4s %s%n",
            "#", "Title", "Author", "ISBN", "Year", "Genre");
        System.out.println("  " + "─".repeat(95));
        for (int i = 0; i < all.length; i++) {
            Book b = all[i];
            System.out.printf("  %-4d %s%n", i + 1, b.toShortString());
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SAMPLE DATA
    // ══════════════════════════════════════════════════════════════════════

    private void loadSampleData() {
        Book[] samples = {
            new Book("The Great Gatsby",          "F. Scott Fitzgerald", "978-0-7432-7356-5", 1925, "Fiction"),
            new Book("To Kill a Mockingbird",     "Harper Lee",          "978-0-06-112008-4", 1960, "Fiction"),
            new Book("1984",                      "George Orwell",       "978-0-45-228285-3", 1949, "Dystopia"),
            new Book("Brave New World",           "Aldous Huxley",       "978-0-06-085052-4", 1932, "Dystopia"),
            new Book("The Catcher in the Rye",   "J.D. Salinger",       "978-0-31-676948-0", 1951, "Fiction"),
            new Book("Pride and Prejudice",       "Jane Austen",         "978-0-14-143951-8", 1813, "Romance"),
            new Book("The Hobbit",                "J.R.R. Tolkien",      "978-0-54-792822-7", 1937, "Fantasy"),
            new Book("Sapiens",                   "Yuval Noah Harari",   "978-0-06-231609-7", 2011, "Non-fiction"),
            new Book("Clean Code",                "Robert C. Martin",    "978-0-13-235088-4", 2008, "Technology"),
            new Book("Introduction to Algorithms","Thomas H. Cormen",    "978-0-26-204630-5", 2009, "Computer Science"),
        };
        for (Book b : samples) library.addBook(b);
        // Pre-load some borrowing history for demo
        library.borrowBook("978-0-7432-7356-5", "Alice Mballa");
        library.borrowBook("978-0-7432-7356-5", "Bruno Foka");
        library.borrowBook("978-0-06-112008-4", "Claire Ngo");
    }

    // ══════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════

    private void printBooks(List<Book> books, String label) {
        System.out.println(CYAN + "\n── Results [" + label + "] — " + books.size() + " found ──" + RESET);
        if (books.isEmpty()) {
            print(YELLOW + "  No books found." + RESET);
        } else {
            for (Book b : books) System.out.println(b);
        }
    }

    private void printBanner() {
        System.out.println(CYAN);
        System.out.println("  ██╗     ██╗██████╗ ██████╗  █████╗ ██████╗ ██╗   ██╗");
        System.out.println("  ██║     ██║██╔══██╗██╔══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝");
        System.out.println("  ██║     ██║██████╔╝██████╔╝███████║██████╔╝ ╚████╔╝ ");
        System.out.println("  ██║     ██║██╔══██╗██╔══██╗██╔══██║██╔══██╗  ╚██╔╝  ");
        System.out.println("  ███████╗██║██████╔╝██║  ██║██║  ██║██║  ██║   ██║   ");
        System.out.println("  ╚══════╝╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ");
        System.out.println(RESET);
        System.out.println("  Management System  —  COMC-06: Data & Program Structure");
        System.out.println("  Sample dataset loaded: 10 books  |  3 borrowing records\n");
    }

    private void printGoodbye() {
        System.out.println(GREEN + "\n  Thank you for using the Library System. Goodbye!\n" + RESET);
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void print(String msg) { System.out.println(msg); }
}

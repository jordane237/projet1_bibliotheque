package library.model;

import library.algorithms.SearchAlgorithms;
import library.algorithms.SortingAlgorithms;
import library.structures.ActivityStack;
import library.structures.BorrowingHistory;

import java.time.LocalDate;
import java.util.*;

/**
 * Core library management class.
 *
 * Uses:
 *   - Book[]          : fixed-capacity array for the collection (resized on demand)
 *   - BorrowingHistory: linked list per book (stored in a Map keyed by ISBN)
 *   - ActivityStack   : LIFO stack for recent activities
 */
public class LibraryManager {

    // ── Internal state ────────────────────────────────────────────────────
    private Book[]                         books;
    private int                            count;
    private static final int               INITIAL_CAPACITY = 50;

    private final Map<String, BorrowingHistory> histories = new HashMap<>();
    private final ActivityStack                 activityLog;

    // ── Constructor ───────────────────────────────────────────────────────
    public LibraryManager() {
        books       = new Book[INITIAL_CAPACITY];
        count       = 0;
        activityLog = new ActivityStack(20);
    }

    // ══════════════════════════════════════════════════════════════════════
    // BOOK MANAGEMENT
    // ══════════════════════════════════════════════════════════════════════

    /** Add a new book to the collection. */
    public boolean addBook(Book book) {
        if (findIndexByISBN(book.getIsbn()) >= 0) {
            return false;   // duplicate ISBN
        }
        if (count == books.length) {
            books = Arrays.copyOf(books, books.length * 2);  // dynamic resize
        }
        books[count++] = book;
        histories.put(book.getIsbn(), new BorrowingHistory());
        log("ADDED book: \"" + book.getTitle() + "\"");
        return true;
    }

    /** Remove a book by ISBN. */
    public boolean removeBook(String isbn) {
        int idx = findIndexByISBN(isbn);
        if (idx < 0) return false;

        String title = books[idx].getTitle();
        // Shift elements left
        System.arraycopy(books, idx + 1, books, idx, count - idx - 1);
        books[--count] = null;
        histories.remove(isbn);
        log("REMOVED book: \"" + title + "\"");
        return true;
    }

    /** Update a book's details by ISBN. */
    public boolean updateBook(String isbn, String newTitle, String newAuthor,
                              int newYear, String newGenre) {
        int idx = findIndexByISBN(isbn);
        if (idx < 0) return false;

        Book b = books[idx];
        if (newTitle  != null && !newTitle.isBlank())  b.setTitle(newTitle);
        if (newAuthor != null && !newAuthor.isBlank()) b.setAuthor(newAuthor);
        if (newYear   > 0)                             b.setPublicationYear(newYear);
        if (newGenre  != null && !newGenre.isBlank())  b.setGenre(newGenre);

        log("UPDATED book: \"" + b.getTitle() + "\"");
        return true;
    }

    // ══════════════════════════════════════════════════════════════════════
    // SEARCH
    // ══════════════════════════════════════════════════════════════════════

    public List<Book> linearSearchByTitle(String title) {
        log("SEARCH (linear) by title: \"" + title + "\"");
        return SearchAlgorithms.linearSearchByTitle(getActiveBooks(), title);
    }

    public List<Book> linearSearchByAuthor(String author) {
        log("SEARCH (linear) by author: \"" + author + "\"");
        return SearchAlgorithms.linearSearchByAuthor(getActiveBooks(), author);
    }

    public Book linearSearchByISBN(String isbn) {
        log("SEARCH (linear) by ISBN: " + isbn);
        return SearchAlgorithms.linearSearchByISBN(getActiveBooks(), isbn);
    }

    public List<Book> linearSearchByKeyword(String keyword) {
        log("SEARCH (linear) keyword: \"" + keyword + "\"");
        return SearchAlgorithms.linearSearchByKeyword(getActiveBooks(), keyword);
    }

    public Book binarySearchByTitle(String title) {
        log("SEARCH (binary) by title: \"" + title + "\"");
        Book[] sorted = getActiveBooks();
        SortingAlgorithms.quickSort(sorted, SortingAlgorithms.BY_TITLE);
        return SearchAlgorithms.binarySearchByTitle(sorted, title);
    }

    public Book binarySearchByAuthor(String author) {
        log("SEARCH (binary) by author: \"" + author + "\"");
        Book[] sorted = getActiveBooks();
        SortingAlgorithms.quickSort(sorted, SortingAlgorithms.BY_AUTHOR);
        return SearchAlgorithms.binarySearchByAuthor(sorted, author);
    }

    public Book binarySearchByISBN(String isbn) {
        log("SEARCH (binary) by ISBN: " + isbn);
        Book[] sorted = getActiveBooks();
        SortingAlgorithms.quickSort(sorted, SortingAlgorithms.BY_TITLE);
        // sort by ISBN for binary search
        Arrays.sort(sorted, (a, b) ->
            a == null ? 1 : b == null ? -1 :
            a.getIsbn().compareToIgnoreCase(b.getIsbn())
        );
        return SearchAlgorithms.binarySearchByISBN(sorted, isbn);
    }

    // ══════════════════════════════════════════════════════════════════════
    // SORT  (sorts the internal array in-place)
    // ══════════════════════════════════════════════════════════════════════

    public void sort(int algorithm, int criterion) {
        String algo  = algorithm == 1 ? "Bubble" : algorithm == 2 ? "Selection" : "Quick";
        String crit  = criterion == SortingAlgorithms.BY_TITLE  ? "title"
                     : criterion == SortingAlgorithms.BY_AUTHOR ? "author" : "year";

        Book[] active = Arrays.copyOf(books, count);

        switch (algorithm) {
            case 1 -> SortingAlgorithms.bubbleSort(active, criterion);
            case 2 -> SortingAlgorithms.selectionSort(active, criterion);
            case 3 -> SortingAlgorithms.quickSort(active, criterion);
        }
        System.arraycopy(active, 0, books, 0, count);
        log("SORTED collection by " + crit + " using " + algo + " Sort");
    }

    // ══════════════════════════════════════════════════════════════════════
    // BORROWING HISTORY
    // ══════════════════════════════════════════════════════════════════════

    public boolean borrowBook(String isbn, String borrowerName) {
        if (findIndexByISBN(isbn) < 0) return false;
        String date = LocalDate.now().toString();
        histories.get(isbn).addBorrower(borrowerName, date);
        log("BORROWED: ISBN " + isbn + " by " + borrowerName);
        return true;
    }

    public List<String> getBorrowingHistory(String isbn) {
        BorrowingHistory bh = histories.get(isbn);
        if (bh == null) return Collections.emptyList();
        return bh.getBorrowers();
    }

    // ══════════════════════════════════════════════════════════════════════
    // ACTIVITY LOG (stack)
    // ══════════════════════════════════════════════════════════════════════

    public List<String> getRecentActivities() { return activityLog.getAll(); }
    public String       peekLastActivity()    { return activityLog.peek(); }

    private void log(String activity) {
        activityLog.push("[" + LocalDate.now() + "] " + activity);
    }

    // ══════════════════════════════════════════════════════════════════════
    // UTILITIES
    // ══════════════════════════════════════════════════════════════════════

    /** Returns a compact, non-null slice of the internal array. */
    public Book[] getActiveBooks() {
        return Arrays.copyOf(books, count);
    }

    public int getCount() { return count; }

    private int findIndexByISBN(String isbn) {
        for (int i = 0; i < count; i++) {
            if (books[i] != null && books[i].getIsbn().equalsIgnoreCase(isbn)) {
                return i;
            }
        }
        return -1;
    }
}

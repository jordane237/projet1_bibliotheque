package library.algorithms;

import library.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * Search algorithms: Linear Search and Binary Search.
 *
 * Linear Search – O(n) – works on unsorted arrays.
 * Binary Search – O(log n) – requires a sorted array.
 */
public class SearchAlgorithms {

    // ══════════════════════════════════════════════════════════════════════
    // LINEAR SEARCH
    // ══════════════════════════════════════════════════════════════════════

    /** Linear search by exact title (case-insensitive). Returns all matches. */
    public static List<Book> linearSearchByTitle(Book[] books, String title) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book != null && book.getTitle().equalsIgnoreCase(title)) {
                results.add(book);
            }
        }
        return results;
    }

    /** Linear search by author. Returns all books by that author. */
    public static List<Book> linearSearchByAuthor(Book[] books, String author) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book != null && book.getAuthor().equalsIgnoreCase(author)) {
                results.add(book);
            }
        }
        return results;
    }

    /** Linear search by ISBN (exact match). */
    public static Book linearSearchByISBN(Book[] books, String isbn) {
        for (Book book : books) {
            if (book != null && book.getIsbn().equalsIgnoreCase(isbn)) {
                return book;
            }
        }
        return null;
    }

    /** Linear search – partial keyword match on title or author. */
    public static List<Book> linearSearchByKeyword(Book[] books, String keyword) {
        List<Book> results = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Book book : books) {
            if (book != null
                    && (book.getTitle().toLowerCase().contains(kw)
                    ||  book.getAuthor().toLowerCase().contains(kw))) {
                results.add(book);
            }
        }
        return results;
    }

    // ══════════════════════════════════════════════════════════════════════
    // BINARY SEARCH  (array must be sorted by the chosen field first)
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Binary search by title.
     * Pre-condition: books[] is sorted by title (ascending).
     * Returns the first matching Book, or null if not found.
     */
    public static Book binarySearchByTitle(Book[] books, String title) {
        int low  = 0;
        int high = books.length - 1;

        while (low <= high) {
            int  mid  = low + (high - low) / 2;
            Book mid_book = books[mid];
            if (mid_book == null) { high = mid - 1; continue; }

            int cmp = mid_book.getTitle().compareToIgnoreCase(title);

            if (cmp == 0)       return mid_book;
            else if (cmp < 0)   low  = mid + 1;
            else                high = mid - 1;
        }
        return null;
    }

    /**
     * Binary search by author.
     * Pre-condition: books[] is sorted by author (ascending).
     */
    public static Book binarySearchByAuthor(Book[] books, String author) {
        int low = 0, high = books.length - 1;

        while (low <= high) {
            int  mid  = low + (high - low) / 2;
            Book mid_book = books[mid];
            if (mid_book == null) { high = mid - 1; continue; }

            int cmp = mid_book.getAuthor().compareToIgnoreCase(author);

            if (cmp == 0)       return mid_book;
            else if (cmp < 0)   low  = mid + 1;
            else                high = mid - 1;
        }
        return null;
    }

    /**
     * Binary search by ISBN.
     * Pre-condition: books[] is sorted by ISBN (ascending).
     */
    public static Book binarySearchByISBN(Book[] books, String isbn) {
        int low = 0, high = books.length - 1;

        while (low <= high) {
            int  mid  = low + (high - low) / 2;
            Book mid_book = books[mid];
            if (mid_book == null) { high = mid - 1; continue; }

            int cmp = mid_book.getIsbn().compareToIgnoreCase(isbn);

            if (cmp == 0)       return mid_book;
            else if (cmp < 0)   low  = mid + 1;
            else                high = mid - 1;
        }
        return null;
    }
}

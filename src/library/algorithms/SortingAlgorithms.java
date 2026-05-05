package library.algorithms;

import library.model.Book;

/**
 * Three sorting algorithms applied to a Book array.
 *
 * Sorting criteria: TITLE | AUTHOR | YEAR
 *
 * Bubble Sort    – O(n²)  – simple, good for nearly-sorted data.
 * Selection Sort – O(n²)  – minimal swaps.
 * Quick Sort     – O(n log n) average – fastest for large datasets.
 */
public class SortingAlgorithms {

    // ── Criterion constants ───────────────────────────────────────────────
    public static final int BY_TITLE  = 1;
    public static final int BY_AUTHOR = 2;
    public static final int BY_YEAR   = 3;

    // ── Helper: compare two books by criterion ────────────────────────────
    private static int compare(Book a, Book b, int criterion) {
        if (a == null && b == null) return 0;
        if (a == null) return 1;
        if (b == null) return -1;
        return switch (criterion) {
            case BY_TITLE  -> a.getTitle().compareToIgnoreCase(b.getTitle());
            case BY_AUTHOR -> a.getAuthor().compareToIgnoreCase(b.getAuthor());
            case BY_YEAR   -> Integer.compare(a.getPublicationYear(), b.getPublicationYear());
            default        -> 0;
        };
    }

    // ── Helper: swap two elements ─────────────────────────────────────────
    private static void swap(Book[] books, int i, int j) {
        Book temp  = books[i];
        books[i]   = books[j];
        books[j]   = temp;
    }

    // ══════════════════════════════════════════════════════════════════════
    // BUBBLE SORT  –  O(n²)
    // Repeatedly compares adjacent elements and swaps if out of order.
    // ══════════════════════════════════════════════════════════════════════
    public static void bubbleSort(Book[] books, int criterion) {
        int n = books.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (compare(books[j], books[j + 1], criterion) > 0) {
                    swap(books, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;   // already sorted — early exit optimisation
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // SELECTION SORT  –  O(n²)
    // Finds the minimum in the unsorted portion and places it at the front.
    // ══════════════════════════════════════════════════════════════════════
    public static void selectionSort(Book[] books, int criterion) {
        int n = books.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (compare(books[j], books[minIdx], criterion) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                swap(books, i, minIdx);
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // QUICK SORT  –  O(n log n) average
    // Divide-and-conquer using a pivot element.
    // ══════════════════════════════════════════════════════════════════════
    public static void quickSort(Book[] books, int criterion) {
        quickSortHelper(books, 0, books.length - 1, criterion);
    }

    private static void quickSortHelper(Book[] books, int low, int high, int criterion) {
        if (low < high) {
            int pivotIndex = partition(books, low, high, criterion);
            quickSortHelper(books, low,          pivotIndex - 1, criterion);
            quickSortHelper(books, pivotIndex + 1, high,         criterion);
        }
    }

    /**
     * Lomuto partition scheme.
     * Selects the last element as pivot and rearranges elements around it.
     */
    private static int partition(Book[] books, int low, int high, int criterion) {
        Book pivot = books[high];
        int  i     = low - 1;

        for (int j = low; j < high; j++) {
            if (compare(books[j], pivot, criterion) <= 0) {
                i++;
                swap(books, i, j);
            }
        }
        swap(books, i + 1, high);
        return i + 1;
    }
}

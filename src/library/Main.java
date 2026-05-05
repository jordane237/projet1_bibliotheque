package library;

import library.ui.ConsoleUI;

/**
 * Entry point for the Library Management System.
 *
 * COMC-06: Data & Program Structure
 *
 * Data Structures used:
 *   - Array          : book collection (dynamic resize)
 *   - Linked List    : borrowing history per book
 *   - Stack          : recent activity log (LIFO)
 *
 * Algorithms implemented:
 *   Search  : Linear Search, Binary Search
 *   Sorting : Bubble Sort, Selection Sort, Quick Sort
 */
public class Main {
    public static void main(String[] args) {
        new ConsoleUI().run();
    }
}

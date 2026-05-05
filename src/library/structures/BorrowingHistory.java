package library.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Singly-linked list that stores the borrowing history of a book.
 * Each node holds a borrower's name and a borrow date.
 */
public class BorrowingHistory {

    // ── Inner Node class ─────────────────────────────────────────────────
    private static class Node {
        String borrower;
        String date;
        Node   next;

        Node(String borrower, String date) {
            this.borrower = borrower;
            this.date     = date;
            this.next     = null;
        }
    }

    private Node head;
    private int  size;

    // ── Add a borrower at the tail ────────────────────────────────────────
    public void addBorrower(String borrower, String date) {
        Node newNode = new Node(borrower, date);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // ── Retrieve all borrowers ────────────────────────────────────────────
    public List<String> getBorrowers() {
        List<String> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(String.format("%-20s  [%s]", current.borrower, current.date));
            current = current.next;
        }
        return result;
    }

    // ── Get the most recent borrower (tail) ───────────────────────────────
    public String getLastBorrower() {
        if (head == null) return null;
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        return current.borrower + " on " + current.date;
    }

    public int size() { return size; }

    public boolean isEmpty() { return head == null; }
}

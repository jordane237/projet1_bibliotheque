package library.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Stack implemented with an ArrayList to track recent system activities.
 * Follows LIFO (Last-In, First-Out) semantics.
 *
 * Activities logged: recent searches, borrowings, additions, removals.
 */
public class ActivityStack {

    private final List<String> stack    = new ArrayList<>();
    private final int          maxSize;   // cap to avoid unbounded growth

    public ActivityStack(int maxSize) {
        this.maxSize = maxSize;
    }

    // ── Push ──────────────────────────────────────────────────────────────
    public void push(String activity) {
        if (stack.size() >= maxSize) {
            stack.remove(0);            // discard oldest entry
        }
        stack.add(activity);
    }

    // ── Pop ───────────────────────────────────────────────────────────────
    public String pop() {
        if (!stack.isEmpty()) {
            return stack.remove(stack.size() - 1);
        }
        return null;
    }

    // ── Peek ──────────────────────────────────────────────────────────────
    public String peek() {
        if (!stack.isEmpty()) {
            return stack.get(stack.size() - 1);
        }
        return null;
    }

    // ── View all activities (newest first) ───────────────────────────────
    public List<String> getAll() {
        List<String> result = new ArrayList<>();
        for (int i = stack.size() - 1; i >= 0; i--) {
            result.add(stack.get(i));
        }
        return result;
    }

    public boolean isEmpty() { return stack.isEmpty(); }
    public int     size()    { return stack.size(); }
}

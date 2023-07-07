package priorityqueues;

import java.util.NoSuchElementException;

/**
 * Priority queue where objects have a priority that is provided extrinsically,
 * i.e., priorities are supplied as an argument during insertion and can be changed
 * using the changePriority method. Cannot contain duplicate or null items.
 */
public interface ExtrinsicMinPQ<T> {

    /**
     * Adds an item with the given priority value.
     * @throws IllegalArgumentException if item is null or is already present in the PQ
     */
    void add(T item, double priority);

    /** Returns true if the PQ contains the given item; false otherwise. */
    boolean contains(T item);

    /**
     * Returns the item with the least-valued priority.
     * @throws NoSuchElementException if the PQ is empty
     */
    T peekMin();

    /**
     * Removes and returns the item with the least-valued priority.
     * @throws NoSuchElementException if the PQ is empty
     */
    T removeMin();

    /**
     * Changes the priority of the given item.
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    void changePriority(T item, double priority);

    /** Returns the number of items in the PQ. */
    int size();

    /** Returns true if the PQ is empty; false otherwise. */
    default boolean isEmpty() {
        return size() == 0;
    }
}

package priorityqueues;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

/**
 * Use this PQ implementation if you don't want to use your ArrayHeapMinPQ.
 * This implementation is, in theory, slower than ArrayHeapMinPQ.
 */
public class DoubleMapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private final TreeMap<Double, Set<T>> priorityToItem = new TreeMap<>();
    private final HashMap<T, Double> itemToPriority = new HashMap<>();

    public DoubleMapMinPQ() {}

    private static <K> K getItem(Set<K> s) {
        Iterator<K> i = s.iterator();
        return i.next();
    }

    @Override
    public void add(T item, double priority) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null, but null items are not supported");
        }
        if (this.itemToPriority.containsKey(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        if (!this.priorityToItem.containsKey(priority)) {
            this.priorityToItem.put(priority, new HashSet<>());
        }
        Set<T> itemsWithPriority = this.priorityToItem.get(priority);
        itemsWithPriority.add(item);
        this.itemToPriority.put(item, priority);
    }

    @Override
    public boolean contains(T item) {
        return this.itemToPriority.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (this.itemToPriority.size() == 0) {
            throw new NoSuchElementException("PQ is empty.");
        }
        Set<T> itemsWithLowestPriority = priorityToItem.get(this.priorityToItem.firstKey());
        return getItem(itemsWithLowestPriority);
    }

    @Override
    public T removeMin() {
        if (this.itemToPriority.size() == 0) {
            throw new NoSuchElementException("PQ is empty.");
        }

        double lowestPriority = this.priorityToItem.firstKey();

        Set<T> itemsWithLowestPriority = this.priorityToItem.get(lowestPriority);
        T item = getItem(itemsWithLowestPriority);

        itemsWithLowestPriority.remove(item);
        if (itemsWithLowestPriority.size() == 0) {
            this.priorityToItem.remove(lowestPriority);
        }
        this.itemToPriority.remove(item);
        return item;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new IllegalArgumentException(item + " not in PQ.");
        }

        double oldP = this.itemToPriority.get(item);
        Set<T> itemsWithOldPriority = this.priorityToItem.get(oldP);
        itemsWithOldPriority.remove(item);

        if (itemsWithOldPriority.size() == 0) {
            this.priorityToItem.remove(oldP);
        }

        this.itemToPriority.remove(item);
        add(item, priority);
    }

    @Override
    public int size() {
        return this.itemToPriority.size();
    }
}

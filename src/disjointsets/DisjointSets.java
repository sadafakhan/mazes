package disjointsets;

/**
 * Represents a collection of non-overlapping (disjoint) sets.
 * The client may create new sets or merge existing ones.
 * @param <T> The type of the items contained.
 */
public interface DisjointSets<T> {
    /**
     * Creates a new set containing just the given item and with a new integer id.
     *
     * The ids are assigned numerically; the first set created gets id 0, the second id 1,
     * and so on.
     *
     * @throws IllegalArgumentException  if `item` is already contained in one of these sets
     */
    void makeSet(T item);

    /**
     * Returns the integer id of the set containing the given item.
     *
     * @throws IllegalArgumentException  if `item` is not contained in any of these sets
     */
    int findSet(T item);

    /**
     * If the given items are in different sets, merges those sets and returns `true`. Otherwise,
     * does nothing and returns `false`.
     *
     * @throws IllegalArgumentException  if `item1` or `item2` are not contained in any of these
     *                                   sets
     */
    boolean union(T item1, T item2);
}

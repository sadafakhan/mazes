package disjointsets;

import java.util.HashMap;

/**
 * A basic {@link DisjointSets} implemented using a map.
 */
public class QuickFindDisjointSets<T> implements DisjointSets<T> {

    private final HashMap<T, Integer> ids;
    private int size;

    public QuickFindDisjointSets() {
        this.ids = new HashMap<>();
        this.size = 0;
    }

    @Override
    public void makeSet(T item) {
        this.ids.put(item, this.size);
        this.size++;
    }

    @Override
    public int findSet(T item) {
        Integer index = this.ids.get(item);
        if (index == null) {
            throw new IllegalArgumentException(item + " is not in any set.");
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        int id1 = findSet(item1);
        int id2 = findSet(item2);

        if (id1 == id2) {
            return false;
        }

        // replace all instances of id1 with id2
        this.ids.replaceAll((item, rep) -> rep == id1 ? id2 : rep);

        return true;
    }
}

package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    HashMap<T, Integer> indexTracker;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        this.pointers = new ArrayList<>(10);
        indexTracker = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        int index = pointers.size();
        indexTracker.put(item, index);
        pointers.add(-1);
    }

    private void pathCompressor(List<Integer> toCompress, int root) {
        for (int index: toCompress) {
            pointers.set(index, root);
        }
    }

    @Override
    public int findSet(T item) {
        if (!indexTracker.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = indexTracker.get(item);
        List<Integer> toCompress = new ArrayList<>();
        while (pointers.get(index) >= 0) {
            toCompress.add(index);
            index = pointers.get(index);
        }
        pathCompressor(toCompress, index);
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        int rootA = findSet(item1);
        int rootB = findSet(item2);
        if (rootA != rootB) {
            int aWeight = pointers.get(rootA);
            int bWeight = pointers.get(rootB);
            int weight = aWeight + bWeight;

            // rootA is heavier tree or the same weight
            if (aWeight <= bWeight) {
                // change the index at rootB to point to rootA
                pointers.set(rootB, rootA);
                // change the weight at rootA to be the combined weight
                pointers.set(rootA, weight);
                return true;
            } else {
                // change the index at rootA to point to rootB
                pointers.set(rootA, rootB);
                // change the weight at rootB to be the combined weight
                pointers.set(rootB, weight);
                return true;
            }
        }
        return false;
    }
}

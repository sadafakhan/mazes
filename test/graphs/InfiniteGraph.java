package graphs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an infinite graph of integers, like the one shown below:
 *  ... <-> 0 <-> 1 <-> 2 <-> 3 <-> ...
 *  Every vertex i has two neighbors: the vertices i-1 and i+1.
 */
public class InfiniteGraph implements Graph<Integer, Edge<Integer>> {
    @Override
    public Collection<Edge<Integer>> outgoingEdgesFrom(Integer vertex) {
        Set<Edge<Integer>> neighbors = new HashSet<>();
        neighbors.add(new Edge<>(vertex, vertex - 1, 1));
        neighbors.add(new Edge<>(vertex, vertex + 1, 1));
        return neighbors;
    }
}

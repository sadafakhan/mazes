package graphs;

import utils.IntWrapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an infinite graph of integers, like the one shown below:
 *  ... <-> 0 <-> 1 <-> 2 <-> 3 <-> ...
 *  Every vertex i has two neighbors: the vertices i-1 and i+1.
 */
public class InfiniteIntWrapperGraph implements Graph<IntWrapper, Edge<IntWrapper>> {
    @Override
    public Collection<Edge<IntWrapper>> outgoingEdgesFrom(IntWrapper vertex) {
        Set<Edge<IntWrapper>> neighbors = new HashSet<>();
        neighbors.add(new Edge<>(vertex, new IntWrapper(vertex.val - 1), 1));
        neighbors.add(new Edge<>(vertex, new IntWrapper(vertex.val + 1), 1));
        return neighbors;
    }
}

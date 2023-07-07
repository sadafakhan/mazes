package graphs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A directed graph stored as an adjacency list.
 *
 * @param <V> The vertex type
 * @param <E> The edge type. Must be a subtype of {@link Edge}.
 */
public class AdjacencyListDirectedGraph<V, E extends BaseEdge<V, E>> implements Graph<V, E> {
    protected final Map<V, Set<E>> adjacencyList;

    /**
     * Constructs a new graph with the given edges.
     *
     * Ignores duplicate edges (edges that are {@code equals}).
     *
     * @param edges The edges in the graph.
     * @throws NullPointerException if edges is null or contains null entries
     */
    public AdjacencyListDirectedGraph(Collection<E> edges) {
        this.adjacencyList = new HashMap<>();
        edges.forEach(e -> adjacencyList.computeIfAbsent(e.from(), v -> new HashSet<>()).add(e));
    }

    @Override
    public Set<E> outgoingEdgesFrom(V vertex) {
        return Collections.unmodifiableSet(adjacencyList.getOrDefault(vertex, Set.of()));
    }
}

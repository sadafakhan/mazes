package graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An undirected graph stored as an adjacency list.
 *
 * @param <V> The vertex type
 * @param <E> The edge type. Must be a subtype of {@link Edge}.
 */
public class AdjacencyListUndirectedGraph<V, E extends BaseEdge<V, E>> implements KruskalGraph<V, E> {
    private final List<E> allEdges;
    protected final Map<V, Set<E>> adjacencyList;

    /**
     * Constructs a new graph with the given edges.
     *
     * Ignores duplicate edges (edges that are exactly equal according to the {@code equals} method,
     * or edges that would be equal if their directions were flipped).
     *
     * @param edges The edges in the graph.
     * @throws NullPointerException if edges is null, contains null entries, or contains edges with null vertices
     */
    public AdjacencyListUndirectedGraph(Collection<E> edges) {
        this.allEdges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        edges.forEach(e -> {
            if (e.from() == null || e.to() == null) {
                throw new NullPointerException(
                    "Graph edge contains a null vertex, but null vertices are not supported.");
            }
            if (adjacencyList.computeIfAbsent(e.from(), v -> new HashSet<>()).add(e)) {
                allEdges.add(e);
            }
            adjacencyList.computeIfAbsent(e.to(), v -> new HashSet<>()).add(e.reversed());
        });
    }

    @Override
    public Set<E> outgoingEdgesFrom(V vertex) {
        return Collections.unmodifiableSet(adjacencyList.getOrDefault(vertex, Set.of()));
    }

    @Override
    public Set<V> allVertices() {
        return Collections.unmodifiableSet(this.adjacencyList.keySet());
    }

    @Override
    public List<E> allEdges() {
        return Collections.unmodifiableList(this.allEdges);
    }
}

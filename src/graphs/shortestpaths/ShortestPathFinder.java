package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Edge;
import graphs.Graph;

/**
 * Interface for finding a shortest path between two vertices of a graph.
 *
 * @param <G> The graph type. Must be a subtype of {@link Graph}.
 * @param <V> The vertex type.
 * @param <E> The edge type. Must be a subtype of {@link Edge}.
 */
public interface ShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>> {
    /**
     * Computes a shortest path from start to end in given graph, and returns a ShortestPath object
     * that can be queried for the list of edges or vertices along the path, or its total weight.
     *
     * Alternatively, if no path exists in the graph from start to end, the returned ShortestPath
     * will have {@link ShortestPath#exists()} false.
     */
    ShortestPath<V, E> findShortestPath(G graph, V start, V end);
}

package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Edge;
import graphs.Graph;

import java.util.Map;

/**
 * A shortest path finder that works by first constructing a shortest paths from the given start
 * vertex in the given graph, then using it to compute a shortest path.
 *
 * @param <G> The graph type. Must be a subtype of {@link Graph}.
 * @param <V> The vertex type.
 * @param <E> The edge type. Must be a subtype of {@link Edge}.
 *
 * @see ShortestPathFinder
 */
public abstract class SPTShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    implements ShortestPathFinder<G, V, E> {

    @Override
    public ShortestPath<V, E> findShortestPath(G graph, V start, V end) {
        Map<V, E> spt = constructShortestPathsTree(graph, start, end);
        return extractShortestPath(spt, start, end);
    }

    /**
     * Returns a (partial) shortest paths tree (a map from vertex to preceding edge) containing
     * the shortest path from start to end in given graph.
     *
     * The start vertex will NOT have an entry in the SPT.
     * If start and end are the same, the SPT will be empty.
     * If there is no path from start to end, the SPT will include all vertices reachable from start.
     */
    protected abstract Map<V, E> constructShortestPathsTree(G graph, V start, V end);

    /**
     * Extracts the shortest path from start to end (to be output by {@link #findShortestPath}
     * from given shortest paths tree (as output by {@link #constructShortestPathsTree}).
     */
    protected abstract ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end);
}

package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
// import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, Double> distTo = new HashMap<>();
        Map<V, E> edgeTo = new HashMap<>();
        distTo.put(start, (double) 0);

        // start and end vertex are the same
        if (Objects.equals(start, end)) {
            return edgeTo;
        }

        ExtrinsicMinPQ<V> perimeter = createMinPQ();
        perimeter.add(start, 0);

        while (!perimeter.isEmpty()) {
            V u = perimeter.removeMin();

            // terminate once we have achieved the shortest path to end
            if (Objects.equals(u, end)) {
                break;
            }

            // for reach edge (u,v) with weight w
            for (E edge : graph.outgoingEdgesFrom(u)) {
                V v = edge.to();
                if (!distTo.containsKey(v)) {
                    distTo.put(v, Double.POSITIVE_INFINITY);
                }
                // prev best path to v
                double oldDist = distTo.get(v);

                // path through u
                double newDist = distTo.get(u) + edge.weight();

                if (newDist < oldDist) {
                    distTo.put(v, newDist);
                    edgeTo.put(v, edge);

                    if (perimeter.contains(v)) {
                        perimeter.changePriority(v, newDist);
                    } else {
                        perimeter.add(v, newDist);
                    }
                }

            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        // start and end vertex are the same
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        E edge = spt.get(end);

        // no shortest path exists
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }

        // backtracking
        List<E> path = new ArrayList<>();
        V curNode = end;
        path.add(edge);

        while (!Objects.equals(curNode, start)) {
            curNode = spt.get(curNode).from();
            if (Objects.equals(curNode, start)) {
                break;
            }
            path.add(spt.get(curNode));
        }

        Collections.reverse(path);
        return new ShortestPath.Success<>(path);

    }

}

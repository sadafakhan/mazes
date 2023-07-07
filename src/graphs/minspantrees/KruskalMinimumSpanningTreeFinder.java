package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        DisjointSets<V> disjointSets = createDisjointSets();
        List<E> finalMST = new ArrayList<>();
        List<V> vertices = new ArrayList<>(graph.allVertices());
        for (V vertex: vertices) {
            disjointSets.makeSet(vertex);
        }
        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));
        // int idToKeepTrack = disjointSets.findSet(edges.get(0).from());
        // boolean isItMST = true;
        Map<Integer, Integer> toKeepTrack = new HashMap();
        int maxValue = 0;
        int firstSet = 0;
        int secondSet = 0;

        for (E edge: edges) {
            int fromSet = disjointSets.findSet(edge.from());
            int toSet =  disjointSets.findSet(edge.to());
            if (fromSet != toSet) {
                finalMST.add((E) edge);
                disjointSets.union(edge.from(), edge.to());
                if (disjointSets.findSet(edge.to()) == fromSet) {
                    firstSet = fromSet;
                    secondSet = toSet;
                }
                else {
                    firstSet = toSet;
                    secondSet = fromSet;
                }
                if (toKeepTrack.containsKey(firstSet) && toKeepTrack.containsKey(secondSet)) {
                    toKeepTrack.put(firstSet, toKeepTrack.get(firstSet)+toKeepTrack.get(secondSet));
                    if (toKeepTrack.get(firstSet) > maxValue) {
                        maxValue = toKeepTrack.get(firstSet);
                    }
                }

                else if (toKeepTrack.containsKey(disjointSets.findSet(edge.to()))) {
                    toKeepTrack.put(disjointSets.findSet(edge.to()),
                        toKeepTrack.get(disjointSets.findSet(edge.to()))+1);
                    if (toKeepTrack.get(disjointSets.findSet(edge.to())) > maxValue) {
                        maxValue = toKeepTrack.get(disjointSets.findSet(edge.to()));
                    }
                }
                else {
                    toKeepTrack.put(disjointSets.findSet(edge.to()), 2);
                }
            }
        }
        MinimumSpanningTree<V, E> mst = null;
        if (maxValue != vertices.size() && vertices.size() > 1) {
            mst = new MinimumSpanningTree.Failure<>();
        }
        else {
            mst = new MinimumSpanningTree.Success<>(finalMST);
        }
        return mst;
    }
}

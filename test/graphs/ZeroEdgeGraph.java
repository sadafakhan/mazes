package graphs;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ZeroEdgeGraph implements KruskalGraph<Integer, Edge<Integer>> {
    private final int numVertices;

    public ZeroEdgeGraph(int numVertices) {
        this.numVertices = numVertices;
    }

    @Override
    public List<Edge<Integer>> outgoingEdgesFrom(Integer vertex) {
        return List.of();
    }

    @Override
    public List<Integer> allVertices() {
        return IntStream.range(0, this.numVertices).boxed().collect(Collectors.toList());
    }

    @Override
    public List<Edge<Integer>> allEdges() {
        return List.of();
    }
}

package graphs.minspantrees;

import graphs.BaseEdge;
import graphs.KruskalGraph;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AbstractDoubleAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.IterableAssert;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.within;

/**
 * Asserts for MinimumSpanningTreeFinders.
 */
public class MinimumSpanningTreeFinderAssert<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    extends AbstractObjectAssert<MinimumSpanningTreeFinderAssert<G, V, E>, MinimumSpanningTreeFinder<G, V, E>> {

    public MinimumSpanningTreeFinderAssert(MinimumSpanningTreeFinder<G, V, E> actual) {
        super(actual, MinimumSpanningTreeFinderAssert.class);
    }

    public MinimumSpanningTreeAssert<V, E> findingMST(G graph) {
        return new MinimumSpanningTreeAssert<>(actual.findMinimumSpanningTree(graph), graph);
    }

    public static class MinimumSpanningTreeAssert<V, E extends BaseEdge<V, E>>
        extends AbstractObjectAssert<MinimumSpanningTreeAssert<V, E>, MinimumSpanningTree<V, E>> {

        public static final double EPSILON = .0001;
        private final KruskalGraph<V, E> graph;

        public MinimumSpanningTreeAssert(MinimumSpanningTree<V, E> actual, KruskalGraph<V, E> graph) {
            super(actual, MinimumSpanningTreeAssert.class);
            this.graph = graph;
        }

        public MinimumSpanningTree<V, E> getActual() {
            return this.actual;
        }

        private AbstractBooleanAssert<?> extractExists() {
            return extracting(MinimumSpanningTree::exists, InstanceOfAssertFactories.BOOLEAN)
                .as("result.exists()");
        }

        // The types in this method aren't perfect, but Object is good enough for our test code.
        private IterableAssert<Object> extractEdges() {
            return extracting(MinimumSpanningTree::edges, InstanceOfAssertFactories.ITERABLE)
                .as("result.edges()");
        }

        private AbstractDoubleAssert<?> extractWeight() {
            return extracting(MinimumSpanningTree::totalWeight, InstanceOfAssertFactories.DOUBLE)
                .as("result.totalWeight()");
        }

        public MinimumSpanningTreeAssert<V, E> doesNotExist() {
            extractExists().isFalse();
            return this;
        }

        public MinimumSpanningTreeAssert<V, E> exists() {
            extractExists().isTrue();
            return this;
        }

        @SafeVarargs
        public final MinimumSpanningTreeAssert<V, E> hasEdges(E... edges) {
            exists();
            // cast to Object[] to disambiguate from a single varargs Object[]... parameter
            extractEdges().containsExactlyInAnyOrder((Object[]) edges);
            return this;
        }

        public final MinimumSpanningTreeAssert<V, E> hasEdges(Iterable<E> edges) {
            exists();
            extractEdges().containsExactlyInAnyOrderElementsOf(edges);
            return this;
        }

        public MinimumSpanningTreeAssert<V, E> isEquivalentTo(MinimumSpanningTree<V, E> expected) {
            extractExists().isEqualTo(expected.exists());
            if (expected.exists()) {
                hasWeightCloseTo(expected.totalWeight());
                isValid();
            }
            return this;
        }

        public MinimumSpanningTreeAssert<V, E> hasWeightCloseTo(double expected) {
            extractWeight().isCloseTo(expected, within(EPSILON));
            return this;
        }

        public MinimumSpanningTreeAssert<V, E> isValid() {
            if (!actual.exists()) {
                // skip if not found; validity check only matters when MST exists
                return this;
            }

            // check mst has correct number of edges
            extractEdges().hasSize(this.graph.allVertices().size() - 1);

            // check edges exist in graph, and none are
            checkHasValidEdges();

            // check is spanning (touches all vertices, and has no self-loops)
            checkIsSpanning();

            return this;
        }

        private void checkHasValidEdges() {
            for (E edge : actual.edges()) {
                V from = edge.from();
                V to = edge.to();

                if (!graph.outgoingEdgesFrom(from).contains(edge)) {
                    failWithMessage("Found edge that does not exist in graph: "
                        + describeFromTo(from, to));
                }
            }
        }

        private void checkIsSpanning() {
            Set<V> verticesContained = new HashSet<>();

            for (E edge : this.actual.edges()) {
                V from = edge.from();
                V to = edge.to();

                if (Objects.equals(from, to)) {
                    failWithMessage("MST should not contain self-loop edges");
                }

                verticesContained.add(from);
                verticesContained.add(to);
            }

            if (verticesContained.size() != this.graph.allVertices().size()) {
                failWithMessage("MST does not reach all vertices; vertices included: %d of %d",
                    verticesContained.size(), this.graph.allVertices());
            }
        }

        private String describeFromTo(V from, V to) {
            String fromString = from.toString().stripTrailing();
            String toString = to.toString().stripTrailing();
            if (fromString.contains("\n") || toString.contains("\n")) {
                return String.format("from:%n%s%n%nto:%s", fromString, toString);
            }
            return String.format("from <%s> to <%s>", fromString, toString);
        }
    }
}

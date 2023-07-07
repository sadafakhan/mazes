package disjointsets;

import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.Test;
import utils.IntWrapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class UnionBySizeCompressingDisjointSetsTests extends BaseTest {

    @SafeVarargs
    protected final <T> DisjointSets<T> createDisjointSets(T... items) {
        DisjointSets<T> disjointSets = new UnionBySizeCompressingDisjointSets<>();
        Arrays.stream(items).forEach(disjointSets::makeSet);
        return disjointSets;
    }

    protected <T> List<Integer> getPointers(DisjointSets<T> disjointSet) {
        return ((UnionBySizeCompressingDisjointSets<T>) disjointSet).pointers;
    }

    protected <T> DisjointSetsAssert<T> assertThat(DisjointSets<T> actual) {
        return new DisjointSetsAssert<>(actual);
    }

    @Test
    void findSet_afterMakeSet_returns0() {
        DisjointSets<String> disjointSets = createDisjointSets("Hello world!");
        assertThat(disjointSets).findingSets("Hello world!").containsExactly(0);
    }

    @Test
    void findSet_newItem_throwsIllegalArgument() {
        DisjointSets<String> disjointSets = createDisjointSets("Hello world!");
        assertThatThrownBy(() -> disjointSets.findSet("foo"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findSet_afterUnion_returnsCorrectIds() {
        DisjointSets<String> disjointSets = createDisjointSets("Hello", "world!");
        disjointSets.union("Hello", "world!");

        int helloId = disjointSets.findSet("Hello");
        assertThat(helloId).isBetween(0, 1);
        assertThat(disjointSets).findingSets("world!").containsExactly(helloId);
    }

    @Test
    void union_newFirstParameter_throwsIllegalArgument() {
        DisjointSets<String> disjointSets = createDisjointSets("Hello", "world!");

        assertThatThrownBy(() -> disjointSets.union("foo", "world!"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void union_newSecondParameter_throwsIllegalArgument() {
        DisjointSets<String> disjointSets = createDisjointSets("Hello", "world!");

        assertThatThrownBy(() -> disjointSets.union("world!", "foo"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void union_twoNewParameters_throwsIllegalArgument() {
        DisjointSets<String> disjointSets = createDisjointSets("Hello", "world!");

        assertThatThrownBy(() -> disjointSets.union("foo", "foo"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void union_twoNewSets_returnsTrue() {
        DisjointSets<String> disjointSets = createDisjointSets("Hello", "world!");
        boolean output = disjointSets.union("Hello", "world!");

        assertThat(output).isTrue();
    }

    @Test
    void union_twoLeafItems_returnsTrue() {
        DisjointSets<String> disjointSets = createDisjointSets("a", "b", "c", "d", "e", "f");

        disjointSets.union("a", "b");
        disjointSets.union("b", "c");

        disjointSets.union("d", "e");
        disjointSets.union("e", "f");

        boolean output = disjointSets.union("c", "f");

        assertThat(output).isTrue();
    }

    @Test
    void union_twoLeafItemsOfSameSet_returnsFalse() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            DisjointSets<String> disjointSets = createDisjointSets("Hello", "there", "world", "!");

            disjointSets.union("Hello", "world");
            disjointSets.union("Hello", "there");
            disjointSets.union("Hello", "!");

            boolean output = disjointSets.union("!", "there");

            assertThat(output).isFalse();
        });
    }

    @Test
    void findSet_afterUnionTwoLeafItemsOfSameSet_returnsCorrectIds() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            DisjointSets<String> disjointSets = createDisjointSets("Hello", "there", "world", "!");

            disjointSets.union("Hello", "world");
            int helloId = disjointSets.findSet("Hello");
            disjointSets.union("Hello", "there");
            disjointSets.union("Hello", "!");
            disjointSets.union("!", "there");

            assertThat(disjointSets).findingSets("Hello", "there", "world", "!")
                .containsExactly(helloId, helloId, helloId, helloId);
        });
    }

    @Test
    void union_customItemLeafToSelf_returnsFalse() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            DisjointSets<IntWrapper> disjointSets = createDisjointSets(
                new IntWrapper(0),
                new IntWrapper(1),
                new IntWrapper(2));

            disjointSets.union(new IntWrapper(0), new IntWrapper(1));
            disjointSets.union(new IntWrapper(1), new IntWrapper(2));
            boolean output = disjointSets.union(new IntWrapper(2), new IntWrapper(2));

            assertThat(output).isFalse();
        });
    }

    @Test
    void findSet_afterUnionCustomItemLeafToSelf_returnsCorrectIds() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            DisjointSets<IntWrapper> disjointSets = createDisjointSets(
                new IntWrapper(0),
                new IntWrapper(1),
                new IntWrapper(2));

            disjointSets.union(new IntWrapper(0), new IntWrapper(1));
            int wrapper1Id = disjointSets.findSet(new IntWrapper(1));
            disjointSets.union(new IntWrapper(1), new IntWrapper(2));
            disjointSets.union(new IntWrapper(2), new IntWrapper(2));


            assertThat(disjointSets)
                .findingSets(new IntWrapper(0), new IntWrapper(1), new IntWrapper(2))
                .containsExactly(wrapper1Id, wrapper1Id, wrapper1Id);
        });
    }
}

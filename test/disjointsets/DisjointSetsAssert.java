package disjointsets;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ObjectAssert;

import java.util.Arrays;
import java.util.function.Function;

public class DisjointSetsAssert<T> extends AbstractObjectAssert<DisjointSetsAssert<T>, DisjointSets<T>> {
    public DisjointSetsAssert(DisjointSets<T> disjointSets) {
        super(disjointSets, DisjointSetsAssert.class);
    }

    @SafeVarargs
    public final AbstractListAssert<?, java.util.List<?>, Object, ObjectAssert<Object>>
    findingSets(T... items) {
        @SuppressWarnings("unchecked")
        Function<DisjointSets<T>, Integer>[] ids = Arrays.stream(items)
            .map(i -> (Function<DisjointSets<T>, Integer>) actual -> actual.findSet(i))
            .toArray(value -> (Function<DisjointSets<T>, Integer>[]) new Function[value]);
        return extracting(ids).as("set ids of %s", Arrays.toString(items));
    }
}

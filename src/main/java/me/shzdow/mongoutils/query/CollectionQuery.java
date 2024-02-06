package me.shzdow.mongoutils.query;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CollectionQuery<T> implements Query<T> {

    private final Collection<T> collection;
    private List<Predicate<T>> filters;

    public CollectionQuery(Collection<T> collection) {
        this.collection = collection;
    }

    @Override
    public @Nullable T queryFirst() {
        for (T obj : collection) {
            if (matchesWithFilters(obj))
                return obj;
        }
        return null;
    }

    @Override
    public void queryAndIterate(@NotNull Consumer<T> action) {
        for (T obj : collection) {
            if (matchesWithFilters(obj))
                action.accept(obj);
        }
    }

    @Override
    public Collection<T> queryAll() {
        List<T> queried = new ArrayList<>();
        queryAndIterate(queried::add);
        return queried;
    }

    @Override
    public Query<T> filter(@NotNull Predicate<T> filter) {
        if (filters == null)
            filters = new ArrayList<>();
        filters.add(filter);
        return this;
    }

    @Override
    public @Nullable Collection<Predicate<T>> getFilters() {
        return filters;
    }
}

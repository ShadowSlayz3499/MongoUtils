package me.shzdow.mongoutils.query;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Query<T> {

    @Nullable
    T queryFirst();

    void queryAndIterate(@NotNull Consumer<T> action);

    Collection<T> queryAll();

    Query<T> filter(@NotNull Predicate<T> filter);

    @Nullable
    Collection<Predicate<T>> getFilters();

    default boolean matchesWithFilters(T object) {
        Collection<Predicate<T>> filters = getFilters();
        if (filters != null) {
            for (Predicate<T> filter : filters) {
                if (!filter.test(object))
                    return false;
            }
        }
        return true;
    }

}

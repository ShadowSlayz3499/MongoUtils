package me.shzdow.mongoutils.query;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DocumentQuery<TDocument> implements Query<TDocument> {

    private List<Predicate<TDocument>> filters;
    private final FindIterable<TDocument> findIterable;

    public DocumentQuery(@NotNull MongoCollection<TDocument> collection) {
        this.findIterable = collection.find();
    }

    @Override
    public @Nullable TDocument queryFirst() {
        return findIterable.first();
    }

    @Override
    public void queryAndIterate(@NotNull Consumer<TDocument> action) {
        findIterable.cursor().forEachRemaining(action);
    }

    @Override
    public Collection<TDocument> queryAll() {
        List<TDocument> queried = new ArrayList<>();
        queryAndIterate(queried::add);
        return queried;
    }

    @Override
    public DocumentQuery<TDocument> filter(@NotNull Predicate<TDocument> filter) {
        if (filters == null)
            filters = new ArrayList<>();
        filters.add(filter);
        return this;
    }

    public DocumentQuery<TDocument> filter(@NotNull Bson filter) {
        findIterable.filter(filter);
        return this;
    }

    public DocumentQuery<TDocument> filterID(@NotNull Object id) {
        return filter(Filters.eq(id));
    }

    @Override
    public @Nullable Collection<Predicate<TDocument>> getFilters() {
        return filters;
    }
}

package me.shzdow.mongoutils.loader;

import me.lucko.helper.terminable.Terminable;
import me.shzdow.mongoutils.async.AsyncRequester;
import me.shzdow.mongoutils.async.SupplierAsyncRequester;
import me.shzdow.mongoutils.datastore.Datastore;
import me.shzdow.mongoutils.query.DocumentQuery;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;

import static me.shzdow.mongoutils.async.AsyncAccess.asyncAction;

public interface MongoLoadSource<T> extends Terminable {
    @NotNull
    Datastore getDatastore();

    T loadFirstByID(@NotNull Object id);

    T loadFirstByDocumentQuery(@NotNull Consumer<DocumentQuery<T>> queryModification);

    Collection<T> loadAllByDocumentQuery(@NotNull Consumer<DocumentQuery<T>> queryModification);

    default AsyncRequester<T> asyncLoadFirstByID(@NotNull Object id) {
        return new SupplierAsyncRequester<>(() -> loadFirstByID(id));
    }

    default AsyncRequester<T> asyncLoadFirstByDocumentQuery(@NotNull Consumer<DocumentQuery<T>> queryModification) {
        return new SupplierAsyncRequester<>(() -> loadFirstByDocumentQuery(queryModification));
    }

    default AsyncRequester<Collection<T>> asyncLoadAllByDocumentQuery(@NotNull Consumer<DocumentQuery<T>> queryModification) {
        return new SupplierAsyncRequester<>(() -> loadAllByDocumentQuery(queryModification));
    }

    default void save(@NotNull T object) {
        getDatastore().save(object);
    }

    default void saveAll(@NotNull T... objects) {
        getDatastore().saveAll(objects);
    }

    default void saveAll(@NotNull Collection<T> objects) {
        getDatastore().saveAll(objects);
    }

    default void asyncSave(@NotNull T object) {
        asyncAction(() -> save(object));
    }

    default void asyncSaveAll(@NotNull T... objects) {
        asyncAction(() -> saveAll(objects));
    }

    default void asyncSaveAll(@NotNull Collection<T> objects) {
        asyncAction(() -> saveAll(objects));
    }
}

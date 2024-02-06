package me.shzdow.mongoutils.datastore;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.shzdow.mongoutils.query.DocumentQuery;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface Datastore {

    @NotNull
    MongoDatabase getMongoDatabase();

    @NotNull
    <T> MongoCollection<T> getMongoCollection(@NotNull String collectionName, @NotNull Class<T> clazz);

    @NotNull
    MongoCollection<Document> getMongoCollection(@NotNull String collectionName);

    @NotNull
    <T> MongoCollection<T> getMongoCollection(@NotNull Class<T> clazz);

    <T> DocumentQuery<T> query(@NotNull String collectionName, @NotNull Class<T> clazz);

    DocumentQuery<Document> query(@NotNull String collectionName);

    <T> DocumentQuery<T> query(@NotNull Class<T> clazz);

    <T> void save(@NotNull String collectionName, @NotNull T object, @Nullable SaveOptions options);

    default <T> void save(@NotNull String collectionName, @NotNull T object) {
        save(collectionName, object, null);
    }

    <T> void save(@NotNull T object, @Nullable SaveOptions options);

    default <T> void save(@NotNull T object) {
        save(object, null);
    }

    <T> void saveAll(@NotNull String collectionName, @Nullable SaveOptions options, @NotNull T... objects);

    default <T> void saveAll(@NotNull String collectionName, @NotNull T... objects) {
        saveAll(collectionName, null, objects);
    }

    <T> void saveAll(@Nullable SaveOptions options, @NotNull T... objects);

    default <T> void saveAll(@NotNull T... objects) {
        saveAll((SaveOptions) null, objects);
    }

    <T> void saveAll(@NotNull String collectionName, @NotNull Collection<T> objects, @Nullable SaveOptions options);

    default <T> void saveAll(@NotNull String collectionName, @NotNull Collection<T> objects) {
        saveAll(collectionName, objects, null);
    }

    <T> void saveAll(@NotNull Collection<T> objects, @Nullable SaveOptions options);

    default <T> void saveAll(@NotNull Collection<T> objects) {
        saveAll(objects, null);
    }

    void dropCollection(@NotNull String collectionName);

    void dropDatabase();

}
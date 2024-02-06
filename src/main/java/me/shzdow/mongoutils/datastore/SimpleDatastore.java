package me.shzdow.mongoutils.datastore;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.shzdow.mongoutils.MongoUtils;
import me.shzdow.mongoutils.annotations.AnnotationConstants;
import me.shzdow.mongoutils.annotations.Pojo;
import me.shzdow.mongoutils.query.DocumentQuery;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class SimpleDatastore implements Datastore {

    private final MongoDatabase database;

    public SimpleDatastore(@NotNull MongoDatabase database) {
        this.database = database;
    }

    public SimpleDatastore(@NotNull MongoUtils mongoUtils, @NotNull String database) {
        this(mongoUtils.getMongoClient().getDatabase(database));
    }

    @Override
    public @NotNull MongoDatabase getMongoDatabase() {
        return database;
    }

    @Override
    public @NotNull <T> MongoCollection<T> getMongoCollection(@NotNull String collectionName, @NotNull Class<T> clazz) {
        return database.getCollection(collectionName, clazz);
    }

    @Override
    public @NotNull MongoCollection<Document> getMongoCollection(@NotNull String collectionName) {
        return database.getCollection(collectionName);
    }

    @Override
    public @NotNull <T> MongoCollection<T> getMongoCollection(@NotNull Class<T> clazz) {
        return getCollectionFromClass(clazz);
    }

    @Override
    public <T> DocumentQuery<T> query(@NotNull String collectionName, @NotNull Class<T> clazz) {
        return new DocumentQuery<>(getMongoCollection(collectionName, clazz));
    }

    @Override
    public DocumentQuery<Document> query(@NotNull String collectionName) {
        return new DocumentQuery<>(getMongoCollection(collectionName));
    }

    @Override
    public <T> DocumentQuery<T> query(@NotNull Class<T> clazz) {
        return new DocumentQuery<>(getMongoCollection(clazz));
    }

    @SuppressWarnings("all")
    private <T> MongoCollection<T> getCollectionFromObject(@NotNull T object) {
        return (MongoCollection<T>) getCollectionFromClass(object.getClass());
    }

    @SuppressWarnings("all")
    private <T> MongoCollection<T> getCollectionFromClass(@NotNull Class<T> clazz) {
        Pojo pojo = clazz.getAnnotation(Pojo.class);
        String name;
        if (pojo == null) {
            name = clazz.getSimpleName().toLowerCase(Locale.ROOT);
        } else {
            name = pojo.collectionName();
            if (name == AnnotationConstants.USE_DEFAULT)
                name = clazz.getName();
        }
        return (MongoCollection<T>) getMongoCollection(name, clazz);
    }

    private <T> void saveWithMongoCollection(@NotNull T object, @NotNull MongoCollection<T> collection) {
        saveWithMongoCollection(object, collection, null);
    }

    @SuppressWarnings("all")
    private <T> void saveWithMongoCollection(@NotNull T object, @NotNull MongoCollection<T> collection, @Nullable SaveOptions options) {
        Object id = getObjectID(object);
        T check = collection.find(Filters.eq(id)).first();
        if (check == null) {
            if (options != null && options.isUsingInsertOneOptions()) {
                collection.insertOne(object, options.getInsertOneOptions());
            } else {
                collection.insertOne(object);
            }
        } else {
            if (options != null && options.isUsingReplaceOptions()) {
                collection.replaceOne(Filters.eq(id), object, options.getReplaceOptions());
            } else {
                collection.replaceOne(Filters.eq(id), object);
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public <T> void save(@NotNull String collectionName, @NotNull T object, @Nullable SaveOptions options) {
        saveWithMongoCollection(object, (MongoCollection<T>) getMongoCollection(collectionName, object.getClass()), options);
    }

    @Override
    @SuppressWarnings("all")
    public <T> void save(@NotNull T object, @Nullable SaveOptions options) {
        saveWithMongoCollection(object, (MongoCollection<T>) getCollectionFromClass(object.getClass()), options);
    }

    @Override
    public <T> void saveAll(@NotNull String collectionName, @Nullable SaveOptions options, @NotNull T... objects) {
        saveAll(collectionName, Arrays.asList(objects), options);
    }

    @Override
    public <T> void saveAll(@Nullable SaveOptions options, @NotNull T... objects) {
        saveAll(Arrays.asList(objects), options);
    }

    @Override
    public <T> void saveAll(@NotNull String collectionName, @NotNull Collection<T> objects, @Nullable SaveOptions options) {
        saveCollection(collectionName, objects, options);
    }

    @Override
    public <T> void saveAll(@NotNull Collection<T> objects, @Nullable SaveOptions options) {
        saveCollection(null, objects, options);
    }

    @Override
    public void dropCollection(@NotNull String collectionName) {
        MongoCollection<Document> collection = getMongoCollection(collectionName);
        collection.drop();
    }

    @Override
    public void dropDatabase() {
        database.drop();
    }

    @SuppressWarnings("all")
    private <T> void saveCollection(@Nullable String collectionName, @NotNull Collection<T> objects, @Nullable SaveOptions options) {
        Map<Class<?>, MongoCollection<?>> collectionMap = new HashMap<>();
        for (T obj : objects) {
            Class<?> type = obj.getClass();
            MongoCollection<T> collection = (MongoCollection<T>) collectionMap.get(type);
            if (collection == null) {
                collection = (MongoCollection<T>) ((collectionName == null) ? getCollectionFromClass(type) : getMongoCollection(collectionName, type));
                collectionMap.put(type, collection);
            }
            saveWithMongoCollection(obj, collection, options);
        }
    }

    @Nullable
    private <T> Object getObjectID(@NotNull T obj) {
        Object possibleID = null;
        boolean foundID = false;
        Class<?> clazz = obj.getClass();
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            try {
                Object value = declaredField.get(obj);
                if (declaredField.getName().equals("id")) {
                    if (value == null) {
                        possibleID = null;
                    } else {
                        possibleID = value;
                        foundID = true;
                    }
                } else if (declaredField.getAnnotation(BsonId.class) != null) {
                    return value;
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            declaredField.setAccessible(false);
        }
        if (!foundID)
            throw new RuntimeException("No object id was found in " + clazz + ". Either annotate the ObjectId variable as @BsonId or have an ObjectId variable named id.");
        return possibleID;
    }

}
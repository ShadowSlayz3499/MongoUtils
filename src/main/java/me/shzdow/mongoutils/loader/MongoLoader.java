package me.shzdow.mongoutils.loader;

import com.mongodb.client.MongoClient;
import me.shzdow.mongoutils.MongoUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class MongoLoader {
    private final MongoUtils mongoUtils;
    private final Map<Class<?>, MongoLoadSource<?>> map = new HashMap<>();

    public MongoLoader(@NotNull MongoClient mongoClient) {
        this(new MongoUtils(mongoClient));
    }

    public MongoLoader(@NotNull MongoUtils mongoUtils) {
        this.mongoUtils = mongoUtils;
    }

    @NotNull
    public MongoUtils getMongoUtils() {
        return mongoUtils;
    }

    @NotNull
    public MongoClient getClient() {
        return mongoUtils.getMongoClient();
    }

    public void addLoadSource(@NotNull MongoLoadSource<?> mongoLoadSource) {
        map.put(mongoLoadSource.getClass(), mongoLoadSource);
    }

    public void removeLoadSource(@NotNull MongoLoadSource<?> mongoLoadSource) {
        removeLoadSource(mongoLoadSource, true);
    }

    public void removeLoadSource(@NotNull MongoLoadSource<?> mongoLoadSource, boolean terminate) {
        map.remove(mongoLoadSource.getClass());
        if (terminate) mongoLoadSource.closeAndReportException();
    }

    @Nullable
    @SuppressWarnings("all")
    public <T> MongoLoadSource<T> getLoadSource(@NotNull Class<T> clazz) {
        return (MongoLoadSource<T>) map.get(clazz);
    }

    public void terminateAll() {
        for (MongoLoadSource<?> source : getSources()) {
            source.closeAndReportException();
        }
    }

    public Collection<MongoLoadSource<?>> getSources() {
        return map.values();
    }
}

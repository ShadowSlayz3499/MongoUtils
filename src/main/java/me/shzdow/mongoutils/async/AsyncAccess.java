package me.shzdow.mongoutils.async;

import me.lucko.helper.Schedulers;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface AsyncAccess<T> {
    /**
     * This method gets called if it is able to reach
     * to the given object.
     *
     * @param obj The object.
     */
    void accessIfSuccessful(T obj);

    static void asyncAction(@NotNull Runnable runnable) {
        Schedulers.async().run(runnable);
    }
}

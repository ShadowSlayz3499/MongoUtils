package me.shzdow.mongoutils.async;

import me.lucko.helper.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * The Async Request is to be used to retrieve data
 * asynchronously. It does not guarantee that the data
 * {@link #getObj()} retrieved is not null.
 *
 * Use {@link #isCompleted()} to check whether the request
 * made has been completed.
 *
 * Use {@link #isSuccessful()} to check whether the request
 * was completed successfully.
 *
 * @param <T> type.
 */
public final class AsyncRequest<T> {
    private volatile boolean complete = false, success = false;
    private final Supplier<T> supplier;
    private volatile T obj;

    public AsyncRequest(T obj, boolean completed, boolean success) {
        this.obj = obj;
        this.supplier = null;
        this.complete = completed;
        this.success = success;
    }

    public AsyncRequest(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public AsyncRequest(@NotNull Supplier<T> supplier, T obj) {
        this.supplier = supplier;
        this.obj = obj;
    }

    public synchronized boolean isCompleted() {
        return complete;
    }

    public synchronized boolean isSuccessful() {
        return success;
    }

    @Nullable
    public synchronized T getObj() {
        return obj;
    }

    public synchronized void setObj(T obj) {
        this.obj = obj;
    }

    public synchronized void reset() {
        if (complete) {
            complete = false;
            success = false;
        }
    }

    public synchronized void tryToAccess() {
        reset();
        access();
    }

    public synchronized void tryToAccess(boolean useInBuiltScheduler) {
        if (useInBuiltScheduler) {
            Schedulers.async().run(this::tryToAccess);
        } else {
            tryToAccess();
        }
    }

    public synchronized void tryToAccess(int attempts) {
        reset();
        for (int i = 0; i < attempts && !complete; i++) {
            access();
        }
    }

    public synchronized void tryToAccess(int attempts, boolean useInBuiltScheduler) {
        if (useInBuiltScheduler) {
            Schedulers.async().run(() -> tryToAccess(attempts));
        } else {
            tryToAccess(attempts);
        }
    }

    private synchronized void access() {
        if (supplier == null) return;
        try {
            this.obj = supplier.get();
            success = true;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        complete = true;
    }
}

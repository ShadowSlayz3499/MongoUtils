package me.shzdow.mongoutils.async;

import me.lucko.helper.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AsyncRequester<T> {
    public AsyncRequest<T> request() {
        AsyncRequest<T> request = new AsyncRequest<>(this::databaseRequest);
        request.tryToAccess();
        return request;
    }

    public AsyncRequest<T> request(int attempts) {
        AsyncRequest<T> request = new AsyncRequest<>(this::databaseRequest);
        request.tryToAccess(attempts);
        return request;
    }

    public AtomicReference<RequestState> access(@NotNull AsyncAccess<T> access) {
        return access(access, null);
    }

    public AtomicReference<RequestState> access(@NotNull AsyncAccess<T> access, @Nullable Runnable failAccess) {
        AtomicReference<RequestState> atomicReference = new AtomicReference<>(RequestState.PENDING);
        Schedulers.async().run(() -> {
            try {
                T obj = databaseRequest();
                if (obj == null) {
                    atomicReference.set(RequestState.FAILED_COMPLETE);
                    if (failAccess != null) failAccess.run();
                } else {
                    access.accessIfSuccessful(obj);
                    atomicReference.set(RequestState.SUCCESSFUL_COMPLETE);
                }
            } catch (Throwable ex) {
                atomicReference.set(RequestState.FAILED_COMPLETE);
                if (failAccess != null) failAccess.run();
            }
        });
        return atomicReference;
    }

    @Nullable
    public abstract T databaseRequest();
}

package me.shzdow.mongoutils.async;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SupplierAsyncRequester<T> extends AsyncRequester<T> {
    private final Supplier<T> supplier;

    public SupplierAsyncRequester(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public synchronized T databaseRequest() {
        return supplier.get();
    }
}

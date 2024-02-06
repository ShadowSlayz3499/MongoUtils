package me.shzdow.mongoutils.datastore;

import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.ReplaceOptions;
import org.jetbrains.annotations.Nullable;

public final class SaveOptions {

    private ReplaceOptions replaceOptions;
    private InsertOneOptions insertOneOptions;

    public boolean isUsingReplaceOptions() {
        return replaceOptions != null;
    }

    @Nullable
    public ReplaceOptions getReplaceOptions() {
        return replaceOptions;
    }

    public SaveOptions setReplaceOptions(ReplaceOptions replaceOptions) {
        this.replaceOptions = replaceOptions;
        return this;
    }

    public boolean isUsingInsertOneOptions() {
        return insertOneOptions != null;
    }

    @Nullable
    public InsertOneOptions getInsertOneOptions() {
        return insertOneOptions;
    }

    public SaveOptions setInsertOneOptions(InsertOneOptions insertOneOptions) {
        this.insertOneOptions = insertOneOptions;
        return this;
    }


}

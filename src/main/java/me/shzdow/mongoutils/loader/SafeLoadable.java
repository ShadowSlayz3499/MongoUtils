package me.shzdow.mongoutils.loader;

import me.shzdow.mongoutils.async.LoadState;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.jetbrains.annotations.Nullable;

/**
 * @see me.shzdow.mongoutils.async.LoadState for more information.
 */
public interface SafeLoadable {
    @Nullable
    @BsonIgnore
    LoadState getLoadState();

    @BsonIgnore
    void setLoadState(@Nullable LoadState loadState);
}

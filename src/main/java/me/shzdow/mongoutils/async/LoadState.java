package me.shzdow.mongoutils.async;

/**
 * This indicates which state at which the object
 * is at when being loaded or saved from/to the database.
 *
 * This is important to avoid conflicts between loaded from
 * and saved to the database.
 *
 * For instance, since most access to the database is done
 * asynchronously, an object might be stored in the cache which is
 * then going to be saved into the database. The object should
 * have a state of SAVING. Any form of request to retrieve that
 * object should be locked.
 */
public enum LoadState {
    LOADING,
    ACTIVE,
    SAVING
}

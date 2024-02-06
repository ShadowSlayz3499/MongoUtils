package me.shzdow.mongoutils.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Pojo {

    /**
     * Indicates which collection of the given {@link me.shzdow.mongoutils.datastore.Datastore}
     * the given POJO will be placed in unless explicitly stated (i.e.
     * {@link me.shzdow.mongoutils.datastore.Datastore#save(String, Object)} instead
     * of {@link me.shzdow.mongoutils.datastore.Datastore#save(String, Object)}).
     *
     * By default, it will use the name of the annotated class.
     *
     * For instance,
     *
     * public class Test {}
     *
     * Since the class Test is not annotated, it will be placed
     * in the collection named "Test".
     *
     * @Pojo(collectionName = "SomeCollection")
     * public class Test {}
     *
     * Since the class Test is annotated, it will be placed
     * in the collection named "SomeCollection".
     *
     * @return The collection name.
     */
    @NotNull
    String collectionName() default AnnotationConstants.USE_DEFAULT;

}

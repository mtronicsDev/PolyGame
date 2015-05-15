package com.mtronicsdev.polygame.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Resources {

    /**
     * The list of {@link com.mtronicsdev.polygame.io.Resources.ResourceHandler resource handlers} registered via
     * {@link Resources#registerResourceHandler(ResourceHandler, Class)}.
     */
    private static Map<String, ResourceHandler> resourceHandlers;

    static {
        resourceHandlers = new HashMap<>();
        registerResourceHandler(file -> file, File.class);
    }

    private Resources() {

    }

    /**
     * Retrieves the file stored in the location represented by {@code filename}.
     *
     * @param filename The location of the file
     * @return The file in the location specified in {@code filename}
     */
    private static File getFile(String filename) {
        return new File(filename);
    }

    /**
     * Retrieves an instance of {@link R} from the file represented by {@code filename}.
     *
     * @param filename The path to the file that the instance of {@link R} gets retrieved from
     * @param type     The type of the {@link Object} that should be retrieved
     * @param <R>      The Class of the {@link Object} that should be retrieved
     * @return An instance of {@link R} or, if no instance could be retrieved, {@code null}
     */
    public static <R> R getResource(String filename, Class<R> type) {
        return getResource(getFile(filename), type);
    }

    /**
     * Retrieves an instance of {@link R} from {@code file}.
     *
     * @param file The file that the instance of {@link R} gets retrieved from
     * @param type The type of the {@link Object} that should be retrieved
     * @param <R>  The Class of the {@link Object} that should be retrieved
     * @return An instance of {@link R} or, if no instance could be retrieved, {@code null}
     */
    public static <R> R getResource(File file, Class<R> type) {
        ResourceHandler handler = resourceHandlers.get(type.getTypeName());
        //noinspection unchecked
        return handler == null ? null : (R) handler.fromFile(file);
    }

    /**
     * Registers a new {@link com.mtronicsdev.polygame.io.Resources.ResourceHandler resource handler} for the type
     * {@link R}.
     *
     * @param handler      The handler that gets called when
     *                     {@link Resources#getResource(String, Class) getResource(filename, R.class)} is invoked
     * @param resourceType The type of the {@link Object} that should be handled by the handler
     * @param <R>          The class of the {@link Object} that should be handled by the handler
     */
    public static <R> void registerResourceHandler(ResourceHandler<R> handler, Class<R> resourceType) {
        resourceHandlers.put(resourceType.getTypeName(), handler);
    }

    /**
     * A functional interface which receives a {@link File} and returns an instance of {@link R}.
     * <p>
     * <p>This instance is somehow (depends on implementation of
     * {@link com.mtronicsdev.polygame.io.Resources.ResourceHandler#fromFile(File)})
     * derived from the given {@link File}.</p>
     *
     * @param <R> The class of the objects that are returned by {@code fromFile(File file)}
     */
    public interface ResourceHandler<R> {

        /**
         * This method has to derive an instance of {@link R} from the given {@link File}.
         *
         * @param file The {@link File} that is used to derive an instance of {@link R}
         * @return An instance of {@link R} that should be derived from {@code file}
         */
        R fromFile(File file);
    }

}

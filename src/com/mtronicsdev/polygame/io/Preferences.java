package com.mtronicsdev.polygame.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Preferences {

    /**
     * The {@link Properties properties} that store the preferences for this class.
     */
    private static Properties properties;
    /**
     * The list of {@link com.mtronicsdev.polygame.io.Preferences.PreferenceHandler preference handlers} registered via
     * {@link Preferences#registerPreferenceHandler(PreferenceHandler, Class)}.
     */
    private static Map<String, PreferenceHandler> preferenceHandlers;

    static {
        preferenceHandlers = new HashMap<>();

        Resources.registerResourceHandler(file -> {
            Properties p = new Properties();
            try {
                p.load(new BufferedInputStream(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return p;
        }, Properties.class);

        properties = Resources.getResource("data/polygame.properties", Properties.class);

        registerPreferenceHandler(v -> v, String.class);
        registerPreferenceHandler(Integer::parseInt, int.class);
        registerPreferenceHandler(Long::parseLong, long.class);
        registerPreferenceHandler(Double::parseDouble, double.class);
        registerPreferenceHandler(Float::parseFloat, float.class);
        registerPreferenceHandler(Boolean::parseBoolean, boolean.class);
    }

    private Preferences() {

    }

    /**
     * Retrieves an instance of {@link P} from the value mapped to {@code preference}.
     *
     * @param preference The preference mapped to the value that the instance of {@link P} gets retrieved from
     * @param type       The type of the {@link Object} that should be retrieved
     * @param <P>        The Class of the {@link Object} that should be retrieved
     * @return An instance of {@link P} or, if no instance could be retrieved, {@code null}
     */
    public static <P> P getPreference(String preference, Class<P> type) {
        String value = properties.getProperty(preference);
        //noinspection unchecked
        return value == null ? null :
                preferenceHandlers.get(type.getTypeName()) == null ? null :
                        (P) preferenceHandlers.get(type.getTypeName()).fromValue(value);
    }

    /**
     * Registers a new {@link com.mtronicsdev.polygame.io.Preferences.PreferenceHandler preference handler} for the type
     * {@link P}.
     *
     * @param handler      The handler that gets called when
     *                     {@link Preferences#getPreference(String, Class)} getPreference(property, P.class)} is invoked
     * @param propertyType The type of the {@link Object} that should be handled by the handler
     * @param <P>          The class of the {@link Object} that should be handled by the handler
     */
    public static <P> void registerPreferenceHandler(PreferenceHandler<P> handler, Class<P> propertyType) {
        preferenceHandlers.put(propertyType.getTypeName(), handler);
    }

    /**
     * A functional interface which receives a {@link String} and returns an instance of {@link P}.
     * <p>
     * <p>This instance is somehow (depends on implementation of
     * {@link com.mtronicsdev.polygame.io.Preferences.PreferenceHandler#fromValue(String)})
     * derived from the given {@link String}.</p>
     *
     * @param <P> The class of the objects that are returned by {@code fromValue(String preference)}
     */
    public interface PreferenceHandler<P> {
        /**
         * This method has to derive an instance of {@link P} from the given {@link String}.
         *
         * @param value The {@link String} that is used to derive an instance of {@link P}
         * @return An instance of {@link P} that should be derived from {@code value}
         */
        P fromValue(String value);
    }
}

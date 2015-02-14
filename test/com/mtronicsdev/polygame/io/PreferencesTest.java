package com.mtronicsdev.polygame.io;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PreferencesTest {

    @Before
    public void setUp() throws Exception {
        Preferences.registerPreferenceHandler(value -> value, String.class);
    }

    @Test
    public void testGetPreference() throws Exception {
        assertEquals(null, Preferences.getPreference("probablyNonExistent", String.class));
    }
}
package com.mtronicsdev.polygame.io;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourcesTest {

    @Before
    public void setUp() throws Exception {
        Resources.registerResourceHandler(file -> "Hi!", String.class);
    }

    @Test
    public void testGetResource() throws Exception {
        assertEquals("Hi!", Resources.getResource("", String.class));
    }
}
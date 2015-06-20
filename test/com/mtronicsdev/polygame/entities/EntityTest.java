package com.mtronicsdev.polygame.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EntityTest {

    Entity e;

    @Before
    public void setUp() throws Exception {
        e = new Entity();

        e.addModule(new ModuleTest(1));
        e.addModule(new ModuleTest(2));
        e.addModule(new ModuleTest(3));
        e.addModule(new ModuleTest2(1));
        e.addModule(new ModuleTest2(2));
    }

    @Test
    public void testAddModule() throws Exception {
        assertTrue(e.getModule(ModuleTest2.class).getParent().equals(e));
    }

    @Test
    public void testGetModules() throws Exception {
        final int[] c = {0};

        e.getModules(ModuleTest.class).forEach(m -> {
            assertTrue(m != null);
            c[0] += m.id;
        });

        assertEquals(c[0], 6);
    }

    @Test
    public void testGetModule() throws Exception {
        assertTrue(e.getModule(ModuleTest.class) != null);
        assertTrue(e.getModules(ModuleTest.class).contains(e.getModule(ModuleTest.class)));
    }

    @Test
    public void testRemoveModule() throws Exception {
        ModuleTest2 m = e.getModule(ModuleTest2.class);
        e.removeModule(m);
        assertTrue(!e.getModules(ModuleTest2.class).contains(m));
    }

    @Test
    public void testSetParent() throws Exception {
        Entity e2 = new Entity3D();
        Entity e3 = new Entity2D();

        e2.setParent(e);
        assertEquals(e2.getParent(), e);

        e2.setParent(e3);
        assertTrue(!e.children.contains(e2));
        assertEquals(e2.getParent(), e3);
    }

    @Test
    public void testGetParent() throws Exception {
        Entity e2 = new Entity2D();
        e2.setParent(e);
        assertEquals(e, e2.getParent());
    }

    private class ModuleTest extends Module {
        int id;

        public ModuleTest(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ModuleTest[" + id + "]";
        }
    }

    private class ModuleTest2 extends Module {
        int id;

        public ModuleTest2(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ModuleTest2[" + id + "]";
        }
    }
}
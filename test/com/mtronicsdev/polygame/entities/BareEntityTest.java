package com.mtronicsdev.polygame.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Maxi on 20.06.2015.
 */
public class BareEntityTest {

    Entity a, b, c, d;

    @Before
    public void setUp() throws Exception {
        a = new Entity();
        b = new Entity();
        c = new Entity();
        d = new Entity(new UpdateTestModule());
    }

    @Test
    public void testAddChild() throws Exception {
        a.addChild(b);
        assertTrue(b.getParent().equals(a)); //A is parent of B
        assertTrue(a.children.contains(b)); //B is child of A
    }

    @Test
    public void testRemoveChild() throws Exception {
        a.addChild(b);
        c.addChild(b);
        assertTrue(b.getParent().equals(c)); //C is parent of B
        assertTrue(!a.children.contains(b)); //B is not child of A
        assertTrue(c.children.contains(b)); //B is child of C
    }

    @Test
    public void testUpdate() throws Exception {
        a.addChild(d);
        a.update();
        assertTrue(d.getModule(UpdateTestModule.class).updated); //Child of a and its module get updated
    }

    private class UpdateTestModule extends Module {
        boolean updated = false;

        @Override
        public void update() {
            updated = true;
        }
    }
}
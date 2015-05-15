package com.mtronicsdev.polygame.graphics;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public abstract class GLObject {

    abstract void cleanUp();

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        cleanUp();
    }
}

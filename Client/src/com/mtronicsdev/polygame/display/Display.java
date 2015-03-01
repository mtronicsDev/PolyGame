package com.mtronicsdev.polygame.display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Display {

    private static final List<Window> windows;

    static {
        windows = new ArrayList<>(1);
    }

    private Display() {

    }

    static void addWindow(Window window) {
        windows.add(window);
    }

    public static List<Window> getWindows() {
        return windows;
    }

    static void refresh() {
        for (Iterator<Window> iterator = windows.iterator(); iterator.hasNext(); ) {
            Window window = iterator.next();
            if (window.shouldClose()) {
                iterator.remove();
                window.close();
            } else window.refresh();
        }
    }

}

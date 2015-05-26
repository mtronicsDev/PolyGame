package com.mtronicsdev.polygame.core;

import com.mtronicsdev.polygame.display.Monitor;
import com.mtronicsdev.polygame.display.Window;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Skybox;
import com.mtronicsdev.polygame.entities.modules.ThirdPersonController;
import com.mtronicsdev.polygame.io.Resources;

import java.awt.image.BufferedImage;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Main {

    public static void main(String... args) {
        ClientEngineManager.init();

        new Window("Polygame", Monitor.getPrimary());

        new Entity3D(new ThirdPersonController(),
                new Skybox(Resources.getResource("res/lostvalley_front.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_back.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_left.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_right.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_bottom.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_top.jpg", BufferedImage.class)));

        ClientEngineManager.run();
    }

}

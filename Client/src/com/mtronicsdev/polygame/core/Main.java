package com.mtronicsdev.polygame.core;

import com.mtronicsdev.polygame.display.Input;
import com.mtronicsdev.polygame.display.Window;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.entities.modules.*;
import com.mtronicsdev.polygame.graphics.Texture;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.awt.image.BufferedImage;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Main {

    public static void main(String... args) {
        ClientEngineManager.init();

        new Window("Polygame", 1280, 720);

        //setCursorVisible(false);
        //setCatchMouse(true);

        new Entity3D(new FirstPersonController(),
                new Skybox(Resources.getResource("res/lostvalley_front.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_back.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_left.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_right.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_bottom.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_top.jpg", BufferedImage.class)),
                new Module() {
                    {
                        Input.registerKeyHandler(GLFW.GLFW_KEY_ESCAPE);
                    }

                    @Override
                    public void update() {
                        if (Input.keyUp(GLFW.GLFW_KEY_ESCAPE)) System.exit(0);
                    }
                });

        new Entity3D(new Vector3f(0, 81, 0), new Terrain(Resources.getResource("res/blendMap.png", Texture.class),
                Resources.getResource("res/Grass 00 seamless.png", Texture.class),
                Resources.getResource("res/Dirt 00 seamless.png", Texture.class),
                Resources.getResource("res/Dirt cracked 00 seamless.png", Texture.class),
                Resources.getResource("res/Seamless cobblestones at sunset texture.png", Texture.class),
                Resources.getResource("res/heightmap.png", BufferedImage.class)));

        new Entity3D(new Vector3f(64, 100, 64), new LightSource(new Vector3f(1, 1, 1)));

        new Entity3D(new Vector3f(45, 0, -70), new Water());

        ClientEngineManager.run();
    }

}

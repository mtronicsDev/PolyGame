package com.mtronicsdev.polygame.core;

import com.mtronicsdev.polygame.display.Input;
import com.mtronicsdev.polygame.display.Window;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.entities.modules.*;
import com.mtronicsdev.polygame.graphics.SharedModel;
import com.mtronicsdev.polygame.graphics.Texture;
import com.mtronicsdev.polygame.gui.*;
import com.mtronicsdev.polygame.io.Preferences;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Vector3f;
import com.mtronicsdev.polygame.util.math.Vector4f;
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

        new Entity3D(new Vector3f(45, 0, -90), new Model(Resources.getResource("res/stall.obj", SharedModel.class)));

        Dimension4f p = Preferences.getPreference("guiEngine.defaultPadding", Dimension4f.class);
        Dimension2f s = new Dimension2f(32, 32, false, false);
        Dimension2f o = new Dimension2f(0, 0, true, true);
        Vector4f c = new Vector4f(1, 1, 1, 1);

        new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.TOP_LEFT, Resources.getResource("res/guiTest/TL.png", Texture.class));
        new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.TOP, Resources.getResource("res/guiTest/T.png", Texture.class));
        new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.TOP_RIGHT, Resources.getResource("res/guiTest/TR.png", Texture.class));
        new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.LEFT, Resources.getResource("res/guiTest/L.png", Texture.class));
        //new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.CENTER, Resources.getResource("res/guiTest/C.png", Texture.class));
        new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.RIGHT, Resources.getResource("res/guiTest/R.png", Texture.class));
        new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.BOTTOM_LEFT, Resources.getResource("res/guiTest/BL.png", Texture.class));
        new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.BOTTOM, Resources.getResource("res/guiTest/B.png", Texture.class));
        new GuiPanel(p, p, s, o, c, GuiEngine.Alignment.BOTTOM_RIGHT, Resources.getResource("res/guiTest/BR.png", Texture.class));
        new GuiText(p, p, s, o, c, GuiEngine.Alignment.CENTER, "Hello World!", 24, "C:\\\\Windows\\Fonts\\Corbel.ttf");

        ClientEngineManager.run();
    }

}

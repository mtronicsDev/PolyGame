package com.mtronicsdev.polygame.io;

import com.mtronicsdev.polygame.graphics.Texture;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public final class Textures {

    static {
        Resources.registerResourceHandler(file -> {
            try {
                BufferedImage image = ImageIO.read(file);

                int[] pixels = new int[image.getWidth() * image.getHeight()];
                image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

                ByteBuffer textureData = BufferUtils.createByteBuffer(pixels.length * 4); //RGBA

                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int pixel = pixels[(image.getHeight() - y - 1) * image.getWidth() + x];

                        textureData.put((byte) ((pixel >> 16) & 0xFF));    // R
                        textureData.put((byte) ((pixel >>  8) & 0xFF));    // G
                        textureData.put((byte) (pixel & 0xFF));            // B
                        textureData.put((byte) ((pixel >> 24) & 0xFF));    // A
                    }
                }

                textureData.flip();

                return new Texture(image.getWidth(), image.getHeight(), textureData);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, Texture.class);
    }

    private Textures() {
    }
}

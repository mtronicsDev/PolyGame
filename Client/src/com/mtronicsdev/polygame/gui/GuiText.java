package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.graphics.Texture;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class GuiText extends GuiPanel {

    private static Map<String, Font> fontMap;

    static {
        Resources.registerResourceHandler(file -> {
            ByteBuffer buffer = null;

            try {
                FileChannel fileChannel = new FileInputStream(file).getChannel();
                buffer = BufferUtils.createByteBuffer((int) (fileChannel.size() + 1));

                //noinspection StatementWithEmptyBody
                while (fileChannel.read(buffer) != -1) ;

                fileChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (buffer == null) return null;

            buffer.flip();
            return buffer;
        }, ByteBuffer.class);

        fontMap = new HashMap<>();
    }

    private String text;
    private int fontSize;
    private String font;

    public GuiText(Dimension4f padding, Dimension4f margin, Dimension2f size, Dimension2f offset,
                   Vector4f color, GuiEngine.Alignment alignment, String text, int fontSize, String fontFile) {
        super(padding, margin, size, offset, color, alignment, loadText(text, fontSize, fontFile));

        this.text = text;
        this.fontSize = fontSize;
        this.font = fontFile;
    }

    private static Texture loadText(String text, int fontSize, String fontFile) {

        /* ------------------------------------------------------ *
         *                    Glyph generation                    *
         * ------------------------------------------------------ */

        Font font = fontMap.get(fontFile);
        ByteBuffer fontInfo;
        IntBuffer ascent;
        if (fontMap.get(fontFile) == null) {
            //Create font //TODO: Store all fonts so that not every text object has to load them again
            ByteBuffer fontData = Resources.getResource(fontFile, ByteBuffer.class);

            fontInfo = BufferUtils.createByteBuffer(STBTTFontinfo.SIZEOF);
            stbtt_InitFont(fontInfo, fontData);

            //Font metrics
            ascent = BufferUtils.createIntBuffer(1);
            IntBuffer descent = BufferUtils.createIntBuffer(1),
                    lineGap = BufferUtils.createIntBuffer(1);

            stbtt_GetFontVMetrics(fontInfo, ascent, descent, lineGap);

            fontMap.put(fontFile, new Font(fontInfo, ascent));

            //ascent.flip();
        } else {
            fontInfo = font.info;
            ascent = font.ascent;
        }

        float scale = stbtt_ScaleForPixelHeight(fontInfo, fontSize);
        float asc = ascent.get(0) * scale;

        int width = 512, height = 256;

        char[] chars = text.toCharArray();

        int x = 0;

        ByteBuffer bitmap = BufferUtils.createByteBuffer(width * height);

        //Render characters into bitmap
        for (int c = 0; c < chars.length; c++) {
            IntBuffer sX = BufferUtils.createIntBuffer(1),
                    sY = BufferUtils.createIntBuffer(1),
                    oX = BufferUtils.createIntBuffer(1),
                    oY = BufferUtils.createIntBuffer(1);

            stbtt_GetCodepointBitmapBox(fontInfo, chars[c], scale, scale, oX, oY, sX, sY);

            //oX.flip();
            //oY.flip();
            //sX.flip();
            //sY.flip();

            int y = (int) (asc + oY.get(0));

            int byteOffset = x + y * width;

            stbtt_MakeCodepointBitmap(fontInfo, (ByteBuffer) bitmap.position(byteOffset),
                    sX.get() - oX.get(), sY.get() - oY.get(),
                    width, scale, scale, chars[c]);

            IntBuffer charWidth = BufferUtils.createIntBuffer(1);
            stbtt_GetCodepointHMetrics(fontInfo, chars[c], charWidth, BufferUtils.createIntBuffer(1));

            //charWidth.flip();

            x += charWidth.get(0) * scale;

            if (c < chars.length - 1) {
                int kern;
                kern = stbtt_GetCodepointKernAdvance(fontInfo, chars[c], chars[c + 1]);
                x += kern * scale;
            }
        }

        /* ------------------------------------------------------ *
         *                   Texture generation                   *
         * ------------------------------------------------------ */

        int id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, width, height, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glBindTexture(GL_TEXTURE_2D, 0);

        return new Texture(id);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setTexture(loadText(text, fontSize, font));
    }

    private static class Font {
        private ByteBuffer info;
        private IntBuffer ascent;

        public Font(ByteBuffer info, IntBuffer ascent) {
            this.info = info;
            this.ascent = ascent;
        }
    }
}

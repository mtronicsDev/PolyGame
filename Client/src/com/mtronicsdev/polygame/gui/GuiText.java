package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.graphics.TextQuadModel;
import com.mtronicsdev.polygame.graphics.Texture;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
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

    private static final Map<String, Font> FONT_MAP = new HashMap<>();

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
    }

    private String text;
    private Font font;
    private float lineHeight;
    private TextQuadModel[] quads;

    public GuiText(Dimension4f padding, Dimension4f margin, Dimension2f size, Dimension2f offset,
                   Vector4f color, GuiEngine.Alignment alignment, String text, int fontSize, String fontFile) {
        super(padding, margin, size, offset, color, alignment, loadFont(fontSize, fontFile));

        this.text = text;
        this.font = FONT_MAP.get(fontFile);
        lineHeight = fontSize;

        loadCharTable();
    }

    private static Texture loadFont(int fontSize, String fontFile) {
        Font font = FONT_MAP.get(fontFile);
        if (font != null) return font.bitmap;

        int bitmapWidth = 512, bitmapHeight = 512;

        ByteBuffer charData = BufferUtils.createByteBuffer(224 * STBTTPackedchar.SIZEOF);
        ByteBuffer fontData = Resources.getResource(fontFile, ByteBuffer.class);

        ByteBuffer fontInfo = BufferUtils.createByteBuffer(STBTTFontinfo.SIZEOF);
        stbtt_InitFont(fontInfo, fontData);

        float scale = stbtt_ScaleForPixelHeight(fontInfo, fontSize) / 4;

        ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
        ByteBuffer packContext = BufferUtils.createByteBuffer(STBTTPackContext.SIZEOF);

        //Pack font data
        stbtt_PackBegin(packContext, bitmap, bitmapWidth, bitmapHeight, 0, 1);
        stbtt_PackSetOversampling(packContext, 2, 2);
        stbtt_PackFontRange(packContext, fontData, 0, fontSize, 32, 224, charData);
        stbtt_PackEnd(packContext);

        int textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, bitmapWidth, bitmapHeight, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glBindTexture(GL_TEXTURE_2D, 0);

        Texture fontTexture = new Texture(textureId);
        FONT_MAP.put(fontFile, new Font(charData, fontTexture, bitmapWidth, bitmapHeight, scale));

        return fontTexture;
    }

    private void loadCharTable() {
        quads = new TextQuadModel[text.length()];
        STBTTAlignedQuad q = new STBTTAlignedQuad();

        FloatBuffer x = BufferUtils.createFloatBuffer(1), y = BufferUtils.createFloatBuffer(1);
        //Point viewport = Display.getCurrentWindow().getSize();

        float xMin = 0, xMax = 0, yMin = 0, yMax = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                y.put(0, y.get(0) + lineHeight);
                x.put(0, 0);
                continue;
            } else if (c < 32 || c >= 256) continue;

            stbtt_GetPackedQuad(font.characterData, font.width, font.height, c - 32, x, y, q.buffer(), 0);

            float s0 = q.getS0(), t0 = q.getT0(),
                    s1 = q.getS1(), t1 = q.getT1();

            float x0 = q.getX0() * font.scaleFactor * 9 / 16, y0 = q.getY0() * font.scaleFactor,
                    x1 = q.getX1() * font.scaleFactor * 9 / 16, y1 = q.getY1() * font.scaleFactor;

            if (i == 0) {
                xMin = x0;
                xMax = x1;
                yMin = y0;
                yMax = y1;
            } else {
                xMin = Math.min(xMin, x0);
                xMax = Math.max(xMax, x1);
                yMin = Math.min(yMin, y0);
                yMax = Math.max(yMax, y1);
            }


            float[] vertices = {x0, y0, x0, y1, x1, y0, x1, y1};
            float[] uvs = {s0, t0, s0, t1, s1, t0, s1, t1};

            TextQuadModel quad = new TextQuadModel(vertices, uvs);

            quads[i] = quad;
        }

        setSize(new Dimension2f(xMax - xMin, yMax - yMin, true, true));
    }

    public TextQuadModel getQuadAt(int index) {
        return quads[index];
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public float getLineHeight() {
        return lineHeight;
    }

    public static class Font {
        private ByteBuffer characterData;
        private Texture bitmap;
        private int width, height;
        private float scaleFactor;

        public Font(ByteBuffer characterData, Texture bitmap, int width, int height, float scaleFactor) {
            this.characterData = characterData;
            this.bitmap = bitmap;
            this.width = width;
            this.height = height;
            this.scaleFactor = scaleFactor;
        }

        public ByteBuffer getCharacterData() {
            return characterData;
        }

        public Texture getBitmap() {
            return bitmap;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public float getScaleFactor() {
            return scaleFactor;
        }
    }
}

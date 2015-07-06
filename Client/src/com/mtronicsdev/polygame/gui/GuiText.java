package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.display.Display;
import com.mtronicsdev.polygame.graphics.TextQuadModel;
import com.mtronicsdev.polygame.graphics.Texture;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;

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
    private int lineHeight;
    private TextQuadModel[] quads;

    public GuiText(Dimension4f padding, Dimension4f margin, Dimension2f size, Dimension2f offset,
                   Vector4f color, GuiEngine.Alignment alignment, String text, int fontSize, String fontFile) {
        super(padding, margin, size, offset, color, alignment, loadText(fontSize, fontFile));

        this.text = text;
        this.font = FONT_MAP.get(fontFile);
        lineHeight = fontSize;

        loadCharTable();
    }

    private static Texture loadText(int fontSize, String fontFile) {
        Font font = FONT_MAP.get(fontFile);
        if (font != null) return font.bitmap;

        int bitmapWidth = 512, bitmapHeight = 512;

        int textureId = glGenTextures();

        ByteBuffer characterData = BufferUtils.createByteBuffer(224 * STBTTBakedChar.SIZEOF);
        ByteBuffer fontData = Resources.getResource(fontFile, ByteBuffer.class);
        ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);

        stbtt_BakeFontBitmap(fontData, fontSize, bitmap, bitmapWidth, bitmapHeight, 32, characterData);

        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, bitmapWidth, bitmapHeight, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glBindTexture(GL_TEXTURE_2D, 0);

        Texture texture = new Texture(textureId);

        FONT_MAP.put(fontFile, new Font(characterData, texture, bitmapWidth, bitmapHeight));

        /*new GuiPanel(new Dimension4f(0, 0, 0, 0, true, true, true, true),
                new Dimension4f(0, 0, 0, 0, true, true, true, true),
                new Dimension2f(512, 512, false, false),
                new Dimension2f(0, 0, true, true),
                new Vector4f(1, 1, 1, 1),
                GuiEngine.Alignment.TOP, FONT_MAP.get(fontFile).bitmap);*/

        return texture;
    }

    private void loadCharTable() {
        quads = new TextQuadModel[text.length()];
        STBTTAlignedQuad q = new STBTTAlignedQuad();

        FloatBuffer x = BufferUtils.createFloatBuffer(1), y = BufferUtils.createFloatBuffer(1);

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                y.put(0, y.get(0) + getLineHeight());
                x.put(0, 0);
                continue;
            } else if (c < 32 || c >= 256) continue;

            stbtt_GetBakedQuad(font.characterData, font.width, font.height,
                    c - 32, x, y, q.buffer(), 1);

            Point viewport = Display.getCurrentWindow().getSize();

            float s0 = q.getS0(), t0 = q.getT0(),
                    s1 = q.getS1(), t1 = q.getT1();

            float x0 = q.getX0() / viewport.x, y0 = -q.getY0() / viewport.y,
                    x1 = q.getX1() / viewport.x, y1 = -q.getY1() / viewport.y;

            float[] vertices = {x0, y0, x0, y1, x1, y0, x1, y1};
            float[] uvs = {s0, t0, s0, t1, s1, t0, s1, t1};

            TextQuadModel quad = new TextQuadModel(vertices, uvs);

            quads[i] = quad;
        }
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

    public int getLineHeight() {
        return lineHeight;
    }

    public static class Font {
        private ByteBuffer characterData;
        private Texture bitmap;
        private int width, height;

        public Font(ByteBuffer characterData, Texture bitmap, int width, int height) {
            this.characterData = characterData;
            this.bitmap = bitmap;
            this.width = width;
            this.height = height;
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
    }
}

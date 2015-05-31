package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.display.Display;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class FrameBufferObject extends GLObject {

    private final Attachment attachment;
    private int width;
    private int height;

    private int id;

    private int textureId, depthId;

    public FrameBufferObject(Attachment attachment, int width, int height) {
        id = glGenFramebuffers();

        this.attachment = attachment;
        this.width = width;
        this.height = height;

        glBindFramebuffer(GL_FRAMEBUFFER, id);

        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        textureId = createTextureAttachment(width, height);
        depthId = attachment == Attachment.DEPTH_BUFFER_ATTACHMENT ? createDepthBufferAttachment(width, height) :
                createDepthTextureAttachment(width, height);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private int createTextureAttachment(int width, int height) {
        int id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, id, 0);

        glBindTexture(GL_TEXTURE_2D, 0);

        return id;
    }

    private int createDepthTextureAttachment(int width, int height) {
        int id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32F, width, height, 0,
                GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, id, 0);

        glBindTexture(GL_TEXTURE_2D, 0);

        return id;
    }

    private int createDepthBufferAttachment(int width, int height) {
        int id = glGenRenderbuffers();

        glBindRenderbuffer(GL_RENDERBUFFER, id);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, id);

        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        return id;
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
        glViewport(0, 0, width, height);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        Display.getCurrentWindow().resetViewport();
    }

    public int getTextureId() {
        return textureId;
    }

    public int getDepthId() {
        return depthId;
    }

    @Override
    void cleanUp() {
        glDeleteFramebuffers(id);
        glDeleteTextures(textureId);

        switch (attachment) {
            case DEPTH_BUFFER_ATTACHMENT:
                glDeleteRenderbuffers(depthId);
                break;
            default:
            case DEPTH_TEXTURE_ATTACHMENT:
                glDeleteTextures(depthId);
                break;
        }
    }

    public enum Attachment {
        DEPTH_TEXTURE_ATTACHMENT,
        DEPTH_BUFFER_ATTACHMENT
    }

}

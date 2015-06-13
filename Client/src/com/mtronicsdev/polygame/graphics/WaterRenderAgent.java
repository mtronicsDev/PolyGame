package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.entities.modules.Water;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Matrix4f;
import com.mtronicsdev.polygame.util.math.Vector3f;
import com.mtronicsdev.polygame.util.math.VectorMath;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class WaterRenderAgent extends RenderAgent<WaterShaderProgram> {

    private static final PlaneModel PLANE_MODEL = new PlaneModel();

    private static final int REFLECTION_RESOLUTION_X = 320;
    private static final int REFLECTION_RESOLUTION_Y = 180;

    private static final int REFRACTION_RESOLUTION_X = 1280;
    private static final int REFRACTION_RESOLUTION_Y = 720;

    private static final Vector3f REFLECTION_CLIP_PLANE = new Vector3f(0, 1, 0);
    private static final Vector3f REFRACTION_CLIP_PLANE = new Vector3f(0, -1, 0);

    private static final Texture DUDV_MAP = Resources.getResource("res/DuDv.png", Texture.class);
    private static final float WAVE_SPEED = 0.0001f;

    private FrameBufferObject reflectionFrameBuffer, refractionFrameBuffer;
    private float offset = 0;

    protected WaterRenderAgent(Matrix4f projectionMatrix) {
        super(new WaterShaderProgram());

        shaderProgram.bind();
        shaderProgram.loadTextureUnits();
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        shaderProgram.unbind();

        reflectionFrameBuffer = new FrameBufferObject(FrameBufferObject.Attachment.DEPTH_BUFFER_ATTACHMENT,
                REFLECTION_RESOLUTION_X, REFLECTION_RESOLUTION_Y);

        refractionFrameBuffer = new FrameBufferObject(FrameBufferObject.Attachment.DEPTH_TEXTURE_ATTACHMENT,
                REFRACTION_RESOLUTION_X, REFRACTION_RESOLUTION_Y);
    }

    void render(Camera camera, List<Water> waters) {
        Vector3f cameraPosition = ((Entity3D) camera.getParent()).getPosition();
        AbstractCamera abstractCamera = new AbstractCamera(camera);

        offset += WAVE_SPEED;
        offset %= 1;

        for (Water water : waters) {
            Vector3f position = ((Entity3D) water.getParent()).getPosition();

            //Render Scene to reflection / refraction textures
            glEnable(GL_CLIP_DISTANCE0);
            RenderEngine.getDefaultRenderAgent().shaderProgram.loadClipPlane(REFLECTION_CLIP_PLANE, -position.y);
            RenderEngine.getTerrainRenderAgent().shaderProgram.loadClipPlane(REFLECTION_CLIP_PLANE, -position.y);

            RenderEngine.setCamera(abstractCamera);

            float delta = 2 * cameraPosition.y - position.y;

            abstractCamera.getPosition().y -= delta;
            abstractCamera.getRotation().x *= -1;

            reflectionFrameBuffer.bind();
            RenderEngine.renderPre();
            RenderEngine.renderMain();
            //Could do that but it would just decrease performance as nex FBO gets bound immediately after
            //reflectionFrameBuffer.unbind();

            abstractCamera.getRotation().x *= -1;
            abstractCamera.getPosition().y += delta;

            RenderEngine.getDefaultRenderAgent().shaderProgram.loadClipPlane(REFRACTION_CLIP_PLANE, position.y);
            RenderEngine.getTerrainRenderAgent().shaderProgram.loadClipPlane(REFRACTION_CLIP_PLANE, position.y);

            refractionFrameBuffer.bind();
            RenderEngine.renderPre();
            RenderEngine.renderMain();
            refractionFrameBuffer.unbind();

            glDisable(GL_CLIP_DISTANCE0);

            RenderEngine.setCamera(camera);

            //Render water
            shaderProgram.bind();
            shaderProgram.loadOffset(offset);
            shaderProgram.loadViewMatrix(camera.getViewMatrix());
            PLANE_MODEL.bind();
            glEnableVertexAttribArray(0);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, getReflectionTextureId());

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, getRefractionTextureId());

            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, DUDV_MAP.getId());

            shaderProgram.loadTransformationMatrix(
                    VectorMath.createTransformationMatrix(position,
                            Vector3f.ZERO, water.getSize()));
            glDrawElements(GL_TRIANGLES, PlaneModel.getSize(), GL_UNSIGNED_INT, 0);

            //glActiveTexture(GL_TEXTURE2); //Performance loss and not necessary, see above
            glBindTexture(GL_TEXTURE_2D, 0);

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, 0);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, 0);

            glDisableVertexAttribArray(0);
            PLANE_MODEL.unbind();
            shaderProgram.unbind();
        }

        RenderEngine.setCamera(camera);
    }

    public int getReflectionTextureId() {
        return reflectionFrameBuffer.getTextureId();
    }

    public int getRefractionTextureId() {
        return refractionFrameBuffer.getTextureId();
    }

    public int getRefractionDepthTextureId() {
        return refractionFrameBuffer.getDepthId();
    }

    void setProjectionMatrix(Matrix4f projectionMatrix) {
        shaderProgram.loadProjectionMatrix(projectionMatrix);
    }

}

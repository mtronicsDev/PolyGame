package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector2f;
import com.mtronicsdev.polygame.math.Vector3f;

import static java.lang.Math.toRadians;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class GuiObject {

    private Vector2f position;
    private Vector2f size;
    private float rotation;
    private Texture texture;

    private Matrix4f transformationMatrix = new Matrix4f();

    public GuiObject(Vector2f position, Vector2f size, float rotation, Texture texture) {
        this.position = position;
        this.size = size;
        this.rotation = rotation;
        this.texture = texture;

        calculateTransformationMatrix();
        RenderEngine.registerGuiObject(this);
    }

    public GuiObject(Vector2f position, Vector2f size, Texture texture) {
        this(position, size, 0, texture);
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    private void calculateTransformationMatrix() {
        transformationMatrix.setIdentity();
        transformationMatrix.translate(position);
        transformationMatrix.rotate((float) toRadians(rotation), new Vector3f(0, 0, -1));
        transformationMatrix.scale(size);
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        calculateTransformationMatrix();
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
        calculateTransformationMatrix();
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        calculateTransformationMatrix();
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        RenderEngine.unRegisterGuiObject(this);
    }
}

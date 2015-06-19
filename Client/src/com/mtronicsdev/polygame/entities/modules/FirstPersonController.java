package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.display.Input;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.util.math.Math;
import com.mtronicsdev.polygame.util.math.*;
import org.lwjgl.glfw.GLFW;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class FirstPersonController extends Camera {

    private static final float HALF_PI = (float) (java.lang.Math.PI / 2);
    private final float speed;
    private final float sprintMultiplier;
    private final float sneakMultiplier;

    public FirstPersonController() {
        this(.07f, 1.7f, .4f);
    }

    public FirstPersonController(float speed, float sprintMultiplier, float sneakMultiplier) {
        Input.registerKeyHandler(GLFW.GLFW_KEY_Q);
        Input.registerKeyHandler(GLFW.GLFW_KEY_W);
        Input.registerKeyHandler(GLFW.GLFW_KEY_E);
        Input.registerKeyHandler(GLFW.GLFW_KEY_A);
        Input.registerKeyHandler(GLFW.GLFW_KEY_S);
        Input.registerKeyHandler(GLFW.GLFW_KEY_D);

        Input.registerKeyHandler(GLFW.GLFW_KEY_LEFT_SHIFT);
        Input.registerKeyHandler(GLFW.GLFW_KEY_LEFT_CONTROL);

        this.speed = speed;
        this.sprintMultiplier = sprintMultiplier;
        this.sneakMultiplier = sneakMultiplier;
    }

    @Override
    public void update() {
        super.update();

        Entity3D parent = (Entity3D) getParent();
        Vector3f direction = new Vector3f();

        if (Input.keyPressed(GLFW.GLFW_KEY_W)) {
            direction.z -= 1;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_S)) {
            direction.z += 1;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_Q)) {
            direction.y -= 1;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_E)) {
            direction.y += 1;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_A)) {
            direction.x -= 1;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_D)) {
            direction.x += 1;
        }

        //Position
        if (direction.length() > 1) direction.normalize(); //No cheaty diagonal movement

        direction.multiply(speed);

        if (Input.keyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) direction.multiply(sprintMultiplier);
        if (Input.keyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) direction.multiply(sneakMultiplier);

        //Rotation
        Vector3f rotation = parent.getRotation();

        Vector2f delta = Input.getMouseDelta();
        rotation.x += delta.y * .01f;
        rotation.y += delta.x * .01f;

        rotation.x = Math.clamp(rotation.x, -HALF_PI, HALF_PI); //Don't rotate your head through your neck

        //Movement vector
        float zRot = (float) (-direction.z * cos(rotation.y) - direction.x * sin(rotation.y));
        float xRot = (float) (-direction.z * sin(rotation.y) + direction.x * cos(rotation.y));

        direction.z = -zRot;
        direction.x = xRot;

        //Movement
        parent.addPosition(direction);
    }

    @Override
    public Matrix4f getViewMatrix() {
        Entity3D parent = (Entity3D) getParent();
        return VectorMath.createViewMatrix(parent.getPosition(), parent.getRotation());
    }
}

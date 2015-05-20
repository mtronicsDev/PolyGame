package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.display.Input;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector2f;
import com.mtronicsdev.polygame.math.Vector3f;
import com.mtronicsdev.polygame.util.VectorMath;
import org.lwjgl.glfw.GLFW;

import static java.lang.Math.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class ThirdPersonController extends Camera {

    private float radius = 20;
    private float pitch = 20;
    private float yaw = 0;
    private float rotationAroundPerson = 0;
    private Vector3f position = new Vector3f();

    public ThirdPersonController() {
        Input.registerMouseButtonHandler(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

        Input.registerKeyHandler(GLFW.GLFW_KEY_Q);
        Input.registerKeyHandler(GLFW.GLFW_KEY_W);
        Input.registerKeyHandler(GLFW.GLFW_KEY_E);
        Input.registerKeyHandler(GLFW.GLFW_KEY_A);
        Input.registerKeyHandler(GLFW.GLFW_KEY_S);
        Input.registerKeyHandler(GLFW.GLFW_KEY_D);
    }

    @Override
    public void update() {
        super.update();

        Entity3D parent = (Entity3D) getParent();

        if (Input.keyPressed(GLFW.GLFW_KEY_W)) {
            Vector3f pos = parent.getPosition();
            pos.z -= .1f;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_S)) {
            Vector3f pos = parent.getPosition();
            pos.z += .1f;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_Q)) {
            Vector3f pos = parent.getPosition();
            pos.y -= .1f;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_E)) {
            Vector3f pos = parent.getPosition();
            pos.y += .1f;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_A)) {
            Vector3f pos = parent.getPosition();
            pos.x -= .1f;
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_D)) {
            Vector3f pos = parent.getPosition();
            pos.x += .1f;
        }

        float zoomLevel = Input.getScrollDelta();
        radius += zoomLevel;

        if (Input.buttonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            Vector2f delta = Input.getMouseDelta();
            pitch += delta.y * .01f;
            rotationAroundPerson -= delta.x * .01f;
        }

        float horizontalDistance = (float) (radius * cos(toRadians(pitch)));
        float verticalDistance = (float) (radius * sin(toRadians(pitch)));

        position = calculatePosition(horizontalDistance, verticalDistance);
    }

    private Vector3f calculatePosition(float horizontalDistance, float verticalDistance) {
        Vector3f position = new Vector3f();
        Entity3D parent = (Entity3D) getParent();

        position.y = parent.getPosition().y + verticalDistance;

        float totalRotationAroundPerson = parent.getRotation().y + rotationAroundPerson;
        float offsetX = (float) (horizontalDistance * sin(toRadians(totalRotationAroundPerson)));
        float offsetZ = (float) (horizontalDistance * cos(toRadians(totalRotationAroundPerson)));

        position.x = parent.getPosition().x - offsetX;
        position.z = parent.getPosition().z - offsetZ;

        yaw = 180 - totalRotationAroundPerson;

        return position;
    }

    @Override
    public Matrix4f getViewMatrix() {
        return VectorMath.createViewMatrix(position, new Vector3f(pitch, yaw, 0));
    }
}

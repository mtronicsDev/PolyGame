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

    private float radius = 40;
    private float pitch = 0;
    private float yaw = 180;
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

        if (Input.buttonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            Vector2f delta = Input.getMouseDelta();
            pitch += delta.y * .01f;
            yaw += delta.x * .01f;
        }

        float alpha = (float) toRadians(yaw - 90);
        float offsetXZ = (float) (cos(toRadians(pitch)) * radius);

        Vector3f camOffset = new Vector3f((float) cos(alpha) * offsetXZ,
                (float) sqrt(radius * radius - offsetXZ * offsetXZ),
                (float) sin(alpha) * offsetXZ);

        position = new Vector3f(parent.getPosition());
        position.add(camOffset);
    }

    @Override
    public Matrix4f getViewMatrix() {
        return VectorMath.createViewMatrix(position, new Vector3f(pitch, yaw, 0));
    }
}

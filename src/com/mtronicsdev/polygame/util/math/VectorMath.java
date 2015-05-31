package com.mtronicsdev.polygame.util.math;

import static java.lang.Math.tan;
import static java.lang.Math.toRadians;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class VectorMath {

    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        Matrix4f matrix4f = new Matrix4f();

        matrix4f.setIdentity();
        matrix4f.translate(translation);

        matrix4f.rotate((float) toRadians(rotation.x), new Vector3f(1, 0, 0));
        matrix4f.rotate((float) toRadians(rotation.y), new Vector3f(0, 1, 0));
        matrix4f.rotate((float) toRadians(rotation.z), new Vector3f(0, 0, 1));

        matrix4f.scale(scale);

        return matrix4f;
    }

    public static Matrix4f createProjectionMatrix(float fieldOfView, float zNear, float zFar, int width, int height) {
        float aspectRatio = (float) width / height;
        float scaleY = (1f / (float) tan(toRadians(fieldOfView / 2))) * aspectRatio;
        float scaleX = scaleY / aspectRatio;
        float depth = zFar - zNear;

        Matrix4f projection = new Matrix4f();
        projection.m00 = scaleX;
        projection.m11 = scaleY;
        projection.m22 = -((zFar + zNear) / depth);
        projection.m23 = -1;
        projection.m32 = -((2 * zNear * zFar) / depth);
        projection.m33 = 0;

        return projection;
    }

    public static Matrix4f createViewMatrix(Vector3f translation, Vector3f rotation) {
        Matrix4f view = new Matrix4f();

        view.setIdentity();
        view.rotate(rotation.x, new Vector3f(1, 0, 0));
        view.rotate(rotation.y, new Vector3f(0, 1, 0));
        view.rotate(rotation.z, new Vector3f(0, 0, 1));

        view.translate(new Vector3f(-translation.x, -translation.y, -translation.z));

        return view;
    }

}

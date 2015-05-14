package com.mtronicsdev.polygame.util;

import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class VectorMath {

    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        Matrix4f matrix4f = new Matrix4f();

        matrix4f.setIdentity();
        matrix4f.translate(translation);

        matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));

        matrix4f.scale(scale);

        return matrix4f;
    }

}

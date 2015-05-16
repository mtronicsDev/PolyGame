package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.math.Vector3f;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Material {

    private Texture texture;
    private Vector3f color;

    private float specularExponent;

    private Vector3f ambientReflectivity;
    private Vector3f diffuseReflectivity;
    private Vector3f specularReflectivity;
    private Vector3f emit;

    public Material(Texture texture, Vector3f color, Vector3f ambientReflectivity,
                    Vector3f diffuseReflectivity, Vector3f specularReflectivity, Vector3f emit, float specularExponent) {
        this.texture = texture;
        this.color = color;
        this.ambientReflectivity = ambientReflectivity;
        this.diffuseReflectivity = diffuseReflectivity;
        this.specularReflectivity = specularReflectivity;
        this.emit = emit;
        this.specularExponent = specularExponent;
    }

    public Material(Texture texture, Vector3f color) {
        this(texture, color, new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), new Vector3f(), 1);
    }

    public Material(Texture texture) {
        this(texture, new Vector3f(1, 1, 1));
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector3f getAmbientReflectivity() {
        return ambientReflectivity;
    }

    public Vector3f getDiffuseReflectivity() {
        return diffuseReflectivity;
    }

    public Vector3f getSpecularReflectivity() {
        return specularReflectivity;
    }

    public Vector3f getEmit() {
        return emit;
    }

    public float getSpecularExponent() {
        return specularExponent;
    }
}

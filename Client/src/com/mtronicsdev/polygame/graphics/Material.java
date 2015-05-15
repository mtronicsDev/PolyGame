package com.mtronicsdev.polygame.graphics;

import java.awt.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Material {

    private Texture texture;
    private Color color;

    private float specularExponent;

    private Color ambientReflectivity;
    private Color diffuseReflectivity;
    private Color specularReflectivity;
    private Color emit;

    public Material(Texture texture, Color color, Color ambientReflectivity,
                    Color diffuseReflectivity, Color specularReflectivity, Color emit, float specularExponent) {
        this.texture = texture;
        this.color = color;
        this.ambientReflectivity = ambientReflectivity;
        this.diffuseReflectivity = diffuseReflectivity;
        this.specularReflectivity = specularReflectivity;
        this.emit = emit;
        this.specularExponent = specularExponent;
    }

    public Material(Texture texture, Color color) {
        this(texture, color, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, 1);
    }

    public Material(Texture texture) {
        this(texture, Color.WHITE);
    }

    public Texture getTexture() {
        return texture;
    }

    public Color getColor() {
        return color;
    }

    public Color getAmbientReflectivity() {
        return ambientReflectivity;
    }

    public Color getDiffuseReflectivity() {
        return diffuseReflectivity;
    }

    public Color getSpecularReflectivity() {
        return specularReflectivity;
    }

    public Color getEmit() {
        return emit;
    }

    public float getSpecularExponent() {
        return specularExponent;
    }
}

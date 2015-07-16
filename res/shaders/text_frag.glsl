#version 400 core

in vec2 pass_textureCoordinates;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec4 color;

void main(void) {
    out_Color = vec4(1, 1, 1, texture(textureSampler, pass_textureCoordinates).r) * color;
}
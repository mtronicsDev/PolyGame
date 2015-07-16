#version 400 core

in vec2 pass_textureCoordinates;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec4 color;

void main(void) {
    out_Color = texture(textureSampler, pass_textureCoordinates) * color;
}
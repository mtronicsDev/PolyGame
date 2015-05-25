#version 400 core

in vec3 position;

out vec3 textureCoordinates;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1);
    textureCoordinates = position;
}
#version 400 core

in vec2 position;

out vec4 clipSpaceCoordinates;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {

    clipSpaceCoordinates = projectionMatrix * viewMatrix * transformationMatrix * vec4(position.x, 0, position.y, 1);
    gl_Position = clipSpaceCoordinates;

}
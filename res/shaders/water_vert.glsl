#version 400 core

in vec2 position;

out vec4 clipSpaceCoordinates;
out vec2 textureCoordinates;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

const float tileSize = 3;

void main(void) {

    clipSpaceCoordinates = projectionMatrix * viewMatrix * transformationMatrix * vec4(position.x, 0, position.y, 1);
    gl_Position = clipSpaceCoordinates;
    textureCoordinates = vec2((position.x + 1) / 2,(position.y + 1) / 2) * tileSize;

}
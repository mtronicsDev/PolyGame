#version 400 core

in vec2 position;

out vec4 clipSpaceCoordinates;
out vec2 textureCoordinates;
out vec3 vectorToCamera;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;

const float tileSize = 16;

void main(void) {

    vec4 worldPosition = transformationMatrix * vec4(position.x, 0, position.y, 1);
    clipSpaceCoordinates = projectionMatrix * viewMatrix * worldPosition;
    gl_Position = clipSpaceCoordinates;
    textureCoordinates = vec2((position.x + 1) / 2,(position.y + 1) / 2) * tileSize;
    vectorToCamera = normalize(cameraPosition - worldPosition.xyz);

}
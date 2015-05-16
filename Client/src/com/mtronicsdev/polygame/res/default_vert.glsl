#version 400 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 pass_textureCoordinates;
out vec3 absoluteNormal;
out vec3 vectorToLightSource;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPosition;

void main(void) {

    vec4 absolutePosition = transformationMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * absolutePosition;
    pass_textureCoordinates = textureCoordinates;

    absoluteNormal = (transformationMatrix * vec4(normal, 0)).xyz;
    vectorToLightSource = lightPosition - absolutePosition.xyz;
}
#version 400 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 pass_textureCoordinates;
out vec3 absoluteNormal;
out vec3[4] vectorToLightSource;
out vec3 vectorToCamera;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3[4] lightPosition;

uniform vec4 clipPlane;

void main(void) {

    vec4 absolutePosition = transformationMatrix * vec4(position, 1);

    gl_ClipDistance[0] = dot(absolutePosition, clipPlane);

    gl_Position = projectionMatrix * viewMatrix * absolutePosition;
    pass_textureCoordinates = textureCoordinates;

    absoluteNormal = (transformationMatrix * vec4(normal, 0)).xyz;
    for (int i = 0; i < 4; i++) {
        vectorToLightSource[i] = lightPosition[i] - absolutePosition.xyz;
    }
    vectorToCamera = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - absolutePosition.xyz;
}
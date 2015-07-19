#version 400 core

in vec4 clipSpaceCoordinates;
in vec2 textureCoordinates;
in vec3 vectorToCamera;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;

uniform float offset;

const float waveStrength = 0.05;

void main(void) {

    vec2 normalizedDeviceCoordinates = (clipSpaceCoordinates.xy / clipSpaceCoordinates.w) / 2 + .5;

    vec2 reflectionTextureCoordinates = vec2(normalizedDeviceCoordinates.x, -normalizedDeviceCoordinates.y);
    vec2 refractionTextureCoordinates = vec2(normalizedDeviceCoordinates.x, normalizedDeviceCoordinates.y);

    vec2 distortionA = (texture(dudvMap, vec2(textureCoordinates.x + offset, textureCoordinates.y)).rg * 2 - 1)
        * waveStrength;
    vec2 distortionB = (texture(dudvMap, vec2(-textureCoordinates.x + offset, textureCoordinates.y + offset)).rg * 2 - 1)
        * waveStrength;

    vec2 distortion = distortionA + distortionB;

    reflectionTextureCoordinates += distortion;
    refractionTextureCoordinates += distortion;

    vec4 reflectionColor = texture(reflectionTexture, reflectionTextureCoordinates);
    vec4 refractionColor = texture(refractionTexture, refractionTextureCoordinates);

    float refractionFactor = dot(vectorToCamera, vec3(0, 1, 0));

    out_Color = mix(reflectionColor, refractionColor, refractionFactor);

}
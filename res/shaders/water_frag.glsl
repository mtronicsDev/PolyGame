#version 400 core

in vec4 clipSpaceCoordinates;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

void main(void) {

    vec2 normalizedDeviceCoordinates = (clipSpaceCoordinates.xy / clipSpaceCoordinates.w) / 2 + .5;

    vec2 reflectionTextureCoordinates = vec2(normalizedDeviceCoordinates.x, -normalizedDeviceCoordinates.y);
    //vec2 refractionTextureCoordinates = vec2(normalizedDeviceCoordinates.xy); //Same, so I'll leave it out

    vec4 reflectionColor = texture(reflectionTexture, reflectionTextureCoordinates);
    vec4 refractionColor = texture(refractionTexture, normalizedDeviceCoordinates);

    out_Color = mix(reflectionColor, refractionColor, .5);

}
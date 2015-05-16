#version 400 core

in vec2 pass_textureCoordinates;
in vec3 absoluteNormal;
in vec3 vectorToLightSource;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main(void) {

    vec3 normalizedNormal = normalize(absoluteNormal);
    vec3 normalizedVectorToLightSource = normalize(vectorToLightSource);

    float nDotL = dot(normalizedNormal, normalizedVectorToLightSource);
    float brightness = max(nDotL, 0.0);

    vec3 diffuseLight = brightness * lightColor;

    out_Color = vec4(diffuseLight, 1.0) * texture(textureSampler, pass_textureCoordinates);
}
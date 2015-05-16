#version 400 core

in vec2 pass_textureCoordinates;
in vec3 absoluteNormal;
in vec3 vectorToLightSource;
in vec3 vectorToCamera;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float ambientLightStrength;

uniform vec3 ambientReflectivity;
uniform vec3 diffuseReflectivity;
uniform vec3 specularReflectivity;

uniform float specularExponent;

void main(void) {

    vec3 normalizedNormal = normalize(absoluteNormal);
    vec3 normalizedVectorToLightSource = normalize(vectorToLightSource);
    vec3 normalizedVectorToCamera = normalize(vectorToCamera);

    float nDotL = dot(normalizedNormal, normalizedVectorToLightSource);
    float brightness = max(nDotL, 0);

    vec3 brightnessVector = vec3(max(brightness, ambientLightStrength * ambientReflectivity.x),
        max(brightness, ambientLightStrength * ambientReflectivity.y),
        max(brightness, ambientLightStrength * ambientReflectivity.z));

    vec3 diffuseLight = brightnessVector * lightColor;

    vec3 lightDirection = -normalizedVectorToLightSource;
    vec3 reflectedLightDirection = reflect(lightDirection, normalizedNormal);

    float specularFactor = dot(reflectedLightDirection, normalizedVectorToCamera);
    specularFactor = max(specularFactor, 0);

    float dampenedSpecularFactor = pow(specularFactor, specularExponent);
    vec3 specularLight = dampenedSpecularFactor * lightColor;

    out_Color = vec4(diffuseLight * diffuseReflectivity, 1) * texture(textureSampler, pass_textureCoordinates)
        + vec4(specularLight * specularReflectivity, 1);
}
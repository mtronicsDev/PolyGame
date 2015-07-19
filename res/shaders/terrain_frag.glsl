#version 400 core

in vec2 pass_textureCoordinates;
in vec3 absoluteNormal;
in vec3[4] vectorToLightSource;
in vec3 vectorToCamera;

out vec4 out_Color;

uniform sampler2D blendMap;

uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D texture2;
uniform sampler2D texture3;

uniform vec3[4] lightColor;
uniform float ambientLightStrength;

uniform vec3 ambientReflectivity;
uniform vec3 diffuseReflectivity;
uniform vec3 specularReflectivity;

uniform float specularExponent;

void main(void) {
    vec4 blendColor = texture(blendMap, pass_textureCoordinates);

    vec2 tiledTextureCoordinates = pass_textureCoordinates * 80;

    float texture0Amount = 1 - (blendColor.r + blendColor.g + blendColor.b);
    vec4 texture0Color = texture(texture0, tiledTextureCoordinates) * texture0Amount;
    vec4 texture1Color = texture(texture1, tiledTextureCoordinates) * blendColor.r;
    vec4 texture2Color = texture(texture2, tiledTextureCoordinates) * blendColor.g;
    vec4 texture3Color = texture(texture3, tiledTextureCoordinates) * blendColor.b;

    vec4 totalColor = texture0Color + texture1Color + texture2Color + texture3Color;

    //Lighting
    vec3 normalizedNormal = normalize(absoluteNormal);
    vec3 normalizedVectorToCamera = normalize(vectorToCamera);

    vec3 totalDiffuseLight = vec3(0, 0, 0);
    vec3 totalSpecularLight = vec3(0, 0, 0);

    for (int i = 0; i < 4; i++) {
        vec3 normalizedVectorToLightSource = normalize(vectorToLightSource[i]);

        float nDotL = dot(normalizedNormal, normalizedVectorToLightSource);
        float brightness = max(nDotL, 0);

        vec3 brightnessVector = vec3(max(brightness, ambientLightStrength * ambientReflectivity.x),
            max(brightness, ambientLightStrength * ambientReflectivity.y),
            max(brightness, ambientLightStrength * ambientReflectivity.z));

        totalDiffuseLight += brightnessVector * lightColor[i];

        vec3 lightDirection = -normalizedVectorToLightSource;
        vec3 reflectedLightDirection = reflect(lightDirection, normalizedNormal);

        float specularFactor = dot(reflectedLightDirection, normalizedVectorToCamera);
        specularFactor = max(specularFactor, 0);

        float dampenedSpecularFactor = pow(specularFactor, specularExponent);
        totalSpecularLight += dampenedSpecularFactor * lightColor[i];
    }

    out_Color = vec4(totalDiffuseLight * diffuseReflectivity, 1) * totalColor
        + vec4(totalSpecularLight * specularReflectivity, 1);
}
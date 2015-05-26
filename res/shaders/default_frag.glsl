#version 400 core

in vec2 pass_textureCoordinates;
in vec3 absoluteNormal;
in vec3[4] vectorToLightSource;
in vec3 vectorToCamera;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3[4] lightColor;
uniform float ambientLightStrength;

uniform vec3 ambientReflectivity;
uniform vec3 diffuseReflectivity;
uniform vec3 specularReflectivity;

uniform float specularExponent;

void main(void) {

    vec4 textureColor = texture(textureSampler, pass_textureCoordinates);

    if(textureColor.a < .5) discard;

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

    out_Color = vec4(totalDiffuseLight * diffuseReflectivity, 1) * textureColor
        + vec4(totalSpecularLight * specularReflectivity, 1);
}
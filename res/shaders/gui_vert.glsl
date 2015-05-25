#version 400 core

in vec2 position;

out vec2 pass_textureCoordinates;

uniform mat4 transformationMatrix;

void main(void) {
    gl_Position = transformationMatrix * vec4(position, 0, 1);
    pass_textureCoordinates = vec2((position.x + 1) / 2, (position.y + 1) / 2);
}
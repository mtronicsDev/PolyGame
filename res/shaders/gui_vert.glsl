#version 400 core

in vec2 position;

out vec2 pass_textureCoordinates;

uniform vec2 offsetVector;
uniform vec2 size;

void main(void) {
    gl_Position = (vec4(offsetVector, 0, 0) + vec4(position, 0, 1) * vec4(size, 0, 1))
        * vec4(2, -2, 0, 1) + vec4(-1, 1, 0, 0); //Transform [-1;1/1;-1] to [0;1/0;1] x/y coordinates
    pass_textureCoordinates = vec2(position.x, -position.y);
}
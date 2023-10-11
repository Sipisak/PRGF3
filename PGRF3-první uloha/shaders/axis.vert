#version 330

in vec3 inPosition;
in vec3 inColor;

uniform mat4 uView;
uniform mat4 uProject;


void main() {
    color = inColor;

    vec4 pos = uProject * uView *vec4(inPosition,1.);
    gl_Position = vec4(inPosition, 1.);
}

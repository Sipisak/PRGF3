#version 330

in vec2 inPosition;

uniform mat4 uWiev;
uniform mat4 uProject;
uniform float uTime;


void main() {
    vec2 pos = inPosition* 2 - 1;
    float z = 0.5 * cos(sqrt(20 * pow(pos.x, 2) + 20 * pow(pos.y, 2)) + uTime);

    vec4 pos_MVP = uProject * uView * vec4(pos, z, 1.);
    gl_Position = pos_MVP;
}

#version 330

in vec2 inPosition;

uniform mat4 uView;
uniform mat4 uProj;
uniform mat4 uTransform;

void main() {
    float radius = 1.0;

    float theta = inPosition.x * 6.28;
    float phi = inPosition.y * 3.14;

    float x = radius * sin(phi) * cos(theta);
    float y = radius * sin(phi) * sin(theta);
    float z = radius * cos(phi);

    vec4 pos = uProj * uView * uTransform * vec4(x, y, z, 1.0);
    gl_Position = pos;
}

#version 330 core

in vec2 inPosition;
in vec3 inColor;

out vec3 color;
out vec2 texCoords;

uniform mat4 uView;
uniform mat4 uProj;
uniform float uTime;

void main() {
    // výpočet sloní hlavy na grid
    float zenit = inPosition.x * Math.PI;
    float azimut = inPosition.y * 2 * Math.PI;

    float R = 3 + cos(4 * azimut);

    float x = R * sin(zenit) * cos(azimut);
    float y = R * sin(zenit) * cos(zenit);
    float z = R * cos(zenit);

    color = inColor;
//    // Převeď 2D pozici na 3D pozici
//    float theta = inPosition.y * 3.14159265359; // Parametr θ
//    float phi = inPosition.x * 6.28318530718; // Parametr ϕ
//    float x = sin(theta) * cos(phi);
//    float y = sin(theta) * sin(phi);
//    float z = cos(theta);

    gl_Position = uProj * uView * vec4(x, y, z, 1.);
}

#version 330

layout(location = 0) in float theta;
layout(location = 1) in float phi;

uniform mat4 uView;
uniform mat4 uProj;

void main(){
    float x = sin(theta) * cos(phi);
    float y = sin(theta) * sin(phi);
    float z = cos(theta) * 3.0 * cos(4.0 * phi);

    vec4 pos_MPV = uProj * uView * vec4(x, y, z ,1.0);
    gl_Position = pos_MPV;
}

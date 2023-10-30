#version 330

in vec2 inPosition;

uniform mat4 model;
uniform mat4 uView;
uniform mat4 uProj;

out vec3 normal;

void main() {

    vec2 pos = inPosition * 2 - 1;
    float z = 0.5 * cos(sqrt(20 * pow(pos.x, 2) + 20 * pow(pos.y, 2)) + uTime);

    vec4 modelPosition = model * vec4(pos, z, 1.0);
    vec4 viewPosition = uView * modelPosition;
    vec4 clipPosition = uProj * viewPosition;

    gl_Position = clipPosition;


    normal = normalize(mat3(model) * vec3(pos, z));
}

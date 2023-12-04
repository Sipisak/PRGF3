#version 330

in vec2 inPosition;

uniform float uTime;
uniform mat4 uView;
uniform mat4 uProj;

out vec4 vertColor;
out vec2 texCoord;

const float PI = 3.14159;

vec3 sphere2cartezian(float r, float a, float z){
    return vec3(
    r * sin(z) * cos(a),
    r * sin(z) * sin(a),
    r * cos(z)
    );
}

vec3 fce(float a, float z){
    float r = 3 + cos(4 * a);
    return vec3(r / 3, a, z) ;
}

void main() {
    vec3 position = vec3(inPosition, 0.f);

    float a = position.x * PI * 2;
    float z = position.y * PI;
    vec3 pos1 = sphere2cartezian(1.f, a, z);
    vec3 pos = fce(a, z);
    vec3 pos2 = sphere2cartezian(pos.x, pos.y, pos.z);

    float alpha = (sin(uTime / 500.) + 1) / 2.;
    vec3 result = mix(pos1, pos2, alpha);
    gl_Position = uProj * uView * vec4(pos1, 1.0);

    vec4 color1 = vec4(0.9f, 0.9f, 0.f, 1.f);
    vec4 color2 = vec4(0.9f, 0.f, 0.9f, 1.f);
    vertColor = mix(color1, color2, alpha);

    texCoord = inPosition;
}

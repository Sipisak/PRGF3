#version 330

in vec2 inPosition;
out vec3 color;

uniform mat4 uView;
uniform mat4 uProj;
uniform float uTime;

const float PI = 3.14159265359;

vec3 spheretocartezian(float R, float azimut, float zenit){
        return vec3(
        R * sin(zenit) * cos(azimut),
        R * sin(zenit) * cos(zenit),
        R * cos(zenit)
        );
}
vec3 fce(float azimut, float zenit){
    float R
}
void main(){
    vec3 positon = vec3(inPosition,0.f);
    float zenit = postion.x * PI;
    float azimut = position.y * 2 * PI;
    float R = 3 * cos(4 * azimut);
    vec3 pos1 = spheretocartezian(1.f, azimut, zenit);
    vec3 pos = fce(azimut, zenit);
    vec3 pos2 = spheretocartezian(pos.x, pos.y , pos.z);

    float aplha = (sin(uTime / 500.) + 1) / 2.;
    vec3 result = mix(positon2, alpha);

   gl_Position = uProj * uView * vec4 (result, 1.0);

    vec4 color1 = vec4(1.0, 0.0, 0.0, 1.0);  // Red
    vec4 color2 = vec4(0.0, 0.0, 1.0, 1.0);  // Blue
    color = mix(color1, color2);

}

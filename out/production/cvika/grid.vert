#version 330

in vec2 inPosition;

uniform mat4 uView;
uniform mat4 uProj;
uniform float uTime;

out vec3 toLightVector;
out vec3 normal;
out vec2 texCoord;
out vec3 debugColor;

vec4 lightPos_W = vec4(0.f, 0.f, 0.4f, 1.f);

vec3 getNormal(){
    return vec3(0 ,0 ,1);
}

void main() {
    texCoord = inPosition;
    vec2 pos = inPosition * 2 - 1;
    float z = 0.5 * cos(sqrt(20 * pow(pos.x, 2) + 20 * pow(pos.y, 2)) + uTime);
    //float z = 0.f;

    //phong
    vec4 pos_W = vec4(pos, z , 1.);
    vec4 pos_V = uView * pos_W;
    vec4 lightPos_V = uView * lightPos_W;
    toLightVector = lightPos_V.xyz - pos_V.xyz;
    normal = transpose(inverse(mat3(uView))) * getNormal();

    debugColor = getNormal();
    //final position
    vec4 pos_MVP = uProj * uView * vec4(pos, z, 1.);
    gl_Position = pos_MVP;
}

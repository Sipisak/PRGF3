//#version 330
//
//in vec2 inPosition;
//
//uniform mat4 uView;
//uniform mat4 uProj;
//uniform float uTime;
//uniform vec3 u_LightPos;
//
//out vec3 toLightVector;
//out vec3 normal;
//out vec2 texCoord;
//out vec3 debugColor;
//out float lightDistance;
//
//vec3 getNormal(float theta, float phi) {
//    float x = 3.0 * cos(4.0 * phi) * sin(theta);
//    float y = 3.0 * cos(4.0 * phi) * cos(theta);
//    float z = 3.0 * sin(4.0 * phi);
//    return normalize(vec3(x, y, z));
//}
//
//void main() {
//    texCoord = inPosition;
//    vec2 pos = inPosition * 2 - 1;  // <-1,1>
//    //float z = 0.5 * cos(sqrt(20 * pow(pos.x, 2) + 20 * pow(pos.y, 2)) + uTime);
//
//    float theta = uTime;
//    float phi = 2.0 * uTime;
//
//    vec4 pos_W = vec4(3.0 * sin(phi) * cos(theta), 3.0 * sin(phi) * sin(theta), 3.0 * cos(phi), 1.0);
//    vec4 pos_V = uView * pos_W;
//    vec4 lightPos_V = uView * vec4(u_LightPos, 1.0);
//    toLightVector = lightPos_V.xyz - pos_V.xyz;
//    normal = transpose(inverse(mat3(uView))) * getNormal(theta, phi);
//    lightDistance = length(toLightVector);
//
//    debugColor = vec3(pos, 0.0);
//
//    // Final position
//    vec4 pos_MVP = uProj * pos_V;
//    gl_Position = pos_MVP;
//}

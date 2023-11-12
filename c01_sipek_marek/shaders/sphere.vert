#version 330

in vec2 inPosition;
out vec2 texCoord;

uniform mat4 uView;
uniform mat4 uProj;
uniform mat4 uTransform;

uniform float uTime;

out vec3 FragPos; // Pozice fragmentu ve světových souřadnicích
out vec3 Normal;  // Normála fragmentu

void main() {
    float radius = 1.0;

    // Decrease the value of theta to shift the shape to the left
    float theta = (inPosition.x - uTime / 2) * 6.28;

    float phi = inPosition.y * 3.14;

    float x = radius * sin(phi) * cos(theta);
    float y = radius * sin(phi) * sin(theta);
    float z = radius * cos(phi);

    vec3 position = vec3(x, y, z);
    FragPos = vec3(uTransform * vec4(position, 1.0));
    Normal = normalize(mat3(transpose(inverse(uTransform))) * vec3(x, y, z));

    vec4 pos = uProj * uView * uTransform * vec4(position, 1.0);
    gl_Position = pos;

    texCoord = inPosition;
}

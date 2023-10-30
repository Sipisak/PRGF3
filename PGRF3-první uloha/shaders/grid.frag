#version 330
in vec3 toLightVector;
in vec3 normal;
out vec4 outColor;

vec4 baseColor = vec4(0.2f, 0.7f, 0.f, 1.f);
vec4 ambientColor = vec4(0.9f, 0.4f, 0.f, 1.f);
vec4 diffuseColor = vec4(0.9f, 0.9f, 0.9f, 1.f);


void main() {

    //diffuse
    vec3 l = normalize(toLightVector);
    vec3 n =normalize(normal);
    float NDotl = max(dot(n, l), 0.);

    vec4 ambient = ambientColor;
    vec4 diffuse = NDotl * diffuseColor;
    vec4 specular = vec4(0);

    outColor = vec4(ambient + diffuse + specular);
}


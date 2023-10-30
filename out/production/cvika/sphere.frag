#version 330

in vec3 normal;

out vec4 fragColor;

uniform vec3 lightColor;
uniform vec3 objectColor;
uniform vec3 lightPos;

void main() {

    vec3 lightDir = normalize(lightPos - gl_FragCoord.xyz);


    float diff = max(dot(normal, lightDir), 0.0);


    vec3 result = (diff * lightColor * objectColor);

    fragColor = vec4(result, 1.0);
}

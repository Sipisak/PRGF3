//#version 330
//
//in vec3 toLightVector;
//in vec3 normal;
//in vec2 texCoord;
//in vec3 debugColor;
//in float lightDistance;
//
//uniform sampler2D textureBricks;
//out vec4 outColor;
//
//vec4 ambientColor = vec4(0.9f, 0.4f, 0.0f, 1.0);
//vec4 diffuseColor = vec4(0.9f, 0.9f, 0.9f, 1.0);
//vec4 baseColor;
//
//float constantAttenuation = 0.4;
//float linearAttenuation = 0.1;
//float quadraticAttenuation = 0.001;
//
//void main() {
//    vec4 textureColor = texture(textureBricks, texCoord);
//    baseColor = textureColor;
//
//    vec3 l = normalize(toLightVector);
//    vec3 n = normalize(normal);
//    float NDotL = max(dot(n, l), 0.0);
//
//    float attenuation = 1.0 / (constantAttenuation + linearAttenuation * lightDistance + quadraticAttenuation * lightDistance * lightDistance);
//
//    vec4 ambient = ambientColor;
//    vec4 diffuse = NDotL * diffuseColor;
//    vec4 specular = vec4(0.0);
//
//    outColor = baseColor * (ambient + attenuation * (diffuse + specular));
//}

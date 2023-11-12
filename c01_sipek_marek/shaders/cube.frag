#version 330

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D textureSampler; // Use this if you want to apply a texture
uniform vec3 color; // Use this if you want a solid color

void main() {
    // Use either texture or color
    // fragColor = texture(textureSampler, texCoord);
    fragColor = vec4(color, 1.0);
}

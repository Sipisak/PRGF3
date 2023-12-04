#version 330
in vec4 vertColor;
in vec2 texCoord;
out vec4 color;
uniform sampler2D textureBricks;


void main() {
    //color = texture(textureBricks, texCoord);
    color = vec4(vertColor);
}


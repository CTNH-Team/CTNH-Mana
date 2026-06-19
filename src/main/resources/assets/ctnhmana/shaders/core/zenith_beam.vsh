#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 texCoord;
out vec3 localPos;
out float heightRatio;

void main() {
    vec4 worldPos = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * worldPos;
    texCoord = UV0;
    localPos = Position;
    heightRatio = clamp(Position.y / 512.0, 0.0, 1.0);
}

#version 150

in vec2 texCoord;
in vec3 localPos;
in float heightRatio;

uniform float Time;
uniform vec3 BeamColor;
uniform float BeamAlpha;

out vec4 fragColor;

#define PI 3.14159265359

float hash31(vec3 p) {
    return fract(sin(dot(p, vec3(127.1, 311.7, 74.7))) * 43758.5453123);
}

float noise(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    return mix(
        mix(mix(hash31(i + vec3(0.0, 0.0, 0.0)), hash31(i + vec3(1.0, 0.0, 0.0)), f.x),
            mix(hash31(i + vec3(0.0, 1.0, 0.0)), hash31(i + vec3(1.0, 1.0, 0.0)), f.x), f.y),
        mix(mix(hash31(i + vec3(0.0, 0.0, 1.0)), hash31(i + vec3(1.0, 0.0, 1.0)), f.x),
            mix(hash31(i + vec3(0.0, 1.0, 1.0)), hash31(i + vec3(1.0, 1.0, 1.0)), f.x), f.y),
        f.z
    );
}

float fbm(vec3 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i = 0; i < 4; i++) {
        v += noise(p) * a;
        p *= 2.0;
        a *= 0.5;
    }
    return v;
}

void main() {
    float t = Time * 2.0;

    // 圆柱坐标
    float r = length(localPos.xz);
    float angle = atan(localPos.z, localPos.x);
    float y = localPos.y;

    // 垂直向上的能量流
    float flow = fbm(vec3(angle * 2.0, y * 0.05 - t * 0.3, t * 0.1));

    // 螺旋结构
    float spiral = sin(angle * 6.0 + y * 0.08 - t * 0.5);
    float spiralMask = smoothstep(0.3, 0.7, spiral) * smoothstep(0.0, 0.4, r);

    // 径向衰减：中心亮，边缘暗但带光晕
    float coreGlow = exp(-r * r * 8.0);
    float midGlow = exp(-r * r * 2.0) * 0.6;
    float edgeGlow = exp(-pow(max(0.0, r - 0.5), 2.0) * 20.0) * 0.4;

    // 高度衰减：顶部略淡，模拟能量消散
    float heightFade = 1.0 - pow(heightRatio, 3.0) * 0.4;

    // 脉动
    float pulse = 1.0 + 0.25 * sin(t * 0.4 + y * 0.02);

    // 合成颜色
    vec3 energy = BeamColor * (coreGlow + midGlow + edgeGlow + spiralMask * 0.5 + flow * 0.25);
    energy *= heightFade * pulse;

    // alpha：核心不透明，边缘淡出
    float alpha = BeamAlpha * (coreGlow * 0.9 + midGlow * 0.5 + edgeGlow * 0.3);
    alpha = clamp(alpha, 0.0, 1.0);

    fragColor = vec4(energy, alpha);
}

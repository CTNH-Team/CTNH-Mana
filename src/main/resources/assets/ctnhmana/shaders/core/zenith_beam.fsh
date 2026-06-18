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
    // 稳定但缓慢变化的时间，用于产生“能量涌动”而非“呼吸抖动”
    float t = Time * 0.5;

    // 圆柱坐标
    float r = length(localPos.xz);
    float angle = atan(localPos.z, localPos.x);
    float y = localPos.y;

    // 缓慢旋转的亚空间裂缝螺旋
    float spiralPhase = angle * 5.0 + y * 0.06 - t * 0.15;
    float spiral = sin(spiralPhase);
    float spiralTear = smoothstep(0.55, 0.85, abs(spiral)) * smoothstep(0.0, 0.35, r);

    // 多股能量流，从裂缝中向上涌动
    float stream = fbm(vec3(angle * 3.0, y * 0.04 - t * 0.12, t * 0.05));
    float streamSharp = pow(stream, 2.0) * smoothstep(0.0, 0.45, r);

    // 裂缝边缘的撕裂辉光：越靠近圆柱侧面越亮
    float tearGlow = exp(-pow(max(0.0, r - 0.42), 2.0) * 60.0);

    // 核心：稳定的亚空间能量柱
    float core = exp(-r * r * 12.0);

    // 中层能量晕
    float mid = exp(-r * r * 3.0) * 0.5;

    // 高度衰减：顶部像被吸入裂缝般消散
    float topFade = 1.0 - smoothstep(0.7, 1.0, heightRatio);
    float bottomBurst = 1.0 - smoothstep(0.0, 0.08, heightRatio);

    // 能量强度合成
    float intensity = core + mid + streamSharp * 0.7 + spiralTear * 0.6 + tearGlow * 0.8;
    intensity *= topFade;
    intensity *= 1.0 + bottomBurst * 0.5;

    // 颜色：主体 BeamColor + 裂缝边缘更亮的粉白高光
    vec3 coreColor = BeamColor;
    vec3 tearColor = vec3(1.0, 0.7, 0.95);
    vec3 color = mix(coreColor, tearColor, clamp(tearGlow + spiralTear * 0.5, 0.0, 1.0));
    color *= intensity;

    // alpha：核心实心，边缘随能量流淡出
    float alpha = BeamAlpha * clamp(core * 0.95 + mid * 0.6 + streamSharp * 0.4 + tearGlow * 0.5, 0.0, 1.0);
    alpha *= topFade;

    fragColor = vec4(color, alpha);
}

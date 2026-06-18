#version 150

in vec2 texCoord;
in vec3 localPos;
in float heightRatio;

uniform float Time;
uniform vec3 BeamColor;
uniform float BeamAlpha;

out vec4 fragColor;

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
    float t = Time * 0.5;

    float r = length(localPos.xz);
    float angle = atan(localPos.z, localPos.x);
    float y = localPos.y;

    // 亚空间裂缝螺旋
    float spiralPhase = angle * 5.0 + y * 0.06 - t * 0.15;
    float spiral = sin(spiralPhase);
    float spiralTear = smoothstep(0.55, 0.85, abs(spiral)) * smoothstep(0.0, 0.5, r);

    // 向上涌动的能量流
    float stream = fbm(vec3(angle * 3.0, y * 0.04 - t * 0.12, t * 0.05));
    float streamSharp = pow(stream, 2.0) * smoothstep(0.0, 0.55, r);

    // 内部电弧
    float arc = fbm(vec3(angle * 8.0, y * 0.15 - t * 0.4, t * 0.2));
    float arcSharp = pow(smoothstep(0.45, 0.85, arc), 3.0) * smoothstep(0.0, 0.45, r);

    // 裂缝边缘撕裂辉光
    float tearGlow = exp(-pow(max(0.0, r - 0.48), 2.0) * 40.0);

    // 核心与中层：衰减更慢，让外部也保持亮度
    float core = exp(-r * r * 6.0);
    float mid = exp(-r * r * 1.8) * 0.85;
    float outer = exp(-r * r * 0.6) * 0.35;

    // 高度衰减
    float topFade = 1.0 - smoothstep(0.7, 1.0, heightRatio);
    float bottomBurst = 1.0 - smoothstep(0.0, 0.08, heightRatio);

    // 能量强度：整柱都有较高亮度
    float intensity = core * 2.0 + mid * 1.8 + outer * 1.2 + streamSharp * 1.0 + arcSharp * 1.2 + spiralTear * 0.9 + tearGlow * 1.2;
    intensity *= topFade;
    intensity *= 1.0 + bottomBurst * 0.5;
    intensity = min(intensity, 1.4);

    // 颜色：核心白热，主体深紫高饱和，边缘粉白撕裂
    vec3 coreColor = vec3(1.0, 0.9, 1.0);
    vec3 midColor = BeamColor * 1.7;
    vec3 tearColor = vec3(1.0, 0.6, 0.95);

    vec3 color = mix(midColor, coreColor, clamp(core * 1.5, 0.0, 1.0));
    color = mix(color, tearColor, clamp(tearGlow + spiralTear * 0.6 + arcSharp, 0.0, 1.0));
    color *= intensity;

    // 全柱接近完全不透明，只在最边缘微淡出
    float edgeFade = smoothstep(0.7, 0.0, r);
    float alpha = clamp(core + mid * 0.9 + outer * 0.7 + streamSharp * 0.7 + arcSharp * 0.8 + tearGlow * 0.7, 0.0, 1.0);
    alpha = mix(alpha, 1.0, core * 0.7 + mid * 0.4);
    alpha *= topFade;
    alpha = clamp(alpha * BeamAlpha, edgeFade, 1.0);

    fragColor = vec4(color, alpha);
}

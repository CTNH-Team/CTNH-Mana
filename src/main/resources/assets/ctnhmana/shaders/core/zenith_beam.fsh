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
    float spiralTear = smoothstep(0.55, 0.85, abs(spiral)) * smoothstep(0.0, 0.35, r);

    // 向上涌动的能量流
    float stream = fbm(vec3(angle * 3.0, y * 0.04 - t * 0.12, t * 0.05));
    float streamSharp = pow(stream, 2.0) * smoothstep(0.0, 0.45, r);

    // 内部电弧
    float arc = fbm(vec3(angle * 8.0, y * 0.15 - t * 0.4, t * 0.2));
    float arcSharp = pow(smoothstep(0.45, 0.85, arc), 3.0) * smoothstep(0.0, 0.3, r);

    // 裂缝边缘撕裂辉光
    float tearGlow = exp(-pow(max(0.0, r - 0.42), 2.0) * 60.0);

    // 极亮核心
    float core = exp(-r * r * 22.0);

    // 中层能量晕
    float mid = exp(-r * r * 5.0) * 0.7;

    // 高度衰减
    float topFade = 1.0 - smoothstep(0.7, 1.0, heightRatio);
    float bottomBurst = 1.0 - smoothstep(0.0, 0.08, heightRatio);

    // 能量强度
    float intensity = core * 3.0 + mid * 1.6 + streamSharp * 1.2 + arcSharp * 1.5 + spiralTear * 1.0 + tearGlow * 1.4;
    intensity *= topFade;
    intensity *= 1.0 + bottomBurst * 0.5;
    // 限制峰值避免近处过曝成纯白
    intensity = min(intensity, 1.6);

    // 颜色：核心白热，中层深紫，边缘粉白撕裂
    vec3 coreColor = vec3(1.0, 0.85, 1.0);
    vec3 midColor = BeamColor * 1.5;
    vec3 tearColor = vec3(1.0, 0.55, 0.95);

    vec3 color = mix(midColor, coreColor, clamp(core * 3.0, 0.0, 1.0));
    color = mix(color, tearColor, clamp(tearGlow + spiralTear * 0.7 + arcSharp, 0.0, 1.0));
    color *= intensity;

    // 全柱接近完全不透明，只在最边缘微淡出以避免锯齿
    float edgeFade = smoothstep(0.65, 0.0, r);
    float alpha = clamp(core * 1.2 + mid * 1.0 + streamSharp * 0.8 + arcSharp * 0.9 + tearGlow * 0.8, 0.0, 1.0);
    alpha = mix(alpha, 1.0, core * 0.85);
    alpha *= topFade;
    alpha = clamp(alpha * BeamAlpha, edgeFade, 1.0);

    fragColor = vec4(color, alpha);
}

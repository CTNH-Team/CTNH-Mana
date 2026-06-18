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

    // 强对比螺旋裂缝
    float spiralPhase = angle * 5.0 + y * 0.08 - t * 0.2;
    float spiral = sin(spiralPhase);
    float spiralTear = smoothstep(0.45, 0.85, abs(spiral)) * smoothstep(0.0, 0.5, r);

    // 向上涌动的能量流
    float stream = fbm(vec3(angle * 3.0, y * 0.05 - t * 0.15, t * 0.05));
    float streamSharp = pow(stream, 2.5);

    // 内部电弧
    float arc = fbm(vec3(angle * 9.0, y * 0.18 - t * 0.5, t * 0.25));
    float arcSharp = pow(smoothstep(0.4, 0.85, arc), 4.0);

    // 高度衰减
    float topFade = 1.0 - smoothstep(0.7, 1.0, heightRatio);
    float bottomBurst = 1.0 - smoothstep(0.0, 0.08, heightRatio);

    // 基础管状亮度（保证外部也能看到），细节在此基础上做加减
    float baseGlow = 0.55;
    float coreBoost = exp(-r * r * 8.0) * 0.6;
    float detail = spiralTear * 0.9 + streamSharp * 0.7 + arcSharp * 1.0;
    float intensity = baseGlow + coreBoost + detail;
    intensity *= topFade;
    intensity *= 1.0 + bottomBurst * 0.5;
    intensity = min(intensity, 1.5);

    // 颜色：基础饱和紫，细节处混入亮粉/白热
    vec3 baseColor = BeamColor * 1.6;
    vec3 coreColor = vec3(1.0, 0.9, 1.0);
    vec3 detailColor = vec3(1.0, 0.55, 0.95);

    vec3 color = mix(baseColor, coreColor, clamp(coreBoost * 2.0, 0.0, 1.0));
    // 细节在颜色上做高亮，不只是亮度
    color = mix(color, detailColor, clamp(detail * 0.7, 0.0, 1.0));
    color *= intensity;

    // 接近完全不透明
    float alpha = clamp(0.92 + coreBoost * 0.3 + detail * 0.25, 0.0, 1.0);
    alpha *= topFade;
    alpha = clamp(alpha * BeamAlpha, 0.0, 1.0);

    fragColor = vec4(color, alpha);
}

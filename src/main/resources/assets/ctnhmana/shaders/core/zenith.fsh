#version 150

in vec3 localDir;

uniform float GameTime;
uniform vec2 CameraYawPitch;

out vec4 fragColor;

#define PI 3.14159265359

// ================= 噪声与基础函数 =================
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
    for (int i = 0; i < 5; i++) {
        v += noise(p) * a;
        p *= 2.0;
        a *= 0.5;
    }
    return v;
}

// 锯齿波
float tri(float x) {
    return abs(fract(x) - 0.5) * 2.0 - 0.5;
}

// ================= 旋转 =================
vec3 rotateX(vec3 p, float a) {
    float c = cos(a), s = sin(a);
    return vec3(p.x, c*p.y - s*p.z, s*p.y + c*p.z);
}

vec3 rotateY(vec3 p, float a) {
    float c = cos(a), s = sin(a);
    return vec3(c*p.x + s*p.z, p.y, -s*p.x + c*p.z);
}

vec3 rotateCameraToWorld(vec3 p, vec2 yawPitch) {
    p = rotateX(p, -yawPitch.y);
    p = rotateY(p, -yawPitch.x);
    return normalize(p);
}

// ================= 主函数 =================
void main() {

    float t = GameTime * 3.0;

    vec2 cam = radians(CameraYawPitch);
    vec3 dir = rotateCameraToWorld(normalize(localDir), cam);

    vec2 uv = dir.xz / max(dir.y, 0.02);

    // ================= 背景 =================
    float heightFactor = smoothstep(-0.1, 0.5, dir.y);
    vec3 baseSkyColor = vec3(0.02, 0.0, 0.05) * heightFactor;
    float baseAlpha = 0.8 * heightFactor;

    // ================= 裂缝 UV =================
    float angle = 0.6;
    float s = sin(angle), c = cos(angle);
    vec2 riftUV = vec2(c * uv.x - s * uv.y, s * uv.x + c * uv.y);

    float y = riftUV.y;

    // ================= 🔥 强化锯齿裂痕 =================
    float zigzag = 0.0;

    zigzag += tri(y * 3.0 + fbm(vec3(y, t * 0.2, 0.0)) * 2.0) * 0.7;
    zigzag += tri(y * 7.0 - t * 0.6) * 0.35;
    zigzag += tri(y * 15.0 + sin(t + y * 10.0)) * 0.18;

    float stepBreak = floor(y * 25.0) * 0.03;
    zigzag += tri(stepBreak + t * 0.5) * 0.25;

    zigzag += (fbm(vec3(y * 8.0, t * 1.2, 0.0)) - 0.5) * 0.4;

    float organic = fbm(vec3(y * 4.0, t * 0.3, 0.0)) * 0.3;

    float warpedX = riftUV.x + zigzag + organic;

    float riftSDF = length(vec2(warpedX * 0.4, y * 2.0));

    float riftMask = 1.0 - smoothstep(1.0, 1.15, riftSDF);
    float sharpGlow = smoothstep(0.9, 1.0, riftSDF) *
    (1.0 - smoothstep(1.0, 1.08, riftSDF));
    float softGlow = smoothstep(0.7, 1.0, riftSDF) *
    (1.0 - smoothstep(1.0, 1.5, riftSDF));

    // ================= 裂缝内部空间 =================
    vec3 dimSpaceColor =
    vec3(0.1, 0.0, 0.3) +
    fbm(dir * 12.0 - vec3(0.0, t * 0.6, 0.0)) *
    vec3(0.8, 0.1, 1.0);

    dimSpaceColor *= 1.5;

    // ================= 🌌 动态星云 =================
    vec2 eyeUV = uv;
    float dEye = length(eyeUV);

    float swirlAngle = dEye * 1.5 - t * 0.4;
    float sa = sin(swirlAngle), ca = cos(swirlAngle);
    vec2 swirlUV = vec2(ca * eyeUV.x - sa * eyeUV.y,
    sa * eyeUV.x + ca * eyeUV.y);

    // 🔥 动态流体星云
    vec2 flow = vec2(
    fbm(vec3(swirlUV * 2.0, t * 0.2)),
    fbm(vec3(swirlUV * 2.0 + 5.2, t * 0.2))
    );

    swirlUV += (flow - 0.5) * 0.6;

    vec3 fogCoord = vec3(swirlUV * 3.5, t * 0.35);

    float fog = fbm(fogCoord);
    fog += fbm(fogCoord * 2.2 + vec3(flow * 2.0, t * 0.1)) * 0.5;
    fog += sin(fogCoord.x * 10.0 + t * 2.0) * 0.03;

    vec3 nebulaColor =
    mix(vec3(0.1, 0.0, 0.4),
    vec3(0.9, 0.3, 1.0),
    fog * 0.9);

    float irisMask = 1.0 - smoothstep(0.6, 1.5, dEye);

    float dPupil = abs(eyeUV.x * 5.0) + abs(eyeUV.y * 1.2);
    float pupilMask = 1.0 - smoothstep(0.28, 0.35, dPupil);

    vec3 eyeColor = mix(dimSpaceColor, nebulaColor, irisMask);
    eyeColor = mix(eyeColor, vec3(0.0), pupilMask * irisMask);

    // ================= 合成 =================
    vec3 finalColor = baseSkyColor;
    float finalAlpha = baseAlpha;

    if (riftMask > 0.0) {
        finalColor = mix(finalColor, eyeColor, riftMask);
        finalAlpha = mix(finalAlpha, 1.0, riftMask);
    }

    vec3 glowColor = vec3(0.9, 0.5, 1.0);
    finalColor += glowColor * sharpGlow * 3.0;
    finalColor += glowColor * softGlow * 1.5;

    finalAlpha = clamp(finalAlpha + sharpGlow + softGlow, 0.0, 1.0);
    finalAlpha *= smoothstep(-0.05, 0.1, dir.y);

    fragColor = vec4(finalColor, finalAlpha);
}
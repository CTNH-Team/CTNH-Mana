#version 150

in vec3 localDir;
in vec2 texCoord;

uniform float Time;

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

// ================= 主函数 =================
void main() {

    float t = Time * 3.0;

    vec2 uv = texCoord * 4.0;

    // ================= 背景 =================
    float heightFactor = smoothstep(0.0, 0.8, 1.0 - length(uv) * 0.22);
    vec3 baseSkyColor = vec3(0.02, 0.0, 0.05) * heightFactor;
    float baseAlpha = 0.8 * heightFactor;

    // ================= 裂缝 UV =================
    float angle = 0.6;
    float s = sin(angle), c = cos(angle);
    vec2 riftUV = vec2(c * uv.x - s * uv.y, s * uv.x + c * uv.y);

    float y = riftUV.y;

    // ================= 🔥 强化裂缝边缘 =================
    float zigzag = 0.0;

    zigzag += tri(y * 3.0 + fbm(vec3(y, t * 0.2, 0.0)) * 2.0) * 0.4;
    zigzag += tri(y * 7.0 + sin(t * 0.4)) * 0.2;
    zigzag += tri(y * 15.0 + sin(t + y * 10.0)) * 0.08;

    float stepBreak = floor(y * 25.0) * 0.03;
    zigzag += tri(stepBreak + t * 0.3) * 0.12;

    zigzag += (fbm(vec3(y * 8.0, t * 0.8, 0.0)) - 0.5) * 0.2;

    float organic = fbm(vec3(y * 4.0, t * 0.25, 0.0)) * 0.18;
    float sideMask = smoothstep(0.35, 0.95, abs(riftUV.x));
    float sideWave = fbm(vec3(abs(riftUV.x) * 4.0, y * 4.0, t * 0.45)) - 0.5;
    float sideFlow = sin(t * 0.9 + y * 6.0 + sideWave * 4.0) * 0.08;
    float sideRipple = fbm(vec3(abs(riftUV.x) * 7.0, y * 6.5, t * 0.7)) - 0.5;
    float sideZigzag = (sideWave * 0.35 + sideFlow + sideRipple * 0.25) * sideMask;

    float warpedX = riftUV.x + zigzag + organic + sideZigzag;

    // 让上下边缘带一点眼睑弧度，越靠近眼角越向中间收
    float eyelidCurve = pow(abs(warpedX) * 0.28, 2.0) * 0.55;
    float curvedY = y + sign(y) * eyelidCurve;
    float riftSDF = length(vec2(warpedX * 0.3, curvedY * 1.8));

    float riftMask = 1.0 - smoothstep(1.0, 1.15, riftSDF);
    float sharpGlow = smoothstep(0.9, 1.0, riftSDF) *
    (1.0 - smoothstep(1.0, 1.08, riftSDF));
    float softGlow = smoothstep(0.7, 1.0, riftSDF) *
    (1.0 - smoothstep(1.0, 1.5, riftSDF));

    // ================= 裂缝内部空间 =================
    vec3 dimSpaceColor =
    vec3(0.1, 0.0, 0.3) +
    fbm(vec3(uv * 4.0, 0.0) - vec3(0.0, t * 0.6, 0.0)) *
    vec3(0.8, 0.1, 1.0);

    dimSpaceColor *= 1.5;

    // ================= 🌌 动态星云 =================
    float gazeStep = floor(t * 0.18);
    float gazePhase = fract(t * 0.18);
    vec2 gazeFrom = vec2(
    hash31(vec3(gazeStep, 1.7, 0.0)) - 0.5,
    hash31(vec3(gazeStep, 5.3, 0.0)) - 0.5
    );
    vec2 gazeTo = vec2(
    hash31(vec3(gazeStep + 1.0, 1.7, 0.0)) - 0.5,
    hash31(vec3(gazeStep + 1.0, 5.3, 0.0)) - 0.5
    );
    float gazeHold = smoothstep(0.72, 0.92, gazePhase);
    vec2 gaze = mix(gazeFrom, gazeTo, gazeHold) * vec2(0.34, 0.22);
    gaze += vec2(
    hash31(vec3(gazeStep, 2.1, 0.0)) - 0.5,
    hash31(vec3(gazeStep, 7.9, 0.0)) - 0.5
    ) * 0.008 * (1.0 - gazeHold);

    vec2 eyeUV = uv;
    vec2 irisUV = eyeUV - gaze * 0.28;
    float dEye = length(irisUV);

    float swirlAngle = dEye * 1.5 - t * 0.4;
    float sa = sin(swirlAngle), ca = cos(swirlAngle);
    vec2 swirlUV = vec2(ca * irisUV.x - sa * irisUV.y,
    sa * irisUV.x + ca * irisUV.y);

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

    float irisMask = 1.0 - smoothstep(0.58, 1.42, dEye);

    vec2 pupilUV = eyeUV - gaze * 0.65;
    float dPupil = abs(pupilUV.x * 4.2) + abs(pupilUV.y * 1.6);
    float pupilMask = 1.0 - smoothstep(0.24, 0.36, dPupil);

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

    fragColor = vec4(finalColor, finalAlpha);
}

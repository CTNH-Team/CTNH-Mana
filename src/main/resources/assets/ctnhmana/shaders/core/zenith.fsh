#version 150

in vec3 localDir;

uniform float GameTime;
uniform vec2 CameraYawPitch;

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
    mix(hash31(i + vec3(0.0, 1.0, 1.0)), hash31(i + vec3(1.0, 1.0, 1.0)), f.x), f.y), f.z
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

float tri(float x) {
    return abs(fract(x) - 0.5) * 2.0 - 0.5;
}

vec3 rotateX(vec3 p, float a) {
    float c = cos(a), s = sin(a);
    return vec3(p.x, c * p.y - s * p.z, s * p.y + c * p.z);
}

vec3 rotateY(vec3 p, float a) {
    float c = cos(a), s = sin(a);
    return vec3(c * p.x + s * p.z, p.y, -s * p.x + c * p.z);
}

vec3 rotateCameraToWorld(vec3 p, vec2 yawPitch) {
    float yaw = -yawPitch.x;
    float pitch = -yawPitch.y;
    p = rotateX(p, pitch);
    p = rotateY(p, yaw);
    return normalize(p);
}

void main() {
    float t = GameTime * 100.0;

    vec2 cam = radians(CameraYawPitch);
    vec3 dir = rotateCameraToWorld(normalize(localDir), cam);

    vec2 uv = dir.xz / max(dir.y, 0.02);

    float heightFactor = smoothstep(-0.1, 0.5, dir.y);
    vec3 baseSkyColor = vec3(0.02, 0.0, 0.05) * heightFactor;
    float baseAlpha = 0.8 * heightFactor;

    float angle = 0.6;
    float s = sin(angle), c = cos(angle);
    vec2 riftUV = vec2(c * uv.x - s * uv.y, s * uv.x + c * uv.y);

    float baseDist = length(vec2(riftUV.x * 0.35, riftUV.y * 2.2));

    float zigzag = 0.0;
    zigzag += tri(riftUV.x * 1.5) * 0.7;
    zigzag += tri(riftUV.x * 3.7 + riftUV.y) * 0.3;
    zigzag += tri(riftUV.x * 8.2 - t * 0.2) * 0.15;

    float organic = fbm(vec3(riftUV.x * 5.0, riftUV.y * 5.0, t * 0.2)) * 0.35;

    float riftSDF = baseDist + zigzag * 0.9 + organic;

    float riftMask = 1.0 - smoothstep(1.0, 1.15, riftSDF);

    float sharpGlow = smoothstep(0.9, 1.0, riftSDF) * (1.0 - smoothstep(1.0, 1.08, riftSDF));
    float softGlow  = smoothstep(0.7, 1.0, riftSDF) * (1.0 - smoothstep(1.0, 1.5, riftSDF));

    vec3 dimSpaceColor = vec3(0.1, 0.0, 0.3) + fbm(dir * 12.0 - vec3(0.0, t * 0.6, 0.0)) * vec3(0.8, 0.1, 1.0);
    dimSpaceColor *= 1.5;

    vec3 viewDir = rotateCameraToWorld(vec3(0.0, 0.0, -1.0), cam);
    vec2 eyeUV = uv - viewDir.xz;
    float dEye = length(eyeUV);

    float rings = pow(abs(sin(dEye * 25.0 - t * 1.5)), 0.6);
    vec3 irisColor = mix(vec3(0.3, 0.0, 0.6), vec3(1.0, 0.4, 1.0), rings);
    float irisMask = 1.0 - smoothstep(0.7, 1.2, dEye);

    float dPupil = abs(eyeUV.x * 5.0) + abs(eyeUV.y * 1.2);

    float pupilScale = sin(t * 0.5) * 0.15 + 1.0;
    dPupil *= pupilScale;
    float pupilMask = 1.0 - smoothstep(0.28, 0.35, dPupil);

    vec3 eyeColor = mix(dimSpaceColor, irisColor, irisMask);
    eyeColor = mix(eyeColor, vec3(0.0), pupilMask * irisMask);

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
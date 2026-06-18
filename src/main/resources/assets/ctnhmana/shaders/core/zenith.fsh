#version 150

// 输入：顶点着色器传来的局部方向与纹理坐标。
in vec3 localDir;
in vec2 texCoord;

// Uniform：由 Java 代码每帧传入。
uniform float Time;    // 时间，驱动所有动画。
uniform float EyeOpen; // 眼睛睁开程度，0 = 闭合，1 = 完全睁开。

out vec4 fragColor;

#define PI 3.14159265359

// ================= 天顶主题色 =================
const vec3 ZENITH_DEEP   = vec3(0.05, 0.0, 0.12); // 裂缝深处/暗部
const vec3 ZENITH_CORE   = vec3(0.6, 0.1, 0.9);   // 核心紫
const vec3 ZENITH_BRIGHT = vec3(1.0, 0.3, 0.85);  // 亮粉高光
const vec3 ZENITH_DARK   = vec3(0.02, 0.0, 0.05);  // 背景夜空

// ================= 噪声与基础函数 =================

// 三维伪随机哈希，输出 0~1。
float hash31(vec3 p) {
    return fract(sin(dot(p, vec3(127.1, 311.7, 74.7))) * 43758.5453123);
}

// 三维 Value Noise，基于三线性插值。
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

// Fractal Brownian Motion：叠加多层噪声，产生自然云状/星云纹理。
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

// 三角波，用于制造锯齿状裂缝边缘。
float tri(float x) {
    return abs(fract(x) - 0.5) * 2.0 - 0.5;
}

// ease-out 三次曲线，用于瞳孔扫视的加速/减速。
float easeOutCubic(float x) {
    return 1.0 - pow(1.0 - x, 3.0);
}

// ease-out-back：在 1 处略微回弹，用于睁眼动画。
float easeOutBack(float x) {
    float c1 = 1.70158;
    float c3 = c1 + 1.0;
    return 1.0 + c3 * pow(x - 1.0, 3.0) + c1 * pow(x - 1.0, 2.0);
}

// ================= 主函数 =================
void main() {

    // t 是内部动画时间，比 Uniform Time 快 3 倍，让星云/瞳孔运动更明显。
    float t = Time * 3.0;
    // 将 EyeOpen 限制在合法范围，防止意外值导致画面撕裂。
    float eyeOpen = clamp(EyeOpen, 0.0, 1.0);

    // UV 范围 [-4, 4]，中央是眼睛注视区域。
    vec2 uv = texCoord * 4.0;

    // 完全移除自然眨眼与上下眼睑裁剪，仅保留整体缩放作为眼睛开合效果。
    float verticalOpen = easeOutBack(eyeOpen);
    float horizontalOpen = mix(0.05, 1.0, easeOutCubic(eyeOpen));
    // 防止除以 0，避免 NaN。
    uv.y /= max(verticalOpen, 0.001);
    uv.x /= max(horizontalOpen, 0.001);

    // ================= 背景 =================
    // 离中心越远越暗，形成 vignette 效果。
    float heightFactor = smoothstep(0.0, 0.8, 1.0 - length(uv) * 0.22);
    vec3 baseSkyColor = ZENITH_DARK * heightFactor;
    float baseAlpha = 0.8 * heightFactor;

    // ================= 裂缝 UV 变换 =================
    // 将 UV 旋转一个固定角度，使裂缝呈现略微倾斜的“竖眼”造型。
    float angle = 0.6;
    float s = sin(angle), c = cos(angle);
    vec2 riftUV = vec2(c * uv.x - s * uv.y, s * uv.x + c * uv.y);

    float y = riftUV.y;

    // ================= 裂缝边缘扰动 =================
    // 用多层三角波 + fbm 构造不规则、有机的裂缝边缘。
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

    // 让上下边缘带一点眼睑弧度，越靠近眼角越向中间收，增强眼睛轮廓。
    float eyelidCurve = pow(abs(warpedX) * 0.28, 2.0) * 0.55;
    float curvedY = y + sign(y) * eyelidCurve;
    // SDF：到裂缝中心的距离，x 压缩、y 拉长以形成竖眼。
    float riftSDF = length(vec2(warpedX * 0.3, curvedY * 1.8));

    // riftMask：裂缝内部区域；sharpGlow/softGlow：边缘内外两层辉光。
    float riftMask = 1.0 - smoothstep(1.0, 1.15, riftSDF);
    float sharpGlow = smoothstep(0.9, 1.0, riftSDF) *
        (1.0 - smoothstep(1.0, 1.08, riftSDF));
    float softGlow = smoothstep(0.7, 1.0, riftSDF) *
        (1.0 - smoothstep(1.0, 1.5, riftSDF));

    // ================= 裂缝内部空间 =================
    // 暗紫色基底 + 缓慢流动的星云噪声。
    vec3 dimSpaceColor =
        vec3(0.1, 0.0, 0.3) +
        fbm(vec3(uv * 4.0, 0.0) - vec3(0.0, t * 0.6, 0.0)) *
        vec3(0.8, 0.1, 1.0);

    dimSpaceColor *= 1.5;

    // ================= 快速扫视瞳孔 =================
    // gazeCycle：注视点切换周期（约 8 / 3 ≈ 2.67 秒一次）。
    float gazeCycle = 8.0;
    float gazePhase = fract(t / gazeCycle);
    // saccadeWindow：切换过程只占周期的 15%，其余时间保持注视。
    float saccadeWindow = 0.15;
    float gazeHold = smoothstep(0.0, saccadeWindow, gazePhase);
    // 使用 easeOutCubic 模拟生物眼球“快速启动、末尾减速”的扫视。
    float saccadeT = easeOutCubic(gazeHold);

    // 当前与下一个注视点，由 hash 生成，范围约 [-0.5, 0.5]。
    float gazeStep = floor(t / gazeCycle);
    vec2 gazeFrom = vec2(
        hash31(vec3(gazeStep, 1.7, 0.0)) - 0.5,
        hash31(vec3(gazeStep, 5.3, 0.0)) - 0.5
    );
    vec2 gazeTo = vec2(
        hash31(vec3(gazeStep + 1.0, 1.7, 0.0)) - 0.5,
        hash31(vec3(gazeStep + 1.0, 5.3, 0.0)) - 0.5
    );

    // 实际注视位置，乘以系数限制在眼白范围内。
    vec2 gaze = mix(gazeFrom, gazeTo, saccadeT) * vec2(0.34, 0.22);

    vec2 eyeUV = uv;
    // 虹膜中心随 gaze 偏移。
    vec2 irisUV = eyeUV - gaze * 0.28;
    float dEye = length(irisUV);

    // 虹膜内星云旋转角度：随距离增大而旋转，形成旋涡。
    float swirlAngle = dEye * 1.5 - t * 0.4;
    float sa = sin(swirlAngle), ca = cos(swirlAngle);
    vec2 swirlUV = vec2(ca * irisUV.x - sa * irisUV.y,
                        sa * irisUV.x + ca * irisUV.y);

    // 动态流体星云：用 fbm 作为流动场，让星云缓慢漂移。
    vec2 flow = vec2(
        fbm(vec3(swirlUV * 2.0, t * 0.2)),
        fbm(vec3(swirlUV * 2.0 + 5.2, t * 0.2))
    );

    swirlUV += (flow - 0.5) * 0.6;

    vec3 fogCoord = vec3(swirlUV * 3.5, t * 0.35);

    float fog = fbm(fogCoord);
    fog += fbm(fogCoord * 2.2 + vec3(flow * 2.0, t * 0.1)) * 0.5;
    fog += sin(fogCoord.x * 10.0 + t * 2.0) * 0.03;

    // 星云颜色在暗紫与亮粉之间根据 fog 密度插值。
    vec3 nebulaColor =
        mix(ZENITH_DEEP * 2.0,
            ZENITH_BRIGHT,
            fog * 0.9);

    // irisMask：圆形虹膜遮罩。
    float irisMask = 1.0 - smoothstep(0.58, 1.42, dEye);

    // 瞳孔：菱形 SDF，并随 gaze 移动。
    vec2 pupilUV = eyeUV - gaze * 0.65;
    float dPupil = abs(pupilUV.x * 4.2) + abs(pupilUV.y * 1.6);
    float pupilMask = 1.0 - smoothstep(0.24, 0.36, dPupil);

    // 眼睛颜色 = 裂缝深处颜色 + 星云虹膜；瞳孔处压暗为黑色。
    vec3 eyeColor = mix(dimSpaceColor, nebulaColor, irisMask);
    eyeColor = mix(eyeColor, vec3(0.0), pupilMask * irisMask);

    // ================= 合成 =================
    vec3 finalColor = baseSkyColor;
    float finalAlpha = baseAlpha;

    if (riftMask > 0.0) {
        finalColor = mix(finalColor, eyeColor, riftMask);
        finalAlpha = mix(finalAlpha, 1.0, riftMask);
    }

    // 裂缝边缘辉光：内层锐利高光 + 外层柔和光晕。
    finalColor += ZENITH_BRIGHT * sharpGlow * 3.0;
    finalColor += ZENITH_CORE * softGlow * 1.5;

    finalAlpha = clamp(finalAlpha + sharpGlow + softGlow, 0.0, 1.0);

    // 睁开时 alpha 也受 eyeOpen 影响，完全闭合时彻底透明。
    finalAlpha *= smoothstep(0.0, 0.2, eyeOpen);

    fragColor = vec4(finalColor, finalAlpha);
}


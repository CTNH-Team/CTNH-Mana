#version 150

in vec2 texCoord;
in vec3 localPos;
in float heightRatio;

uniform float Time;
uniform vec3 BeamColor;
uniform float BeamAlpha;

out vec4 fragColor;

// ------------------------------------------------------------
// 伪随机哈希：把三维坐标映射到 [0,1] 的伪随机数。
// ------------------------------------------------------------
float hash31(vec3 p) {
    return fract(sin(dot(p, vec3(127.1, 311.7, 74.7))) * 43758.5453123);
}

// ------------------------------------------------------------
// 三线性插值噪声：在整数格点之间做平滑过渡，
// 为后续 fbm 提供连续的湍流基础。
// ------------------------------------------------------------
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

// ------------------------------------------------------------
// 分形布朗运动：四层倍频叠加，产生具备自相似性的湍流。
// ------------------------------------------------------------
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

// ------------------------------------------------------------
// 快速脉冲锯齿波，用于能量流的周期性闪烁。
// ------------------------------------------------------------
float sawPulse(float x, float duty) {
    float f = fract(x);
    return smoothstep(0.0, 0.05, f) * (1.0 - smoothstep(duty, duty + 0.05, f));
}

void main() {
    // 时间尺度：t 用于慢速旋转，ft 用于快速电弧/闪烁。
    float t = Time * 0.5;
    float ft = Time * 1.2;

    // 圆柱坐标
    float r = length(localPos.xz);
    float angle = atan(localPos.z, localPos.x);
    float y = localPos.y;

    // ============================================================
    // 1. 多层圆柱壳（Layered Shells）
    //    三条以不同角速度旋转的噪声壳，形成“亚空间裂缝”的层叠结构。
    // ============================================================
    float shell1 = fbm(vec3(angle * 2.2 + t * 0.3, y * 0.06 - t * 0.22, t * 0.1));
    float shell2 = fbm(vec3(angle * 3.4 - t * 0.5, y * 0.09 - t * 0.30, t * 0.15));
    float shell3 = fbm(vec3(angle * 4.8 + t * 0.7, y * 0.12 - t * 0.42, t * 0.2));

    // 使用较宽的阈值保留流动纹理，避免切出密集刺眼的竖向亮丝。
    float tear1 = smoothstep(0.32, 0.78, shell1);
    float tear2 = smoothstep(0.30, 0.82, shell2) * 0.7;
    float tear3 = smoothstep(0.36, 0.80, shell3) * 0.55;

    // ============================================================
    // 2. 强对比螺旋裂缝（Spiral Tear）
    //    像拧开的裂缝一样盘旋上升，边缘高亮，中间留暗。
    // ============================================================
    float spiralPhase = angle * 3.0 + y * 0.09 - t * 0.65;
    float spiral = sin(spiralPhase);
    float spiralEdge = abs(spiral);
    float spiralTear = smoothstep(0.65, 1.0, spiralEdge) * smoothstep(0.0, 0.6, r) * 0.45;

    // ============================================================
    // 3. 向上喷射的能量流（Upward Streams）
    //    多条细流以不同速度向上涌动，产生“能量被抽上天”的动感。
    // ============================================================
    float streamA = fbm(vec3(angle * 2.4 + 1.7, y * 0.18 - t * 1.1, t * 0.1));
    float streamB = fbm(vec3(angle * 3.2 - 2.3, y * 0.22 - t * 0.9, t * 0.15));
    float streamC = fbm(vec3(angle * 4.0 + 0.5, y * 0.26 - t * 1.3, t * 0.12));

    float streamSharp = smoothstep(0.45, 0.95, max(streamA, max(streamB, streamC))) * 0.45;

    // ============================================================
    // 4. 内部电弧（Arcs）
    //    高频闪烁的枝状电弧，随机出现、快速消失，增强“不稳定”感。
    // ============================================================
    float arcNoise = fbm(vec3(angle * 8.0, y * 0.30 - ft * 0.5, ft * 0.4));
    float arcFlicker = step(0.72, hash31(vec3(floor(ft * 4.0), floor(y * 2.0), floor(angle * 3.0))));
    float arcSharp = pow(smoothstep(0.50, 0.88, arcNoise), 4.0) * arcFlicker * 0.45;

    // ============================================================
    // 5. 核心与径向分布
    //    中心极亮，向外快速衰减；外层由壳层结构接管。
    // ============================================================
    float core = exp(-r * r * 22.0);
    float innerShell = exp(-r * r * 6.0) * 0.7;

    // ============================================================
    // 6. 亮度合成
    //    细节层之间做“加性高亮”，暗隙通过低 alpha 体现，而不是把亮度压黑。
    // ============================================================
    float detail = tear1 * 0.24 + tear2 * 0.18 + tear3 * 0.14
                 + spiralTear * 0.30
                 + streamSharp * 0.35
                 + arcSharp * 0.50;

    float intensity = core * 1.8 + innerShell + detail;
    intensity = clamp(intensity, 0.0, 1.8);

    // ============================================================
    // 7. 高度衰减与底部爆发
    //    底部刚涌出时更亮；顶部逐渐消散进天空。
    // ============================================================
    float topFade = 1.0 - smoothstep(0.55, 1.0, heightRatio);
    float bottomBurst = 1.0 - smoothstep(0.0, 0.06, heightRatio);
    intensity *= topFade * (1.0 + bottomBurst * 0.6);

    // ============================================================
    // 8. 颜色分层
    //    - 核心：白热
    //    - 内层：亮粉
    //    - 主体：天顶紫
    //    - 外层：深紫罗兰
    // ============================================================
    vec3 coreColor = vec3(1.0, 0.95, 1.0);
    vec3 brightColor = vec3(1.0, 0.45, 0.95);
    vec3 baseColor = BeamColor * 1.7;
    vec3 deepColor = vec3(0.35, 0.0, 0.65);

    vec3 color = mix(deepColor, baseColor, clamp(innerShell * 2.0, 0.0, 1.0));
    color = mix(color, brightColor, clamp((streamSharp + spiralTear) * 0.35, 0.0, 1.0));
    color = mix(color, coreColor, clamp(core * 2.5, 0.0, 1.0));
    color *= intensity;

    // ============================================================
    // 9. Alpha
    //    基础不透明，但在裂缝/暗隙处略微透明，以营造体积感。
    //    这样既保留“有力量感”的实体感，又不会糊成纯色柱子。
    // ============================================================
    float alphaBase = 0.92;
    float alphaDetail = detail * 0.08;
    float alphaVoid = -(tear1 * 0.06 + tear2 * 0.05 + tear3 * 0.04);
    float alpha = alphaBase + alphaDetail + alphaVoid;
    alpha *= topFade;
    alpha = clamp(alpha * BeamAlpha, 0.0, 1.0);

    fragColor = vec4(color, alpha);
}

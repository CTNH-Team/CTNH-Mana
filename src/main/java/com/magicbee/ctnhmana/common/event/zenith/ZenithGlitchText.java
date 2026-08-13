package com.magicbee.ctnhmana.common.event.zenith;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * 虚境入侵期间的 UI 乱码工具：保留样式 / 颜色码风格，替换可见字符。
 */
public final class ZenithGlitchText {

    private static final char[] GLITCH_CHARS = "¤░▒▓█▀▄■□◆◇※†‡‽⌀∆Ωψξжфб▲▼★☆✦✧❖╳✕✖✗✘#@$%&*?!~".toCharArray();

    private ZenithGlitchText() {}

    public static void scrambleIfInvading(Level level, List<Component> textList) {
        if (level == null || textList == null || textList.isEmpty()) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (ZenithInvadeManager.get(serverLevel).hasActive()) {
            RandomSource random = serverLevel.getRandom();
            List<Component> scrambled = new ArrayList<>(textList.size());
            for (Component component : textList) {
                scrambled.add(scrambleComponent(component, random));
            }
            textList.clear();
            textList.addAll(scrambled);
        }
    }

    private static Component scrambleComponent(Component component, RandomSource random) {
        MutableComponent result = Component.empty();
        component.visit((style, text) -> {
            result.append(Component.literal(scrambleString(text, random)).withStyle(style));
            return java.util.Optional.empty();
        }, Style.EMPTY);
        return result;
    }

    private static String scrambleString(String text, RandomSource random) {
        if (text == null || text.isEmpty()) return text;
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '§' && i + 1 < text.length()) {
                sb.append(c).append(text.charAt(++i));
                continue;
            }
            if (Character.isWhitespace(c) || Character.isDigit(c)) {
                sb.append(c);
            } else {
                sb.append(GLITCH_CHARS[random.nextInt(GLITCH_CHARS.length)]);
            }
        }
        return sb.toString();
    }
}

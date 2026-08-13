package com.magicbee.ctnhmana.common.event.zenith;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import com.ctnhlang.CN;
import com.ctnhlang.EN;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;

/**
 * 虚境入侵对外文案与查询辅助。
 */
public final class ZenithInvadeMessages {

    @CN("未知的力量干扰了无线信道")
    @EN("An unknown force interferes with the wireless channel")
    public static Lang wirelessJamLang;

    @CN("无法理解之物透过思维之狭缝涌入了世界之中")
    @EN("Something incomprehensible pours into the world through a slit in thought")
    public static Lang invadeStartBroadcastLang;

    private ZenithInvadeMessages() {}

    /** 该维度是否存在活跃的虚境入侵（仅服务端有效）。 */
    public static boolean isJammingWireless(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        return ZenithInvadeManager.get(serverLevel).hasActive();
    }

    /** 向玩家 action bar 提示无线信道被干扰。 */
    public static void notifyWirelessJammed(Player player) {
        if (player == null || player.level().isClientSide) return;
        if (wirelessJamLang != null) {
            player.displayClientMessage(wirelessJamLang.translate(), true);
        } else {
            player.displayClientMessage(Component.literal("未知的力量干扰了无线信道"), true);
        }
    }

    /** 虚境入侵开始时，向维度内全体玩家发送紫色系统广播。 */
    public static void broadcastInvadeStart(ServerLevel level) {
        Component message = invadeStartBroadcastLang != null ?
                invadeStartBroadcastLang.translate().withStyle(ChatFormatting.LIGHT_PURPLE) :
                Component.literal("无法理解之物透过思维之狭缝涌入了世界之中")
                        .withStyle(ChatFormatting.LIGHT_PURPLE);
        for (ServerPlayer player : level.players()) {
            player.sendSystemMessage(message);
        }
    }
}

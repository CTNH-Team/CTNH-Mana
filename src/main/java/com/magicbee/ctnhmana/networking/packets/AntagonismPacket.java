package com.magicbee.ctnhmana.networking.packets;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.magicbee.ctnhmana.client.render.AntagonismRender;

/**
 * S2C：服务端在“物理拮抗/奥法拮抗”效果生效期间周期性推送，客户端据此在怪物头顶渲染效果图标。
 * 1.20.1 原版只会把效果包同步给乘客，普通怪物的效果客户端无法感知，因此需要手动同步。
 */
public class AntagonismPacket implements IPacket {

    private int entityId;
    private int effectId;
    private int duration;

    public AntagonismPacket(int entityId, MobEffect effect, int duration) {
        this.entityId = entityId;
        this.effectId = BuiltInRegistries.MOB_EFFECT.getId(effect);
        this.duration = duration;
    }

    public AntagonismPacket() {}

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.entityId);
        friendlyByteBuf.writeVarInt(this.effectId);
        friendlyByteBuf.writeInt(this.duration);
    }

    @Override
    public void decode(FriendlyByteBuf friendlyByteBuf) {
        this.entityId = friendlyByteBuf.readInt();
        this.effectId = friendlyByteBuf.readVarInt();
        this.duration = friendlyByteBuf.readInt();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        IPacket.super.execute(handler);
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        MobEffect effect = BuiltInRegistries.MOB_EFFECT.byId(this.effectId);
        if (effect == null) {
            return;
        }
        AntagonismRender.updateExpiry(this.entityId, effect, level.getGameTime() + this.duration);
    }
}

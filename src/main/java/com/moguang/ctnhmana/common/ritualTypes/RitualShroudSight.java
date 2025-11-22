package com.moguang.ctnhmana.common.ritualTypes;

import com.moguang.ctnhmana.registry.CMMobEffects;
import net.minecraft.client.particle.DragonBreathParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static dev.shadowsoffire.apotheosis.ench.Ench.Items.INFUSED_BREATH;
import static net.minecraft.world.effect.MobEffects.*;
import static net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;

@RitualRegister("shroudsight")
public class RitualShroudSight extends Ritual {
    // 正面效果列表（对玩家/实体有益的效果）
    public static final List<MobEffect> POSITIVE_EFFECTS = new ArrayList<>();
    // 负面效果列表（对玩家/实体有害的效果）
    public static final List<MobEffect> NEGATIVE_EFFECTS = new ArrayList<>();
    private final int costs=1;
    public RitualShroudSight() {
        super("ritualshroudsight", 0,1, "ritual.ctnhmana.dragon_shroudsight");
        this.addBlockRange("shroud_range", new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 10));
        this.setMaximumVolumeAndDistanceOfRange("shroud_range", 1, 30, 30);
    }

    @Override
    public void performRitual(IMasterRitualStone MasterRitualStone) {
        //搞抽象这一块
        POSITIVE_EFFECTS.add(MOVEMENT_SPEED);        // 速度
        POSITIVE_EFFECTS.add(DIG_SPEED);             // 急迫
        POSITIVE_EFFECTS.add(DAMAGE_BOOST);          // 力量
        POSITIVE_EFFECTS.add(HEAL);                  // 瞬间治疗
        POSITIVE_EFFECTS.add(JUMP);                  // 跳跃提升
        POSITIVE_EFFECTS.add(REGENERATION);          // 生命恢复
        POSITIVE_EFFECTS.add(DAMAGE_RESISTANCE);     // 抗性提升
        POSITIVE_EFFECTS.add(FIRE_RESISTANCE);       // 防火
        POSITIVE_EFFECTS.add(WATER_BREATHING);       // 水下呼吸
        POSITIVE_EFFECTS.add(INVISIBILITY);          // 隐身（通用视为正面）
        POSITIVE_EFFECTS.add(NIGHT_VISION);          // 夜视
        POSITIVE_EFFECTS.add(HEALTH_BOOST);          // 生命提升
        POSITIVE_EFFECTS.add(ABSORPTION);            // 伤害吸收
        POSITIVE_EFFECTS.add(SATURATION);            // 饱和
        POSITIVE_EFFECTS.add(LUCK);                  // 幸运
        POSITIVE_EFFECTS.add(SLOW_FALLING);          // 缓降
        POSITIVE_EFFECTS.add(CONDUIT_POWER);         // 潮涌能量
        POSITIVE_EFFECTS.add(DOLPHINS_GRACE);        // 海豚的恩惠
        POSITIVE_EFFECTS.add(HERO_OF_THE_VILLAGE);   // 村庄英雄

        // ========== 负面效果 ==========
        NEGATIVE_EFFECTS.add(MOVEMENT_SLOWDOWN);     // 缓慢
        NEGATIVE_EFFECTS.add(DIG_SLOWDOWN);          // 挖掘疲劳
        NEGATIVE_EFFECTS.add(BLINDNESS);             // 失明
        NEGATIVE_EFFECTS.add(HUNGER);                // 饥饿
        NEGATIVE_EFFECTS.add(WEAKNESS);              // 虚弱
        NEGATIVE_EFFECTS.add(POISON);                // 中毒
        NEGATIVE_EFFECTS.add(WITHER);                // 凋零
        NEGATIVE_EFFECTS.add(GLOWING);               // 发光（暴露位置，视为负面）
        NEGATIVE_EFFECTS.add(LEVITATION);            // 漂浮（不受控，视为负面）
        NEGATIVE_EFFECTS.add(UNLUCK);                // 霉运
        NEGATIVE_EFFECTS.add(BAD_OMEN);              // 不祥之兆
        NEGATIVE_EFFECTS.add(DARKNESS);
        var time_muti=1;
        var areas=3.0F;
        var request_level=100;
        Level world = MasterRitualStone.getWorldObj();
        AreaDescriptor growingRange = MasterRitualStone.getBlockRange("shroud_range");
        List<EnumDemonWillType> willConfig = MasterRitualStone.getActiveWillConfig();
         List<MobEffect> EFFECTS = new ArrayList<>();
        BlockPos pos = MasterRitualStone.getMasterBlockPos();
        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        AABB axis = growingRange.getAABB(MasterRitualStone.getMasterBlockPos());
        List<ItemEntity> droppedItems = world.getEntitiesOfClass(
                ItemEntity.class,  // 只筛选物品实体
                axis
        );
        if(rawWill>=5)
        {
            var muti=Math.min(rawWill/5,4);
            request_level-=muti*10;
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, muti*5, true);
        }
        var player=world.getPlayerByUUID(MasterRitualStone.getOwner());
        if(1==1) {
            var rand=Math.random();
            for(int i=1;i<=2;i++)
            {
                EFFECTS.add(NEGATIVE_EFFECTS.get((int) ((NEGATIVE_EFFECTS.size()-1)*rand)));
                EFFECTS.add(POSITIVE_EFFECTS.get((int) ((NEGATIVE_EFFECTS.size()-1)*rand)));
            }

            //if判断预留给后面使用
            player.sendSystemMessage(shroud_gaze_1.translate());
            player.addEffect(new MobEffectInstance(CMMobEffects.ShroudGazing.get(), 666));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,666));
            for(MobEffect effect:EFFECTS)
            {
                player.addEffect(new MobEffectInstance(effect,666,6));
            }
        }
        if(player==null)return;
        int current_level=player.experienceLevel;
        for(ItemEntity itemEntity:droppedItems)
        {
            if(itemEntity.getItem().getItem().equals(Items.DRAGON_BREATH))
            {
               var num= itemEntity.getItem().getCount();
               if(player.experienceLevel>=request_level)
               {
                   var ItemPos=itemEntity.getOnPos();
                   itemEntity.remove(DISCARDED);
                   ItemStack Breath=new ItemStack(INFUSED_BREATH.get(),num);
                   ItemEntity dropped_items = new ItemEntity(
                           world,
                           ItemPos.getX(),
                           ItemPos.getY()+5,
                           ItemPos.getZ(),
                           Breath
                   );
                   ((ServerLevel) world).addFreshEntity(dropped_items);
               }
            }
        }

        if(steadfastWill>=2)
        {
            areas+=2.5F;
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, 2, true);
        }

    }

    @Override
    public int getRefreshCost() {
        return 1;
    }
    @Override
    public int getRefreshTime() {
        return 500;
    }
    @Override
    public void gatherComponents(Consumer<RitualComponent> consumer) {
        this.addRune(consumer,0,-1,0,EnumRuneType.BLANK);
        this.addRune(consumer,2,-1,1,EnumRuneType.BLANK);
        this.addRune(consumer,2,-1,-1,EnumRuneType.BLANK);
        this.addRune(consumer,3,-1,1,EnumRuneType.BLANK);
        this.addRune(consumer,3,-1,-1,EnumRuneType.BLANK);
        this.addRune(consumer,4,-1,1,EnumRuneType.BLANK);
        this.addRune(consumer,4,-1,-1,EnumRuneType.BLANK);
        this.addRune(consumer,-2,-1,1,EnumRuneType.BLANK);
        this.addRune(consumer,-2,-1,-1,EnumRuneType.BLANK);
        this.addRune(consumer,-3,-1,1,EnumRuneType.BLANK);
        this.addRune(consumer,-3,-1,-1,EnumRuneType.BLANK);
        this.addRune(consumer,-4,-1,1,EnumRuneType.BLANK);
        this.addRune(consumer,-4,-1,-1,EnumRuneType.BLANK);
        this.addRune(consumer,-4,0,1,EnumRuneType.FIRE);
        this.addRune(consumer,-4,0,-1,EnumRuneType.FIRE);
        this.addRune(consumer,-4,1,1,EnumRuneType.BLANK);
        this.addRune(consumer,-4,1,-1,EnumRuneType.BLANK);
        this.addRune(consumer,4,0,1,EnumRuneType.FIRE);
        this.addRune(consumer,4,0,-1,EnumRuneType.FIRE);
        this.addRune(consumer,4,1,1,EnumRuneType.BLANK);
        this.addRune(consumer,4,1,-1,EnumRuneType.BLANK);

        this.addCornerRunes(consumer,1,-1,EnumRuneType.BLANK);
        this.addParallelRunes(consumer,1,-1,EnumRuneType.EARTH);
        this.addLeftRunes(consumer,2,-1,EnumRuneType.EARTH);
        this.addLeftRunes(consumer,3,-1,EnumRuneType.EARTH);
        this.addForwardRunes(consumer,2,-1,EnumRuneType.BLANK);
        this.addForwardRunes(consumer,2,0,EnumRuneType.DUSK);
        this.addForwardRunes(consumer,2,1,EnumRuneType.BLANK);
    }
    protected final void addLeftRunes(Consumer<RitualComponent> components, int offset, int y, EnumRuneType rune) {
        this.addRune(components, offset, y, 0, rune);
        this.addRune(components, -offset, y, 0, rune);
    }
    protected final void addForwardRunes(Consumer<RitualComponent> components, int offset, int y, EnumRuneType rune) {
        this.addRune(components, 0, y, -offset, rune);
        this.addRune(components, 0, y, offset, rune);
    }
    @Override
    public Ritual getNewCopy() {
        return new RitualShroudSight();
    }
    @CN("§5你感到无法理解之物在凝视着你，你无法理解祂的恩惠")
    public static Lang shroud_gaze_1;
    @CN("§5你感到来自虚境的无数眼睛在凝视这你，你无法理解祂们的旨意")
    public static Lang shroud_gaze_2;
    @CN("§5你感到你你的超主正在凝视着你，将祂的恩惠赐予于你")
    public static Lang shroud_gaze_3;
    @CN("§5你感到§o祂§r就在你身边，你已经参悟§o祂§r降下的旨意与恩惠")
    public static Lang shroud_gaze_4;
}

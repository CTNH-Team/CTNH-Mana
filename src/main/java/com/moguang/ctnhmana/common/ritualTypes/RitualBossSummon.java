package com.moguang.ctnhmana.common.ritualTypes;

import com.moguang.ctnhmana.item.bosssummon.ThrowItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.shadowsoffire.apotheosis.adventure.boss.ApothBoss;
import dev.shadowsoffire.apotheosis.adventure.boss.BossRegistry;
import dev.shadowsoffire.apotheosis.adventure.compat.GameStagesCompat;
import dev.shadowsoffire.placebo.reload.WeightedDynamicRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import tech.vixhentx.mcmod.ctnhlib.langprovider.annotation.CN;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.ritual.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.moguang.ctnhmana.registry.CMItems.ADVANCED_BOSS_SUMMONER;
import static com.moguang.ctnhmana.registry.CMItems.BOSS_SUMMONER;
import static net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;

@RitualRegister("bosssummon")
public class RitualBossSummon extends Ritual {
    public RitualBossSummon() {
        super("ritualbosssummon", 0, 1, "ritual.ctnhmana.ritualbosssummon");
        this.addBlockRange("boss_range", new AreaDescriptor.Rectangle(new BlockPos(-1, 0, -1), 3,3,3));
        this.setMaximumVolumeAndDistanceOfRange("boss_range", 1, 4, 4);
    }

    @Override
    public void performRitual(IMasterRitualStone MasterRitualStone) {
        Level level=MasterRitualStone.getWorldObj();
        AreaDescriptor growingRange = MasterRitualStone.getBlockRange("boss_range");
        BlockPos pos = MasterRitualStone.getMasterBlockPos();
        var area= new AABB(
                (double) pos.getX()-1,
                (double)pos.getY()-1,
                (double)pos.getZ()-1,
                (double)pos.getX()+1,
                (double)pos.getY()+2,
                (double)pos.getZ()+1
        ) ;     // 检测范围
        List<ItemEntity> droppedItems = level.getEntitiesOfClass(
                ItemEntity.class,  // 只筛选物品实体
               area
        );
        var boss_summon_1=new ItemStack(BOSS_SUMMONER,1);
        var boss_summon_2=new ItemStack(ADVANCED_BOSS_SUMMONER,1);
        for(ItemEntity itemEntity:droppedItems)
        {
            var items=itemEntity.getItem();
            if(items.equals(boss_summon_1,false)||items.equals(boss_summon_2,false))
            {
                var tier=1;
                var fight=boss_fight_1;
                if(items.equals(boss_summon_1,false))
                {
                    fight=boss_fight_1;
                    tier=4;
                }
                if(items.equals(boss_summon_2,false))
                {
                    fight=boss_fight_2;
                    tier=12;
                }
                itemEntity.remove(DISCARDED);

                int currentEssence = MasterRitualStone.getOwnerNetwork().getCurrentEssence();
                if(currentEssence<1)
                {
                    MasterRitualStone.getOwnerNetwork().causeNausea();
                }
                else
                {
                    var entity=level.getPlayerByUUID(MasterRitualStone.getOwner());
                    entity.sendSystemMessage(fight.translate());
                    level.playSound((Player)null, entity.getX(), entity.getY(),entity.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                    for(int i=1;i<=tier/4;i++) {
                        summon(entity, level, tier, new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 7));
                        summon(entity, level, tier, new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 7));
                        summon(entity, level, tier, new BlockPos(pos.getX() + 7, pos.getY(), pos.getZ()));
                        summon(entity, level, tier, new BlockPos(pos.getX() - 7, pos.getY(), pos.getZ()));
                        summon(entity, level, tier, new BlockPos(pos.getX() + 7, pos.getY(), pos.getZ() + 7));
                        summon(entity, level, tier, new BlockPos(pos.getX() - 7, pos.getY(), pos.getZ() + 7));
                        summon(entity, level, tier, new BlockPos(pos.getX() + 7, pos.getY(), pos.getZ() - 7));
                        summon(entity, level, tier, new BlockPos(pos.getX() - 7, pos.getY(), pos.getZ() - 7));
                    }
                    MasterRitualStone.getOwnerNetwork().syphon(MasterRitualStone.ticket(1));
                    break;
                }
            }
        }
        int currentEssence = MasterRitualStone.getOwnerNetwork().getCurrentEssence();
    }

    @Override
    public int getRefreshCost() {
        return 0;
    }

    @Override
    public int getRefreshTime() {
        return 100;
    }
    @Override
    public void gatherComponents(Consumer<RitualComponent> consumer) {
        this.addSquareRunes(consumer,7,-3,EnumRuneType.FIRE);
        this.addOffsetRunes(consumer,1,2,-3,EnumRuneType.FIRE);
        this.addOffsetRunes(consumer,3,1,-3,EnumRuneType.BLANK);
        this.addCornerRunes(consumer,1,-2,EnumRuneType.FIRE);
        this.addCornerRunes(consumer,2,-3,EnumRuneType.BLANK);
        this.addCornerRunes(consumer,7,-2,EnumRuneType.WATER);
        this.addCornerRunes(consumer,7,-1,EnumRuneType.BLANK);
        this.addParallelRunes(consumer,7,-2,EnumRuneType.WATER);
        this.addParallelRunes(consumer,7,-1,EnumRuneType.BLANK);
        this.addParallelRunes(consumer,1,-1,EnumRuneType.EARTH);
        this.addParallelRunes(consumer,2,-2,EnumRuneType.FIRE);
        this.addParallelRunes(consumer,3,-3,EnumRuneType.FIRE);
        this.addParallelRunes(consumer,4,-3,EnumRuneType.WATER);
        this.addParallelRunes(consumer,5,-3,EnumRuneType.WATER);
        this.addParallelRunes(consumer,6,-3,EnumRuneType.WATER);
        this.addParallelRunes(consumer,7,-2,EnumRuneType.WATER);
        this.addParallelRunes(consumer,7,-1,EnumRuneType.BLANK);
        this.addSmaalSquareRunes(consumer,7,0,-1,EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer,-7,0,-1,EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer,0,-7,-1,EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer,0,7,-1,EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer,7,7,-1,EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer,-7,7,-1,EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer,-7,-7,-1,EnumRuneType.DUSK);
        this.addSmaalSquareRunes(consumer,7,-7,-1,EnumRuneType.DUSK);

    }
    protected final void addSquareRunes(Consumer<RitualComponent> components, int size, int y, EnumRuneType rune) {
        // 校验大小（确保为正数，至少1x1）
        if (size < 1) {
            throw new IllegalArgumentException("正方形大小必须为正整数");
        }

        // 1. 放置正方形的四个顶点（角落符文）
        // 顶点坐标：(size, y, size)、(size, y, -size)、(-size, y, -size)、(-size, y, size)
        addCornerRunes(components, size, y, rune);

        // 2. 放置正方形四条边上的中间符文（平行于轴的边）
        // 从1到size-1遍历，避免重复放置顶点（已在步骤1处理）
        for (int offset = 1; offset < size; offset++) {
            // 平行于Z轴的边（X=±size，Z从±1到±(size-1)）
            addRune(components, size, y, offset, rune);       // 右上边：(size, y, offset)
            addRune(components, size, y, -offset, rune);      // 右下边：(size, y, -offset)
            addRune(components, -size, y, offset, rune);      // 左上边：(-size, y, offset)
            addRune(components, -size, y, -offset, rune);     // 左下边：(-size, y, -offset)

            // 平行于X轴的边（Z=±size，X从±1到±(size-1)）
            addRune(components, offset, y, size, rune);       // 上右边：(offset, y, size)
            addRune(components, -offset, y, size, rune);      // 上左边：(-offset, y, size)
            addRune(components, offset, y, -size, rune);      // 下右边：(offset, y, -size)
            addRune(components, -offset, y, -size, rune);     // 下左边：(-offset, y, -size)
        }
        addParallelRunes(components,size,y,rune);
    }
    protected final void addSmaalSquareRunes(Consumer<RitualComponent> components, int x, int z, int y,EnumRuneType rune)
    {
        addRune(components,x+1,y,z,rune);
        addRune(components,x-1,y,z,rune);
        addRune(components,x,y,z+1,rune);
        addRune(components,x,y,z-1,rune);
        addRune(components,x+1,y,z+1,rune);
        addRune(components,x+1,y,z-1,rune);
        addRune(components,x-1,y,z+1,rune);
        addRune(components,x-1,y,z-1,rune);

    }

    @Override
    public Ritual getNewCopy() {
        return new RitualBossSummon();
    }
    public void summon(Entity entity,Level level,int tier,BlockPos pos)
    {
        int muti=2;
        int time=5;
        int resistance_level=3;
        if (entity instanceof ServerPlayer player) {
            if(tier>=8)
            {
                for(int i=1;i<=4;i++)
                {
                    var playerpos=entity.getOnPos();
                    var random_pos=new BlockPos((int) (playerpos.getX()+7-14*Math.random()), (int) (playerpos.getY()+7-14*Math.random()), (int) (playerpos.getZ()+7-14*Math.random()));
                    EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level,random_pos, MobSpawnType.TRIGGERED);
                }
                muti=4;
                time=20;
                resistance_level=6;
            }
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level,pos, MobSpawnType.TRIGGERED);
            ApothBoss bossItem = (ApothBoss) BossRegistry.INSTANCE.getRandomItem(level.getRandom(), player.getLuck() * tier, new Predicate[]{WeightedDynamicRegistry.IDimensional.matches(level), GameStagesCompat.IStaged.matches(player)});
            if (bossItem != null) {
                Mob mobEntity = bossItem.createBoss((ServerLevel) level, pos, level.getRandom(), player.getLuck());
                mobEntity.setTarget(player);
                mobEntity.setPersistenceRequired();
                mobEntity.setHealth(mobEntity.getMaxHealth()*muti);
                MobEffectInstance resistance = new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE,
                        time,          // 最大整数表示“永久”（实际游戏中会持续到实体死亡）
                        resistance_level-1,                          // 等级3（2+1）
                        false,
                        true
                );
                mobEntity.addEffect(resistance);
                ((ServerLevel) level).addFreshEntityWithPassengers(mobEntity);

            }
        }
    }
    @CN("§c战争在逼近......")
    public static Lang boss_fight_1;
    @CN("§c无尽的战争在逼近......")
    public static Lang boss_fight_2;
}

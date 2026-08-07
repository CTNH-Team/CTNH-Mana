package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.common.parts.RedstoneSignalBroadcastHatch;
import com.moguang.ctnhmana.utils.CTNHManaUtils;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.*;

public class EternalGarden extends RecipeElectricMultiblockMachine implements ITieredMachine, IChannelMachine {

    public EternalGarden(IMachineBlockEntity holder) {
        super(holder);
    }

    @Persisted
    private int foodlist_length = 0;
    @Persisted
    private int nutrition_length = 0;
    @Persisted
    private LinkedList<Double> nutritionlist = new LinkedList<>();
    @Persisted
    private int Temperature = 0;
    @Persisted
    private boolean wither = false;
    @Persisted
    private boolean thunder = false;
    @Persisted
    private boolean burn = false;
    @Persisted
    private double nutrition = 1;
    @Persisted
    public String flower_type = "None";
    public Map<Integer, Integer> channelSignal = new HashMap<>();
    public List<RedstoneSignalBroadcastHatch> hatchList = new ArrayList<>();

    @Override
    public int getChannelSignal(int channel) {
        return channelSignal.getOrDefault(channel, 0);
    }

    public List<RedstoneSignalBroadcastHatch> broadcastSelf() {
        List<RedstoneSignalBroadcastHatch> hatches = new ArrayList<>();
        for (IMultiPart part : this.getParts()) {
            if (part instanceof RedstoneSignalBroadcastHatch hatch) {
                hatch.setRedstoneSignalOutput((getChannelSignal(hatch.channel)));
                hatches.add(hatch);
            }
        }
        return hatches;
    }

    public void changeSignal() {
        if (this.burn && this.Temperature >= 12500) channelSignal.put(0, 10);
        if (!hatchList.isEmpty()) {
            for (RedstoneSignalBroadcastHatch hatch : hatchList) {
                hatch.setRedstoneSignalOutput(getChannelSignal(hatch.channel));
            }
        }
    }

    // todo
    @Deprecated
    public static List<ItemStack> getallrunes() {
        List<ItemStack> ItemsRune = new ArrayList<>();
        List<ItemStack> ItemsFlower = new ArrayList<>();
        TagKey<Item> RuneTag = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),
                ResourceLocation.tryBuild("botania", "runes"));
        TagKey<Item> FlowerTag = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),
                ResourceLocation.tryBuild("botania", "generating_special_flowers"));
        for (Item item : ForgeRegistries.ITEMS) {
            ItemStack itemStack = item.getDefaultInstance();

            if (itemStack.is(RuneTag)) {
                ItemsRune.add(itemStack);
            }
            if (itemStack.is(FlowerTag)) {
                ItemsFlower.add(itemStack);
            }
        }
        return ItemsRune;
    }

    // todo
    @Deprecated
    public static List<ItemStack> getallflowers() {
        List<ItemStack> ItemsRune = new ArrayList<>();
        List<ItemStack> ItemsFlower = new ArrayList<>();
        TagKey<Item> RuneTag = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),
                ResourceLocation.tryBuild("botania", "runes"));
        TagKey<Item> FlowerTag = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),
                ResourceLocation.tryBuild("botania", "generating_special_flowers"));
        for (Item item : ForgeRegistries.ITEMS) {
            ItemStack itemStack = item.getDefaultInstance();

            if (itemStack.is(RuneTag)) {
                ItemsRune.add(itemStack);
            }
            if (itemStack.is(FlowerTag)) {
                ItemsFlower.add(itemStack);
            }
        }
        return ItemsFlower;
    }

    public void onStructureFormed() {
        super.onStructureFormed();
        hatchList = broadcastSelf();
        changeSignal();
        // flower=getallflowers();
        // rune=getallrunes();
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        changeSignal();
    }

    @Override
    public boolean onWorking() {
        var level = getLevel();
        var pos = getPos();
        if (getOffsetTimer() % 5 == 0) {
            if (thunder) {
                if (level != null) {
                    var area = AABB.of(BoundingBox.fromCorners(pos.offset(-20, -10, -20), pos.offset(10, 10, 10)));
                    var entities = level.getEntities(null, area);
                    for (Entity i : entities) {
                        if (!(i instanceof LivingEntity)) {
                            continue;
                        }
                        var poser = i.blockPosition();
                        EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, poser, MobSpawnType.TRIGGERED);

                    }
                }
            }
        }
        if (getOffsetTimer() % 20 == 0) {
            if (wither) {
                level = getLevel();
                pos = getPos();
                Holder<DamageType> type = level
                        .registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(DamageTypes.WITHER);
                // forge文档真不如Fabric一根吧
                DamageSource damageSource = new DamageSource(type);
                var area = AABB.of(BoundingBox.fromCorners(pos.offset(-20, -10, -20), pos.offset(10, 10, 10)));
                if (level != null) {
                    var entities = level.getEntities(null, area);
                    for (Entity i : entities) {
                        if (!(i instanceof LivingEntity)) {
                            continue;
                        }
                        i.hurt(damageSource, 6);
                    }
                }
            }
            if (burn) {
                level = getLevel();
                pos = getPos();
                Holder<DamageType> type = level
                        .registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(DamageTypes.WITHER);
                // forge文档真不如Fabric一根吧
                DamageSource damageSource = new DamageSource(type);
                var area = AABB.of(BoundingBox.fromCorners(pos.offset(-20, -10, -20), pos.offset(10, 10, 10)));
                if (level != null) {
                    var entities = level.getEntities(null, area);
                    for (Entity i : entities) {
                        if (!(i instanceof LivingEntity)) {
                            i.setSecondsOnFire(10);

                        }

                    }
                }
            }
        }
        return super.onWorking();
    }

    /** Reject parallel<=0 so we never multiplyInputs(0) into a matchable empty recipe. */
    private static final Component INSUFFICIENT_INPUTS = Component
            .translatable("gtceu.recipe_logic.insufficient_in");

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof EternalGarden mmachine) {
            int tier = mmachine.getTier();
            int recipe_tier = GTUtil.getFloorTierByVoltage(RecipeHelper.getRealEUt(recipe));
            var base_overclock = 1.1;
            double overclock = Math.pow(base_overclock, tier - recipe_tier);
            String type = recipe.data.getString("type");
            if (type.equals("water")) {
                // 水绣球
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                int parallel = CTNHManaUtils.getParallelAmount(group, recipe, 8);
                if (parallel <= 0) return INSUFFICIENT_INPUTS;
                int outMul = Math.max(1, (int) (parallel * overclock));
                CTNHManaUtils.multiplyInputs(recipe, parallel);
                recipe.multiplyOutputs(outMul);
                recipe.multiplyTickOutputs(outMul);
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (type.equals("eat")) {
                // 彼方兰
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                int maxparallel = 8 + (mmachine.tier -
                        GTUtil.getFloorTierByVoltage(RecipeHelper.getRealEUt(recipe))) * 4;
                int parallel = CTNHManaUtils.getParallelAmount(group, recipe, maxparallel);
                if (parallel <= 0) return INSUFFICIENT_INPUTS;
                CTNHManaUtils.multiplyAllContents(recipe, parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (type.equals("fire")) {
                // 炎修花烧燃料：先算并行，避免燃料耗尽后 multiply(0) 跑空配方
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                int temp = recipe.data.getInt("temp");
                int parallel = CTNHManaUtils.getParallelAmount(group, recipe, 8);
                if (parallel <= 0) return INSUFFICIENT_INPUTS;
                mmachine.Temperature += temp;
                FluidStack pyrotheumFluid = new FluidStack(
                        Objects.requireNonNull(
                                ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse("ctnhcore:cryotheum"))),
                        1000);
                if (MachineUtils.inputFluid(pyrotheumFluid, mmachine)) {
                    mmachine.Temperature -= 100000;
                }
                double rate = Math.sqrt(Math.max(0, temp));
                int outMul = Math.max(1, (int) (parallel * overclock * Math.max(rate, 1)));
                CTNHManaUtils.multiplyInputs(recipe, parallel);
                recipe.multiplyOutputs(outMul);
                recipe.multiplyTickOutputs(outMul);
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (type.equals("boom")) {
                // 热爆
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                int maxparallel = (int) Math.pow(2, tier) * 32;
                int parallel = CTNHManaUtils.getParallelAmount(group, recipe, maxparallel);
                if (parallel <= 0) return INSUFFICIENT_INPUTS;
                CTNHManaUtils.multiplyAllContents(recipe, parallel);
                recipe.parallels = parallel;
                return null;
            }

            if (type.equals("wither")) {
                // 凋零兔葵
                mmachine.wither = true;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                int parallel = CTNHManaUtils.getParallelAmount(group, recipe, 4);
                if (parallel <= 0) return INSUFFICIENT_INPUTS;
                int outMul = Math.max(1, (int) (parallel * overclock));
                CTNHManaUtils.multiplyInputs(recipe, parallel);
                recipe.multiplyOutputs(outMul);
                recipe.multiplyTickOutputs(outMul);
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (type.equals("lighting")) {
                // 雷德兰
                mmachine.wither = false;
                mmachine.thunder = true;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                var level = mmachine.getLevel();
                var pos = mmachine.getPos();
                if (level != null && level.isThundering()) {
                    if (recipe.data.getBoolean("light")) {
                        EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, pos, MobSpawnType.TRIGGERED);
                        int outMul = Math.max(1, (int) (100000 * overclock));
                        recipe.multiplyOutputs(outMul);
                        recipe.multiplyTickOutputs(outMul);
                        return null;
                    }
                    int outMul = Math.max(1, (int) overclock);
                    recipe.multiplyOutputs(outMul);
                    recipe.multiplyTickOutputs(outMul);
                    return null;
                }
                if (level != null && level.isRaining()) {
                    int outMul = Math.max(1, (int) (300 * overclock));
                    recipe.multiplyOutputs(outMul);
                    recipe.multiplyTickOutputs(outMul);
                    return null;
                }
                return null;
            }
            if (type.equals("blame") || type.equals("flame")) {
                // 炽玫瑰 / 热绣球（旧 data type=flame 兼容）
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = true;
                mmachine.Temperature = 0;
                int maxparallel = 8 + Math.max((tier - 3), 0) * 4;
                int parallel = CTNHManaUtils.getParallelAmount(group, recipe, maxparallel);
                if (parallel <= 0) return INSUFFICIENT_INPUTS;
                int outMul = Math.max(1, (int) (parallel * overclock));
                CTNHManaUtils.multiplyInputs(recipe, parallel);
                recipe.multiplyOutputs(outMul);
                recipe.multiplyTickOutputs(outMul);
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (type.equals("fly")) {
                // 勿落草
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                var pos = mmachine.getPos();
                var level = mmachine.getLevel();
                int muti = 1;
                var area = AABB.of(BoundingBox.fromCorners(pos.offset(-20, -10, -20), pos.offset(10, 10, 10)));
                if (level != null) {
                    var entities = level.getEntities(null, area);
                    for (Entity i : entities) {
                        if (!(i instanceof LivingEntity living)) {
                            continue;
                        }
                        if (living.hasEffect(MobEffects.LEVITATION)) {
                            muti += 1;
                            Holder<DamageType> damageType = level
                                    .registryAccess()
                                    .registryOrThrow(Registries.DAMAGE_TYPE)
                                    .getHolderOrThrow(DamageTypes.MAGIC);
                            living.hurt(new DamageSource(damageType), 200);
                        }
                        level.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                                BotaniaSounds.shulkMeNot, SoundSource.BLOCKS, 10.0F, 1.0F);
                    }
                }
                recipe.multiplyEUt(muti);
                int outputMultiplier = Math.max(1,
                        (int) (Math.pow(2, Math.min(17, muti * 0.5)) * overclock));
                recipe.multiplyOutputs(outputMultiplier);
                recipe.multiplyTickOutputs(outputMultiplier);
                return null;
            }

        }
        return null;
    }

    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        var tier = getTier();
        if (nutrition > 1) {
            textList.add(textList.size(),
                    Component.translatable("ctnh.eternalgarden.info.eat", String.format("%.1f", nutrition)));
        }
        if (Temperature > 0) {
            textList.add(textList.size(),
                    Component.translatable("ctnh.eternalgarden.info.fire", String.format("%d", Temperature)));
        }
    }
}

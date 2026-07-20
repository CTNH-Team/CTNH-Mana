package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import com.moguang.ctnhmana.common.parts.RedstoneSignalBroadcastHatch;
import com.moguang.ctnhmana.registry.CMMaterials;
import com.moguang.ctnhmana.registry.CMRecipeTypes;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.*;

import javax.annotation.Nullable;

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

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object... args) {
        return new GardenLogic(this);
    }

    class GardenLogic extends RecipeLogic {

        public GardenLogic(IRecipeLogicMachine machine) {
            super(machine);
        }

        public @NotNull Iterator<GTRecipe> searchRecipe() {
            GTRecipe flowerRecipe = searchFlowerRecipe();
            if (flowerRecipe != null) {
                if (RecipeHelper
                        .matchContents(machine.getRecipeHandlerGroups().get(0), flowerRecipe).isSuccess()) {
                    return Collections.singleton(flowerRecipe).iterator();
                }
            }
            return Collections.emptyIterator();
        }

        @Nullable
        private GTRecipe searchFlowerRecipe() {
            IRecipeCapabilityHolder holder = machine;
            var recipeHandlers = holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP);
            for (var handler : recipeHandlers) {
                for (var content : handler.getContents()) {
                    if (!(content instanceof ItemStack stack)) continue;
                    if (stack.isEmpty()) continue;
                    if (stack.getItem().equals(BotaniaFlowerBlocks.endoflame.asItem())) return searchBurnRecipe();
                    if (stack.getItem().equals(BotaniaFlowerBlocks.gourmaryllis.asItem())) return searchFoodRecipe();
                }
            }
            return null;
        }

        @Nullable
        private GTRecipe searchFoodRecipe() {
            IRecipeCapabilityHolder holder = machine;
            var recipeHandlers = holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP);
            for (var handler : recipeHandlers) {
                for (var content : handler.getContents()) {
                    if (!(content instanceof ItemStack stack)) continue;
                    if (stack.isEmpty()) continue;
                    if (stack.getFoodProperties(null) != null) {
                        var properties = stack.copy().getFoodProperties(null);
                        var num = Math.pow(properties.getNutrition(), 3);
                        var builder = CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder("eats")
                                .notConsumable(BotaniaFlowerBlocks.gourmaryllis.asItem())
                                .inputItems(stack.copyWithCount(1))
                                .outputFluids(CMMaterials.Mana.getFluid((int) (num * 1.5)))
                                .duration((int) (num * 5))
                                .EUt(GTValues.HV, 1)
                                .addData("type", "eat")
                                .buildRawRecipe().toRuntime();
                        return builder;
                    }
                }
            }
            return null;
        }

        @Nullable
        private GTRecipe searchBurnRecipe() {
            IRecipeCapabilityHolder holder = machine;
            var recipeHandlers = holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP);
            for (var handler : recipeHandlers) {
                for (var content : handler.getContents()) {
                    if (!(content instanceof ItemStack stack)) continue;
                    if (stack.isEmpty()) continue;
                    if (stack.getBurnTime(RecipeType.SMELTING) > 0) {
                        var burns = stack.copy().getBurnTime(RecipeType.SMELTING);
                        var builder = CMRecipeTypes.ETERNAL_GARDEN.recipeBuilder("burns")
                                .notConsumable(BotaniaFlowerBlocks.endoflame.asItem())
                                .inputItems(stack.copyWithCount(1)) // 输入1个食??
                                .outputFluids(CMMaterials.Mana.getFluid((int) (1)))
                                .duration(1)
                                .EUt(GTValues.HV, 1)
                                .addData("temp", burns)
                                .addData("type", "fire")
                                .buildRawRecipe().toRuntime();
                        return builder;
                    }
                }
            }
            return null;
        }
    }

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof EternalGarden mmachine) {
            int tier = mmachine.getTier();
            int recipe_tier = GTUtil.getFloorTierByVoltage(RecipeHelper.getRealEUt(recipe));
            var base_overclock = 1.1;
            double overclock = Math.pow(base_overclock, tier - recipe_tier);
            if (recipe.data.getString("type").equals("water")) {
                // 水绣??
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                int maxparallel = 8;
                int parallel = ParallelLogic.getParallelAmount(group, recipe, maxparallel);
                recipe.multiplyInputs(parallel);
                recipe.multiplyTickInputs(parallel);
                recipe.multiplyOutputs((int) (parallel * overclock));
                recipe.multiplyTickOutputs((int) (parallel * overclock));
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (recipe.data.getString("type").equals("eat")) {
                // 彼方??
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                int maxparallel = 8 + (mmachine.tier -
                        GTUtil.getFloorTierByVoltage(RecipeHelper.getRealEUt(recipe))) * 4;
                int parallel = ParallelLogic.getParallelAmount(group, recipe, maxparallel);
                recipe.multiplyAllContents(parallel);
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (recipe.data.getString("type").equals("fire")) {
                // 烧煤??
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                int maxparallel = 8;
                int temp = recipe.data.getInt("temp");
                mmachine.Temperature += temp;
                FluidStack pyrotheumFluid = new FluidStack(
                        Objects.requireNonNull(
                                ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse("gtceu:cryotheum"))),
                        1000);
                // 检查输入仓是否有足够流??
                boolean isFluidSufficient = MachineUtils.inputFluid(pyrotheumFluid, mmachine);
                if (isFluidSufficient) mmachine.Temperature -= 100000;
                double rate = Math.sqrt(12500 * 12500 - Math.abs(temp - 12500 * 12500));
                int parallel = ParallelLogic.getParallelAmount(group, recipe, maxparallel);
                recipe.multiplyInputs((int) (parallel * Math.max(1, Math.sqrt(rate))));
                recipe.multiplyTickInputs((int) (parallel * Math.max(1, Math.sqrt(rate))));
                recipe.multiplyOutputs((int) (parallel * overclock * Math.max(rate, 1)));
                recipe.multiplyTickOutputs((int) (parallel * overclock * Math.max(rate, 1)));
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (recipe.data.getString("type").equals("boom")) {
                // 热爆??
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                int maxparallel = (int) Math.pow(2, tier) * 32;
                int parallel = ParallelLogic.getParallelAmount(group, recipe, maxparallel);
                recipe.multiplyAllContents(parallel);
                recipe.parallels = parallel;
                return null;
            }

            if (recipe.data.getString("type").equals("wither")) {
                // 凋零兔葵
                mmachine.wither = true;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                int maxparallel = 4;
                int parallel = ParallelLogic.getParallelAmount(group, recipe, maxparallel);
                recipe.multiplyInputs(parallel);
                recipe.multiplyTickInputs(parallel);
                recipe.multiplyOutputs((int) (parallel * overclock));
                recipe.multiplyTickOutputs((int) (parallel * overclock));
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (recipe.data.getString("type").equals("lighting")) {
                // 雷德??
                mmachine.wither = false;
                mmachine.thunder = true;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                var level = mmachine.getLevel();
                var pos = mmachine.getPos();
                if (level.isThundering()) {
                    if (recipe.data.getBoolean("light")) {
                        EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, pos, MobSpawnType.TRIGGERED);
                        recipe.multiplyOutputs((int) (100000 * overclock));
                        recipe.multiplyTickOutputs((int) (100000 * overclock));
                        return null;
                    }
                    recipe.multiplyOutputs((int) overclock);
                    recipe.multiplyTickOutputs((int) overclock);
                    return null;
                }
                if (level.isRaining()) {
                    recipe.multiplyOutputs((int) (300 * overclock));
                    recipe.multiplyTickOutputs((int) (300 * overclock));
                    return null;
                }

            }
            if (recipe.data.getString("type").equals("blame")) {
                // 热绣??
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = true;
                mmachine.Temperature = 0;
                int maxparallel = 8 + Math.max((tier - 3), 0) * 4;
                int parallel = ParallelLogic.getParallelAmount(group, recipe, maxparallel);
                recipe.multiplyInputs(parallel);
                recipe.multiplyTickInputs(parallel);
                recipe.multiplyOutputs((int) (parallel * overclock));
                recipe.multiplyTickOutputs((int) (parallel * overclock));
                recipe.multiplyEUt(parallel);
                recipe.parallels = parallel;
                return null;
            }
            if (recipe.data.getString("type").equals("fly")) {
                // 勿落??
                mmachine.wither = false;
                mmachine.thunder = false;
                mmachine.burn = false;
                mmachine.Temperature = 0;
                var pos = mmachine.getPos();
                var level = mmachine.getLevel();
                // forge文档真不如Fabric一根吧
                int muti = 0;
                var area = AABB.of(BoundingBox.fromCorners(pos.offset(-20, -10, -20), pos.offset(10, 10, 10)));
                if (level != null) {
                    var entities = level.getEntities(null, area);
                    muti = 1;
                    for (Entity i : entities) {
                        if (!(i instanceof LivingEntity)) {
                            continue;
                        }
                        if (((LivingEntity) i).hasEffect(MobEffects.LEVITATION)) {
                            muti += 1;
                            Holder<DamageType> type = level
                                    .registryAccess()
                                    .registryOrThrow(Registries.DAMAGE_TYPE)
                                    .getHolderOrThrow(DamageTypes.MAGIC);
                            // forge文档真不如Fabric一根吧
                            DamageSource damageSource = new DamageSource(type);
                            i.hurt(damageSource, 200);
                        }
                        level.playSound((Player) null, (double) pos.getX() + (double) 0.5F,
                                (double) pos.getY() + (double) 0.5F, (double) pos.getZ() + (double) 0.5F,
                                BotaniaSounds.shulkMeNot, SoundSource.BLOCKS, 10.0F, 1.0F);
                    }
                }
                recipe.multiplyEUt(muti);
                int outputMultiplier = (int) (Math.pow(2, Math.min(17, muti * 0.5)) * overclock);
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

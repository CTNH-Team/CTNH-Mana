package com.moguang.ctnhmana.common.ritualTypes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import com.moguang.ctnhmana.registry.CMMaterials;
import mythicbotany.alfheim.content.AlfPixie;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.gem;

@RitualRegister("extractor")
public class RitualLifeExtractor extends Ritual {

    public int START_COST = 1000000;
    public int COSTS = 500000;
    public int REFRESH_TIME = 100;

    public RitualLifeExtractor() {
        super("ritualextractor", 0, 1, "ritual.ctnhmana.ritualextractor");
        this.addBlockRange("extraction_range", new AreaDescriptor.Rectangle(new BlockPos(-8, -2, -8), 8, 9, 18));
        this.setMaximumVolumeAndDistanceOfRange("extraction_range", 1, 16, 16);
    }

    @SuppressWarnings("all")
    @Override
    public void performRitual(IMasterRitualStone MasterRitualStone) {
        Level world = MasterRitualStone.getWorldObj();
        AreaDescriptor growingRange = MasterRitualStone.getBlockRange("extraction_range");
        BlockPos pos = MasterRitualStone.getMasterBlockPos();
        List<EnumDemonWillType> willConfig = MasterRitualStone.getActiveWillConfig();

        AABB axis = growingRange.getAABB(MasterRitualStone.getMasterBlockPos());
        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double CORROSIVEWILL = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);
        var pixielList = world.getEntitiesOfClass(AlfPixie.class, axis);
        var animalList = world.getEntitiesOfClass(Animal.class, axis);
        var fxxk = world.getEntities(null, axis);
        Holder<DamageType> type = world
                .registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.MAGIC);
        // forge文档真不如Fabric一根吧
        DamageSource damageSource = new DamageSource(type);
        List<Material> material = new ArrayList<>();
        material.add(GTMaterials.Diamond);
        material.add(GTMaterials.Ruby);
        material.add(GTMaterials.Sapphire);
        material.add(GTMaterials.Emerald);
        material.add(GTMaterials.GreenSapphire);
        material.add(GTMaterials.Americium);
        material.add(GTMaterials.YellowLimonite);
        for (Animal animal : animalList) {
            var px = animal.getX();
            var py = animal.getY();
            var pz = animal.getZ();
            animal.hurt(damageSource, 200);
            int min_num = 1;
            int max_num = 6;
            double flawed = 0.4;
            double wonderful = 0.7;
            if (rawWill >= 2) {
                max_num += 1;
                wonderful -= 0.2;
                flawed -= 0.2;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, 2, true);
            }
            if (steadfastWill >= 2) {
                wonderful -= 0.2;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, 2, true);
            }
            if (destructiveWill >= 2) {
                wonderful += 0.1;
                flawed += 0.05;
                max_num += 3;
                min_num += 3;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, 2, true);
            }
            if (vengefulWill >= 2) {
                flawed += 0.1;
                min_num += 3;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, 2, true);
            }
            if (CORROSIVEWILL >= 2) {
                flawed -= 0.25;
                wonderful += 0.1;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, 2, true);
            }
            var rander = Math.random();
            ItemStack gems = null;
            var tag = gem;

            var Gem = material.get((int) (rander * 7));
            if (Math.random() <= flawed) {
                gems = ChemicalHelper.get(gemFlawed, Gem, min_num + (int) (Math.random() * max_num));
            } else if (Math.random() <= wonderful) {
                gems = ChemicalHelper.get(gem, Gem, min_num + (int) (Math.random() * max_num));
            } else {
                gems = ChemicalHelper.get(gem, Gem, min_num + (int) (Math.random() * max_num));
            }
            ItemEntity itemEntity = new ItemEntity(
                    world,
                    px,
                    py,
                    pz,
                    gems);
            ((ServerLevel) world).addFreshEntity(itemEntity);
        }
        for (AlfPixie pixie : pixielList) {

            var px = pixie.getX();
            var py = pixie.getY();
            var pz = pixie.getZ();
            pixie.hurt(damageSource, 200);
            int max_num = 2;
            int min_num = 1;
            double broken = 0.4;
            double flawed = 0.85;
            if (rawWill >= 2) {
                broken -= 0.1;
                flawed -= 0.1;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, 2, true);
            }
            if (steadfastWill >= 2) {
                broken -= 0.2;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, 2, true);
            }
            if (destructiveWill >= 2) {
                broken += 0.1;
                flawed += 0.05;
                max_num += 1;
                min_num += 1;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, 2, true);
            }
            if (vengefulWill >= 2) {
                flawed += 0.1;
                min_num += 1;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, 2, true);
            }
            if (CORROSIVEWILL >= 2) {
                flawed -= 0.25;
                broken += 0.1;
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, 2, true);
            }
            flawed = Math.max(flawed, broken);
            var rander = Math.random();
            ItemStack gems = null;
            var tag = gem;
            if (Math.random() <= broken) {
                gems = ChemicalHelper.get(gemChipped, CMMaterials.Psionic_Medulla,
                        min_num + (int) (Math.random() * max_num));
            } else if (Math.random() <= flawed) {
                gems = ChemicalHelper.get(gemFlawed, CMMaterials.Psionic_Medulla,
                        min_num + (int) (Math.random() * max_num));
            } else {
                gems = ChemicalHelper.get(gem, CMMaterials.Psionic_Medulla, min_num + (int) (Math.random() * max_num));
            }
            ItemEntity itemEntity = new ItemEntity(
                    world,
                    px,
                    py,
                    pz,
                    gems);
            ((ServerLevel) world).addFreshEntity(itemEntity);

        }
    }

    @Override
    public int getRefreshCost() {
        return COSTS;
    }

    @Override
    public int getRefreshTime() {
        return REFRESH_TIME;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> consumer) {
        this.addRune(consumer, 0, -1, 0, EnumRuneType.DUSK);
        this.addCornerRunes(consumer, 1, -1, EnumRuneType.WATER);
        this.addCornerRunes(consumer, 2, -1, EnumRuneType.BLANK);
        this.addCornerRunes(consumer, 2, 0, EnumRuneType.BLANK);
        this.addCornerRunes(consumer, 2, 1, EnumRuneType.FIRE);
        this.addCornerRunes(consumer, 2, 2, EnumRuneType.BLANK);
        this.addCornerRunes(consumer, 3, -1, EnumRuneType.FIRE);
        this.addCornerRunes(consumer, 4, -1, EnumRuneType.DUSK);
        this.addCornerRunes(consumer, 4, 4, EnumRuneType.BLANK);
        this.addOffsetRunes(consumer, 2, 1, -1, EnumRuneType.BLANK);
        this.addOffsetRunes(consumer, 3, 1, -1, EnumRuneType.FIRE);
        this.addOffsetRunes(consumer, 3, 2, -1, EnumRuneType.FIRE);
        this.addOffsetRunes(consumer, 3, 3, -1, EnumRuneType.FIRE);
        this.addOffsetRunes(consumer, 4, 1, -1, EnumRuneType.DUSK);
        this.addOffsetRunes(consumer, 4, 2, -1, EnumRuneType.DUSK);
        this.addOffsetRunes(consumer, 4, 3, -1, EnumRuneType.DUSK);
        this.addOffsetRunes(consumer, 5, 1, -1, EnumRuneType.EARTH);
        this.addOffsetRunes(consumer, 5, 2, -1, EnumRuneType.EARTH);
        this.addOffsetRunes(consumer, 5, 3, -1, EnumRuneType.EARTH);
        this.addOffsetRunes(consumer, 5, 2, 4, EnumRuneType.BLANK);
        this.addOffsetRunes(consumer, 5, 3, 4, EnumRuneType.BLANK);
        this.addOffsetRunes(consumer, 6, 1, 4, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 1, -1, EnumRuneType.WATER);
        this.addParallelRunes(consumer, 2, -1, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 3, -1, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 3, 0, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 3, 1, EnumRuneType.FIRE);
        this.addParallelRunes(consumer, 3, 2, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 4, -1, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 5, -1, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 6, -1, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 6, 0, EnumRuneType.BLANK);
        this.addParallelRunes(consumer, 6, 1, EnumRuneType.DUSK);
        this.addParallelRunes(consumer, 6, 2, EnumRuneType.FIRE);
        this.addParallelRunes(consumer, 6, 3, EnumRuneType.EARTH);
        this.addParallelRunes(consumer, 6, 4, EnumRuneType.BLANK);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualLifeExtractor();
    }
}

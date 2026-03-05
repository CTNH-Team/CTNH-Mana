package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.blockentity.machine.IManaMachineBlockEntity;
import com.moguang.ctnhmana.common.blockentity.machine.IZenithMartixBlockEntity;
import com.moguang.ctnhmana.common.blockentity.machine.MysticSpireBlockEntity;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.registrate.CNRegistrate;
import tech.vixhentx.mcmod.ctnhlib.registrate.builders.CTNHMachineBuilder;
import tech.vixhentx.mcmod.ctnhlib.registrate.builders.CTNHMultiblockMachineBuilder;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

public class CMRegistrate extends CNRegistrate {

    protected CMRegistrate() {
        super(CTNHMana.MODID);
    }

    public static CMRegistrate create() {
        return new CMRegistrate();
    }

    // @Override @NotNull @ParametersAreNonnullByDefault
    // public <DEFINITION extends MachineDefinition> MachineBuilder<DEFINITION> machine(String name,
    // Function<ResourceLocation, DEFINITION> definitionFactory,
    // Function<IMachineBlockEntity, MetaMachine> metaMachine,
    // BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory,
    // BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
    // TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
    // return super.machine(name, definitionFactory, metaMachine, blockFactory, itemFactory, blockEntityFactory)
    // .hasBER(false);
    // }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public CTNHMachineBuilder<MachineDefinition> machine(String name,
                                                         Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return (CTNHMachineBuilder<MachineDefinition>) super.machine(name, metaMachine)
                .hasBER(false);
    }

    public <DEFINITION extends MachineDefinition> CTNHMachineBuilder<DEFINITION> machine(String name,
                                                                                         String cnname,
                                                                                         Function<ResourceLocation, DEFINITION> definitionFactory,
                                                                                         Function<IMachineBlockEntity, MetaMachine> metaMachine,
                                                                                         BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory,
                                                                                         BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
                                                                                         TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        return new CTNHMachineBuilder<>(this, name, cnname, definitionFactory, metaMachine,
                blockFactory, itemFactory, blockEntityFactory);
    }

    public CTNHMachineBuilder<MachineDefinition> manamachine(String name,
                                                             Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return new CTNHMachineBuilder<>(this, name, MachineDefinition::new, metaMachine,
                MetaMachineBlock::new, MetaMachineItem::new, IManaMachineBlockEntity::new);
    }

    public CTNHMultiblockMachineBuilder zenithmultiblock(String name,
                                                         Function<IMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return multiblock(name, metaMachine, MetaMachineBlock::new, MetaMachineItem::new,
                IZenithMartixBlockEntity::new);
    }

    public CTNHMultiblockMachineBuilder mysticmultiblock(String name,
                                                         Function<IMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return multiblock(name, metaMachine, MetaMachineBlock::new, MetaMachineItem::new, MysticSpireBlockEntity::new);
    }
}

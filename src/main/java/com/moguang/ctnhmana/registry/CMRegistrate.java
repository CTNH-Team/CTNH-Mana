package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.moguang.ctnhmana.CTNHMana;
import com.moguang.ctnhmana.common.blockentity.machine.IManaMachineBlockEntity;
import com.moguang.ctnhmana.common.blockentity.machine.IZenithMartixBlockEntity;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhlib.registrate.CNRegistrate;
import tech.vixhentx.mcmod.ctnhlib.registrate.builders.CTNHMachineBuilder;
import tech.vixhentx.mcmod.ctnhlib.registrate.builders.CTNHMultiblockMachineBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public class CMRegistrate extends CNRegistrate {
    protected CMRegistrate() {
        super(CTNHMana.MODID);
    }
    public static CMRegistrate create() {
        return new CMRegistrate();
    }



//    @Override @NotNull @ParametersAreNonnullByDefault
//    public <DEFINITION extends MachineDefinition> MachineBuilder<DEFINITION> machine(String name,
//                                                                                     Function<ResourceLocation, DEFINITION> definitionFactory,
//                                                                                     Function<IMachineBlockEntity, MetaMachine> metaMachine,
//                                                                                     BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory,
//                                                                                     BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
//                                                                                     TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
//        return super.machine(name, definitionFactory, metaMachine, blockFactory, itemFactory, blockEntityFactory)
//                .hasBER(false);
//    }

    @Override @NotNull @ParametersAreNonnullByDefault
    public CTNHMachineBuilder<MachineDefinition> machine(String name, Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return (CTNHMachineBuilder<MachineDefinition>)super.machine(name, metaMachine)
                .hasBER(false);
    }
    public CTNHMachineBuilder<MachineDefinition> manamachine(String name,
                                                     Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return new CTNHMachineBuilder<>(this, name, MachineDefinition::new, metaMachine,
                MetaMachineBlock::new, MetaMachineItem::new, IManaMachineBlockEntity::new);
    }
    public CTNHMultiblockMachineBuilder zenithmultiblock(String name, Function<IMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return multiblock(name, metaMachine, MetaMachineBlock::new, MetaMachineItem::new, IZenithMartixBlockEntity::new);
    }


}

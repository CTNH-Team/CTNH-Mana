package com.moguang.ctnhmana.client.render;


import com.moguang.ctnhmana.client.model.CMModels;
import com.moguang.ctnhmana.common.blockentity.machine.FlowerCakeBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StarCakeMachineBERProvider {

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderer(BlockEntityType<BlockEntity> beType, String name){
        var typed = (BlockEntityType<FlowerCakeBlockEntity>) (BlockEntityType<?>)beType;
        BlockEntityRenderers.register(typed, ctx -> new StarCakeRender(CMModels.MODELS.get(name)));
    }

}


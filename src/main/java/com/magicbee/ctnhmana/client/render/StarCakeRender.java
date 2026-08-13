package com.magicbee.ctnhmana.client.render;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import com.magicbee.ctnhmana.common.blockentity.machine.FlowerCakeBlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class StarCakeRender extends GeoBlockRenderer<FlowerCakeBlockEntity> {

    public StarCakeRender(GeoModel<?> model) {
        super((GeoModel<FlowerCakeBlockEntity>) model);
    }

    @Override
    public RenderType getRenderType(FlowerCakeBlockEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}

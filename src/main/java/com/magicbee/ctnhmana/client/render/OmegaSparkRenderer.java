package com.magicbee.ctnhmana.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;

import com.magicbee.ctnhmana.common.entity.OmegaSpark;
import vazkii.botania.client.render.entity.BaseSparkRenderer;

import java.util.Objects;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OmegaSparkRenderer extends BaseSparkRenderer<OmegaSpark> {

    private final TextureAtlasSprite dispersiveIcon;
    private final TextureAtlasSprite dominantIcon;
    private final TextureAtlasSprite recessiveIcon;
    private final TextureAtlasSprite isolatedIcon;

    public OmegaSparkRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        var atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        this.dispersiveIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_dispersive")));
        this.dominantIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_dominant")));
        this.recessiveIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_recessive")));
        this.isolatedIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_isolated")));
    }

    @Override
    public TextureAtlasSprite getSpinningIcon(OmegaSpark entity) {
        return null;
    }
}

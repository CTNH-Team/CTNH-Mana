package com.moguang.ctnhmana.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;

import com.moguang.ctnhmana.common.entity.DeltaSpark;
import vazkii.botania.client.render.entity.BaseSparkRenderer;

import java.util.Objects;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class DeltaSparkRenderer extends BaseSparkRenderer<DeltaSpark> {

    private final TextureAtlasSprite dispersiveIcon;
    private final TextureAtlasSprite dominantIcon;
    private final TextureAtlasSprite recessiveIcon;
    private final TextureAtlasSprite isolatedIcon;

    public DeltaSparkRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        var atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        this.dispersiveIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_dispersive")));
        this.dominantIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_dominant")));
        this.recessiveIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_recessive")));
        this.isolatedIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_isolated")));
    }

    @Override
    public TextureAtlasSprite getSpinningIcon(DeltaSpark entity) {
        return null;
    };
}

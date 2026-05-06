package com.moguang.ctnhmana.Mutiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.util.Mth;

import com.moguang.ctnhmana.api.networks.BotaniaEffectPacketExtend;
import com.moguang.ctnhmana.api.networks.BotaniaExtendEffectType;
import com.moguang.ctnhmana.common.recipe.ZenithCondition;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class ManaReactor extends BaseManaMachine {

    public ManaReactor(IMachineBlockEntity holder, int consumption) {
        super(holder, consumption);
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        return true;
    }

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ManaReactor.class, BaseManaMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean onWorking() {
        float progress = Mth.clamp((float) this.getProgress() / this.getMaxProgress(), 0.0F, 1.0F);
        int proportion = Float.floatToIntBits(progress);
        var pos = MachineUtils.getOffset(this, 0, 10, -10);
        XplatAbstractions.INSTANCE.sendToNear(
                this.getLevel(),
                pos,
                new BotaniaEffectPacketExtend(
                        BotaniaExtendEffectType.TERRA_PLATE,
                        pos.getX(), pos.getY(), pos.getZ(),
                        proportion));
        return super.onWorking();
    }

    @Override
    protected @Nullable GTRecipe getRealRecipe(GTRecipe recipe) {
        SyncManaData();
        List<ZenithCondition> conditions = recipe.conditions.stream()
                .filter(ZenithCondition.class::isInstance)
                .map(ZenithCondition.class::cast)
                .toList();
        if (conditions.isEmpty()) return super.getRealRecipe(recipe);
        var condition = conditions.get(0);
        if (!condition.getZenithType().equals("Blank")) {
            var type = condition.getType();
            var tier = condition.getTier();
            if (tier > 0 && tier - ManaLevel.get(type) > 0) {
                var speed_up = 0.2 * (tier - ManaLevel.get(type));
            }
        }

        var newRecipe = recipe.copy();
        return super.getRealRecipe(newRecipe);
    }
}

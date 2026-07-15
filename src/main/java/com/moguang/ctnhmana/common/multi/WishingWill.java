package com.moguang.ctnhmana.common.multi;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.gregtechceu.gtceu.data.recipe.CustomTags.CIRCUITS;

public class WishingWill extends WorkableMultiblockMachine {

    @Persisted
    public final NotifiableItemStackHandler machineStorage;
    @Persisted
    protected final NotifiableItemStackHandler dummyOutputStorage;
    protected TickableSubscription ManaSubs = null;

    public WishingWill(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.machineStorage = new NotifiableItemStackHandler(this, 1, IO.IN, IO.IN);
        dummyOutputStorage = new NotifiableItemStackHandler(this, 114514, IO.OUT, IO.OUT);
    }

    public void GetPoolItems() {
        var world = this.getLevel();
        var pos = this.getPos();
        AABB area = new AABB(new BlockPos(pos.getX() - 1, pos.getY() - 10, pos.getZ() - 1),
                new BlockPos(pos.getX() + 1, pos.getY() + 10, pos.getZ() + 1));
        List<ItemEntity> droppedItems = world.getEntitiesOfClass(
                ItemEntity.class,  // 只筛选物品实体
                area);
        for (ItemEntity item : droppedItems) {
            if (item.getItem().is(CIRCUITS)) {
                if (machineStorage.getStackInSlot(0).isEmpty()) {
                    machineStorage.insertItem(0, (item.getItem()), false);
                    item.remove(Entity.RemovalReason.KILLED);
                }
            }
        }
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object... args) {
        return new WishingWillLogic(this);
    }

    public void GetAward(ItemStack[] itemStacks, String award_type) {
        if (this.getLevel() instanceof ClientLevel) return;
        var level = this.getLevel();
        var pos = this.getPos();
        for (ItemStack item : itemStacks) {
            ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY() + 3, pos.getZ(), item);
            itemEntity.setDeltaMovement(
                    0.0,
                    0.5 + level.random.nextDouble() * 2.0,
                    0.0);
            itemEntity.setPickUpDelay(10); // 设置拾取延迟
            level.addFreshEntity(itemEntity);
        }
    }

    public void updateTick() {
        ManaSubs = subscribeServerTick(ManaSubs, this::GetPoolItems);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateTick));
            addHandlerList(RecipeHandlerList.of(IO.IN, machineStorage));
            addHandlerList(RecipeHandlerList.of(IO.OUT, dummyOutputStorage));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (ManaSubs != null) {
            ManaSubs.unsubscribe();
        }
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (machine instanceof WishingWill wmachine) {
            var parallel = ParallelLogic.getParallelAmount(wmachine, recipe, 10);
            return ModifierFunction.builder()
                    .inputModifier(ContentModifier.multiplier(parallel))
                    .outputModifier(ContentModifier.multiplier(parallel))
                    .eutMultiplier(parallel / 2)
                    .build();
        }
        return ModifierFunction.NULL;
    }

    class WishingWillLogic extends RecipeLogic {

        public WishingWillLogic(IRecipeLogicMachine machine) {
            super(machine);
        }

        @Override
        protected ActionResult handleRecipeIO(GTRecipe recipe, IO io) {
            if (io == IO.IN) {
                // 输入处理：正常处理
                return super.handleRecipeIO(recipe, io);
            }
            if (io == IO.OUT) {
                var safe_recipe = lastOriginRecipe;
                List<Content> outputContents = safe_recipe.getOutputContents(ItemRecipeCapability.CAP);
                if (!outputContents.isEmpty()) {
                    for (Content content : outputContents) {
                        var safe_content = content.copy(ItemRecipeCapability.CAP);
                        // 从Content中获取ItemStack
                        Ingredient ingredient = ItemRecipeCapability.CAP.of(safe_content.getContent());
                        if (ingredient != null) {
                            ItemStack[] stacks = ingredient.getItems();
                            if (stacks != null && stacks.length > 0) {
                                GetAward(stacks, "None");
                            }
                        }
                    }
                }
                return ActionResult.SUCCESS;
            }

            return super.handleRecipeIO(recipe, io);
        }
    }
}

package com.moguang.ctnhmana.common.multiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.RecipeMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerGroup;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.ingredient.item.ItemIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.gregtechceu.gtceu.data.recipe.CustomTags.CIRCUITS;

public class WishingWill extends RecipeMultiblockMachine {

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
                ItemEntity.class,  // åªç­›é€‰ç‰©å“å®žä½?
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
            itemEntity.setPickUpDelay(10); // è®¾ç½®æ‹¾å–å»¶è¿Ÿ
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
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        var inputHandlers = RecipeHandlerList.of(List.of(machineStorage));
        var outputHandlers = RecipeHandlerList.of(List.of(dummyOutputStorage));
        recipeHandlerLists.add(inputHandlers);
        recipeHandlerLists.add(outputHandlers);
        traitSubscriptions.add(inputHandlers.subscribe(recipeLogic::updateTickSubscription));
        traitSubscriptions.add(outputHandlers.subscribe(recipeLogic::updateTickSubscription));
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (ManaSubs != null) {
            ManaSubs.unsubscribe();
        }
    }

    public static Component recipeModifier(MetaMachine machine, RecipeHandlerGroup group, GTRecipe recipe) {
        if (machine instanceof WishingWill wmachine) {
            var parallel = ParallelLogic.getParallelAmount(group, recipe, 10);
            recipe.multiplyAllContents(parallel);
            recipe.multiplyEUt(parallel / 2.0);
            recipe.parallels = parallel;
            return null;
        }
        return null;
    }

    class WishingWillLogic extends RecipeLogic {

        public WishingWillLogic(IRecipeLogicMachine machine) {
            super(machine);
        }

        @Override
        protected ActionResult handleRecipeIO(GTRecipe recipe, IO io) {
            if (io == IO.IN) {
                // è¾“å…¥å¤„ç†ï¼šæ­£å¸¸å¤„ç?
                return super.handleRecipeIO(recipe, io);
            }
            if (io == IO.OUT) {
                var safe_recipe = lastOriginRecipe;
                List<ItemIngredient> outputContents = safe_recipe.getOutputContents(ItemRecipeCapability.CAP);
                if (!outputContents.isEmpty()) {
                    for (ItemIngredient content : outputContents) {
                        var safe_content = content.copy();
                        // ä»ŽContentä¸­èŽ·å–ItemStack
                        if (safe_content != null) {
                            ItemStack[] stacks = safe_content.getItems();
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

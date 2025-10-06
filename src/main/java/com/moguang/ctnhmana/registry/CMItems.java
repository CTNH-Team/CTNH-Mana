package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.ICustomDescriptionId;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Arrays;
import java.util.List;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;
import static com.moguang.ctnhmana.data.lang.ChineseLangHandler.botaniacoreLang;


public class CMItems {
    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.ITEM);
    }
    public static void init() {

    }

    public static  ItemEntry<ComponentItem> HORIZEN_RUNE = REGISTRATE
            .item("horizen_rune",ComponentItem::create)
            .lang("horizen_rune")
            .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
//            .onRegister(attach(new TooltipBehavior(list -> {
//                list.add(Component.translatable("ctnh.item.runes.horizen_rune").withStyle(ChatFormatting.DARK_PURPLE));
//            })))

            .register();
    public static ItemEntry<ComponentItem> STARLIGHT_RUNE = REGISTRATE
            .item("starlight_rune",ComponentItem::create)
            .lang("starlight_rune")
            .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
//            .onRegister(attach(new TooltipBehavior(list -> {
//                list.add(Component.translatable("ctnh.item.runes.starlight_rune").withStyle(ChatFormatting.BLUE));
//            })))
            .register();
    public static ItemEntry<ComponentItem> TWIST_RUNE = REGISTRATE
            .item("twist_rune",ComponentItem::create)
            .lang("twist_rune")
            .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
//            .onRegister(attach(new TooltipBehavior(list -> {
//                list.add(Component.translatable("ctnh.item.runes.twist_rune").withStyle(ChatFormatting.RED));
//            })))
            .register();
    public static ItemEntry<ComponentItem> QUASAR_RUNE = REGISTRATE
            .item("quasar_rune",ComponentItem::create)
            .lang("quasar_rune")
            .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
//            .onRegister(attach(new TooltipBehavior(list -> {
//                list.add(Component.translatable("ctnh.item.runes.quasar_rune").withStyle(ChatFormatting.LIGHT_PURPLE));
//            })))
            .register();
    public static ItemEntry<ComponentItem> PROLIFERATION_RUNE = REGISTRATE
            .item("proliferation_rune",ComponentItem::create)
            .lang("proliferation_rune")
            .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
//            .onRegister(attach(new TooltipBehavior(list -> {
//                list.add(Component.translatable("ctnh.item.runes.proliferation_rune").withStyle(ChatFormatting.GREEN));
//            })))
            .register();
    public static ItemEntry<ComponentItem> FLOWER_UPDATE = REGISTRATE
            .item("flower_core",ComponentItem::create)
            .lang("flower_core")
            .tag(BotaniaTags.Items.RUNES,CMTags.MANA_UPDATE_TIER1)
            .onRegister(attach(new TooltipBehavior(list -> itemTooltipsAdd(botaniacoreLang,list))))
            .register();
    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent components) {
        return item -> item.attachComponents(components);
    }

    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent... components) {
        return item -> item.attachComponents(components);
    }

    public static ICustomDescriptionId cellName() {
        return new ICustomDescriptionId() {

            @Override
            public Component getItemName(ItemStack stack) {
                Component prefix = FluidUtil.getFluidContained(stack).map(FluidStack::getDisplayName)
                        .orElse(Component.translatable("gtceu.fluid.empty"));
                return Component.translatable(stack.getDescriptionId(), prefix);
            }
        };
    }
    //为不具有format的列表lang提供列表
    public static List<Component> itemTooltipsAdd(Lang[] langs, List<Component> list)
    {
        for(Lang lang:langs)
        {
            list.add(lang.translate());
        }
        return list;
    }


}

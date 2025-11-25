package com.moguang.ctnhmana.registry;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.ICustomDescriptionId;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.moguang.ctnhmana.item.bosssummon.BossSummonerBehavior;
import com.moguang.ctnhmana.item.manamachineupdate.BMUpgradeItemT1;
import com.moguang.ctnhmana.item.manamachineupdate.BTUpgradeItemT1;
import com.moguang.ctnhmana.item.manamachineupdate.GTUpgradeItemT1;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import tech.vixhentx.mcmod.ctnhlib.langprovider.Lang;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.moguang.ctnhmana.CTNHMana.REGISTRATE;

import com.moguang.ctnhmana.item.bosssummon.ThrowItem;
public class CMItems {
    static {
        REGISTRATE.creativeModeTab(() -> CMCreativeModeTabs.ITEM);
    }

    public static void registerItem()
    {
        BT_UPDATE_T1=REGISTRATE
                .item("botania_update_t1", BTUpgradeItemT1::new)
                .cnlang("§9繁花与星空之祝福")
                .register();
        GT_UPDATE_T1=REGISTRATE
                .item("gt_update_t1", GTUpgradeItemT1::new)
                .cnlang("§9流水线之视野")
                .register();
        BM_UPDATE_T1=REGISTRATE
                .item("bm_update_t1", BMUpgradeItemT1::new)
                .cnlang("§c扭曲之铸造")
                .register();
        BOSS_SUMMONER = REGISTRATE
                .item("boss_summoner", ThrowItem::new)
                .cnlang("boss召唤器")
                .lang("Boss Summoner")
                .onRegister(attach(new BossSummonerBehavior(1)))
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.boss_summoner.use").withStyle(ChatFormatting.RED));
                })))
                .register();
        ADVANCED_BOSS_SUMMONER = REGISTRATE
                .item("advanced_boss_summoner", ThrowItem::new)
                .cnlang("进阶boss召唤器")
                .lang("Advanced Boss Summoner")
                .onRegister(attach(new BossSummonerBehavior(2)))
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.boss_summoner.use").withStyle(ChatFormatting.DARK_RED));
                })))
                .register();
        HORIZEN_RUNE = REGISTRATE
                .item("horizen_rune",ComponentItem::create)
                .cnlang("§5视域§r符文")
                .lang("§5Horizen§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.horizen_rune").withStyle(ChatFormatting.DARK_PURPLE));
                })))
                .register();
        STARLIGHT_RUNE = REGISTRATE
                .item("starlight_rune",ComponentItem::create)
                .cnlang("§9星光§r符文")
                .lang("§9Starlight§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.starlight_rune").withStyle(ChatFormatting.BLUE));
                })))
                .register();
        TWIST_RUNE = REGISTRATE
                .item("twist_rune",ComponentItem::create)
                .cnlang("§c扭曲§r符文")
                .lang("§cTwist§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.twist_rune").withStyle(ChatFormatting.RED));
                })))
                .register();
        QUASAR_RUNE = REGISTRATE
                .item("quasar_rune",ComponentItem::create)
                .cnlang("§k类星体§r符文")
                .lang("§kQuasar§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.quasar_rune").withStyle(ChatFormatting.LIGHT_PURPLE));
                })))
                .register();
        PROLIFERATION_RUNE = REGISTRATE
                .item("proliferation_rune",ComponentItem::create)
                .cnlang("§a增殖§r符文")
                .lang("§aProliferation§r Rune")
                .tag(BotaniaTags.Items.RUNES,CMTags.TIER5_RUNES)
                .onRegister(attach(new TooltipBehavior(list -> {
                    list.add(Component.translatable("ctnh.item.runes.proliferation_rune").withStyle(ChatFormatting.GREEN));
                })))
                .register();
    }
    public static void init() {
        registerItem();
    }
    public static ItemEntry<ThrowItem> BOSS_SUMMONER;
    public static ItemEntry<ThrowItem> ADVANCED_BOSS_SUMMONER;
    public static ItemEntry<BTUpgradeItemT1>BT_UPDATE_T1;
    public static ItemEntry<GTUpgradeItemT1>GT_UPDATE_T1;
    public static ItemEntry<BMUpgradeItemT1>BM_UPDATE_T1;
    public static ItemEntry<ComponentItem> HORIZEN_RUNE;
    public static ItemEntry<ComponentItem> STARLIGHT_RUNE;
    public static ItemEntry<ComponentItem> TWIST_RUNE;
    public static ItemEntry<ComponentItem> QUASAR_RUNE;
    public static ItemEntry<ComponentItem> PROLIFERATION_RUNE;
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

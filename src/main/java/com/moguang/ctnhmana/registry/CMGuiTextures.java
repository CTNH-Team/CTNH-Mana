package com.moguang.ctnhmana.registry;

import com.lowdragmc.lowdraglib.gui.texture.AnimationTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.moguang.ctnhmana.common.gui.AnimationTextureY;

public class CMGuiTextures {
    public static  final ResourceTexture PROGRESS_BAR_MANA_EMPTY=new ResourceTexture("ctnhmana:textures/gui/mana_bar_empty.png");
    public static  final ResourceTexture PROGRESS_BAR_MANA_FULL=new ResourceTexture("ctnhmana:textures/gui/progress_bar_mana_full.png");
    public static final ResourceTexture SHROUND_RING=new ResourceTexture("ctnhmana:textures/gui/shroud/plane_center.png");
    public static final ResourceTexture SHROUND_BACKGROUND=new ResourceTexture("ctnhmana:textures/gui/shroud/shroud_plane_bg.png");
    public static final ResourceTexture DELVE_WINDOW=new ResourceTexture("ctnhmana:textures/gui/shroud/delve_window.png");
    public static final ResourceTexture PATERN_BACKGROUND=new ResourceTexture("ctnhmana:textures/gui/shroud/patern_background.png");
    public static final ResourceTexture QUADRANT_NEUTRAL=new ResourceTexture("ctnhmana:textures/gui/shroud/quadrant_neutral.png");
    public static final ResourceTexture QUADRANT_NEUTRAL_SELECTED=new ResourceTexture("ctnhmana:textures/gui/shroud/quadrant_neutral_selected.png");
    public static final ResourceTexture QUADRANT_NEUTRAL_SELECTED_BORDER=new ResourceTexture("ctnhmana:textures/gui/shroud/quadrant_neutral_selected_border.png");//中性象限
    public static final ResourceTexture TEST_ACTIVE_BAR=new ResourceTexture("ctnhmana:models/block/machine/part/manahatch.json");
    public static  final ResourceTexture PROGRESS_BAR_BT_MANA_HATCH_EMPTY=new ResourceTexture("ctnhmana:textures/gui/bar/bt_bar_empty.png");
    public static  final AnimationTexture PROGRESS_BAR_BT_MANA_HATCH_DYNAMIC=new AnimationTextureY("ctnhmana:textures/gui/bar/bt_bar_full.png").setCellSize(7).setAnimation(0,6).setAnimation(5);
    public static  final ResourceTexture PROGRESS_BAR_BM_MANA_HATCH_EMPTY=new ResourceTexture("ctnhmana:textures/gui/bar/bm_bar_empty.png");
    public static  final AnimationTexture PROGRESS_BAR_BM_MANA_HATCH_DYNAMIC=new AnimationTextureY("ctnhmana:textures/gui/bar/bm_bar_full.png").setCellSize(7).setAnimation(0,6).setAnimation(5);
    public static  final ResourceTexture PROGRESS_BAR_MANA_HATCH_EMPTY=new ResourceTexture("ctnhmana:textures/gui/bar/mana_bar_empty.png");
    public static  final AnimationTexture PROGRESS_BAR_MANA_HATCH_DYNAMIC=new AnimationTextureY("ctnhmana:textures/gui/bar/mana_bar_full.png").setCellSize(7).setAnimation(0,6).setAnimation(5);
    public static final ResourceTexture MACHINE_STATUS_ICON=new ResourceTexture("ctnhmana:textures/gui/status_icon.png");
    public static final ResourceTexture SPIRE_CONNECT_OFF = new ResourceTexture("ctnhmana:textures/gui/spire_mode/connect_off.png");
    public static final ResourceTexture SPIRE_CONNECT_ON = new ResourceTexture("ctnhmana:textures/gui/spire_mode/connect_on.png");
    public static final ResourceTexture SPIRE_FOCUS_OFF = new ResourceTexture("ctnhmana:textures/gui/spire_mode/focus_off.png");
    public static final ResourceTexture SPIRE_FOCUS_ON = new ResourceTexture("ctnhmana:textures/gui/spire_mode/focus_on.png");
    public static final ResourceTexture SPIRE_GLOBAL_ON = new ResourceTexture("ctnhmana:textures/gui/spire_mode/global_send_on.png");
    public static final ResourceTexture SPIRE_GLOBAL_OFF = new ResourceTexture("ctnhmana:textures/gui/spire_mode/global_send_off.png");
    public static final ResourceTexture SPIRE_SPARK_SEND_OFF = new ResourceTexture("ctnhmana:textures/gui/spire_mode/spark_send_off.png");
    public static final ResourceTexture SPIRE_SPARK_SEND_ON = new ResourceTexture("ctnhmana:textures/gui/spire_mode/spark_send_on.png");
    public static final ResourceTexture SPIRE_ANIMATION_OFF = new ResourceTexture("ctnhmana:textures/gui/spire_mode/animation_off.png");
    public static final ResourceTexture SPIRE_ANIMATION_ON = new ResourceTexture("ctnhmana:textures/gui/spire_mode/animation_on.png");
    public static final ResourceTexture ETERNAL_WOS_DIFFUSION_ON=new ResourceTexture("ctnhmana:textures/gui/eternal_wos_mode/diffusion_on.png");
    public static final ResourceTexture ETERNAL_WOS_DIFFUSION_OFF=new ResourceTexture("ctnhmana:textures/gui/eternal_wos_mode/diffusion_off.png");

    public static final ResourceTexture BT_BACKGROUND=new ResourceTexture("ctnhmana:textures/gui/background/botania_background.png");
    public static final ResourceTexture BM_BACKGROUND=new ResourceTexture("ctnhmana:textures/gui/background/bloodmagic_background.png");
    public static final ResourceTexture ARS_BACKGROUND=new ResourceTexture("ctnhmana:textures/gui/background/ars_background.png");
    public static final ResourceTexture GT_BACKGROUND=new ResourceTexture("ctnhmana:textures/gui/background/gt_background.png");
    public static final ResourceTexture AHCC_BACKGROUND=new ResourceTexture("ctnhmana:textures/gui/background/ahcc_background_a.png");
    public static final ResourceTexture INDEX_TARGET=new ResourceTexture("ctnhmana:textures/gui/index_target.png");

    /** 槽位图标：仅限戒指（如魔力戒指） */
    public static final ResourceTexture SLOT_RING = new ResourceTexture("ctnhmana:textures/gui/slots/ring_slots.png");
    /** 槽位图标：仅限宝珠（如血魔法宝珠） */
    public static final ResourceTexture SLOT_ORB = new ResourceTexture("ctnhmana:textures/gui/slots/orb_slots.png");
}
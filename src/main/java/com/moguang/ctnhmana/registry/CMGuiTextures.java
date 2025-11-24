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
    public static  final ResourceTexture PROGRESS_BAR_MANA_HATCH_EMPTY=new ResourceTexture("ctnhmana:textures/gui/mana_bar_empty.png");
    public static  final AnimationTexture PROGRESS_BAR_MANA_HATCH_DYNAMIC=new AnimationTextureY("ctnhmana:textures/gui/mana_bar_full.png").setCellSize(7).setAnimation(0,6).setAnimation(5);


}

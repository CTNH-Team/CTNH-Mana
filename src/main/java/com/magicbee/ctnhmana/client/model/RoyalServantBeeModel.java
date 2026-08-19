package com.magicbee.ctnhmana.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.util.Mth;

import com.magicbee.ctnhmana.common.entity.RoyalServantBee;

/**
 * 皇家侍从蜜蜂模型：几何与动画参考巨蜂模型（{@link GiantBeeModel}）。
 * 修复旧版歪斜（bone 绕 Y 轴旋转 90°）与翅膀错位：扇翅/收翅/悬停微俯仰/上下浮动与巨蜂一致，
 * 身体朝向完全由实体自身旋转驱动，不再斜着追踪。
 */
public class RoyalServantBeeModel extends HierarchicalModel<RoyalServantBee> {

    private final ModelPart bone;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart frontLeg;
    private final ModelPart midLeg;
    private final ModelPart backLeg;
    private final ModelPart stinger;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;

    private float rollAmount;

    public RoyalServantBeeModel(ModelPart root) {
        this.bone = root.getChild("bone");
        ModelPart body = this.bone.getChild("body");
        this.stinger = body.getChild("stinger");
        this.leftAntenna = body.getChild("left_antenna");
        this.rightAntenna = body.getChild("right_antenna");
        this.rightWing = this.bone.getChild("right_wing");
        this.leftWing = this.bone.getChild("left_wing");
        this.frontLeg = this.bone.getChild("front_legs");
        this.midLeg = this.bone.getChild("middle_legs");
        this.backLeg = this.bone.getChild("back_legs");
    }

    /** 几何与巨蜂一致（原版蜜蜂部件） */
    public static LayerDefinition createBodyLayer() {
        return GiantBeeModel.createBodyLayer();
    }

    @Override
    public ModelPart root() {
        return this.bone;
    }

    @Override
    public void prepareMobModel(RoyalServantBee bee, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(bee, limbSwing, limbSwingAmount, partialTick);
        this.rollAmount = bee.rollAmount;
    }

    @Override
    public void setupAnim(RoyalServantBee bee, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        // 与巨蜂模型完全一致的动画逻辑
        this.rightWing.xRot = 0.0F;
        this.leftAntenna.xRot = 0.0F;
        this.rightAntenna.xRot = 0.0F;
        this.bone.xRot = 0.0F;
        boolean grounded = bee.onGround() && bee.getDeltaMovement().lengthSqr() < 1.0E-7D;
        if (grounded) {
            // 落地静止：收翅
            this.rightWing.yRot = -0.2618F;
            this.rightWing.zRot = 0.0F;
            this.leftWing.xRot = 0.0F;
            this.leftWing.yRot = 0.2618F;
            this.leftWing.zRot = 0.0F;
            this.frontLeg.xRot = 0.0F;
            this.midLeg.xRot = 0.0F;
            this.backLeg.xRot = 0.0F;
        } else {
            // 飞行：扇翅（绕 Z 轴上下扑动，与原版蜜蜂一致）
            float wingFlap = ageInTicks * 120.32113F * 0.017453292F;
            this.rightWing.yRot = 0.0F;
            this.rightWing.zRot = Mth.cos(wingFlap) * (float) Math.PI * 0.15F;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
            this.frontLeg.xRot = 0.7853982F;
            this.midLeg.xRot = 0.7853982F;
            this.backLeg.xRot = 0.7853982F;
            // 身体朝向不动（由实体自身 yRot 驱动），避免斜着追踪
            this.bone.xRot = 0.0F;
            this.bone.yRot = 0.0F;
            this.bone.zRot = 0.0F;
        }
        // 悬停微俯仰 + 触角摆动 + 上下浮动（与原版蜜蜂一致）
        this.bone.xRot = 0.0F;
        this.bone.yRot = 0.0F;
        this.bone.zRot = 0.0F;
        if (!grounded) {
            float f = Mth.cos(ageInTicks * 0.18F);
            this.bone.xRot = 0.1F + f * (float) Math.PI * 0.025F;
            this.leftAntenna.xRot = f * (float) Math.PI * 0.03F;
            this.rightAntenna.xRot = f * (float) Math.PI * 0.03F;
            this.frontLeg.xRot = -f * (float) Math.PI * 0.1F + 0.3926991F;
            this.backLeg.xRot = -f * (float) Math.PI * 0.05F + 0.7853982F;
            this.bone.y = 19.0F - Mth.cos(ageInTicks * 0.18F) * 0.9F;
        }
        if (this.rollAmount > 0.0F) {
            this.bone.xRot = ModelUtils.rotlerpRad(this.bone.xRot, 0.25F, this.rollAmount);
        }
    }
}

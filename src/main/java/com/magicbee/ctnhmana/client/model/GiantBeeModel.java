package com.magicbee.ctnhmana.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import com.magicbee.ctnhmana.common.entity.GiantBee;

/**
 * 原版 BeeModel 的适配版：几何/UV/动画与原版蜜蜂一致（可直接复用原版蜜蜂贴图）。
 * 结构：bone(0,19,0) → body（脸上在 body 上，无独立 head）+ 翅膀 + 腿；body → stinger + 触角。
 * 去掉了原版蜜蜂专属状态（hasStung 隐藏蜇刺）。
 */
public class GiantBeeModel extends HierarchicalModel<GiantBee> {

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

    public GiantBeeModel(ModelPart root) {
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

    public static LayerDefinition createBodyLayer() {
        float yBase = 19.0F;
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(),
                PartPose.offset(0.0F, yBase, 0.0F));
        PartDefinition body = bone.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F), PartPose.ZERO);
        body.addOrReplaceChild("stinger",
                CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F), PartPose.ZERO);
        body.addOrReplaceChild("left_antenna",
                CubeListBuilder.create().texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, -2.0F, -5.0F));
        body.addOrReplaceChild("right_antenna",
                CubeListBuilder.create().texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, -2.0F, -5.0F));
        CubeDeformation cubedeformation = new CubeDeformation(0.001F);
        bone.addOrReplaceChild("right_wing",
                CubeListBuilder.create().texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, cubedeformation),
                PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F));
        bone.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F,
                        cubedeformation),
                PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F));
        bone.addOrReplaceChild("front_legs",
                CubeListBuilder.create().addBox("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1),
                PartPose.offset(1.5F, 3.0F, -2.0F));
        bone.addOrReplaceChild("middle_legs",
                CubeListBuilder.create().addBox("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3),
                PartPose.offset(1.5F, 3.0F, 0.0F));
        bone.addOrReplaceChild("back_legs",
                CubeListBuilder.create().addBox("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5),
                PartPose.offset(1.5F, 3.0F, 2.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.bone;
    }

    @Override
    public void prepareMobModel(GiantBee bee, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(bee, limbSwing, limbSwingAmount, partialTick);
        this.rollAmount = bee.rollAmount;
    }

    @Override
    public void setupAnim(GiantBee bee, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                          float headPitch) {
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
            // 飞行/移动：扇翅
            float wingFlap = ageInTicks * 120.32113F * 0.017453292F;
            this.rightWing.yRot = 0.0F;
            this.rightWing.zRot = Mth.cos(wingFlap) * (float) Math.PI * 0.15F;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
            this.frontLeg.xRot = 0.7853982F;
            this.midLeg.xRot = 0.7853982F;
            this.backLeg.xRot = 0.7853982F;
            this.bone.xRot = 0.0F;
            this.bone.yRot = 0.0F;
            this.bone.zRot = 0.0F;
        }
        if (!bee.isAngry()) {
            // 中立：悬停微俯仰 + 触角摆动 + 上下浮动
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
        }
        if (this.rollAmount > 0.0F) {
            // 升空时微微抬头悬停（原版蜜蜂会翻成肚皮朝上，巨蜂不需要）
            this.bone.xRot = ModelUtils.rotlerpRad(this.bone.xRot, 0.25F, this.rollAmount);
        }
    }
}

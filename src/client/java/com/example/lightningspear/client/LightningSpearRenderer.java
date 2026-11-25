package com.example.lightningspear.client;

import com.example.lightningspear.LightningSpearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * Draws the thrown spear with its real 3D item model, pointed along its flight
 * path (the same way vanilla orients a thrown trident).
 */
public class LightningSpearRenderer extends EntityRenderer<LightningSpearEntity, LightningSpearRenderState> {
	private static final int FULL_BRIGHT = 0xF000F0;

	private final ItemModelResolver itemModelResolver;

	public LightningSpearRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemModelResolver = context.getItemModelResolver();
	}

	@Override
	public LightningSpearRenderState createRenderState() {
		return new LightningSpearRenderState();
	}

	@Override
	public void extractRenderState(LightningSpearEntity entity, LightningSpearRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
		state.xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
		this.itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.NONE, entity);
	}

	@Override
	public void submit(LightningSpearRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
		// The model's tip points +Y, so tip -> forward (+X) is -90 degrees about Z.
		poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot - 90.0F));
		state.item.submit(poseStack, collector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, state.outlineColor);
		poseStack.popPose();

		super.submit(state, poseStack, collector, cameraState);
	}
}

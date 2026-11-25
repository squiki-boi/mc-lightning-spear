package com.example.lightningspear.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class LightningSpearRenderState extends EntityRenderState {
	public final ItemStackRenderState item = new ItemStackRenderState();
	public float xRot;
	public float yRot;
}

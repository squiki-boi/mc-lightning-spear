package com.example.lightningspear.client;

import com.example.lightningspear.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class LightningSpearClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRenderers.register(ModEntityTypes.LIGHTNING_SPEAR, LightningSpearRenderer::new);
	}
}

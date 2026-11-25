package com.example.lightningspear;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class LightningSpearMod implements ModInitializer {
	public static final String MOD_ID = "lightning_spear";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModEntityTypes.initialize();
		ModItems.initialize();
	}
}

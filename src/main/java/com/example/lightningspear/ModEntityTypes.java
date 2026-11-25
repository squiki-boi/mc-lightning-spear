package com.example.lightningspear;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntityTypes {
	public static final ResourceKey<EntityType<?>> LIGHTNING_SPEAR_KEY =
			ResourceKey.create(Registries.ENTITY_TYPE, LightningSpearMod.id("lightning_spear"));

	public static final EntityType<LightningSpearEntity> LIGHTNING_SPEAR = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			LIGHTNING_SPEAR_KEY,
			EntityType.Builder.<LightningSpearEntity>of(LightningSpearEntity::new, MobCategory.MISC)
					.sized(0.5F, 0.5F)
					.clientTrackingRange(4)
					.updateInterval(5)
					.build(LIGHTNING_SPEAR_KEY)
	);

	private ModEntityTypes() {
	}

	public static void initialize() {
	}
}

package com.example.lightningspear;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TridentItem;

public final class ModItems {
	public static final ResourceKey<Item> LIGHTNING_SPEAR_KEY =
			ResourceKey.create(Registries.ITEM, LightningSpearMod.id("lightning_spear"));

	// Same melee stats / tool behaviour as a vanilla trident.
	public static final Item LIGHTNING_SPEAR = register(
			LIGHTNING_SPEAR_KEY,
			LightningSpearItem::new,
			new Item.Properties()
					.rarity(Rarity.EPIC)
					.durability(350)
					.attributes(TridentItem.createAttributes())
					.component(DataComponents.TOOL, TridentItem.createToolProperties())
	);

	private ModItems() {
	}

	private static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
		Item item = factory.apply(properties.setId(key));
		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
				.register(tab -> tab.accept(LIGHTNING_SPEAR));
	}
}

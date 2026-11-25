package com.example.lightningspear;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;

/**
 * Extends the vanilla trident item so use animation, charge-up, melee stats and
 * "can't break blocks in creative" all behave exactly like a trident. Only the
 * release (the throw) is replaced so it launches a LightningSpearEntity.
 */
public class LightningSpearItem extends TridentItem {
	private static final int USE_DURATION = 72000;
	private static final int MIN_CHARGE_TICKS = 10;
	private static final float THROW_POWER = 2.5F;

	public LightningSpearItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		if (!(entity instanceof Player player)) {
			return false;
		}

		int charged = USE_DURATION - timeLeft;
		if (charged < MIN_CHARGE_TICKS) {
			return false;
		}

		// Never throw a spear that would break on the throw.
		if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
			return false;
		}

		if (level instanceof ServerLevel serverLevel) {
			boolean creative = player.getAbilities().instabuild;

			// Damage first so the thrown copy carries the wear.
			stack.setDamageValue(stack.getDamageValue() + 1);

			LightningSpearEntity spear = Projectile.spawnProjectileFromRotation(
					LightningSpearEntity::new, serverLevel, stack, player, 0.0F, THROW_POWER, 1.0F);
			spear.setReturnToOwner(!creative);

			if (!creative) {
				player.getInventory().removeItem(stack);
			}

			serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 1.0F, 0.6F);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		return true;
	}

	// Dispensers fire the lightning spear too (it won't return to anyone).
	@Override
	public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
		LightningSpearEntity spear = new LightningSpearEntity(level, position.x(), position.y(), position.z(), stack.copyWithCount(1));
		spear.setReturnToOwner(false);
		return spear;
	}
}

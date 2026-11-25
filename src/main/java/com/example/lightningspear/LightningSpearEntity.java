package com.example.lightningspear;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.projectile.throwableitemprojectile.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * The thrown spear. Flies like a trident, and on any impact calls down a bolt,
 * then returns to its owner's inventory (like Loyalty) - or drops as an
 * invulnerable item if the owner is gone / the inventory is full.
 */
public class LightningSpearEntity extends ThrowableItemProjectile {
	/** If the thrower is closer than this to the impact, the bolt is visual-only. */
	private static final double SAFE_RADIUS = 4.0D;
	private static final float BOLT_DAMAGE = 12.0F;
	private static final int MAX_FLIGHT_TICKS = 300;

	private boolean returnToOwner = true;
	private boolean spent;

	public LightningSpearEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
		super(type, level);
	}

	public LightningSpearEntity(Level level, LivingEntity owner, ItemStack stack) {
		super(ModEntityTypes.LIGHTNING_SPEAR, owner, level, stack);
	}

	public LightningSpearEntity(Level level, double x, double y, double z, ItemStack stack) {
		super(ModEntityTypes.LIGHTNING_SPEAR, x, y, z, level, stack);
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.LIGHTNING_SPEAR;
	}

	public void setReturnToOwner(boolean returnToOwner) {
		this.returnToOwner = returnToOwner;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.level() instanceof ServerLevel serverLevel && !this.isRemoved()) {
			serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
					this.getX(), this.getY(), this.getZ(), 2, 0.1D, 0.1D, 0.1D, 0.02D);

			if (this.tickCount > MAX_FLIGHT_TICKS) {
				this.finish(serverLevel, this.position(), false);
			}
		}
	}

	@Override
	protected void onHit(HitResult hitResult) {
		super.onHit(hitResult);

		if (this.level() instanceof ServerLevel serverLevel) {
			this.finish(serverLevel, hitResult.getLocation(), true);
		}
	}

	private void finish(ServerLevel level, Vec3 at, boolean strike) {
		if (this.spent) {
			return;
		}
		this.spent = true;

		if (strike) {
			this.strike(level, at);
		}
		this.giveBack(level);
		this.discard();
	}

	private void strike(ServerLevel level, Vec3 at) {
		LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
		if (bolt == null) {
			return;
		}

		bolt.setPos(at.x, at.y, at.z);

		Entity owner = this.getOwner();
		boolean ownerTooClose = owner != null && owner.position().distanceToSqr(at) < SAFE_RADIUS * SAFE_RADIUS;
		if (ownerTooClose) {
			bolt.setVisualOnly(true);
		} else {
			bolt.setDamage(BOLT_DAMAGE);
		}
		level.addFreshEntity(bolt);

		level.sendParticles(ParticleTypes.ELECTRIC_SPARK, at.x, at.y, at.z, 40, 0.5D, 0.5D, 0.5D, 0.3D);
	}

	private void giveBack(ServerLevel level) {
		ItemStack stack = this.getItem().copy();
		if (!this.returnToOwner || stack.isEmpty()) {
			return;
		}

		Entity owner = this.getOwner();
		if (owner instanceof ServerPlayer player && player.isAlive()) {
			if (player.getInventory().add(stack)) {
				level.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.5F);
				return;
			}
		}

		// Fallback: drop it, and make it survive its own lightning.
		ItemEntity drop = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), stack);
		drop.setInvulnerable(true);
		drop.setDefaultPickUpDelay();
		level.addFreshEntity(drop);
	}
}

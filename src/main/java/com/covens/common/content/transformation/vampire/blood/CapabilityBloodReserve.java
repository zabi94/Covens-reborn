package com.covens.common.content.transformation.vampire.blood;

import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.api.transformation.IBloodReserve;

import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityBloodReserve implements IBloodReserve {

	@CapabilityInject(IBloodReserve.class)
	public static final Capability<IBloodReserve> CAPABILITY = null;

	private int max_blood, blood;
	@Nullable
	private UUID lastDrinker;

	public CapabilityBloodReserve() {
		this.max_blood = 0;
		this.blood = 0;
		this.lastDrinker = null;
	}

	public static void init() {
		CapabilityManager.INSTANCE.register(IBloodReserve.class, new BloodReserveStorage(), CapabilityBloodReserve::new);
	}

	/**
	 * Gets the maximum amount of blood for that entity
	 *
	 * @return The maximum amount of blood for the entity, if the entity is
	 *         something that shouldn't have blood the max should be set to a
	 *         negative value
	 */
	@Override
	public int getMaxBlood() {
		return this.max_blood;
	}

	@Override
	public int getBlood() {
		if (this.getMaxBlood() >= 0) {
			return this.blood;
		}
		return 0;
	}

	@Override
	@Nullable
	public String getLastDrinker(World world) {
		if (this.lastDrinker != null) {
			try {
				return world.getMinecraftServer().getPlayerProfileCache().getProfileByUUID(this.lastDrinker).getName();
			} catch (NullPointerException e) {
				// There are like a billion things that could be null in that line. I believe
				// this is actually more efficient than checking 4 times for null
			}
		}
		return null;
	}

	@Override
	public UUID getDrinkerUUID() {
		return this.lastDrinker;
	}

	@Override
	public void setBlood(int amount) {
		this.blood = amount;
		if (this.blood > this.getMaxBlood()) {
			this.blood = this.getMaxBlood();
		}
		if (this.blood < 0) {
			this.blood = 0;
		}
	}

	@Override
	public void setMaxBlood(int amount) {
		this.max_blood = amount;
		if (amount <= 0) {
			this.setBlood(0);
		}
	}

	@Override
	public void setDrinker(UUID uuid) {
		this.lastDrinker = uuid;
	}
}

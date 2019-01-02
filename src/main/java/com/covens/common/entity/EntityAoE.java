package com.covens.common.entity;

import com.covens.api.CovensAPI;
import com.covens.api.cauldron.DefaultModifiers;
import com.covens.api.cauldron.IBrewEffect;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.client.fx.ParticleF;
import com.covens.common.Covens;
import com.covens.common.content.cauldron.BrewData.BrewEntry;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAoE extends Entity {

	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityAoE.class, DataSerializers.VARINT);

	private BrewEntry entry = null;
	private int maxLife;

	public EntityAoE(World worldIn) {
		super(worldIn);
		this.setEntityInvulnerable(true);
		this.noClip = true;
		this.isImmuneToFire = true;
		this.width = 1;
		this.height = 1;
	}

	public EntityAoE(World world, BrewEntry data, BlockPos pos) {
		this(world);
		this.entry = data;
		this.dataManager.set(COLOR, this.entry.getPotion().getLiquidColor());
		this.width = this.entry.getModifierList().getLevel(DefaultModifiers.RADIUS).orElse(0) + 1;// FIXME not working
		this.height = this.width;
		this.maxLife = this.getMaxLife();
		this.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void onUpdate() {
		if (!this.world.isRemote) {
			this.firstUpdate = false;
			IBrewEffect pot = CovensAPI.getAPI().getBrewFromPotion(this.entry.getPotion());
			if (pot instanceof IBrewEffectAoEOverTime) {
				IBrewEffectAoEOverTime eff = (IBrewEffectAoEOverTime) pot;
				this.world.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox()).forEach(e -> {
					eff.performEffectAoEOverTime(e, this.entry.getModifierList());
				});

			} else {
				Covens.logger.warn(this.entry.getPotion().getName() + " has no AoE Over Time effect associated");
				this.setDead();
			}

			if (this.ticksExisted >= this.maxLife) {
				this.setDead();
			}
		} else {
			for (int i = 0; i < 20; i++) {
				double pposX = this.posX + ((this.world.rand.nextGaussian() * this.width) / 2);
				double pposY = this.posY + (this.world.rand.nextDouble() * this.height);
				double pposZ = this.posZ + ((this.world.rand.nextGaussian() * this.width) / 2);
				if (this.world.rand.nextBoolean()) {
					Covens.proxy.spawnParticle(ParticleF.CAULDRON_BUBBLE, pposX, pposY, pposZ, 0, 0, 0, this.dataManager.get(COLOR));
				} else {
					this.world.spawnParticle(EnumParticleTypes.END_ROD, pposX, pposY, pposZ, 0, 0, 0);
				}
			}
		}
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(COLOR, Integer.valueOf(0));
	}

	private int getMaxLife() {
		return 100 * (1 + this.entry.getModifierList().getLevel(DefaultModifiers.GAS_CLOUD_DURATION).orElse(0));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		this.entry = new BrewEntry(tag.getCompoundTag("entry"));
		this.dataManager.set(COLOR, this.entry.getPotion().getLiquidColor());
		this.dataManager.setDirty(COLOR);
		this.maxLife = this.getMaxLife();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("entry", this.entry.serializeNBT());
	}

	public static interface IBrewEffectAoEOverTime {
		public void performEffectAoEOverTime(Entity entity, IBrewModifierList modifiers);
	}
}

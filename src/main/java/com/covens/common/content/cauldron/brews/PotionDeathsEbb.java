package com.covens.common.content.cauldron.brews;

import com.covens.api.cauldron.DefaultModifiers;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionDeathsEbb extends BrewMod {

	public PotionDeathsEbb() {
		super("deaths_ebb", true, 0x6c7c59, true, 0, 50);
		this.setIconIndex(5, 1);
	}

	@Override
	public void applyInWorld(World world, BlockPos pos, EnumFacing side, IBrewModifierList modifiers, EntityLivingBase thrower) {
		if (!world.isRemote) {
			int zombies = 1 + modifiers.getLevel(DefaultModifiers.POWER).orElse(0);
			for (int i = 0; i < zombies; i++) {
				EntityZombie zombie = new EntityZombie(world);
				zombie.setPosition(pos.getX(), pos.getY(), pos.getZ());
				world.spawnEntity(zombie);
				this.setZombieAI(zombie);
			}
		}
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase elb, int amplifier, double health) {
		if (elb instanceof EntityZombie) {
			this.setZombieAI(elb);
		} else {
			if (!elb.world.isRemote) {
				int zombies = 1 + amplifier;
				for (int i = 0; i < zombies; i++) {
					EntityZombie zombie = new EntityZombie(elb.world);
					zombie.setPosition(elb.posX, elb.posY, elb.posZ);
					elb.world.spawnEntity(zombie);
					this.setZombieAI(zombie);
				}
			}
		}
	}

	private void setZombieAI(EntityLivingBase elb) {
		elb.getName();// Placeholder code for codacy
	}
}

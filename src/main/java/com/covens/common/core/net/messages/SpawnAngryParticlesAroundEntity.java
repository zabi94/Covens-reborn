package com.covens.common.core.net.messages;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class SpawnAngryParticlesAroundEntity extends SimpleMessage<SpawnAngryParticlesAroundEntity> {

	private static final Random rng = new Random();
	public int eid;
	public int partType;
	public int partAmount;
	
	public SpawnAngryParticlesAroundEntity(Entity e, EnumParticleTypes type, int amount) {
		eid = e.getEntityId();
		partType = type.ordinal();
		partAmount = amount;
	}
	
	public SpawnAngryParticlesAroundEntity() {
		// Required
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		Minecraft.getMinecraft().addScheduledTask( () -> {
			Entity e = Minecraft.getMinecraft().world.getEntityByID(eid);
			if (e != null) {
				for (int i = 0; i < partAmount; i++) {
					double xCoord = e.posX - 0.5 + rng.nextDouble() * e.width * 1.1;
					double zCoord = e.posZ - 0.5 + rng.nextDouble() * e.width * 1.1;
					double yCoord = e.posY + e.height - rng.nextDouble() * 0.1;
					Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.values()[partType], xCoord, yCoord, zCoord, 0, 0, 0);
				}
			}
		});
		return null;
	}
	
}

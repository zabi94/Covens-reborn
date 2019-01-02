package com.covens.common.core.net.messages;

import java.util.Random;

import com.covens.client.fx.ParticleF;
import com.covens.common.Covens;
import com.covens.common.core.net.SimpleMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WitchfireFlame extends SimpleMessage<WitchfireFlame> {

	public BlockPos posA, posB;
	public int color;

	public WitchfireFlame(BlockPos start, BlockPos end, int color) {
		this.posA = start;
		this.posB = end;
		this.color = color;
	}

	public WitchfireFlame() {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IMessage handleMessage(MessageContext context) {
		for (int i = 0; i < 100; i++) {
			Random r = Minecraft.getMinecraft().player.getRNG();
			Covens.proxy.spawnParticle(ParticleF.COLORED_FLAME, this.posA.getX() + r.nextDouble(), this.posA.getY() + (r.nextDouble() * 2), this.posA.getZ() + r.nextDouble(), 0.05 * r.nextGaussian(), 0.1 * r.nextGaussian(), 0.05 * r.nextGaussian(), this.color);
			Covens.proxy.spawnParticle(ParticleF.COLORED_FLAME, this.posB.getX() + r.nextDouble(), this.posB.getY() + (r.nextDouble() * 2), this.posB.getZ() + r.nextDouble(), 0.05 * r.nextGaussian(), 0.1 * r.nextGaussian(), 0.05 * r.nextGaussian(), this.color);
		}
		Minecraft.getMinecraft().world.playSound(this.posB, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 0.5f, 0.9f, false);
		Minecraft.getMinecraft().world.playSound(this.posA, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 0.5f, 0.9f, false);
		return null;
	}
}

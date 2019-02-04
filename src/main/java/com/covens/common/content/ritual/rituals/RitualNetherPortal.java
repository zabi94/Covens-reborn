package com.covens.common.content.ritual.rituals;

import java.util.List;
import java.util.Random;

import com.covens.api.ritual.IRitual;
import com.covens.common.core.util.EmptyTeleporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualNetherPortal implements IRitual {

	private static final EmptyTeleporter tp = new EmptyTeleporter();

	@Override
	public void onFinish(EntityPlayer player, TileEntity tile, World world, BlockPos circlePos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		world.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(effectivePosition)).grow(5)).stream().forEach(p -> p.changeDimension(-1, tp));
	}

	@Override
	public boolean isValid(EntityPlayer player, World world, BlockPos mainGlyphPos, List<ItemStack> recipe, BlockPos effectivePosition, int covenSize) {
		return world.provider.getDimension() == 0;
	}
	
	@Override
	public void onRandomDisplayTick(World world, BlockPos mainGlyphPos, BlockPos ep, Random rng) {
		for (int i=0; i< 20; i++) {
			double cx = ep.getX() + 0.5;
			double cy = ep.getY() + 0.5;
			double cz = ep.getZ() + 0.5;
			double sx = cx + rng.nextGaussian()*0.5;
			double sy = cy + rng.nextGaussian()*0.5;
			double sz = cz + rng.nextGaussian()*0.5;
			world.spawnParticle(EnumParticleTypes.PORTAL, sx, sy, sz, 0.6 * (sx - cx), 0.6 * (sy - cy), 0.6 * (sz - cz));
		}
	}
	
}

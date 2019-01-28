package com.covens.common.content.ritual.rituals;

import com.covens.api.ritual.IRitual;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualSandsTime implements IRitual {

	@Override
	public void onUpdate(EntityPlayer player, TileEntity tile, World world, BlockPos pos, NBTTagCompound data, int ticks, BlockPos effectivePosition, int covenSize) {
		world.setWorldTime(world.getWorldTime() + 5);
	}
}

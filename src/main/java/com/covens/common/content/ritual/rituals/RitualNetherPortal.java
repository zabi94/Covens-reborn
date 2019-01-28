package com.covens.common.content.ritual.rituals;

import java.util.List;

import com.covens.api.ritual.IRitual;
import com.covens.common.core.util.EmptyTeleporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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

}

package com.covens.common.content.ritual.rituals;

import java.util.List;
import java.util.Optional;

import com.covens.api.ritual.IRitual;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class RitualHighMoon implements IRitual {

	@Override
	public void onFinish(EntityPlayer player, TileEntity tile, World world, BlockPos pos, NBTTagCompound tag, BlockPos effectivePosition, int covenSize) {
		if (!world.isRemote) {
			world.setWorldTime(17600);
		}
	}

	@Override
	public Optional<ITextComponent> isValid(EntityPlayer player, World world, BlockPos pos, List<ItemStack> recipe, BlockPos effectivePosition, int covenSize) {
		return world.isDaytime()?Optional.empty():Optional.of(new TextComponentTranslation("ritual.problem.not_daytime"));
	}
}

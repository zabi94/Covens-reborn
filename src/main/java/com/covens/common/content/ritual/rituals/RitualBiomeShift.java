package com.covens.common.content.ritual.rituals;

import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;

import com.covens.api.ritual.IRitual;
import com.covens.common.content.ritual.AdapterIRitual;
import com.covens.common.item.ModItems;
import com.covens.common.world.BiomeChangingUtils.BiomeChangerWalker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class RitualBiomeShift implements IRitual {

	@Override
	public void onFinish(EntityPlayer player, TileEntity tile, World world, BlockPos circlePos, NBTTagCompound data, BlockPos effectivePos, int covenSize) {
		covenSize = 4; // TODO use the actual coven size
		NonNullList<ItemStack> recipe = AdapterIRitual.getItemsUsedForInput(data);
		int id = Biome.getIdForBiome(Biomes.PLAINS);
		for (ItemStack is : recipe) {
			if ((is.getItem() == ModItems.boline) && is.hasTagCompound() && is.getTagCompound().hasKey("biome_id")) {
				id = is.getTagCompound().getInteger("biome_id");
			}
		}
		int radius = (int) MathHelper.sqrt(64 * (1 + (2 * covenSize)) * (1 + (2 * covenSize)));
		BiomeChangerWalker walker = new BiomeChangerWalker(id);
		MutableBlockPos mpos = new MutableBlockPos();
		mpos.setPos(effectivePos.getX() - radius, 0, effectivePos.getZ() - radius);
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				if (((x * x) + (z * z)) <= (radius * radius)) {
					walker.visit(world, mpos);
				}
				mpos.move(EnumFacing.SOUTH);
			}
			mpos.move(EnumFacing.NORTH, (2 * radius) + 1);
			mpos.move(EnumFacing.EAST);
		}
		walker.complete();
	}

	@Override
	@Nonnull
	public ItemStack modifyOutput(ItemStack originalOutput, NonNullList<ItemStack> input, NBTTagCompound tag) {
		if (originalOutput.getItem() == ModItems.boline) {
			Optional<ItemStack> oldBoline = input.parallelStream().filter(is -> is.getItem() == ModItems.boline).findFirst();
			if (oldBoline.isPresent()) {
				ItemStack res = oldBoline.get().copy();
				if (res.attemptDamageItem(50, new Random(), null)) {
					return ItemStack.EMPTY;
				}
				return res;
			} else {
				return ItemStack.EMPTY;
			}
		}
		return originalOutput;
	}

}

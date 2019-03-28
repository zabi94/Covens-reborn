package com.covens.common.content.cauldron.brews;

import com.covens.api.cauldron.DefaultModifiers;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionGrowFlowers extends BrewMod {

	public PotionGrowFlowers() {
		super("grow_flowers", false, 0xff63ff, false, 600, 10);
		this.setIconIndex(7, 0);
	}

	@Override
	public void applyInWorld(World world, BlockPos pos, EnumFacing side, IBrewModifierList modifiers, EntityLivingBase thrower) {
		int amplifier = modifiers.getLevel(DefaultModifiers.POWER).orElse(0);
		int box = 1 + modifiers.getLevel(DefaultModifiers.RADIUS).orElse(0);
		BlockPos posI = pos.add(box, box, box);
		BlockPos posF = pos.add(-box, -box, -box);

		Iterable<BlockPos> spots = BlockPos.getAllInBox(posI, posF);
		for (BlockPos spot : spots) {
			if (world.rand.nextInt(7) <= amplifier && canPlaceBlockHere(world, spot)) {
				world.getBiome(pos).plantFlower(world, world.rand, spot);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return (duration % Math.max(30 - (5 * amplifier), 5)) == 0;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		if (canPlaceBlockHere(entity.world, entity.getPosition())) {
			entity.world.getBiome(entity.getPosition()).plantFlower(entity.world, entity.world.rand, entity.getPosition());
		}
	}
	
	private static boolean canPlaceBlockHere(World world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() == Blocks.GRASS && world.getBlockState(pos).getBlock().isReplaceable(world, pos);
	}

}

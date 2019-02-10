package com.covens.common.content.incantation;

import com.covens.api.incantation.IIncantation;
import com.covens.api.state.StateProperties;
import com.covens.common.block.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class IncantationCandlelight implements IIncantation {

	@Override
	public void cast(EntityPlayer sender, String[] args) {
		World world = sender.getEntityWorld();
		BlockPos source = sender.getPosition();
		for (BlockPos pos : BlockPos.getAllInBox(source.add(7, 3, 7), source.add(-7, -3, -7))) {
			IBlockState state = world.getBlockState(pos);
			boolean flag = false;
			if (state.getBlock() == ModBlocks.candle_medium) {
				world.setBlockState(pos, ModBlocks.candle_medium_lit.getDefaultState().withProperty(StateProperties.COLOR, state.getValue(StateProperties.COLOR)), 3);
				flag = true;
			}
			if (state.getBlock() == ModBlocks.candle_small) {
				world.setBlockState(pos, ModBlocks.candle_small_lit.getDefaultState().withProperty(StateProperties.COLOR, state.getValue(StateProperties.COLOR)), 3);
				flag = true;
			}
			if (flag) {
				for (int i = 0; i < 5; i++) {
					double x = pos.getX() + world.rand.nextFloat();
					double y = pos.getY() + world.rand.nextFloat();
					double z = pos.getZ() + world.rand.nextFloat();
					world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0, 0, 0);
				}
			}
		}
	}

	@Override
	public int getCost() {
		return 100;
	}
}

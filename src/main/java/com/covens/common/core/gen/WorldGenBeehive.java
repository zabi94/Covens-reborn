package com.covens.common.core.gen;

import com.covens.common.block.ModBlocks;
import com.covens.common.core.statics.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenBeehive implements IWorldGenerator {

	private static void generateBeehives(World world, Random random, int chunkX, int chunkZ) {
		final MutableBlockPos variableBlockPos = new BlockPos.MutableBlockPos();
		if (random.nextFloat() < ModConfig.WORLD_GEN.beehive.beehive_gen_chance / 33.0f) {
			int x = chunkX + random.nextInt(16);
			int z = chunkZ + random.nextInt(16);
			int y = world.getHeight(x, z) - 1; // if there is a tree, world height will be just above the top leaves of the tree.
			variableBlockPos.setPos(x, y, z);
			if (!isBlockLeaves(world, variableBlockPos)) {
				return;
			}
			int newY = getHeightOfFirstAirBlockBelowLeaves(world, new MutableBlockPos(x, y, z));
			if (newY < 0) {
				return;
			}
			variableBlockPos.setY(newY);
			world.setBlockState(variableBlockPos, ModBlocks.beehive.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[random.nextInt(4)]), 2);
		}
	}

	private static boolean isBlockLeaves(World world, BlockPos blockPos) {
		IBlockState blockState = world.getBlockState(blockPos);
		final Block block = blockState.getBlock();
		return block.isLeaves(blockState, world, blockPos);
	}

	private static int getHeightOfFirstAirBlockBelowLeaves(World world, MutableBlockPos pos) {
		final IBlockState blockState = world.getBlockState(pos);
		final Block block = blockState.getBlock();
		if (block.isLeaves(blockState, world, pos)) {
			pos.setY(pos.getY()-1);
			return getHeightOfFirstAirBlockBelowLeaves(world, pos);
		}
		if (world.isAirBlock(pos)) {
			return pos.getY();
		}
		return -1;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		final Biome biome = world.getBiomeForCoordsBody(new BlockPos(chunkX, 0, chunkZ));
		if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
			return;
		}
		generateBeehives(world, random, chunkX * 16 + 8, chunkZ * 16 + 8);
	}

}
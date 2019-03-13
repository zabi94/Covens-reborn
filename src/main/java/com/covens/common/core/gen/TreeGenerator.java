package com.covens.common.core.gen;

import java.util.Random;

import com.covens.common.block.natural.tree.BlockModSapling;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class TreeGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		BlockPos chunkCenter = new BlockPos(8 + (chunkX * 16), 0, 8 + (chunkZ * 16));
		int x = (chunkCenter.getX() + random.nextInt(11)) - 5;
		int y = world.getHeight(chunkCenter.getX(), chunkCenter.getZ());
		int z = (chunkCenter.getZ() + random.nextInt(11)) - 5;
		BlockPos terrain = new BlockPos(x, y, z);
		if ((world.getBlockState(terrain.down()).getBlock() == Blocks.GRASS) && (random.nextInt(30) == 0)) {
			switch (random.nextInt(4)) {
				case 0:
					BlockModSapling.generateCypressTree(world, terrain, random);
					return;
				case 1:
					BlockModSapling.generateYewTree(world, terrain, random);
					return;
				case 2:
					BlockModSapling.generateElderTree(world, terrain, random);
					return;
				case 3:
					BlockModSapling.generateJuniperTree(world, terrain, random);
					return;
				default:
					return;
			}
		}

	}
}

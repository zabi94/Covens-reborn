package com.covens.common.block.misc;

import com.covens.common.block.BlockMod;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.lib.LibBlockName;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Joseph on 7/16/2018.
 */
//Todo: Allow grass to grow over this. Also create a class for graveyard dirt, which will act as a slow spawner of undead. Graveyard dirt's spawn rate will only be slightly higher than vanilla undead.
public class BlockPurifyingEarth extends BlockMod {

	public BlockPurifyingEarth() {
		super(LibBlockName.PURIFYING_EARTH, Material.GROUND);
		this.setResistance(1F);
		this.setHardness(1F);
		this.setSoundType(SoundType.GROUND);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof EntityLivingBase && MobHelper.isSupernatural((EntityLivingBase) entityIn)) {
			if (!entityIn.isBurning()) {
				entityIn.setFire(1500);
			}
			super.onEntityWalk(worldIn, pos, entityIn);
		}
	}

}

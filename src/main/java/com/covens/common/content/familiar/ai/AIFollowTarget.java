package com.covens.common.content.familiar.ai;

import com.covens.common.core.util.EntitySyncHelper;
import com.covens.common.core.util.UUIDs;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AIFollowTarget extends FamiliarAIBase {
	
	private EntityLivingBase target;
	private int timeToRecalcPath = 0;
	private PathNavigate petPathfinder;

	public AIFollowTarget(EntityLiving familiarIn) {
		super(familiarIn);
		petPathfinder = familiar.getNavigator();
		setMutexBits(3);
	}
	
	@Override
	public void startExecuting() {
		timeToRecalcPath = 0;
		target = EntitySyncHelper.getEntityAcrossDimensions(getCap().target);
		familiar.getNavigator().setPath(familiar.getNavigator().getPathToEntityLiving(target), 1f);
	}
	
	@Override
	public void resetTask() {
		target = null;
	}
	
	@Override
	public void updateTask() {
		familiar.getLookHelper().setLookPositionWithEntity(target, 10.0F, (float) familiar.getVerticalFaceSpeed());
		if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(target, 1f)) {
                if (!familiar.getLeashed() && !familiar.isRiding()) {
                    if (familiar.getDistanceSq(target) >= 144.0D) {
                        int i = MathHelper.floor(target.posX) - 2;
                        int j = MathHelper.floor(target.posZ) - 2;
                        int k = MathHelper.floor(target.getEntityBoundingBox().minY);
                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
                                    familiar.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), familiar.rotationYaw, familiar.rotationPitch);
                                    this.petPathfinder.clearPath();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
	}
	
	@Override
	public boolean shouldExecute() {
		return !getCap().target.equals(UUIDs.NULL_UUID);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute() && target != null && !target.isDead && target.getEntityWorld().provider.getDimension() == familiar.getEntityWorld().provider.getDimension();
	}
	
	protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        IBlockState iblockstate = familiar.world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(familiar.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(familiar) && familiar.world.isAirBlock(blockpos.up()) && familiar.world.isAirBlock(blockpos.up(2));
    }

}

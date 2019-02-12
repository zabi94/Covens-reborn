package com.covens.common.content.familiar.ai;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import zabi.minecraft.minerva.common.data.UUIDs;

public class AIFollowTarget extends FamiliarAIBase {

	private EntityLivingBase target;
	private int timeToRecalcPath = 0;
	private PathNavigate petPathfinder;

	public AIFollowTarget(EntityLiving familiarIn) {
		super(familiarIn);
		if (!CovensAPI.getAPI().isValidFamiliar(familiarIn)) {
			throw new IllegalArgumentException("I can't add familiar AI to non familiar entities");
		}
		this.petPathfinder = this.familiar.getNavigator();
		this.setMutexBits(3);
	}

	@Override
	public void startExecuting() {
		this.timeToRecalcPath = 0;
		try {
			CapabilityFamiliarCreature.setSitting(familiar, false);
			this.target = this.familiar.world.getEntities(EntityLivingBase.class, e -> !e.isDead && UUIDs.of(e).equals(this.getCap().target)).get(0);
			this.familiar.getNavigator().setPath(this.familiar.getNavigator().getPathToEntityLiving(this.target), 1.2f);
		} catch (ArrayIndexOutOfBoundsException e) {
			this.target = null;
		}
	}

	@Override
	public void resetTask() {
		this.target = null;
	}

	@Override
	public boolean shouldExecute() {
		return this.getCap().hasOwner() && !this.getCap().getTargetUUID().equals(UUIDs.NULL_UUID) && !this.familiar.world.getEntities(EntityLivingBase.class, e -> !e.isDead && UUIDs.of(e).equals(this.getCap().getTargetUUID())).isEmpty();
	}

	@Override
	public void updateTask() {
		this.familiar.getLookHelper().setLookPositionWithEntity(this.target, 10.0F, this.familiar.getVerticalFaceSpeed());
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = 10;
			if (!UUIDs.of(target).equals(getCap().getTargetUUID())) {
				this.target = this.familiar.world.getEntities(EntityLivingBase.class, e -> !e.isDead && UUIDs.of(e).equals(this.getCap().target)).get(0);
				if (target == null) {
					return;
				}
			}
			
			if (this.familiar.getDistanceSq(this.target) < 16D) {
				this.petPathfinder.clearPath();
				return;
			}
			
			if (!CapabilityFamiliarCreature.isSitting(this.familiar)) {
				if (!this.familiar.getLeashed() && !this.familiar.isRiding() && (this.familiar.getDistanceSq(this.target) >= 144.0D || !this.petPathfinder.tryMoveToEntityLiving(this.target, 1.35f))) {
					if (!this.familiar.getLeashed() && !this.familiar.isRiding()) {
						if (this.familiar.getDistanceSq(this.target) >= 144.0D) {
							int i = MathHelper.floor(this.target.posX) - 2;
							int j = MathHelper.floor(this.target.posZ) - 2;
							int k = MathHelper.floor(this.target.getEntityBoundingBox().minY);
							for (int l = 0; l <= 4; ++l) {
								for (int i1 = 0; i1 <= 4; ++i1) {
									if (((l < 1) || (i1 < 1) || (l > 3) || (i1 > 3)) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
										this.familiar.setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, this.familiar.rotationYaw, this.familiar.rotationPitch);
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
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.getCap().target.equals(UUIDs.NULL_UUID) && (this.target != null) && !this.target.isDead && (this.target.getEntityWorld().provider.getDimension() == this.familiar.getEntityWorld().provider.getDimension());
	}

	protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
		BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
		IBlockState iblockstate = this.familiar.world.getBlockState(blockpos);
		return (iblockstate.getBlockFaceShape(this.familiar.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID) && iblockstate.canEntitySpawn(this.familiar) && this.familiar.world.isAirBlock(blockpos.up()) && this.familiar.world.isAirBlock(blockpos.up(2));
	}

}

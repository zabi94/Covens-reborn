package com.covens.common.content.familiar.ai;

import java.lang.ref.WeakReference;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import zabi.minecraft.minerva.common.utils.entity.PlayerHelper;

public class AIFollowOwner extends FamiliarAIBase {

	private WeakReference<EntityPlayer> target;
	private int timeToRecalcPath = 0;
	private PathNavigate petPathfinder;

	public AIFollowOwner(EntityLiving familiarIn) {
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
		CapabilityFamiliarCreature.setSitting(familiar, false);
		if (target == null) {
			EntityPlayer p = PlayerHelper.getPlayerAcrossDimensions(getCap().owner);
			if (p != null) {
				this.target = new WeakReference<>(p);
				this.familiar.getNavigator().setPath(this.familiar.getNavigator().getPathToEntityLiving(p), 1.2f);
			}
		}
	}

	@Override
	public void resetTask() {
		this.target = null;
	}

	@Override
	public boolean shouldExecute() {
		return this.getCap().hasOwner() && !CapabilityFamiliarCreature.isSitting(familiar);
	}

	@Override
	public void updateTask() {
		if (target != null && target.get() != null) {
			this.familiar.getLookHelper().setLookPositionWithEntity(this.target.get(), 10.0F, this.familiar.getVerticalFaceSpeed());
		}
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = 10;
			if (target != null && target.get() != null) {
				EntityPlayer p = target.get();
				if (!this.familiar.getLeashed() && !this.familiar.isRiding() && this.familiar.getDistanceSq(p) >= 16.0D && !this.petPathfinder.tryMoveToEntityLiving(p, 1.3f)) {
					if (!this.familiar.getLeashed() && !this.familiar.isRiding()) {
						if (this.familiar.getDistanceSq(p) >= 16.0D) {
							int i = MathHelper.floor(p.posX) - 2;
							int j = MathHelper.floor(p.posZ) - 2;
							int k = MathHelper.floor(p.getEntityBoundingBox().minY);
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

	protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
		BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
		IBlockState iblockstate = this.familiar.world.getBlockState(blockpos);
		return (iblockstate.getBlockFaceShape(this.familiar.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID) && iblockstate.canEntitySpawn(this.familiar) && this.familiar.world.isAirBlock(blockpos.up()) && this.familiar.world.isAirBlock(blockpos.up(2));
	}

}

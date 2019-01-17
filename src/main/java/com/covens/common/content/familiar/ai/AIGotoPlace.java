package com.covens.common.content.familiar.ai;

import com.covens.api.CovensAPI;
import com.covens.common.core.util.syncTasks.NotificationPlayer;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import zabi.minecraft.minerva.common.utils.entity.EntitySyncHelper;

public class AIGotoPlace extends FamiliarAIBase {

	private int timeToRecalcPath = 0;
	private PathNavigate petPathfinder;
	private boolean failed = false;

	public AIGotoPlace(EntityLiving familiarIn) {
		super(familiarIn);
		if (!CovensAPI.getAPI().isValidFamiliar(familiarIn)) {
			throw new IllegalArgumentException("I can't add familiar AI to non familiar entities");
		}
		this.petPathfinder = this.familiar.getNavigator();
		this.setMutexBits(1);
	}

	@Override
	public void startExecuting() {
		this.timeToRecalcPath = 0;
		failed = false;
		try {
			this.familiar.getNavigator().setPath(this.familiar.getNavigator().getPathToPos(getCap().destination), 1.2f);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	@Override
	public void resetTask() {
		failed = false;
	}

	@Override
	public boolean shouldExecute() {
		return this.getCap().destination != null;
	}

	@Override
	public void updateTask() {
		BlockPos d = getCap().destination;
		if (d != null) {
			if (--this.timeToRecalcPath <= 0) {
				this.timeToRecalcPath = 10;
				if (!this.familiar.getLeashed() && !this.familiar.isRiding() && this.familiar.getDistanceSq(d) >= 16.0D && !this.petPathfinder.tryMoveToXYZ(d.getX(), d.getY(), d.getZ(), 1.2d)) {
					if (!this.familiar.getLeashed() && !this.familiar.isRiding()) {
						if (this.familiar.getDistanceSq(d) < 25.0D) {
							if (this.isTeleportFriendlyBlock(d.getX(), d.getY(), d.getZ())) {
								this.familiar.setLocationAndAngles(d.getX() + 0.5, d.getY(), d.getZ() + 0.5, this.familiar.rotationYaw, this.familiar.rotationPitch);
								this.petPathfinder.clearPath();
								return;
							} else {
								failed = true;
								notifyOwner();
							}
						}
					}
				}
			}
		}
	}

	private void notifyOwner() {
		EntitySyncHelper.executeOnPlayerAvailable(getCap().owner, new NotificationPlayer(new TextComponentTranslation("familiar.command.goto.failed", familiar.getName()), true));
		getCap().destination = null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !failed;
	}

	protected boolean isTeleportFriendlyBlock(int x, int z, int y) {
		BlockPos blockpos = new BlockPos(x + 0.5, y - 1, z + 0.5);
		IBlockState iblockstate = this.familiar.world.getBlockState(blockpos);
		return (iblockstate.getBlockFaceShape(this.familiar.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID) && iblockstate.canEntitySpawn(this.familiar) && this.familiar.world.isAirBlock(blockpos.up()) && this.familiar.world.isAirBlock(blockpos.up(2));
	}

}

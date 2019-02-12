package com.covens.common.content.familiar.ai;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.util.syncTasks.NotificationPlayer;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import zabi.minecraft.minerva.common.utils.entity.EntitySyncHelper;

public class AIGotoPlace extends FamiliarAIBase {

	private int timeToRecalcPath = 0;
	private PathNavigate petPathfinder;
	private boolean failed = false;
	private boolean arrived = false;

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
		this.failed = false;
		try {
			CapabilityFamiliarCreature.setSitting(this.familiar, false);
			this.familiar.getNavigator().setPath(this.familiar.getNavigator().getPathToPos(this.getCap().destination), 1.2f);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	@Override
	public void resetTask() {
		this.getCap().destination = null;
		this.failed = false;
		this.arrived = false;
	}

	@Override
	public boolean shouldExecute() {
		return this.getCap().hasOwner() && (this.getCap().destination != null);
	}

	@Override
	public void updateTask() {
		BlockPos d = this.getCap().destination;
		if (d != null) {
			if (--this.timeToRecalcPath <= 0) {
				this.timeToRecalcPath = 10;
				if (d.distanceSq(this.familiar.posX, this.familiar.posY, this.familiar.posZ) < 2) {
					this.arrived = true;
				} else if (!this.familiar.getLeashed() && !this.familiar.isRiding() && ((this.familiar.getDistanceSq(d) >= 144.0D) || !this.petPathfinder.tryMoveToXYZ(d.getX(), d.getY(), d.getZ(), 1.4d))) {
					int destX = MathHelper.floor(d.getX()) - 2;
					int destZ = MathHelper.floor(d.getZ()) - 2;
					int destY = MathHelper.floor(d.getY() + 1);

					for (int dx = 0; dx <= 4; ++dx) {
						for (int dz = 0; dz <= 4; ++dz) {
							if (((dx < 1) || (dz < 1) || (dx > 3) || (dz > 3)) && this.isTeleportFriendlyBlock(destX + dx, destZ, destY + dz)) {
								this.familiar.setLocationAndAngles(destX + dx, destZ, destY + dz, this.familiar.rotationYaw, this.familiar.rotationPitch);
								this.petPathfinder.clearPath();
								return;
							}
						}
					}
					this.failed = true;
					this.notifyOwner();
				}
			}
		}
	}

	private void notifyOwner() {
		EntitySyncHelper.executeOnPlayerAvailable(this.getCap().owner, new NotificationPlayer(new TextComponentTranslation("familiar.command.goto.failed", this.familiar.getName()), true));
		this.getCap().destination = null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.failed && !this.arrived && this.shouldExecute();
	}

	protected boolean isTeleportFriendlyBlock(int x, int z, int y) {
		BlockPos blockpos = new BlockPos(x + 0.5, y - 1, z + 0.5);
		IBlockState iblockstate = this.familiar.world.getBlockState(blockpos);
		return (iblockstate.getBlockFaceShape(this.familiar.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID) && iblockstate.canEntitySpawn(this.familiar) && this.familiar.world.isAirBlock(blockpos.up()) && this.familiar.world.isAirBlock(blockpos.up(2));
	}

}

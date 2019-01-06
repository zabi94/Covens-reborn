package com.covens.common.tile.tiles;

import java.util.UUID;

import com.covens.api.state.StateProperties;
import com.covens.api.transformation.DefaultTransformations;
import com.covens.client.core.event.custom.MimicEvent;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.core.capability.mimic.CapabilityMimicData;
import com.covens.common.core.capability.mimic.IMimicData;
import com.covens.common.core.helper.NBTHelper;
import com.covens.common.item.ModItems;
import com.covens.common.item.magic.ItemTaglock;
import com.covens.common.tile.ModTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityMagicMirror extends ModTileEntity implements ITickable {
	private static final int REFRESH_TIME = 10;
	private static final double SHADE_DISTANCE_1 = 2.0;
	private static final double SHADE_DISTANCE_2 = 3.0;
	private static final double SHADE_DISTANCE_3 = 4.0;

	private int refreshTimer;
	private int shadeType;

	public TileEntityMagicMirror() {
		this.refreshTimer = REFRESH_TIME;
	}

	private void activate(boolean active, double distanceSq) {
		if (!active) {
			this.shadeType = 0;
		} else if (distanceSq <= (SHADE_DISTANCE_1 * SHADE_DISTANCE_1)) {
			this.shadeType = 3;
		} else if (distanceSq <= (SHADE_DISTANCE_2 * SHADE_DISTANCE_2)) {
			this.shadeType = 2;
		} else {
			this.shadeType = 1;
		}
		this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withProperty(StateProperties.MIRROR_VARIANTS, this.shadeType), 3);
		this.syncToClient();
		this.markDirty();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) {
			return false;
		}
		// The "!worldIn.isRemote" is intentional. This code should only run on the
		// client.
		if (!worldIn.isRemote) {
			return true;
		}

		ItemStack held = playerIn.getHeldItem(hand);
		if (ItemStack.areItemsEqual(held, new ItemStack(ModItems.taglock))) {
			UUID victimID = NBTHelper.getUniqueID(held, ItemTaglock.TAGLOCK_ENTITY);
			String victimName = NBTHelper.getString(held, ItemTaglock.TAGLOCK_ENTITY_NAME);
			final IMimicData capability = playerIn.getCapability(CapabilityMimicData.CAPABILITY, null);
			capability.setMimickedPlayerID(victimID);
			capability.setMimickedPlayerName(victimName);
			if (playerIn.getUniqueID().equals(victimID)) {
				capability.setMimicking(false, playerIn);
				MinecraftForge.EVENT_BUS.post(new MimicEvent(playerIn, playerIn.getUniqueID(), playerIn.getName(), true));
			} else {
				capability.setMimicking(true, playerIn);
				MinecraftForge.EVENT_BUS.post(new MimicEvent(playerIn, victimID, victimName, false));
			}
		}
		return true;
	}

	@Override
	public void update() {
		if (this.world.isRemote) {
			return;
		}

		if (this.refreshTimer >= REFRESH_TIME) {
			EntityPlayer closestPlayer = this.world.getClosestPlayer(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SHADE_DISTANCE_3, false);
			if (closestPlayer == null) {
				this.activate(false, -1.0f);
			} else if (closestPlayer.hasCapability(CapabilityTransformation.CAPABILITY, null)) {
				final CapabilityTransformation capability = closestPlayer.getCapability(CapabilityTransformation.CAPABILITY, null);
				if (capability.getType() != DefaultTransformations.VAMPIRE) {
					this.activate(true, closestPlayer.getDistanceSq(this.pos));
				} else {
					this.activate(false, -1.0f);
				}
			} else {
				this.activate(true, closestPlayer.getDistanceSq(this.pos));
			}
			this.refreshTimer = 0;
		}
		this.refreshTimer++;
	}

	public int getShadeType() {
		return this.shadeType;
	}

	@Override
	protected void writeAllModDataNBT(NBTTagCompound tag) {
		tag.setInteger("shadeType", this.shadeType);
	}

	@Override
	protected void readAllModDataNBT(NBTTagCompound tag) {
		this.shadeType = tag.getInteger("shadeType");
	}

	@Override
	protected void writeModSyncDataNBT(NBTTagCompound tag) {
		tag.setInteger("shadeType", this.shadeType);
	}

	@Override
	protected void readModSyncDataNBT(NBTTagCompound tag) {
		this.shadeType = tag.getInteger("shadeType");
	}
}

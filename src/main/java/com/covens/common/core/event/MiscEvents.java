package com.covens.common.core.event;

import com.covens.common.core.capability.bedowner.CapabilityBedOwner;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBed.EnumPartType;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.minerva.common.entity.UUIDs;

@EventBusSubscriber
public class MiscEvents {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBedSet(SleepingLocationCheckEvent evt) {
		TileEntityBed tile = getBedTile(evt.getEntityPlayer().world, evt.getSleepingLocation());
		if (tile != null) {
			CapabilityBedOwner cbo = tile.getCapability(CapabilityBedOwner.CAPABILITY, null);
			if (!cbo.getUuid().equals(UUIDs.of(evt.getEntityPlayer()))) {
				cbo.setPlayer(evt.getEntityPlayer());
				tile.markDirty();
			}
		}
	}

	private static TileEntityBed getBedTile(World world, BlockPos bedPos) {
		IBlockState bedClicked = world.getBlockState(bedPos);
		if (bedClicked.getBlock() != Blocks.BED) {
			return null;
		}
		if (bedClicked.getValue(BlockBed.PART) == EnumPartType.HEAD) {
			return (TileEntityBed) world.getTileEntity(bedPos);
		}
		return getBedTile(world, bedPos.offset(bedClicked.getValue(BlockHorizontal.FACING)));
	}
	
}

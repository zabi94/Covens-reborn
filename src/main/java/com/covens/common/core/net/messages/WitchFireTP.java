package com.covens.common.core.net.messages;

import com.covens.common.block.ModBlocks;
import com.covens.common.block.misc.BlockWitchFire;
import com.covens.common.content.cauldron.teleportCapability.CapabilityCauldronTeleport;
import com.covens.common.core.net.NetworkHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class WitchFireTP extends SimpleMessage<WitchFireTP> {

	public String destination = null;

	public WitchFireTP(String to) {
		this.destination = to;
	}

	public WitchFireTP() {
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayer sender = context.getServerHandler().player;
		CapabilityCauldronTeleport ctp = sender.world.getCapability(CapabilityCauldronTeleport.CAPABILITY, null);
		BlockPos dest = ctp.get(this.destination, sender.world);
		if ((dest != null) && (sender.world.getBlockState(sender.getPosition()).getBlock() == ModBlocks.witchfire)) {
			int color = sender.world.getBlockState(sender.getPosition()).getValue(BlockWitchFire.TYPE).getColor();
			NetworkHandler.HANDLER.sendToAllTracking(new WitchfireFlame(sender.getPosition(), dest, color), sender);
			NetworkHandler.HANDLER.sendTo(new WitchfireFlame(sender.getPosition(), dest, color), (EntityPlayerMP) sender);
			sender.world.setBlockState(sender.getPosition(), ModBlocks.witchfire.getDefaultState(), 3);
			sender.setPositionAndUpdate(dest.getX() + 0.5d, dest.getY() + 0.5, dest.getZ() + 0.5d);
		}
		return null;
	}

}

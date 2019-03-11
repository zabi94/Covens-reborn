package com.covens.common.content.ritual.rituals;

import com.covens.api.infusion.IInfusion;
import com.covens.api.mp.MPContainer;
import com.covens.api.ritual.IRitual;
import com.covens.common.content.infusion.capability.InfusionCapability;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.net.messages.InfusionChangedMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualInfusion implements IRitual {

	private IInfusion type;

	public RitualInfusion(IInfusion type) {
		this.type = type;
	}

	@Override
	public void onFinish(EntityPlayer player, TileEntity tile, World world, BlockPos pos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		if (player == null) {
			return;
		}
		MPContainer pc = player.getCapability(MPContainer.CAPABILITY, null);
		pc.drain(pc.getAmount());
		player.getCapability(InfusionCapability.CAPABILITY, null).setType(this.type);
		if (player instanceof EntityPlayerMP) {
			NetworkHandler.HANDLER.sendTo(new InfusionChangedMessage(player), (EntityPlayerMP) player);
		}
	}
}

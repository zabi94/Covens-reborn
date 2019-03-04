package com.covens.common.core.net.messages;

import java.util.List;

import com.covens.common.content.familiar.FamiliarDescriptor;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.google.common.collect.Lists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.minerva.common.entity.EntityHelper;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class FetchFamiliarsMessage extends SimpleMessage<FetchFamiliarsMessage> {
	
	public FetchFamiliarsMessage() {
		//NO-OP
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayer p = context.getServerHandler().player;
		CapabilityFamiliarOwner owner = p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null);
		List<FamiliarDescriptor> list = Lists.newArrayList();
		owner.familiars.forEach((uuid, desc) -> retrieveAndAdd(p, list, desc));
		return new PlayerFamiliarsDefinition(list);
	}

	private void retrieveAndAdd(EntityPlayer p, List<FamiliarDescriptor> list, FamiliarDescriptor desc) {
		EntityLivingBase e = EntityHelper.getEntityAcrossDimensions(desc.getUuid());
		boolean available = e != null;
		if (!available) {
			
		}
	}
}

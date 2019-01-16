package com.covens.common.core.net.messages;

import java.util.UUID;

import com.covens.api.transformation.IBloodReserve;
import com.covens.common.Covens;
import com.covens.common.content.transformation.vampire.blood.CapabilityBloodReserve;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class EntityInternalBloodChanged extends SimpleMessage<EntityInternalBloodChanged> {

	public UUID id_drainer;
	public int amount, max, entity_id;

	public EntityInternalBloodChanged() {
	}

	public EntityInternalBloodChanged(EntityLivingBase entity) {
		this.entity_id = entity.getEntityId();
		IBloodReserve data = entity.getCapability(CapabilityBloodReserve.CAPABILITY, null);
		this.amount = data.getBlood();
		this.max = data.getMaxBlood();
		this.id_drainer = data.getDrinkerUUID();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IMessage handleMessage(MessageContext context) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			if (Minecraft.getMinecraft().world != null) {
				EntityLivingBase ent = (EntityLivingBase) Minecraft.getMinecraft().world.getEntityByID(this.entity_id);
				if (ent != null) {
					IBloodReserve br = ent.getCapability(CapabilityBloodReserve.CAPABILITY, null);
					br.setMaxBlood(this.max); // Max blood before blood!
					br.setBlood(this.amount);// Blood after max blood!
					br.setDrinker(this.id_drainer);
				}
			} else {
				Covens.logger.warn("Couldn't find entity " + this.entity_id + " for EntityInternalBloodChanged message");
			}
		});
		return null;
	}

}

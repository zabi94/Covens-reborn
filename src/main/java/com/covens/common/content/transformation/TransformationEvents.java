package com.covens.common.content.transformation;

import com.covens.api.CovensAPI;
import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.actionbar.HotbarAction;
import com.covens.common.content.transformation.vampire.CapabilityVampire;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.net.messages.EntityInternalBloodChanged;
import com.covens.common.core.net.messages.PlayerTransformationChangedMessage;
import com.covens.common.potion.ModPotions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@Mod.EventBusSubscriber
public class TransformationEvents {

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone event) {
		CapabilityTransformation data = event.getOriginal().getCapability(CapabilityTransformation.CAPABILITY, null);
		CovensAPI.getAPI().setTypeAndLevel(event.getEntityPlayer(), data.getType(), data.getLevel(), false);
		if (event.isWasDeath()) {
			event.getEntityPlayer().getCapability(CapabilityVampire.CAPABILITY, null).setBlood(50);
			if (data.getType() == DefaultTransformations.VAMPIRE) {
				event.getEntityPlayer().addPotionEffect(new PotionEffect(ModPotions.sun_ward, 600, 0));
			}
		} else {
			int oldBlood = event.getOriginal().getCapability(CapabilityVampire.CAPABILITY, null).blood;
			event.getEntityPlayer().getCapability(CapabilityVampire.CAPABILITY, null).setBlood(oldBlood);
		}
		HotbarAction.refreshActions(event.getEntityPlayer(), event.getEntityPlayer().world);
	}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerLoggedInEvent evt) {
		if (evt.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) evt.player;
			NetworkHandler.HANDLER.sendTo(new PlayerTransformationChangedMessage(player), player);
			NetworkHandler.HANDLER.sendTo(new EntityInternalBloodChanged(player), player);
		}
	}
}

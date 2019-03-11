package com.covens.common.core.capability;

import com.covens.api.mp.MPContainer;
import com.covens.common.content.cauldron.teleportCapability.CapabilityCauldronTeleport;
import com.covens.common.content.crystalBall.capability.CapabilityFortune;
import com.covens.common.content.infusion.capability.InfusionCapability;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.content.transformation.vampire.CapabilityVampire;
import com.covens.common.content.transformation.vampire.blood.CapabilityBloodReserve;
import com.covens.common.content.transformation.werewolf.CapabilityWerewolfStatus;
import com.covens.common.core.capability.altar.AltarCapabilities;
import com.covens.common.core.capability.energy.MagicPowerConsumer;
import com.covens.common.core.capability.energy.MagicPowerContainer;
import com.covens.common.core.capability.energy.MagicPowerUsingItem;
import com.covens.common.core.capability.energy.player.expansion.CapabilityMPExpansion;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.capability.mimic.CapabilityMimicData;
import com.covens.common.core.capability.simple.BarkCapability;
import com.covens.common.lib.LibMod;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import zabi.minecraft.minerva.common.capability.SimpleCapability;

@Mod.EventBusSubscriber
public class ModCapabilities {

	public static void preInit() {
		CapabilityFortune.init();
		InfusionCapability.init();
		MagicPowerUsingItem.init();
		MagicPowerContainer.init();
		MagicPowerConsumer.init();
		CapabilityBloodReserve.init();
		CapabilityCauldronTeleport.init();
		CapabilityMimicData.init();
		CapabilityMPExpansion.init();
		AltarCapabilities.init();
		SimpleCapability.preInit(BarkCapability.class);
		SimpleCapability.preInit(CapabilityWerewolfStatus.class);
		SimpleCapability.preInit(CapabilityTransformation.class);
		SimpleCapability.preInit(CapabilityVampire.class);
		SimpleCapability.preInit(CapabilityFamiliarCreature.class);
		SimpleCapability.preInit(CapabilityFamiliarOwner.class);
	}
	
	public static void init() {
		SimpleCapability.init(BarkCapability.class, LibMod.MOD_ID, BarkCapability.CAPABILITY, BarkCapability.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityWerewolfStatus.class, LibMod.MOD_ID, CapabilityWerewolfStatus.CAPABILITY, CapabilityWerewolfStatus.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityTransformation.class, LibMod.MOD_ID, CapabilityTransformation.CAPABILITY, CapabilityTransformation.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityVampire.class, LibMod.MOD_ID, CapabilityVampire.CAPABILITY, CapabilityVampire.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityFamiliarCreature.class, LibMod.MOD_ID, CapabilityFamiliarCreature.CAPABILITY, CapabilityFamiliarCreature.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityFamiliarOwner.class, LibMod.MOD_ID, CapabilityFamiliarOwner.CAPABILITY, CapabilityFamiliarOwner.DEFAULT_INSTANCE);
		AltarCapabilities.loadObjects();
	}
	
	@SubscribeEvent
	public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
		copyDataOnPlayerRespawn(event, CapabilityMPExpansion.CAPABILITY);
		copyDataOnPlayerRespawn(event, MPContainer.CAPABILITY);
		copyDataOnPlayerRespawn(event, CapabilityFortune.CAPABILITY);
		copyDataOnPlayerRespawn(event, CapabilityFamiliarOwner.CAPABILITY);
		copyDataOnPlayerRespawn(event, CapabilityWerewolfStatus.CAPABILITY);
	}
	
	@SubscribeEvent
	public static void onPlayerDimensionChange(PlayerChangedDimensionEvent evt) {
//		HotbarAction.refreshActions(evt.player, evt.player.world);
	}
	
	public static <T> void copyDataOnPlayerRespawn(PlayerEvent.Clone event, Capability<T> c) {
		c.getStorage().readNBT(c, event.getEntityPlayer().getCapability(c, null), null, c.writeNBT(event.getOriginal().getCapability(c, null), null));
	}

}

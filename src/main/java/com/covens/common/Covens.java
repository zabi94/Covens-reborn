package com.covens.common;

import static com.covens.common.lib.LibMod.MOD_NAME;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.covens.common.api.ApiInstance;
import com.covens.common.block.ModBlocks;
import com.covens.common.block.natural.plants.BlockMoonbell;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.content.cauldron.CauldronRegistry;
import com.covens.common.content.cauldron.ModBrewModifiers;
import com.covens.common.content.cauldron.teleportCapability.CapabilityCauldronTeleport;
import com.covens.common.content.crystalBall.ModFortunes;
import com.covens.common.content.crystalBall.capability.CapabilityFortune;
import com.covens.common.content.incantation.ModIncantations;
import com.covens.common.content.infusion.ModInfusions;
import com.covens.common.content.infusion.capability.InfusionCapability;
import com.covens.common.content.ritual.ModRituals;
import com.covens.common.content.spell.ModSpells;
import com.covens.common.content.tarot.ModTarots;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.content.transformation.ModTransformations;
import com.covens.common.content.transformation.vampire.CapabilityVampire;
import com.covens.common.content.transformation.vampire.blood.CapabilityBloodReserve;
import com.covens.common.content.transformation.werewolf.CapabilityWerewolfStatus;
import com.covens.common.core.capability.energy.MagicPowerConsumer;
import com.covens.common.core.capability.energy.MagicPowerContainer;
import com.covens.common.core.capability.energy.MagicPowerUsingItem;
import com.covens.common.core.capability.energy.player.expansion.CapabilityMPExpansion;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.capability.mimic.CapabilityMimicData;
import com.covens.common.core.capability.simple.BarkCapability;
import com.covens.common.core.command.CommandCreateTaglock;
import com.covens.common.core.command.CommandForceFortune;
import com.covens.common.core.command.CommandFortuneActivator;
import com.covens.common.core.command.CommandIncantation;
import com.covens.common.core.command.CommandTransformationModifier;
import com.covens.common.core.event.LootTableEventHandler;
import com.covens.common.core.gen.ModGen;
import com.covens.common.core.helper.CropHelper;
import com.covens.common.core.helper.Log;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.net.GuiHandler;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.proxy.ISidedProxy;
import com.covens.common.core.statics.ModLootTables;
import com.covens.common.core.util.Watchdog;
import com.covens.common.crafting.FrostFireRecipe;
import com.covens.common.crafting.ModDistilleryRecipes;
import com.covens.common.crafting.ModOvenSmeltingRecipes;
import com.covens.common.crafting.ModSpinningThreadRecipes;
import com.covens.common.entity.ModEntities;
import com.covens.common.integration.extraalchemy.ExtraAlchemy;
import com.covens.common.integration.optifine.Optifine;
import com.covens.common.integration.patchouli.Patchouli;
import com.covens.common.integration.thaumcraft.ThaumcraftLoader;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibMod;
import com.covens.common.potion.ModPotions;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zabi.minecraft.minerva.common.capability.SimpleCapability;

@Mod(modid = LibMod.MOD_ID, name = MOD_NAME, version = LibMod.MOD_VER, dependencies = LibMod.DEPENDENCIES, acceptedMinecraftVersions = "[1.12,1.13]", certificateFingerprint = LibMod.FINGERPRINT)
public class Covens {

	public static final Logger logger = LogManager.getLogger(MOD_NAME);
	
	@SidedProxy(serverSide = LibMod.PROXY_COMMON, clientSide = LibMod.PROXY_CLIENT)
	public static ISidedProxy proxy;
	@Instance(LibMod.MOD_ID)
	public static Covens instance;

	static {
		Watchdog.init();
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new LootTableEventHandler());
		ApiInstance.initAPI();
		MobHelper.init();
		CapabilityFortune.init();
		InfusionCapability.init();
		MagicPowerUsingItem.init();
		MagicPowerContainer.init();
		MagicPowerConsumer.init();
		CapabilityBloodReserve.init();
		CapabilityCauldronTeleport.init();
		CapabilityMimicData.init();
		CapabilityMPExpansion.init();
		ModAbilities.dummyMethodToLoadClass();
		SimpleCapability.preInit(BarkCapability.class);
		SimpleCapability.preInit(CapabilityWerewolfStatus.class);
		SimpleCapability.preInit(CapabilityTransformation.class);
		SimpleCapability.preInit(CapabilityVampire.class);
		SimpleCapability.preInit(CapabilityFamiliarCreature.class);
		SimpleCapability.preInit(CapabilityFamiliarOwner.class);
		NetworkHandler.init();
		ModInfusions.init();
		ModTransformations.init();
		ModEntities.init();
		ModSpells.init();
		ModFortunes.init();
		ModLootTables.registerLootTables();
		FrostFireRecipe.init();
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		NetworkRegistry.INSTANCE.registerGuiHandler(Covens.instance, new GuiHandler());
		ModPotions.init();
		SimpleCapability.init(BarkCapability.class, LibMod.MOD_ID, BarkCapability.CAPABILITY, BarkCapability.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityWerewolfStatus.class, LibMod.MOD_ID, CapabilityWerewolfStatus.CAPABILITY, CapabilityWerewolfStatus.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityTransformation.class, LibMod.MOD_ID, CapabilityTransformation.CAPABILITY, CapabilityTransformation.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityVampire.class, LibMod.MOD_ID, CapabilityVampire.CAPABILITY, CapabilityVampire.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityFamiliarCreature.class, LibMod.MOD_ID, CapabilityFamiliarCreature.CAPABILITY, CapabilityFamiliarCreature.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityFamiliarOwner.class, LibMod.MOD_ID, CapabilityFamiliarOwner.CAPABILITY, CapabilityFamiliarOwner.DEFAULT_INSTANCE);
		ModItems.init();
		ModBlocks.init();
		ModTarots.init();
		CauldronRegistry.init();
		ModDistilleryRecipes.init();
		CropHelper.initSeedDrops();
		ModGen.init();
		ModSpinningThreadRecipes.init();
		ModOvenSmeltingRecipes.init();
		ModRituals.init();
		ModBrewModifiers.init();
		ThaumcraftLoader.init();
		Patchouli.init();
		CauldronRegistry.postInit();
		ExtraAlchemy.init();
		Optifine.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).parallelStream().filter(b -> BiomeDictionary.hasType(b, BiomeDictionary.Type.DENSE)).forEach(b -> {
			BlockMoonbell.addValidMoonbellBiome(b);
		});
	}

	@EventHandler
	public void start(FMLServerStartingEvent event) {
		ModIncantations.init();
		event.registerServerCommand(new CommandIncantation());
		event.registerServerCommand(new CommandTransformationModifier());
		event.registerServerCommand(new CommandFortuneActivator());
		event.registerServerCommand(new CommandForceFortune());
		event.registerServerCommand(new CommandCreateTaglock());
	}

	@EventHandler
	public void setFlight(FMLServerStartedEvent evt) {
		FMLCommonHandler.instance().getMinecraftServerInstance().setAllowFlight(true);
	}

	@EventHandler
	public void fingerprintViolation(FMLFingerprintViolationEvent evt) {
		if (!"true".equals(System.getProperty("ignoreCovensFingerprint"))) {
			throw new FingerprintViolationException();
		} else {
			Log.w("WARNING: Covens signature mismatch!");
			Log.w("Ignoring as per launch option");
		}
	}

	@SuppressWarnings("serial")
	private static class FingerprintViolationException extends RuntimeException {
		public FingerprintViolationException() {
			super("\n\n!! WARNING:\n\nThe mod " + LibMod.MOD_NAME + " has an invalid signature, this is likely due to someone messing with the jar without permission.\nThe execution will be stopped in order to prevent damages to your system.\n" + "If you wish to continue executing, please add -DignoreCovensFingerprint=true to your launch arguments\n\n");
		}
	}
}

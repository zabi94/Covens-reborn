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
import com.covens.common.core.capability.mimic.CapabilityMimicData;
import com.covens.common.core.capability.simple.BarkCapability;
import com.covens.common.core.capability.simple.SimpleCapability;
import com.covens.common.core.command.CommandCreateTaglock;
import com.covens.common.core.command.CommandForceFortune;
import com.covens.common.core.command.CommandFortuneActivator;
import com.covens.common.core.command.CommandIncantation;
import com.covens.common.core.command.CommandTransformationModifier;
import com.covens.common.core.event.LootTableEventHandler;
import com.covens.common.core.gen.ModGen;
import com.covens.common.core.helper.CropHelper;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.proxy.ISidedProxy;
import com.covens.common.core.statics.ModLootTables;
import com.covens.common.core.util.Watchdog;
import com.covens.common.crafting.FrostFireRecipe;
import com.covens.common.crafting.ModDistilleryRecipes;
import com.covens.common.crafting.ModOvenSmeltingRecipes;
import com.covens.common.crafting.ModSpinningThreadRecipes;
import com.covens.common.entity.ModEntities;
import com.covens.common.integration.patchouli.Patchouli;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibMod;
import com.covens.common.potion.ModPotions;
import com.covens.common.world.EntityPlacementHelper;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * This class was created by <Arekkuusu> on 26/02/2017.
 * It's distributed as part of Covens under
 * the MIT license.
 */
@SuppressWarnings("WeakerAccess")
@Mod(modid = LibMod.MOD_ID, name = MOD_NAME, version = LibMod.MOD_VER, dependencies = LibMod.DEPENDENCIES, acceptedMinecraftVersions = "[1.12,1.13]", certificateFingerprint = "@FINGERPRINT@")
public class Covens {

	public static final Logger logger = LogManager.getLogger(MOD_NAME);
	//Constants
	public static final String TAGLOCK_ENTITY = "tag_entity";
	public static final String TAGLOCK_ENTITY_NAME = "tag_entity_name";
	//States
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	public static final PropertyEnum<BlockStairs.EnumHalf> HALF = PropertyEnum.create("half", BlockStairs.EnumHalf.class);
	@SidedProxy(serverSide = LibMod.PROXY_COMMON, clientSide = LibMod.PROXY_CLIENT)
	public static ISidedProxy proxy;
	@Instance(LibMod.MOD_ID)
	public static Covens instance;

	static {
		FluidRegistry.enableUniversalBucket();
		Watchdog.init();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new LootTableEventHandler());
		ApiInstance.initAPI();
		MobHelper.init();
		SimpleCapability.setup(NetworkHandler.HANDLER);
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
		NetworkHandler.init();
		ModInfusions.init();
		ModTransformations.init();
		ModEntities.init();
		ModSpells.init();
		ModFortunes.init();
		ModLootTables.registerLootTables();
		FrostFireRecipe.init();
		proxy.preInit(event);
		EntityPlacementHelper.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		ModPotions.init();
		SimpleCapability.init(BarkCapability.class, LibMod.MOD_ID, BarkCapability.CAPABILITY, BarkCapability.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityWerewolfStatus.class, LibMod.MOD_ID, CapabilityWerewolfStatus.CAPABILITY, CapabilityWerewolfStatus.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityTransformation.class, LibMod.MOD_ID, CapabilityTransformation.CAPABILITY, CapabilityTransformation.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityVampire.class, LibMod.MOD_ID, CapabilityVampire.CAPABILITY, CapabilityVampire.DEFAULT_INSTANCE);
		SimpleCapability.init(CapabilityFamiliarCreature.class, LibMod.MOD_ID, CapabilityFamiliarCreature.CAPABILITY, CapabilityFamiliarCreature.DEFAULT_INSTANCE);
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
		Patchouli.init();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).parallelStream().filter(b -> BiomeDictionary.hasType(b, BiomeDictionary.Type.DENSE)).forEach(b -> {
			BlockMoonbell.addValidMoonbellBiome(b);
		});

		CauldronRegistry.postInit();
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
}

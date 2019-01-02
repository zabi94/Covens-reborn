package com.covens.common.potion;

import com.covens.common.content.cauldron.brews.PotionAbsence;
import com.covens.common.content.cauldron.brews.PotionArrowDeflection;
import com.covens.common.content.cauldron.brews.PotionBaneArthropods;
import com.covens.common.content.cauldron.brews.PotionCorruption;
import com.covens.common.content.cauldron.brews.PotionCursedLeaping;
import com.covens.common.content.cauldron.brews.PotionDeathsEbb;
import com.covens.common.content.cauldron.brews.PotionDemonsbane;
import com.covens.common.content.cauldron.brews.PotionDisrobing;
import com.covens.common.content.cauldron.brews.PotionEnderInhibition;
import com.covens.common.content.cauldron.brews.PotionExtinguishFire;
import com.covens.common.content.cauldron.brews.PotionFertilize;
import com.covens.common.content.cauldron.brews.PotionFireWorld;
import com.covens.common.content.cauldron.brews.PotionFrostbite;
import com.covens.common.content.cauldron.brews.PotionGrace;
import com.covens.common.content.cauldron.brews.PotionGrowFlowers;
import com.covens.common.content.cauldron.brews.PotionHarvest;
import com.covens.common.content.cauldron.brews.PotionHolyWater;
import com.covens.common.content.cauldron.brews.PotionIceWorld;
import com.covens.common.content.cauldron.brews.PotionInfestation;
import com.covens.common.content.cauldron.brews.PotionLove;
import com.covens.common.content.cauldron.brews.PotionMending;
import com.covens.common.content.cauldron.brews.PotionMowing;
import com.covens.common.content.cauldron.brews.PotionOutcastsShame;
import com.covens.common.content.cauldron.brews.PotionOzymandias;
import com.covens.common.content.cauldron.brews.PotionPathOfTheDeep;
import com.covens.common.content.cauldron.brews.PotionPlant;
import com.covens.common.content.cauldron.brews.PotionPowerBoost;
import com.covens.common.content.cauldron.brews.PotionPowerDampening;
import com.covens.common.content.cauldron.brews.PotionPowerDrain;
import com.covens.common.content.cauldron.brews.PotionPowerRegen;
import com.covens.common.content.cauldron.brews.PotionProjectileResistance;
import com.covens.common.content.cauldron.brews.PotionPruneLeaves;
import com.covens.common.content.cauldron.brews.PotionPulverize;
import com.covens.common.content.cauldron.brews.PotionPurification;
import com.covens.common.content.cauldron.brews.PotionRevealing;
import com.covens.common.content.cauldron.brews.PotionRotting;
import com.covens.common.content.cauldron.brews.PotionSaltedEarth;
import com.covens.common.content.cauldron.brews.PotionSetehsWastes;
import com.covens.common.content.cauldron.brews.PotionShellArmor;
import com.covens.common.content.cauldron.brews.PotionSinking;
import com.covens.common.content.cauldron.brews.PotionSnowTrail;
import com.covens.common.content.cauldron.brews.PotionSpiderNightmare;
import com.covens.common.content.cauldron.brews.PotionTillLand;
import com.covens.common.content.cauldron.brews.PotionVolatility;
import com.covens.common.content.cauldron.brews.PotionWolfsbane;
import com.covens.common.potion.potions.PotionBloodDrained;
import com.covens.common.potion.potions.PotionMesmerize;
import com.covens.common.potion.potions.PotionSunWard;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModPotions {

	// Normal effects
	public static Potion bloodDrained, sun_ward, mesmerized;

	// Also brews
	public static Potion wolfsbane, arrow_deflect, absence, plant, bane_arthropods, corruption, cursed_leaping, demons_bane;
	public static Potion projectile_resistance, disrobing, ender_inhibition, extinguish_fires, fertilize, fireworld, grace;
	public static Potion mending, flower_growth, harvest, holy_water, ice_world, outcasts_shame, infestation, ozymandias;
	public static Potion purification, path_of_the_deep, prune_leaves, rotting, setehs_wastes, salted_earth, shell_armor;
	public static Potion till_land, snow_trail, spider_nightmare, volatility, pulverize, mowing, love, revealing;
	public static Potion deaths_ebb, power_boon, power_boost, power_drain, power_dampening;

	public static PotionFrostbite freezing;
	public static PotionSinking sinking;

	private ModPotions() {
	}

	public static void init() {
		bloodDrained = new PotionBloodDrained();
		sun_ward = new PotionSunWard();
		wolfsbane = new PotionWolfsbane();
		arrow_deflect = new PotionArrowDeflection();
		absence = new PotionAbsence();
		plant = new PotionPlant();
		bane_arthropods = new PotionBaneArthropods();
		corruption = new PotionCorruption();
		cursed_leaping = new PotionCursedLeaping();
		demons_bane = new PotionDemonsbane();
		projectile_resistance = new PotionProjectileResistance();
		disrobing = new PotionDisrobing();
		ender_inhibition = new PotionEnderInhibition();
		extinguish_fires = new PotionExtinguishFire();
		fertilize = new PotionFertilize();
		fireworld = new PotionFireWorld();
		grace = new PotionGrace();
		freezing = new PotionFrostbite();
		mending = new PotionMending();
		flower_growth = new PotionGrowFlowers();
		harvest = new PotionHarvest();
		holy_water = new PotionHolyWater();
		ice_world = new PotionIceWorld();
		outcasts_shame = new PotionOutcastsShame();
		infestation = new PotionInfestation();
		ozymandias = new PotionOzymandias();
		purification = new PotionPurification();
		path_of_the_deep = new PotionPathOfTheDeep();
		prune_leaves = new PotionPruneLeaves();
		rotting = new PotionRotting();
		setehs_wastes = new PotionSetehsWastes();
		salted_earth = new PotionSaltedEarth();
		shell_armor = new PotionShellArmor();
		till_land = new PotionTillLand();
		sinking = new PotionSinking();
		snow_trail = new PotionSnowTrail();
		spider_nightmare = new PotionSpiderNightmare();
		volatility = new PotionVolatility();
		pulverize = new PotionPulverize();
		mowing = new PotionMowing();
		love = new PotionLove();
		revealing = new PotionRevealing();
		deaths_ebb = new PotionDeathsEbb();
		power_boon = new PotionPowerRegen();
		power_drain = new PotionPowerDrain();
		power_boost = new PotionPowerBoost();
		power_dampening = new PotionPowerDampening();
		mesmerized = new PotionMesmerize();

		ForgeRegistries.POTIONS.registerAll(//
				bloodDrained, wolfsbane, arrow_deflect, absence, plant, //
				bane_arthropods, corruption, cursed_leaping, demons_bane, //
				projectile_resistance, disrobing, ender_inhibition, extinguish_fires, //
				fertilize, freezing, fireworld, grace, mending, flower_growth, //
				harvest, holy_water, ice_world, outcasts_shame, infestation, ozymandias, //
				purification, path_of_the_deep, prune_leaves, rotting, setehs_wastes, //
				salted_earth, shell_armor, till_land, sinking, snow_trail, spider_nightmare, //
				volatility, pulverize, mowing, sun_ward, love, revealing, //
				deaths_ebb, power_boon, mesmerized, power_drain, power_boost, //
				power_dampening);
	}
}

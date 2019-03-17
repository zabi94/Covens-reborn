package com.covens.common.lib;

import com.covens.common.block.ModBlocks;
import com.covens.common.item.ModItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import zabi.minecraft.minerva.common.crafting.IngredientMultiOreDict;

public class LibIngredients {
	
	static {
		if (ModItems.bottled_magic == null) {
			throw new IllegalStateException("The class LibIngredients is being loaded too early!");
		}
	}

	// TODO any time we call Ingredient.from*, the call should be cached here and
	// used multiple times

	public static Ingredient ironOre = Ingredient.fromItem(Item.getItemFromBlock(Blocks.IRON_ORE));
	public static Ingredient blazePowder = Ingredient.fromItem(Items.BLAZE_POWDER);
	public static Ingredient goldNugget = Ingredient.fromItem(Items.GOLD_NUGGET);
	public static Ingredient slime = Ingredient.fromItem(Items.SLIME_BALL);
	public static Ingredient dirt = Ingredient.fromStacks(new ItemStack(Blocks.DIRT, 1, 0));
	public static Ingredient stickyPiston = Ingredient.fromStacks(new ItemStack(Blocks.STICKY_PISTON, 1, 0));
	public static Ingredient sponge = Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 0));
	public static Ingredient anyLog = new OreIngredient("logWood");
	public static Ingredient anyLeaf = new OreIngredient("treeLeaves");
	public static Ingredient pentacle = Ingredient.fromItem(ModItems.pentacle);
	public static Ingredient fumeReekOfDeath = Ingredient.fromItem(ModItems.reek_of_death);
	public static Ingredient anyDye = new OreIngredient("dye");
	public static Ingredient acaciaLog = Ingredient.fromStacks(new ItemStack(Blocks.LOG2, 1, 0));
	public static Ingredient redstone = Ingredient.fromItem(Items.REDSTONE);
	public static Ingredient ghastTear = Ingredient.fromItem(Items.GHAST_TEAR);
	public static Ingredient obsidian = Ingredient.fromItem(Item.getItemFromBlock(Blocks.OBSIDIAN));
	public static Ingredient fire_charge = Ingredient.fromItem(Items.FIRE_CHARGE);
	public static Ingredient goldIngot = new OreIngredient("ingotGold");
	public static Ingredient netherBrickItem = Ingredient.fromItem(Items.NETHERBRICK);
	public static Ingredient sand = new OreIngredient("sand");
	public static Ingredient dimensionalSand = Ingredient.fromItem(ModItems.dimensional_sand);
	public static Ingredient normalRitualChalk = Ingredient.fromStacks(new ItemStack(ModItems.ritual_chalk));
	public static Ingredient fumeFieryBreeze = Ingredient.fromItem(ModItems.fiery_breeze);
	public static Ingredient fumeHeavenlyWind = Ingredient.fromItem(ModItems.heavenly_winds);
	public static Ingredient fumePetrichorOdour = Ingredient.fromItem(ModItems.petrichor_odour);
	public static Ingredient fumeZephyrOfDepths = Ingredient.fromItem(ModItems.zephyr_of_the_depths);
	public static Ingredient fumeCleansingAura = Ingredient.fromItem(ModItems.cleansing_aura);
	public static Ingredient fumeCloudyOil = Ingredient.fromItem(ModItems.cloudy_oil);
	public static Ingredient fumeBottledMagic = Ingredient.fromItem(ModItems.bottled_magic);
	public static Ingredient fumeBirchSoul = Ingredient.fromItem(ModItems.birch_soul);
	public static Ingredient fumeDropletOfWisdom = Ingredient.fromItem(ModItems.droplet_of_wisdom);
	public static Ingredient fumeEmanationDishonesty = Ingredient.fromItem(ModItems.emanation_of_dishonesty);
	public static Ingredient fumeEverchangingPresence = Ingredient.fromItem(ModItems.everchanging_presence);
	public static Ingredient fumeUndyingImage = Ingredient.fromItem(ModItems.undying_image);
	public static Ingredient graveyardDust = Ingredient.fromItem(ModItems.graveyard_dust);
	public static Ingredient wormwood = Ingredient.fromItem(ModItems.wormwood);
	public static Ingredient empty_honeycomb = Ingredient.fromItem(ModItems.empty_honeycomb);
	public static Ingredient emptyGoblet = Ingredient.fromStacks(new ItemStack(ModBlocks.goblet, 1, 0));
	public static Ingredient diamondOre = new OreIngredient("oreDiamond");
	public static Ingredient glowstoneBlock = Ingredient.fromItem(Item.getItemFromBlock(Blocks.GLOWSTONE));
	public static Ingredient goldenCarrot = Ingredient.fromItem(Items.GOLDEN_CARROT);
	public static Ingredient athame = Ingredient.fromItem(ModItems.athame);
	public static Ingredient apple = Ingredient.fromItem(Items.APPLE);
	public static Ingredient potato = Ingredient.fromItem(Items.POTATO);
	public static Ingredient poisonousPotato = Ingredient.fromItem(Items.POISONOUS_POTATO);
	public static Ingredient witherSkull = Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 1));
	public static Ingredient soulSand = Ingredient.fromItem(Item.getItemFromBlock(Blocks.SOUL_SAND));
	public static Ingredient woodAsh = Ingredient.fromItem(ModItems.wood_ash);
	public static Ingredient clayBall = Ingredient.fromItem(Items.CLAY_BALL);
	public static Ingredient locationStoneBound = Ingredient.fromStacks(new ItemStack(ModItems.location_stone, 1, 1));
	public static Ingredient blazeRod = Ingredient.fromItem(Items.BLAZE_ROD);
	public static Ingredient coal = Ingredient.fromItem(Items.COAL);
	public static Ingredient whiteSage = Ingredient.fromItem(ModItems.white_sage);
	public static Ingredient sagebrush = Ingredient.fromItem(ModItems.sagebrush);
	public static Ingredient enderPearl = Ingredient.fromItem(Items.ENDER_PEARL);
	public static Ingredient lapisPowder = Ingredient.fromItem(ModItems.lapis_powder);
	public static Ingredient salt = Ingredient.fromItem(ModItems.salt);
	public static Ingredient paper = Ingredient.fromItem(Items.PAPER);
	public static Ingredient wax = Ingredient.fromItem(ModItems.wax);
	public static Ingredient craftingTable = Ingredient.fromItem(Item.getItemFromBlock(Blocks.CRAFTING_TABLE));
	public static Ingredient anyGlass = new OreIngredient("blockGlass");
	public static Ingredient quartz = new OreIngredient("gemQuartz");
	public static Ingredient logCypress = Ingredient.fromStacks(new ItemStack(ModBlocks.log_cypress, 1, 0));
	public static Ingredient logYew = Ingredient.fromStacks(new ItemStack(ModBlocks.log_yew, 1, 0));
	public static Ingredient logJuniper = Ingredient.fromStacks(new ItemStack(ModBlocks.log_juniper, 1, 0));
	public static Ingredient logElder = Ingredient.fromStacks(new ItemStack(ModBlocks.log_elder, 1, 0));
	public static Ingredient broomMundane = Ingredient.fromStacks(new ItemStack(ModItems.broom, 1, 0));
	public static Ingredient magicSalve = Ingredient.fromItem(ModItems.magic_salve);
	public static Ingredient elytra = Ingredient.fromItem(Items.ELYTRA);
	public static Ingredient anyString = new OreIngredient("string");
	public static Ingredient string = Ingredient.fromItems(Items.STRING);
	public static Ingredient glowstoneDust = Ingredient.fromItem(Items.GLOWSTONE_DUST);
	public static Ingredient boline = Ingredient.fromItem(ModItems.boline);
	public static Ingredient bloodyRags = Ingredient.fromItem(ModItems.sanguine_fabric);
	public static Ingredient eyes = Ingredient.fromStacks(new ItemStack(Items.ENDER_EYE), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.FERMENTED_SPIDER_EYE));
	public static Ingredient anySapling = new OreIngredient("treeSapling");
	public static Ingredient acaciaResin = Ingredient.fromItem(ModItems.acacia_essence);
	public static Ingredient yewEssence = Ingredient.fromItem(ModItems.vital_essence);
	public static Ingredient tulsi = Ingredient.fromItem(ModItems.tulsi);
	public static Ingredient oakAppleGall = Ingredient.fromItem(ModItems.oak_apple_gall);
	public static Ingredient empty_jar = Ingredient.fromItem(ModItems.empty_jar);
	public static Ingredient anySeed = new OreIngredient("listAllSeeds");// TODO check
	public static Ingredient feather = Ingredient.fromItem(Items.FEATHER);
	public static Ingredient spider_web = Ingredient.fromStacks(new ItemStack(Blocks.WEB));
	public static Ingredient hay = Ingredient.fromStacks(new ItemStack(Blocks.HAY_BLOCK));
	public static Ingredient wheat = Ingredient.fromItem(Items.WHEAT);
	public static Ingredient soul_string = Ingredient.fromItem(ModItems.soul_string);
	public static Ingredient kenaf = Ingredient.fromItem(ModItems.kenaf);
	public static Ingredient witches_stitching = Ingredient.fromItem(ModItems.witches_stitching);
	public static Ingredient honeycomb = Ingredient.fromItem(ModItems.honeycomb);
	public static Ingredient ingotIron = Ingredient.fromItem(Items.IRON_INGOT);
	public static Ingredient saplingElder = Ingredient.fromStacks(new ItemStack(ModBlocks.sapling, 1, 0));
	public static Ingredient saplingJuniper = Ingredient.fromStacks(new ItemStack(ModBlocks.sapling, 1, 1));
	public static Ingredient saplingYew = Ingredient.fromStacks(new ItemStack(ModBlocks.sapling, 1, 2));
	public static Ingredient saplingCypress = Ingredient.fromStacks(new ItemStack(ModBlocks.sapling, 1, 3));
	public static Ingredient woolOfBat = Ingredient.fromItem(ModItems.wool_of_bat);
	public static Ingredient beetroot = Ingredient.fromItem(Items.BEETROOT);
	public static Ingredient brick_item = Ingredient.fromItem(Items.BRICK);
	public static Ingredient hellebore = Ingredient.fromItem(ModItems.hellebore);
	public static Ingredient snowball = Ingredient.fromItem(Items.SNOWBALL);
	public static Ingredient iceBlock = Ingredient.fromItem(Item.getItemFromBlock(Blocks.ICE));
	public static Ingredient coldIronDustSmall = Ingredient.fromItem(ModItems.cold_iron_dust_small);
	public static Ingredient coldIronDust = Ingredient.fromItem(ModItems.cold_iron_dust);
	public static Ingredient speckledMelon = Ingredient.fromItem(Items.SPECKLED_MELON);
	public static Ingredient goldenApple = Ingredient.fromStacks(new ItemStack(Items.GOLDEN_APPLE, 1, 0));
	public static Ingredient egg = new IngredientMultiOreDict("egg", "foodSimpleEgg", "ingredientEgg", "listAllegg", "bakingEgg");
	public static Ingredient sugar = Ingredient.fromItem(Items.SUGAR);
	public static Ingredient garlic = new IngredientMultiOreDict("cropGarlic");
	public static Ingredient stick = Ingredient.fromItem(Items.STICK);
	
}

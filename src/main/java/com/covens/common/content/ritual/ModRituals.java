package com.covens.common.content.ritual;

import static com.covens.api.ritual.EnumGlyphType.ANY;
import static com.covens.api.ritual.EnumGlyphType.ENDER;
import static com.covens.api.ritual.EnumGlyphType.NETHER;
import static com.covens.api.ritual.EnumGlyphType.NORMAL;

import com.covens.api.ritual.EnumGlyphType;
import com.covens.common.block.ModBlocks;
import com.covens.common.block.natural.tree.BlockModSapling;
import com.covens.common.content.ritual.rituals.RitualBiomeShift;
import com.covens.common.content.ritual.rituals.RitualConjurationBlaze;
import com.covens.common.content.ritual.rituals.RitualConjurationGhast;
import com.covens.common.content.ritual.rituals.RitualConjurationMagmaCube;
import com.covens.common.content.ritual.rituals.RitualConjurationVex;
import com.covens.common.content.ritual.rituals.RitualConjurationWitch;
import com.covens.common.content.ritual.rituals.RitualConjurationWither;
import com.covens.common.content.ritual.rituals.RitualCreateVampireLair;
import com.covens.common.content.ritual.rituals.RitualDrawing;
import com.covens.common.content.ritual.rituals.RitualFlames;
import com.covens.common.content.ritual.rituals.RitualGateway;
import com.covens.common.content.ritual.rituals.RitualHighMoon;
import com.covens.common.content.ritual.rituals.RitualNetherPortal;
import com.covens.common.content.ritual.rituals.RitualPerception;
import com.covens.common.content.ritual.rituals.RitualSandsTime;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibIngredients;
import com.covens.common.tile.tiles.TileEntityGlyph;

import net.minecraft.item.ItemStack;

public class ModRituals {

	public static void init() {
		
		RitualFactory.create("high_moon")
			.addInput(LibIngredients.goldIngot)
			.addInput(LibIngredients.netherBrickItem)
			.withStartingPower(800)
			.withRunningTime(100)
			.withSmallCircle(NORMAL)
			.disallowRemote()
			.buildAndRegister(new RitualHighMoon());

		RitualFactory.create("time_sands")
			.addInput(LibIngredients.sand)
			.addInput(LibIngredients.diamondOre)
			.withStartingPower(1000)
			.withTickCost(5)
			.withAllCircles(NORMAL)
			.neverEnding()
			.disallowRemote()
			.buildAndRegister(new RitualSandsTime());
		
		RitualFactory.create("perception")
			.addInput(LibIngredients.glowstoneBlock)
			.addInput(LibIngredients.goldenCarrot)
			.withAllCircles(NORMAL)
			.withStartingPower(700)
			.withTickCost(3)
			.neverEnding()
			.buildAndRegister(new RitualPerception());
		
		RitualFactory.create("conjure_witch")
			.addInput(LibIngredients.athame)
			.addInput(LibIngredients.apple, 2)
			.addInput(LibIngredients.pentacle)
			.addInput(LibIngredients.poisonousPotato)
			.addOutput(new ItemStack(ModItems.athame))
			.withRunningTime(200)
			.withStartingPower(3000)
			.withTickCost(3)
			.withAllCircles(NETHER)
			.withMediumCircle(ENDER)
			.buildAndRegister(new RitualConjurationWitch());
		
		RitualFactory.create("conjure_magma_cube")
			.addInput(LibIngredients.athame)
			.addInput(LibIngredients.blazePowder, 2)
			.addInput(LibIngredients.slime, 2)
			.addOutput(new ItemStack(ModItems.athame))
			.withRunningTime(120)
			.withSmallCircle(NETHER)
			.withMediumCircle(NETHER)
			.withStartingPower(1200)
			.withTickCost(2)
			.buildAndRegister(new RitualConjurationMagmaCube());

		RitualFactory.create("conjure_vex")
			.addInput(LibIngredients.athame)
			.addInput(LibIngredients.graveyardDust)
			.addInput(LibIngredients.wormwood, 2)
			.addOutput(new ItemStack(ModItems.athame))
			.withRunningTime(120)
			.withStartingPower(1000)
			.withTickCost(2)
			.withSmallCircle(NORMAL)
			.withMediumCircle(ENDER)
			.buildAndRegister(new RitualConjurationVex());
		
		
		RitualFactory.create("conjure_blaze")
			.addInput(LibIngredients.athame)
			.addInput(LibIngredients.anyLog, 2)
			.addInput(LibIngredients.netherBrickItem, 2)
			.addOutput(new ItemStack(ModItems.athame))
			.withRunningTime(120)
			.withStartingPower(1200)
			.withTickCost(2)
			.withSmallCircle(NETHER)
			.withMediumCircle(NETHER)
			.disallowRemote()
			.buildAndRegister(new RitualConjurationBlaze());
		
		RitualFactory.create("conjure_ghast")
			.addInput(LibIngredients.athame)
			.addInput(LibIngredients.soulSand, 3)
			.addInput(LibIngredients.fire_charge)
			.addInput(LibIngredients.fumeReekOfDeath)
			.addInput(LibIngredients.glowstoneDust)
			.addOutput(new ItemStack(ModItems.athame))
			.withRunningTime(250)
			.withStartingPower(3400)
			.withTickCost(3)
			.withAllCircles(NETHER)
			.disallowRemote()
			.buildAndRegister(new RitualConjurationGhast());
		
		RitualFactory.create("conjure_wither")
			.addInput(LibIngredients.athame)
			.addInput(LibIngredients.soulSand, 4)
			.addInput(LibIngredients.witherSkull)
			.addInput(LibIngredients.fumeReekOfDeath)
			.addOutput(new ItemStack(ModItems.athame))
			.withRunningTime(400)
			.withStartingPower(5000)
			.withTickCost(4)
			.withAllCircles(NETHER)
			.buildAndRegister(new RitualConjurationWither());
		
		RitualFactory.create("draw_circle_small")
			.addInput(LibIngredients.woodAsh)
			.withSmallCircle(ANY)
			.withRunningTime(40)
			.withStartingPower(100)
			.buildAndRegister(new RitualDrawing(TileEntityGlyph.small));
		
		RitualFactory.create("draw_circle_medium")
			.addInput(LibIngredients.woodAsh)
			.addInput(LibIngredients.clayBall) // balanced
			.withSmallCircle(ANY)
			.withRunningTime(40)
			.withStartingPower(100)
			.buildAndRegister(new RitualDrawing(TileEntityGlyph.medium));
		
		RitualFactory.create("draw_circle_large")
			.addInput(LibIngredients.woodAsh, 2)
			.addInput(LibIngredients.clayBall, 2) // balanced
			.withSmallCircle(ANY)
			.withMediumCircle(ANY)
			.withRunningTime(40)
			.withStartingPower(100)
			.buildAndRegister(new RitualDrawing(TileEntityGlyph.big));
		
		RitualFactory.create("gateway")
			.addInput(LibIngredients.locationStoneBound)
			.addInput(LibIngredients.enderPearl)
			.addInput(LibIngredients.dimensionalSand)
			.neverEnding()
			.withAllCircles(ENDER)
			.withMediumCircle(NORMAL)
			.withStartingPower(4000)
			.withTickCost(8)
			.buildAndRegister(new RitualGateway());
		
		RitualFactory.create("nether_portal")
			.addInput(LibIngredients.obsidian, 4)
			.addInput(LibIngredients.fire_charge)
			.withRunningTime(50)
			.withSmallCircle(NETHER)
			.withStartingPower(4000)
			.disallowRemote()
			.buildAndRegister(new RitualNetherPortal());
		
		RitualFactory.create("shift_biome")
			.addInput(LibIngredients.anyGlass, 4)
			.addInput(LibIngredients.boline)
			.addOutput(new ItemStack(ModItems.boline))
			.withRunningTime(400)
			.withAllCircles(NORMAL)
			.withStartingPower(2000)
			.withTickCost(8)
			.buildAndRegister(new RitualBiomeShift());
		
		RitualFactory.create("grafting_elder")
			.addInput(LibIngredients.anySapling)
			.addInput(LibIngredients.dirt)
			.addInput(LibIngredients.anyString, 2)
			.addInput(LibIngredients.stick)
			.addInput(LibIngredients.logElder)
			.withRunningTime(60)
			.withTickCost(5)
			.withSmallCircle(EnumGlyphType.NORMAL)
			.addOutput(new ItemStack(ModBlocks.sapling, 1, BlockModSapling.EnumSaplingType.ELDER.ordinal()))
			.disallowRemote()
			.buildAndRegisterSimple();
		
		RitualFactory.create("grafting_juniper")
			.addInput(LibIngredients.anySapling)
			.addInput(LibIngredients.dirt)
			.addInput(LibIngredients.anyString, 2)
			.addInput(LibIngredients.stick)
			.addInput(LibIngredients.logJuniper)
			.withRunningTime(60)
			.withTickCost(5)
			.withSmallCircle(EnumGlyphType.NORMAL)
			.addOutput(new ItemStack(ModBlocks.sapling, 1, BlockModSapling.EnumSaplingType.JUNIPER.ordinal()))
			.disallowRemote()
			.buildAndRegisterSimple();
		
		RitualFactory.create("grafting_yew")
			.addInput(LibIngredients.anySapling)
			.addInput(LibIngredients.dirt)
			.addInput(LibIngredients.anyString, 2)
			.addInput(LibIngredients.stick)
			.addInput(LibIngredients.logYew)
			.withRunningTime(60)
			.withTickCost(5)
			.withSmallCircle(EnumGlyphType.NORMAL)
			.addOutput(new ItemStack(ModBlocks.sapling, 1, BlockModSapling.EnumSaplingType.YEW.ordinal()))
			.disallowRemote()
			.buildAndRegisterSimple();
		
		RitualFactory.create("grafting_cypress")
			.addInput(LibIngredients.anySapling)
			.addInput(LibIngredients.dirt)
			.addInput(LibIngredients.anyString, 2)
			.addInput(LibIngredients.stick)
			.addInput(LibIngredients.logCypress)
			.withRunningTime(60)
			.withTickCost(5)
			.withSmallCircle(EnumGlyphType.NORMAL)
			.addOutput(new ItemStack(ModBlocks.sapling, 1, BlockModSapling.EnumSaplingType.CYPRESS.ordinal()))
			.disallowRemote()
			.buildAndRegisterSimple();
		
//		new RitualFactory(DefaultInfusions.OVERWORLD.getRegistryName())
//			.addInput(LibIngredients.fumePetrichorOdour)
//			.withAllCircles(NORMAL)
//			.withRunningTime(60)
//			.withStartingPower(6000)
//			.withTickCost(1)
//			.disallowRemote()
//			.buildAndRegister(new RitualInfusion(DefaultInfusions.OVERWORLD));
//		
//		new RitualFactory(DefaultInfusions.NETHER.getRegistryName())
//			.addInput(LibIngredients.fumeFieryBreeze)
//			.withAllCircles(NETHER)
//			.withRunningTime(60)
//			.withStartingPower(6000)
//			.withTickCost(1)
//			.disallowRemote()
//			.buildAndRegister(new RitualInfusion(DefaultInfusions.NETHER));
//		
//		new RitualFactory(DefaultInfusions.END.getRegistryName())
//			.addInput(LibIngredients.fumeHeavenlyWind)
//			.withAllCircles(ENDER)
//			.withRunningTime(60)
//			.withStartingPower(6000)
//			.withTickCost(1)
//			.disallowRemote()
//			.buildAndRegister(new RitualInfusion(DefaultInfusions.END));
//		
//		new RitualFactory(DefaultInfusions.DREAM.getRegistryName())
//			.addInput(LibIngredients.fumeZephyrOfDepths)
//			.withAllCircles(ANY)
//			.withRunningTime(60)
//			.withStartingPower(6000)
//			.withTickCost(1)
//			.disallowRemote()
//			.buildAndRegister(new RitualInfusion(DefaultInfusions.DREAM));
		
		
		RitualFactory.create("flames")
			.addInput(LibIngredients.blazeRod)
			.addInput(LibIngredients.coal, 3)
			.withSmallCircle(NETHER)
			.withRunningTime(3600)
			.withStartingPower(300)
			.withTickCost(4)
			.buildAndRegister(new RitualFlames());
		
		RitualFactory.create("sanctuary")
			.addInput(LibIngredients.whiteSage)
			.addInput(LibIngredients.sagebrush)
			.addInput(LibIngredients.salt)
			.addInput(LibIngredients.dirt, 3)
			.addOutput(new ItemStack(ModBlocks.purifying_earth, 3))
			.withSmallCircle(NORMAL)
			.withMediumCircle(NORMAL)
			.withRunningTime(130)
			.withStartingPower(500)
			.withTickCost(4)
			.buildAndRegisterSimple();
		
		RitualFactory.create("deck")
			.addInput(LibIngredients.anyDye, 2)
			.addInput(LibIngredients.paper, 12)
			.addInput(LibIngredients.fumeBirchSoul)
			.addInput(LibIngredients.wax)
			.addOutput(new ItemStack(ModItems.tarots))
			.withSmallCircle(NORMAL)
			.withRunningTime(130)
			.withStartingPower(350)
			.withTickCost(1)
			.buildAndRegisterSimple();
		
		RitualFactory.create("table")
			.addInput(LibIngredients.anyDye, 2)
			.addInput(LibIngredients.anyString, 12)
			.addInput(LibIngredients.craftingTable)
			.addInput(LibIngredients.fumeDropletOfWisdom)
			.addInput(LibIngredients.fumeBottledMagic)
			.addOutput(new ItemStack(ModBlocks.tarot_table))
			.withSmallCircle(NORMAL)
			.withMediumCircle(NORMAL)
			.withRunningTime(130)
			.withStartingPower(350)
			.withTickCost(1)
			.buildAndRegisterSimple();
		
		RitualFactory.create("crystal_ball")
			.addInput(LibIngredients.quartz, 2)
			.addInput(LibIngredients.anyGlass, 4)
			.addInput(LibIngredients.fumeBottledMagic)
			.addOutput(new ItemStack(ModBlocks.crystal_ball))
			.withSmallCircle(NORMAL)
			.withMediumCircle(ENDER)
			.withRunningTime(750)
			.withStartingPower(350)
			.withTickCost(3)
			.buildAndRegisterSimple();
		
		RitualFactory.create("elder_broom")
			.addInput(LibIngredients.logElder, 3)
			.addInput(LibIngredients.broomMundane)
			.addInput(LibIngredients.saplingElder, 4)
			.addInput(LibIngredients.magicSalve)
			.addInput(LibIngredients.elytra)
			.addOutput(new ItemStack(ModItems.broom, 1, 1))
			.withAllCircles(NORMAL)
			.withBigCircle(ENDER)
			.withRunningTime(2000)
			.withStartingPower(1000)
			.withTickCost(3)
			.buildAndRegisterSimple();
		
		RitualFactory.create("juniper_broom")
			.addInput(LibIngredients.logJuniper, 3)
			.addInput(LibIngredients.broomMundane)
			.addInput(LibIngredients.saplingJuniper, 4)
			.addInput(LibIngredients.magicSalve)
			.addInput(LibIngredients.elytra)
			.addOutput(new ItemStack(ModItems.broom, 1, 2))
			.withAllCircles(NORMAL)
			.withBigCircle(ENDER)
			.withRunningTime(2000)
			.withStartingPower(1000)
			.withTickCost(3)
			.buildAndRegisterSimple();
		
		RitualFactory.create("yew_broom")
			.addInput(LibIngredients.logYew, 3)
			.addInput(LibIngredients.broomMundane)
			.addInput(LibIngredients.saplingYew, 4)
			.addInput(LibIngredients.magicSalve)
			.addInput(LibIngredients.elytra)
			.addOutput(new ItemStack(ModItems.broom, 1, 3))
			.withAllCircles(NORMAL)
			.withBigCircle(ENDER)
			.withRunningTime(2000)
			.withStartingPower(1000)
			.withTickCost(3)
			.buildAndRegisterSimple();
		
		RitualFactory.create("cypress_broom")
			.addInput(LibIngredients.logCypress, 3)
			.addInput(LibIngredients.broomMundane)
			.addInput(LibIngredients.saplingCypress, 4)
			.addInput(LibIngredients.magicSalve)
			.addInput(LibIngredients.elytra)
			.addOutput(new ItemStack(ModItems.broom, 1, 4))
			.withAllCircles(NORMAL)
			.withBigCircle(ENDER)
			.withRunningTime(2000)
			.withStartingPower(1000)
			.withTickCost(3)
			.buildAndRegisterSimple();
		
		RitualFactory.create("vampire_lair")
			.addInput(LibIngredients.bloodyRags, 8)
			.addInput(LibIngredients.anySapling)
			.addInput(LibIngredients.woolOfBat)
			.withAllCircles(NORMAL)
			.withRunningTime(200)
			.withStartingPower(1000)
			.withTickCost(3)
			.buildAndRegister(new RitualCreateVampireLair());
		
	}
}

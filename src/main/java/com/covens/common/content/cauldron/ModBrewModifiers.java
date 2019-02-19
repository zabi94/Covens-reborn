package com.covens.common.content.cauldron;

import java.util.Arrays;

import com.covens.api.CovensAPI;
import com.covens.api.cauldron.DefaultModifiers;
import com.covens.api.cauldron.IBrewEffect;
import com.covens.api.cauldron.IBrewModifier;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibMod;
import com.covens.common.tile.tiles.TileEntityCauldron;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.oredict.OreIngredient;
import zabi.minecraft.minerva.common.utils.ColorHelper;

public class ModBrewModifiers {

	public static void init() {

		initApiModifiers();

		CovensAPI api = CovensAPI.getAPI();
		api.registerBrewModifier(DefaultModifiers.POWER);
		api.registerBrewModifier(DefaultModifiers.DURATION);
		api.registerBrewModifier(DefaultModifiers.RADIUS);
		api.registerBrewModifier(DefaultModifiers.GAS_CLOUD_DURATION);
		api.registerBrewModifier(DefaultModifiers.SUPPRESS_ENTITY_EFFECT);
		api.registerBrewModifier(DefaultModifiers.SUPPRESS_IN_WORLD_EFFECT);
		api.registerBrewModifier(DefaultModifiers.COLOR);
		api.registerBrewModifier(DefaultModifiers.SUPPRESS_PARTICLES);
	}

	private static void initApiModifiers() {
		DefaultModifiers.POWER = new SimpleModifier("power", Ingredient.fromItem(Items.GLOWSTONE_DUST)) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}
			
			@Override
			public int getCostForLevel(int base, Integer level) {
				return super.getCostForLevel(base, (int) (1.4*level));
			}
		};

		DefaultModifiers.DURATION = new SimpleModifier("length", Ingredient.fromItem(Items.REDSTONE)) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return !CovensAPI.getAPI().getPotionFromBrew(brew).isInstant();
			}
		};

		DefaultModifiers.RADIUS = new SimpleModifier("radius", Ingredient.fromItem(ModItems.sagebrush)) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}
		};

		DefaultModifiers.GAS_CLOUD_DURATION = new SimpleModifier("gas_duration", Ingredient.fromItem(ModItems.hellebore)) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}
		};

		DefaultModifiers.SUPPRESS_ENTITY_EFFECT = new SimpleModifier("suppress_entity", Ingredient.fromItem(Items.BRICK)) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}

			@Override
			public boolean hasMultipleLevels() {
				return false;
			}
			
			@Override
			public int getCostForLevel(int base, Integer level) {
				return level * 5;
			}
		};

		DefaultModifiers.SUPPRESS_IN_WORLD_EFFECT = new SimpleModifier("suppress_in_world", Ingredient.fromItem(Items.NETHERBRICK)) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return brew.hasInWorldEffect();
			}

			@Override
			public boolean hasMultipleLevels() {
				return false;
			}
			
			@Override
			public int getCostForLevel(int base, Integer level) {
				return level * 5;
			}
		};

		DefaultModifiers.SUPPRESS_PARTICLES = new SimpleModifier("suppress_particles", Ingredient.fromItem(Items.BEETROOT)) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}

			@Override
			public boolean hasMultipleLevels() {
				return false;
			}
			
			@Override
			public int getCostForLevel(int base, Integer level) {
				return level * 5;
			}
		};

		DefaultModifiers.COLOR = new IBrewModifier() {

			private final ResourceLocation name = new ResourceLocation(LibMod.MOD_ID, "color");
			private final Ingredient ingredient = new CompoundIngredient(Arrays.asList(Ingredient.fromItem(Items.DYE), new OreIngredient("dye"))) {
			}; // TODO add the dye tag to items

			@Override
			public IBrewModifier setRegistryName(ResourceLocation name) {
				throw new UnsupportedOperationException("Don't mess with covens default implementation of modifiers!");
			}

			@Override
			public Class<IBrewModifier> getRegistryType() {
				return IBrewModifier.class;
			}

			@Override
			public ResourceLocation getRegistryName() {
				return this.name;
			}

			@Override
			public Ingredient getJEIStackRepresentative() {
				return this.ingredient;
			}

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}

			@Override
			public ModifierResult acceptIngredient(IBrewEffect brew, ItemStack stack, IBrewModifierList currentModifiers) {
				if (DyeUtils.isDye(stack)) {
					int currentColor = currentModifiers.getLevel(this).orElse(TileEntityCauldron.DEFAULT_COLOR);
					int newColor = DyeUtils.colorFromStack(stack).map(e -> e.getColorValue()).orElse(currentColor);
					return new ModifierResult(ColorHelper.blendColor(currentColor, newColor, 0.5f), ResultType.SUCCESS);
				}
				return new ModifierResult(ResultType.PASS);
			}

			@Override
			public boolean hasMultipleLevels() {
				return false;
			}

			@Override
			@SideOnly(Side.CLIENT)
			public String getTooltipString(int lvl) {
				return I18n.format("modifier.covens.color", String.format("%06X", lvl));
			}
			
			@Override
			public int getCostForLevel(int base, Integer level) {
				return 0;
			}
		};
	}
}

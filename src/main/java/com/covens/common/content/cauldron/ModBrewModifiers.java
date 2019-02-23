package com.covens.common.content.cauldron;

import com.covens.api.CovensAPI;
import com.covens.api.cauldron.DefaultModifiers;
import com.covens.api.cauldron.IBrewEffect;
import com.covens.api.cauldron.IBrewModifier;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.lib.LibIngredients;
import com.covens.common.lib.LibMod;
import com.covens.common.tile.tiles.TileEntityCauldron;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.DyeUtils;
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
		DefaultModifiers.POWER = new SimpleModifier("power", LibIngredients.glowstoneDust) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}
			
			@Override
			public int getCostForLevel(int base, Integer level) {
				return super.getCostForLevel(base, (int) (1.4*level));
			}
		};

		DefaultModifiers.DURATION = new SimpleModifier("length", LibIngredients.redstone) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return !CovensAPI.getAPI().getPotionFromBrew(brew).isInstant();
			}
		};

		DefaultModifiers.RADIUS = new SimpleModifier("radius", LibIngredients.sagebrush) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}
		};

		DefaultModifiers.GAS_CLOUD_DURATION = new SimpleModifier("gas_duration", LibIngredients.hellebore) {

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}
		};

		DefaultModifiers.SUPPRESS_ENTITY_EFFECT = new SimpleModifier("suppress_entity", LibIngredients.brick_item) {

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

		DefaultModifiers.SUPPRESS_IN_WORLD_EFFECT = new SimpleModifier("suppress_in_world", LibIngredients.netherBrickItem) {

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

		DefaultModifiers.SUPPRESS_PARTICLES = new SimpleModifier("suppress_particles", LibIngredients.beetroot) {

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
				return LibIngredients.anyDye;
			}

			@Override
			public boolean canApply(IBrewEffect brew) {
				return true;
			}

			@Override
			public ModifierResult acceptIngredient(IBrewEffect brew, ItemStack stack, IBrewModifierList currentModifiers) {
				if (DyeUtils.isDye(stack)) {
					int currentColor = currentModifiers.getLevel(this).orElse(TileEntityCauldron.DEFAULT_COLOR);
					int newColor = DyeUtils.colorFromStack(stack).map(e -> getDyeColor(e)).orElse(currentColor);
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
	
	public static final int[] dyeColors = {16383998, 16351261, 13061821, 3847130, 16701501, 8439583, 15961002, 4673362, 10329495, 1481884, 8991416, 3949738, 8606770, 6192150, 11546150, 1908001};
	
	public static int getDyeColor(EnumDyeColor dye) {
		return dyeColors[dye.ordinal()];
	}
}

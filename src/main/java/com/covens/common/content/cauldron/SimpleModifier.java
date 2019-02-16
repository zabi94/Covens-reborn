package com.covens.common.content.cauldron;

import com.covens.api.cauldron.IBrewEffect;
import com.covens.api.cauldron.IBrewModifier;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.core.helper.RomanNumberHelper;
import com.covens.common.lib.LibMod;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SimpleModifier implements IBrewModifier {

	private final ResourceLocation name;
	private final Ingredient ingredient;

	public SimpleModifier(String name, Ingredient ingredient) {
		this.name = new ResourceLocation(LibMod.MOD_ID, name);
		if (ingredient.getMatchingStacks().length == 0) {
			throw new IllegalArgumentException("Empty ingredient when registering the following brew modifier: " + name);
		}
		this.ingredient = ingredient;
	}

	@Override
	public IBrewModifier setRegistryName(ResourceLocation nameIn) {
		throw new UnsupportedOperationException("Don't mess with covens default implementation of modifiers!");
	}

	@Override
	public ResourceLocation getRegistryName() {
		return this.name;
	}

	@Override
	public Class<IBrewModifier> getRegistryType() {
		return IBrewModifier.class;
	}

	@Override
	public ModifierResult acceptIngredient(IBrewEffect brew, ItemStack stack, IBrewModifierList currentModifiers) {
		int currentLevel = currentModifiers.getLevel(this).orElse(0);
		if (this.ingredient.apply(stack)) {
			if (currentLevel < 5) {
				return new ModifierResult(currentLevel + 1, ResultType.SUCCESS);
			}
			return new ModifierResult(ResultType.FAIL);
		}
		return new ModifierResult(ResultType.PASS);
	}

	@Override
	public Ingredient getJEIStackRepresentative() {
		return this.ingredient;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IBrewModifier) {
			return this.getRegistryName().equals(((IBrewModifier) obj).getRegistryName());
		}
		return false;
	}

	@Override
	public boolean hasMultipleLevels() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTooltipString(int lvl) {
		if (this.hasMultipleLevels()) {
			return I18n.format("modifier." + this.getRegistryName().toString().replace(':', '.'), RomanNumberHelper.getRoman(lvl));
		}
		return I18n.format("modifier." + this.getRegistryName().toString().replace(':', '.'));
	}
}

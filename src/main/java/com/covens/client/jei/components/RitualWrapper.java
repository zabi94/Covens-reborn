package com.covens.client.jei.components;

import java.util.List;

import com.covens.api.ritual.EnumGlyphType;
import com.covens.common.content.ritual.AdapterIRitual;
import com.covens.common.core.helper.Log;
import com.covens.common.lib.LibMod;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RitualWrapper implements IRecipeWrapper {
	private final IDrawable centerGlyph, circle1, circle2, circle3;
	private List<List<ItemStack>> input;
	private List<ItemStack> output;
	private int circles, powerStart, powerTick;
	private String name;

	public RitualWrapper(AdapterIRitual ritual, IGuiHelper igh) {
		this.setOutput(ritual.getOutputRaw());
		this.setInput(ritual.getJeiInput());
		this.circles = ritual.getCircles();
		this.powerStart = ritual.getRequiredStartingPower();
		this.powerTick = ritual.getRunningPower();
		this.name = I18n.format("ritual." + ritual.getRegistryName().toString().replace(':', '.') + ".name");
		this.centerGlyph = igh.drawableBuilder(new ResourceLocation(LibMod.MOD_ID, "textures/gui/jei_ritual_0.png"), 0, 0, 34, 34).setTextureSize(34, 34).build();
		this.circle1 = igh.drawableBuilder(new ResourceLocation(LibMod.MOD_ID, "textures/gui/jei_ritual_1.png"), 0, 0, 34, 34).setTextureSize(34, 34).build();
		this.circle2 = igh.drawableBuilder(new ResourceLocation(LibMod.MOD_ID, "textures/gui/jei_ritual_2.png"), 0, 0, 34, 34).setTextureSize(34, 34).build();
		this.circle3 = igh.drawableBuilder(new ResourceLocation(LibMod.MOD_ID, "textures/gui/jei_ritual_3.png"), 0, 0, 34, 34).setTextureSize(34, 34).build();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, this.getInput());
		if (!this.getOutput().isEmpty()) {
			ingredients.setOutputs(VanillaTypes.ITEM, this.getOutput());
		}
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		FontRenderer fr = minecraft.fontRenderer;
		String powerFlatDesc = I18n.format("jei.ritual.power.flat", this.powerStart);
		String powerTickDesc = I18n.format("jei.ritual.power.tick", this.powerTick * 20);
		int mult = (int) (this.powerTick > 0 ? 3.1 : 2);
		if (this.powerStart > 0) {
			fr.drawString(powerFlatDesc, (recipeWidth - fr.getStringWidth(powerFlatDesc)) / 2, recipeHeight - (mult * fr.FONT_HEIGHT), 0, false);
		}
		if (this.powerTick > 0) {
			fr.drawString(powerTickDesc, (recipeWidth - fr.getStringWidth(powerTickDesc)) / 2, recipeHeight - (2 * fr.FONT_HEIGHT), 0, false);
		}
		fr.drawString(this.name, (recipeWidth - fr.getStringWidth(this.name)) / 2, 0, 0);

		int requiredCircles = this.circles & 3;
		EnumGlyphType typeFirst = EnumGlyphType.fromMeta((this.circles >> 2) & 3);
		EnumGlyphType typeSecond = EnumGlyphType.fromMeta((this.circles >> 4) & 3);
		EnumGlyphType typeThird = EnumGlyphType.fromMeta((this.circles >> 6) & 3);
		this.color(EnumGlyphType.GOLDEN, null, 0);

		int dx = 53, dy = 35;

		this.centerGlyph.draw(minecraft, dx, dy);
		this.color(typeFirst, minecraft, 0);
		this.circle1.draw(minecraft, dx, dy);
		if (requiredCircles > 0) {
			this.color(typeSecond, minecraft, 1);
			this.circle2.draw(minecraft, dx, dy);
		}
		if (requiredCircles > 1) {
			this.color(typeThird, minecraft, 2);
			this.circle3.draw(minecraft, dx, dy);
		}
	}

	private void color(EnumGlyphType gt, Minecraft minecraft, int circle) {
		switch (gt) {
			case ENDER:
				GlStateManager.color(0.5f, 0f, 0.5f);
				break;
			case GOLDEN:
				GlStateManager.color(1f, 1f, 0f);
				break;
			case NETHER:
				GlStateManager.color(0.8f, 0f, 0f);
				break;
			case NORMAL:
				GlStateManager.color(0.9f, 0.9f, 0.9f);
				break;
			case ANY:
				this.colorRandom(minecraft.world.getTotalWorldTime(), circle);
				break;
			default:
				Log.w("Probable bug in Covens [RitualWrapper.java]");
				break;
		}
	}

	private void colorRandom(long v, int circle) {
		int r = (int) ((v % 60) / 20);
		switch ((r + circle) % 3) {
			case 1:
				this.color(EnumGlyphType.NORMAL, null, 0);
				break;
			case 2:
				this.color(EnumGlyphType.ENDER, null, 0);
				break;
			case 0:
				this.color(EnumGlyphType.NETHER, null, 0);
				break;
			default:
				break;
		}
	}

	public List<ItemStack> getOutput() {
		return this.output;
	}

	private void setOutput(List<ItemStack> output) {
		this.output = output;
	}

	public List<List<ItemStack>> getInput() {
		return this.input;
	}

	private void setInput(List<List<ItemStack>> input) {
		this.input = input;
	}

}

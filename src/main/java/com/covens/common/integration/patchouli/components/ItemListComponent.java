package com.covens.common.integration.patchouli.components;

import java.util.List;

import com.covens.common.integration.patchouli.Patchouli;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.VariableHolder;

public class ItemListComponent implements ICustomComponent {

	int x, y;

	@VariableHolder
	public int slots;

	@VariableHolder
	public String registry;

	@VariableHolder
	public String name;

	@VariableHolder
	public String list_type;

	transient List<Ingredient> ingredients;
	transient int index = 0;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
		ingredients = Patchouli.getInputsFromRegistry(registry, name, list_type);
	}

	@Override
	public void render(IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		if (ingredients.size() > 0) {
			int maxIndex = 0;
			if (slots < ingredients.size()) {
				maxIndex = ingredients.size() - slots;
			}

			if (maxIndex > 0 && !GuiScreen.isShiftKeyDown()) {
				index = (int) ((System.currentTimeMillis() / 1000) % maxIndex);
			}

			int border = slots <= ingredients.size() ? 0 : (slots - ingredients.size())*17/2;
			
			GlStateManager.pushMatrix();
			for (int i = 0; i < Math.min(slots, ingredients.size()); i++) {
				context.renderIngredient(border + x + 17*i, y, mouseX, mouseY, ingredients.get(index + i));
			}
			GlStateManager.popMatrix();
		}
	}

}

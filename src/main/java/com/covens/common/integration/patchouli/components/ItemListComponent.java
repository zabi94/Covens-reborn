package com.covens.common.integration.patchouli.components;

import java.util.List;

import com.covens.common.integration.patchouli.Patchouli;

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

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
		ingredients = Patchouli.getInputsFromRegistry(registry, name, list_type);
	}

	@Override
	public void render(IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		if (ingredients.size() > 0) {
			int remaining = ingredients.size();
			int row = 0;
			int border = slots <= ingredients.size() ? 0 : (slots - ingredients.size())*17/2;
			GlStateManager.pushMatrix();
			while (remaining > 0) {
				for (int i = 0; i < Math.min(slots, remaining); i++) {
					context.renderIngredient(border + x + 17*i, y + 17*row, mouseX, mouseY, ingredients.get(row * slots + i));
				}
				row++;
				remaining -= slots;
			}
			GlStateManager.popMatrix();
		}
		
	}


}

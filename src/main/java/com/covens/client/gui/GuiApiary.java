package com.covens.client.gui;

import com.covens.client.ResourceLocations;
import com.covens.common.container.ContainerApiary;
import com.covens.common.tile.tiles.TileEntityApiary;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;


public class GuiApiary extends GuiContainer {

	public GuiApiary(InventoryPlayer playerInventory, TileEntityApiary tileApiary) {
		super(new ContainerApiary(playerInventory, tileApiary));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(ResourceLocations.APIARY_GUI);
		final int i = (this.width - this.xSize) / 2;
		final int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}
}

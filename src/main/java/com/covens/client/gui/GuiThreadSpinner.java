package com.covens.client.gui;

import com.covens.client.ResourceLocations;
import com.covens.common.container.ContainerThreadSpinner;
import com.covens.common.tile.tiles.TileEntityThreadSpinner;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiThreadSpinner extends GuiContainer {

	private TileEntityThreadSpinner tileEntity;

	public GuiThreadSpinner(InventoryPlayer playerInventory, TileEntityThreadSpinner te) {
		super(new ContainerThreadSpinner(playerInventory, te));
		this.xSize = 176;
		this.ySize = 166;
		this.tileEntity = te;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(ResourceLocations.THREAD_SPINNER_GUI);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		double progress = (double) this.tileEntity.getWork() / (TileEntityThreadSpinner.TOTAL_WORK - 10);
		this.drawTexturedModalRect(this.guiLeft + 85, this.guiTop + 33, 176, 0, (int) (22 * progress), 17);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = this.tileEntity.getName();
		this.fontRenderer.drawString(s, (this.xSize / 2) - (this.fontRenderer.getStringWidth(s) / 2), 6, 4210752);
		this.renderHoveredToolTip(mouseX - this.guiLeft, mouseY - this.guiTop);
	}

}

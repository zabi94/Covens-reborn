package com.covens.client.gui;

import java.io.IOException;
import java.util.List;

import com.covens.client.gui.components.GuiButtonFamiliar;
import com.covens.common.content.familiar.FamiliarDescriptor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiFamiliarSelector extends GuiScreen {

	private List<FamiliarDescriptor> playerFamiliars;
	
	private static final int buttonWidth = 300;
	private static final int buttonHeight = 20;
	private static final int buttonPadding = 10;
	private static int buttonAmount = 5;
	
	public GuiFamiliarSelector(List<FamiliarDescriptor> familiars) {
		if (familiars == null) {
			throw new IllegalStateException("Null familiar list");
		} else {
			playerFamiliars = familiars;
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		for (int i = 0; i < playerFamiliars.size(); i++) {
			GuiButtonFamiliar btn = new GuiButtonFamiliar(i, centered(buttonWidth, sr.getScaledWidth()), centered(buttonHeight, sr.getScaledHeight()) + offset(i), buttonWidth, buttonHeight, playerFamiliars.get(i), this);
			btn.visible = i <= buttonAmount;
			btn.enabled = btn.visible;
			this.addButton(btn);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof GuiButtonFamiliar) {
			((GuiButtonFamiliar) button).selectFamiliar();
			Minecraft.getMinecraft().displayGuiScreen(null);
		} else {
			super.actionPerformed(button);
		}
	}
	
	private static int offset(int i) {
		int index = (i % buttonAmount) - (buttonAmount/2);
		int position = index * (buttonPadding + buttonHeight);
		if (buttonAmount % 2 == 0) {
			position -= (buttonPadding/2);
		}
		return position;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawWorldBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private static int centered(int elementSize, int screenSize) {
		return (screenSize - elementSize)/2;
	}
	
}

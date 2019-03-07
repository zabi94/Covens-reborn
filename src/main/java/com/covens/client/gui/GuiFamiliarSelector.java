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
	
	private static final int buttonWidth = 200;
	private static final int buttonHeight = 20;
	private static final int buttonPadding = 5;
	private static int buttonAmount = 8;
	
	private GuiButton left;
	private GuiButton right;
	private int page = 0;
	
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
		buttonList.clear();
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		for (int i = 0; i < playerFamiliars.size(); i++) {
			GuiButtonFamiliar btn = new GuiButtonFamiliar(i, centered(buttonWidth, sr.getScaledWidth()), centered(buttonHeight, sr.getScaledHeight()) + offset(i), buttonWidth, buttonHeight, playerFamiliars.get(i));
			this.addButton(btn);
		}
		left = new GuiButton(-1, centered(350, sr.getScaledWidth()), centered(buttonHeight, sr.getScaledHeight()), 20, 20, "<");
		right = new GuiButton(-1, centered(-350, sr.getScaledWidth()) - 20, centered(buttonHeight, sr.getScaledHeight()), 20, 20, ">");
		this.addButton(left);
		this.addButton(right);
		setPage(0);
	}
	
	public void setPage(int page) {
		this.page = page;
		for (int i = 0; i < playerFamiliars.size(); i++) {
			boolean inPage = i / buttonAmount == page;
			buttonList.get(i).visible = inPage;
			buttonList.get(i).enabled = inPage;
		}
		left.enabled = page != 0;
		right.enabled = playerFamiliars.size() / buttonAmount > page;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof GuiButtonFamiliar) {
			((GuiButtonFamiliar) button).selectFamiliar();
			Minecraft.getMinecraft().displayGuiScreen(null);
		} else {
			if (button.equals(left) && page > 0) {
				setPage(page - 1);
			} else if (button.equals(right) && page < playerFamiliars.size() / buttonAmount) {
				setPage(page + 1);
			}
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
		this.buttonList.stream()
			.filter(b -> b instanceof GuiButtonFamiliar)
			.filter(b -> b.isMouseOver())
			.map(b -> (GuiButtonFamiliar) b)
			.forEach(b -> {
				drawHoveringText(b.getTooltip(), mouseX, mouseY);
		});
	}
	
	
	private static int centered(int elementSize, int screenSize) {
		return (screenSize - elementSize)/2;
	}
	
}

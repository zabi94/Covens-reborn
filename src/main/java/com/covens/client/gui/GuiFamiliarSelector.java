package com.covens.client.gui;

import java.util.List;
import java.util.stream.Collectors;

import com.covens.common.content.familiar.FamiliarDescriptor;
import com.covens.common.core.helper.Log;

import net.minecraft.client.gui.GuiScreen;

public class GuiFamiliarSelector extends GuiScreen {

	private List<FamiliarDescriptor> playerFamiliars;
	
	public GuiFamiliarSelector(List<FamiliarDescriptor> familiars) {
		if (familiars == null) {
			throw new IllegalStateException("Null familiar list");
		} else {
			playerFamiliars = familiars;
			playerFamiliars.forEach(fd -> Log.i(fd));
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawWorldBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		List<String> strings = playerFamiliars.stream()
				.map(fd -> fd.getUuid() + " - " + fd.getName()+" - "+fd.getLastKnownPos())
				.collect(Collectors.toList());
		this.drawHoveringText(""+playerFamiliars.size(), 0, 0);
		this.drawHoveringText(strings, 0, 50);
	}
	
}

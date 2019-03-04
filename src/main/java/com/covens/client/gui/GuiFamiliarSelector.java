package com.covens.client.gui;

import java.util.List;

import com.covens.common.content.familiar.FamiliarDescriptor;

import net.minecraft.client.gui.GuiScreen;

public class GuiFamiliarSelector extends GuiScreen {

	List<FamiliarDescriptor> playerFamiliars;
	
	public GuiFamiliarSelector(List<FamiliarDescriptor> familiars) {
		playerFamiliars = familiars;
	}
	
}

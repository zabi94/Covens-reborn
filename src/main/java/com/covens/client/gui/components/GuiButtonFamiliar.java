package com.covens.client.gui.components;

import java.util.List;

import com.covens.common.content.familiar.FamiliarDescriptor;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.net.messages.SelectFamiliar;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiButtonFamiliar extends GuiButton {
	
	private FamiliarDescriptor descriptor;

	public GuiButtonFamiliar(int buttonId, int x, int y, int w, int h, FamiliarDescriptor desc) {
		super(buttonId, x, y, w, h, desc.getName());
		this.descriptor = desc;
	}
	
	public void selectFamiliar() {
		NetworkHandler.HANDLER.sendToServer(new SelectFamiliar(descriptor));
	}

	public List<String> getTooltip() {
		List<String> res = Lists.newArrayList();
		res.add(descriptor.getName());
		if (descriptor.getLastKnownPos().getDim() == Minecraft.getMinecraft().world.provider.getDimension()) {
			res.add(TextFormatting.GRAY+I18n.format("covens.familiar.last_at", descriptor.getLastKnownPos().getX(), descriptor.getLastKnownPos().getY(), descriptor.getLastKnownPos().getZ()));
		} else {
			res.add(TextFormatting.GRAY+I18n.format("covens.familiar.last_at_dim", descriptor.getLastKnownPos().getDim()));
		}
		if (!descriptor.isAvailable()) {
			res.add(TextFormatting.DARK_RED+I18n.format("covens.familiar.unavailable"));
		}
		return res;
	}

}

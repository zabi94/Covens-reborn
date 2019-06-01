package com.covens.client.core.hud;

import java.util.List;

import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.lib.LibMod;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.hud.IHudComponent;

@SideOnly(Side.CLIENT)
public class FamiliarHUD implements IHudComponent {
	
	private static final ResourceLocation ICON = new ResourceLocation(LibMod.MOD_ID, "textures/gui/familiar_icon.png");
	private static final ResourceLocation ID = new ResourceLocation(LibMod.MOD_ID, "selected_familiar");

//	public FamiliarHUD() {
//		super(64, 32, , "covens.hud.familiar.description");
//	}


	@Override
	public List<String> getTooltip(ITooltipFlag flag) {
		return Lists.newArrayList(I18n.format("covens.hud.open_familiar_gui"));
	}

	@Override
	public void drawAt(int x, int y, int w, int h, RenderMode m) {
		String name = "---";
		if (m == RenderMode.NORMAL) {
			name = Minecraft.getMinecraft().player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliarName;
		}
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		fr.drawString(name, (float) (x + ((w - fr.getStringWidth(name)) / 2)), (float) y, 0xFFFFFF, true);
		String abname = ModAbilities.COMMAND_FAMILIAR.lastCommand == null ? I18n.format("familiar.command.none") : I18n.format("covens.familiar_order."+ModAbilities.COMMAND_FAMILIAR.lastCommand.name().toLowerCase());
		fr.drawString(abname, (float) (x + ((w - fr.getStringWidth(abname)) / 2)), y + fr.FONT_HEIGHT + 2, 0xFFFFFF, true);
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		GuiScreen.drawScaledCustomSizeModalRect(x + 24, y + 19, 0, 0, w, h, 16, 16, w, h);
	}

	@Override
	public boolean isShown() {
		if (Minecraft.getMinecraft().player != null) {
			String name = Minecraft.getMinecraft().player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliarName;
			return name != null && name.length()>0;
		}
		return false;
	}

	@Override
	public ResourceLocation getIdentifier() {
		return ID;
	}

	@Override
	public String getTitleTranslationKey() {
		return "covens.hud.familiar.title";
	}

	@Override
	public boolean canResize() {
		return true;
	}

}

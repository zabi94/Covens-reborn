package com.covens.client.core.hud;

import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import zabi.minecraft.minerva.client.hud.HudComponent;

public class FamiliarHUD extends HudComponent {
	
	private static final ResourceLocation ICON = new ResourceLocation(LibMod.MOD_ID, "textures/gui/familiar_icon.png");

	public FamiliarHUD() {
		super(64, 32);
	}

	@Override
	public void resetConfig() {
		ModConfig.CLIENT.FAMILIAR_HUD.v_anchor = EnumHudAnchor.END_ABSOLUTE;
		ModConfig.CLIENT.FAMILIAR_HUD.h_anchor = EnumHudAnchor.END_ABSOLUTE;
		ModConfig.CLIENT.FAMILIAR_HUD.x = 10;
		ModConfig.CLIENT.FAMILIAR_HUD.y = 10;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return "covens.hud.open_familiar_gui";
	}

	@Override
	public void onClick(int mouseX, int mouseY) {
		// TODO open familiar GUI
	}

	@Override
	public void render(ScaledResolution resolution, float partialTicks, boolean renderDummy) {
		String name = Minecraft.getMinecraft().player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliarName;
		if (name != null && name.length()>0) {
			FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
			fr.drawString(name, (float) (this.getX() + ((this.w - fr.getStringWidth(name)) / 2)), (float) this.getY(), 0xFFFFFF, true);
			Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
			renderTextureAt(this.getX() + 24, this.getY() + 16, 16, 16);
		}
	}

	@Override
	public boolean isActive() {
		return !ModConfig.CLIENT.FAMILIAR_HUD.deactivate;
	}

	@Override
	public void setHidden(boolean hidden) {
		ModConfig.CLIENT.FAMILIAR_HUD.deactivate = hidden;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public double getX() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.FAMILIAR_HUD.h_anchor.dataToPixel(ModConfig.CLIENT.FAMILIAR_HUD.x, this.getWidth(), sr.getScaledWidth());
	}

	@Override
	public double getY() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.FAMILIAR_HUD.v_anchor.dataToPixel(ModConfig.CLIENT.FAMILIAR_HUD.y, this.getHeight(), sr.getScaledHeight());
	}

	@Override
	public void setRelativePosition(double x, double y, EnumHudAnchor horizontal, EnumHudAnchor vertical) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		ModConfig.CLIENT.FAMILIAR_HUD.v_anchor = vertical;
		ModConfig.CLIENT.FAMILIAR_HUD.h_anchor = horizontal;
		ModConfig.CLIENT.FAMILIAR_HUD.x = horizontal.pixelToData(x, this.getWidth(), sr.getScaledWidth());
		ModConfig.CLIENT.FAMILIAR_HUD.y = vertical.pixelToData(y, this.getHeight(), sr.getScaledHeight());
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public EnumHudAnchor getAnchorHorizontal() {
		return ModConfig.CLIENT.FAMILIAR_HUD.h_anchor;
	}

	@Override
	public EnumHudAnchor getAnchorVertical() {
		return ModConfig.CLIENT.FAMILIAR_HUD.v_anchor;
	}

}

package com.covens.client.core.hud;

import com.covens.api.hotbar.IHotbarAction;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.config.ConfigManager;
import zabi.minecraft.minerva.client.hud.HudComponent;

@SideOnly(Side.CLIENT)
public class SelectedActionHUD extends HudComponent {

	public SelectedActionHUD() {
		super(32, 32, "covens.hud.selected_action.title", "covens.hud.selected_action.description");
	}

	@Override
	public boolean isActive() {
		return !ModConfig.CLIENT.CURRENTACTION_HUD.deactivate;
	}

	@Override
	public void setHidden(boolean hidden) {
		ModConfig.CLIENT.CURRENTACTION_HUD.deactivate = hidden;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public double getX() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.CURRENTACTION_HUD.h_anchor.dataToPixel(ModConfig.CLIENT.CURRENTACTION_HUD.x, this.getWidth(), sr.getScaledWidth());
	}

	@Override
	public double getY() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.CURRENTACTION_HUD.v_anchor.dataToPixel(ModConfig.CLIENT.CURRENTACTION_HUD.y, this.getHeight(), sr.getScaledHeight());
	}

	@Override
	public void setRelativePosition(double x, double y, EnumHudAnchor horizontal, EnumHudAnchor vertical) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		ModConfig.CLIENT.CURRENTACTION_HUD.v_anchor = vertical;
		ModConfig.CLIENT.CURRENTACTION_HUD.h_anchor = horizontal;
		ModConfig.CLIENT.CURRENTACTION_HUD.x = horizontal.pixelToData(x, this.getWidth(), sr.getScaledWidth());
		ModConfig.CLIENT.CURRENTACTION_HUD.y = vertical.pixelToData(y, this.getHeight(), sr.getScaledHeight());
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public void resetConfig() {
		ModConfig.CLIENT.CURRENTACTION_HUD.v_anchor = EnumHudAnchor.START_RELATIVE;
		ModConfig.CLIENT.CURRENTACTION_HUD.h_anchor = EnumHudAnchor.CENTER_ABSOLUTE;
		ModConfig.CLIENT.CURRENTACTION_HUD.x = 0;
		ModConfig.CLIENT.CURRENTACTION_HUD.y = 0.25;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public EnumHudAnchor getAnchorHorizontal() {
		return ModConfig.CLIENT.CURRENTACTION_HUD.h_anchor;
	}

	@Override
	public EnumHudAnchor getAnchorVertical() {
		return ModConfig.CLIENT.CURRENTACTION_HUD.v_anchor;
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return null;
	}

	@Override
	public int getWidth() {
		return (int) (super.getWidth() * ModConfig.CLIENT.CURRENTACTION_HUD.scale);
	}

	@Override
	public int getHeight() {
		return (int) (super.getHeight() * ModConfig.CLIENT.CURRENTACTION_HUD.scale);
	}

	@Override
	public void onClick(int mouseX, int mouseY) {
		// NO-OP
	}

	@Override
	public void render(ScaledResolution resolution, float partialTicks, boolean renderDummy) {
		if (ExtraBarButtonsHUD.INSTANCE.isInExtraBar() || renderDummy) {
			IHotbarAction sel = renderDummy ? ModAbilities.NIGHT_VISION_VAMPIRE : ExtraBarButtonsHUD.INSTANCE.actionScroller[0];
			if (sel != null) {
				GlStateManager.pushMatrix();
				sel.render(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0.4f);
				GlStateManager.popMatrix();
			}
		}
	}

}

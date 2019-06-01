package com.covens.client.core.hud;

import java.util.List;

import com.covens.api.transformation.DefaultTransformations;
import com.covens.api.transformation.ITransformation;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.lib.LibMod;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.hud.IHudComponent;

@SideOnly(Side.CLIENT)
public class MoonHUD implements IHudComponent {

	private static final float minWarn = 12000;
	private static final int transform = 12900;
	private static final ResourceLocation MOON = new ResourceLocation(LibMod.MOD_ID, "textures/gui/moon_warning.png");
	private static final ResourceLocation ID = new ResourceLocation(LibMod.MOD_ID, "moon_warn");

//	public MoonHUD() {
//		super(24, 24, , "covens.hud.moon.description");
//	}

	@Override
	public List<String> getTooltip(ITooltipFlag flag) {
		if (Minecraft.getMinecraft().world.getMoonPhase() == 0) {
			int warn = 0;
			if (Minecraft.getMinecraft().world.getWorldTime() >= minWarn) {
				if (Minecraft.getMinecraft().world.getWorldTime() > transform) {
					warn = 2;
				} else {
					warn = 1;
				}
			}
			return Lists.newArrayList(I18n.format("moon.tooltip." + warn));
		}
		return null;
	}

	@Override
	public void drawAt(int x, int y, int w, int h, RenderMode m) {
		World world = Minecraft.getMinecraft().world;
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(MOON);
		if (m != RenderMode.NORMAL) {
			GuiScreen.drawScaledCustomSizeModalRect(x, y, 0, 0, w/2, h/2, 16, 16, 8, 8);
		} else {
			if (world.getWorldTime() < minWarn) {
				float state = 1f - ((minWarn - world.getWorldTime()) / (2 * minWarn));
				GlStateManager.disableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.color(1, 1, 1, state);
			} else if (world.getWorldTime() > transform) {
				GlStateManager.color(1, 1f, 1f, 1);
			} else {
				float shade = 0.75f + (0.25f * (float) Math.sin(Math.PI * (Minecraft.getMinecraft().player.ticksExisted / 5f)));
				GlStateManager.color(1, shade, shade, 1);
			}
			GuiScreen.drawScaledCustomSizeModalRect(x, y, 0, 0, w, h, 8, 8, w, h);
		}

		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
	}
	
	@Override
	public boolean isShown() {
		World world = Minecraft.getMinecraft().world;
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player == null || world == null) {
			return false;
		}
		ITransformation t = player.getCapability(CapabilityTransformation.CAPABILITY, null).getType();
		return t == DefaultTransformations.WEREWOLF && world.getMoonPhase() == 0;
	}

	@Override
	public ResourceLocation getIdentifier() {
		return ID;
	}

	@Override
	public String getTitleTranslationKey() {
		return "covens.hud.moon.title";
	}
	
	@Override
	public boolean canResize() {
		return true;
	}

}

package com.covens.client.core.hud;

import org.lwjgl.opengl.GL11;

import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.content.transformation.vampire.CapabilityVampire;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.hud.HudComponent;

@SideOnly(Side.CLIENT)
public class VampireBloodBarHUD extends HudComponent {

	private static final ResourceLocation TEXTURE = new ResourceLocation(LibMod.MOD_ID, "textures/gui/blood_meter.png");

	public VampireBloodBarHUD() {
		super(80, 8, "covens.hud.vampire_blood.title", "covens.hud.vampire_blood.description");
		MinecraftForge.EVENT_BUS.register(this);
	}

	protected static void renderTextureAt(double x, double y, int w, int h, double uMin, double vMin, double uMax, double vMax) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();

		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buff.pos(x, y + h, 0).tex(uMin, vMax).endVertex();
		buff.pos(x + w, y + h, 0).tex(uMax, vMax).endVertex();
		buff.pos(x + w, y, 0).tex(uMax, vMin).endVertex();
		buff.pos(x, y, 0).tex(uMin, vMin).endVertex();

		tessellator.draw();
	}

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Pre event) {
		if (((event.getType() == RenderGameOverlayEvent.ElementType.FOOD) || (event.getType() == RenderGameOverlayEvent.ElementType.AIR)) && (Minecraft.getMinecraft().player.getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.VAMPIRE)) {
			event.setCanceled(true);
		}
	}

	@Override
	public boolean isActive() {
		return !ModConfig.CLIENT.VAMPIRE_METER_HUD.deactivate;
	}

	@Override
	public void setHidden(boolean hidden) {
		ModConfig.CLIENT.VAMPIRE_METER_HUD.deactivate = hidden;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public double getX() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.VAMPIRE_METER_HUD.h_anchor.dataToPixel(ModConfig.CLIENT.VAMPIRE_METER_HUD.x, this.getWidth(), sr.getScaledWidth());
	}

	@Override
	public double getY() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.VAMPIRE_METER_HUD.v_anchor.dataToPixel(ModConfig.CLIENT.VAMPIRE_METER_HUD.y, this.getHeight(), sr.getScaledHeight());
	}

	@Override
	public void setRelativePosition(double x, double y, EnumHudAnchor horizontal, EnumHudAnchor vertical) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		ModConfig.CLIENT.VAMPIRE_METER_HUD.v_anchor = vertical;
		ModConfig.CLIENT.VAMPIRE_METER_HUD.h_anchor = horizontal;
		ModConfig.CLIENT.VAMPIRE_METER_HUD.x = horizontal.pixelToData(x, this.getWidth(), sr.getScaledWidth());
		ModConfig.CLIENT.VAMPIRE_METER_HUD.y = vertical.pixelToData(y, this.getHeight(), sr.getScaledHeight());
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public void resetConfig() {
		ModConfig.CLIENT.VAMPIRE_METER_HUD.v_anchor = EnumHudAnchor.END_ABSOLUTE;
		ModConfig.CLIENT.VAMPIRE_METER_HUD.h_anchor = EnumHudAnchor.CENTER_ABSOLUTE;
		ModConfig.CLIENT.VAMPIRE_METER_HUD.x = 9 + (this.getWidth() / 2);
		ModConfig.CLIENT.VAMPIRE_METER_HUD.y = 39 - this.getHeight();
		ModConfig.CLIENT.VAMPIRE_METER_HUD.deactivate = false;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public EnumHudAnchor getAnchorHorizontal() {
		return ModConfig.CLIENT.VAMPIRE_METER_HUD.h_anchor;
	}

	@Override
	public EnumHudAnchor getAnchorVertical() {
		return ModConfig.CLIENT.VAMPIRE_METER_HUD.v_anchor;
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
//		ITransformationData data = Minecraft.getMinecraft().player.getCapability(CapabilityTransformationData.CAPABILITY, null);
//		if (data.getType() == DefaultTransformations.VAMPIRE) {
//			return data.getBlood()+"/"+data.getMaxBlood();
//		}
		return null;
	}

	@Override
	public void onClick(int mouseX, int mouseY) {
		// NO-OP
	}

	@Override
	public void render(ScaledResolution resolution, float partialTicks, boolean renderDummy) {
		int level = 5;
		double blood = 0;
		boolean doRender = false;
		if (renderDummy) {
			blood = (System.currentTimeMillis() % 3000) / 3000d;
			doRender = true;
		} else {
			CapabilityTransformation t_data = Minecraft.getMinecraft().player.getCapability(CapabilityTransformation.CAPABILITY, null);
			CapabilityVampire v_data = Minecraft.getMinecraft().player.getCapability(CapabilityVampire.CAPABILITY, null);
			if (t_data.getType() == DefaultTransformations.VAMPIRE) {
				doRender = true;
				blood = v_data.getBlood() / (double) v_data.getMaxBlood(Minecraft.getMinecraft().player);
				level = t_data.getLevel();
			}
		}
		if (doRender) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1, 1, 1, 1);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
			GlStateManager.enableBlend();

			renderTextureAt(this.getX() + 1, this.getY(), (int) ((this.getWidth() - 2) * blood), this.getHeight(), 0, 0.5, blood, 1); // Draw the content
			renderTextureAt(this.getX(), this.getY(), 1, this.getHeight(), 4d / this.getWidth(), 0, 5d / this.getWidth(), 0.5); // Draw the first vertical line
			renderTextureAt((this.getX() + this.getWidth()) - 2, this.getY(), 2, this.getHeight(), 3d / this.getWidth(), 0, 5d / this.getWidth(), 0.5); // Draw the last 2 vertical lines
			int pixelsPerSlot = (int) ((this.getWidth() - 2d) / (level + 1d));
			for (int i = 0; i < level; i++) { // Draw each slot
				renderTextureAt(1 + this.getX() + (i * pixelsPerSlot), this.getY(), 1, this.getHeight(), 3d / this.getWidth(), 0, 4d / this.getWidth(), 0.5);
				renderTextureAt(2 + this.getX() + (i * pixelsPerSlot), this.getY(), pixelsPerSlot - 1, this.getHeight(), 0, 0, 1d / this.getWidth(), 0.5);
			}
			renderTextureAt(1 + this.getX() + (level * pixelsPerSlot), this.getY(), 1, this.getHeight(), 3d / this.getWidth(), 0, 4d / this.getWidth(), 0.5);
			renderTextureAt(2 + this.getX() + (level * pixelsPerSlot), this.getY(), this.getWidth() - (pixelsPerSlot * level) - 4, this.getHeight(), 0, 0, 1d / this.getWidth(), 0.5);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public int getWidth() {
		return 80;
	}

	@Override
	public int getHeight() {
		return 8;
	}

}

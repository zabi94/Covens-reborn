package com.covens.client.core.hud;

import org.lwjgl.opengl.GL11;

import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.content.transformation.vampire.CapabilityVampire;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.hud.IHudComponent;

@SideOnly(Side.CLIENT)
public class VampireBloodBarHUD implements IHudComponent {

	private static final ResourceLocation TEXTURE = new ResourceLocation(LibMod.MOD_ID, "textures/gui/blood_meter.png");
	private static final ResourceLocation ID = new ResourceLocation(LibMod.MOD_ID, "vampire_meter");

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

	@Override
	public boolean isShown() {
		EntityPlayer player = Minecraft.getMinecraft().player;
		return player != null && player.getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.VAMPIRE;
	}

	@Override
	public void drawAt(int x, int y, int w, int h, RenderMode m) {
		int level = 5;
		double blood = 0;
		if (m != RenderMode.NORMAL) {
			blood = (System.currentTimeMillis() % 3000) / 3000d;
		} else {
			CapabilityTransformation t_data = Minecraft.getMinecraft().player.getCapability(CapabilityTransformation.CAPABILITY, null);
			CapabilityVampire v_data = Minecraft.getMinecraft().player.getCapability(CapabilityVampire.CAPABILITY, null);
			if (t_data.getType() == DefaultTransformations.VAMPIRE) {
				blood = v_data.getBlood() / (double) v_data.getMaxBlood(Minecraft.getMinecraft().player);
				level = t_data.getLevel();
			}
			GlStateManager.color(1, 1, 1, 1);
		}
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		GlStateManager.enableBlend();

		renderTextureAt(x + 1, y, (int) ((w - 2) * blood), h, 0, 0.5, blood, 1); // Draw the content
		renderTextureAt(x, y, 1, h, 4d / w, 0, 5d / w, 0.5); // Draw the first vertical line
		renderTextureAt((x + w) - 2, y, 2, h, 3d / w, 0, 5d / w, 0.5); // Draw the last 2 vertical lines
		int pixelsPerSlot = (int) ((w - 2d) / (level + 1d));
		for (int i = 0; i < level; i++) { // Draw each slot
			renderTextureAt(1 + x + (i * pixelsPerSlot), y, 1, h, 3d / w, 0, 4d / w, 0.5);
			renderTextureAt(2 + x + (i * pixelsPerSlot), y, pixelsPerSlot - 1, h, 0, 0, 1d / w, 0.5);
		}
		renderTextureAt(1 + x + (level * pixelsPerSlot), y, 1, h, 3d / w, 0, 4d / w, 0.5);
		renderTextureAt(2 + x + (level * pixelsPerSlot), y, w - (pixelsPerSlot * level) - 4, h, 0, 0, 1d / w, 0.5);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	@Override
	public ResourceLocation getIdentifier() {
		return ID;
	}

	@Override
	public String getTitleTranslationKey() {
		return "covens.hud.vampire_blood.title";
	}

}

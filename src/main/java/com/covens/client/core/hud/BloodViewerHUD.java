package com.covens.client.core.hud;

import com.covens.api.transformation.IBloodReserve;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.content.transformation.vampire.blood.CapabilityBloodReserve;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.hud.IHudComponent;

@SideOnly(Side.CLIENT)
public class BloodViewerHUD implements IHudComponent {

	private static final ResourceLocation TEXTURE = new ResourceLocation(LibMod.MOD_ID, "textures/gui/blood_droplet.png");
	private static final ResourceLocation ID = new ResourceLocation(LibMod.MOD_ID, "bloodviewer");

	@Override
	public void drawAt(int x, int y, int w, int h, RenderMode m) {
		float filled = (System.currentTimeMillis() % 3000) / 3000f;
		if (m == RenderMode.NORMAL) {
			IBloodReserve ibr = Minecraft.getMinecraft().pointedEntity.getCapability(CapabilityBloodReserve.CAPABILITY, null);
			filled = ibr.getPercentFilled();
		}
		GlStateManager.pushMatrix();
		GlStateManager.color(filled, filled, filled);
		GlStateManager.enableAlpha();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		GuiScreen.drawScaledCustomSizeModalRect(x, y, 0, 0, w, h, 10, 14, w, h);
		GlStateManager.popMatrix();

	}
	@Override
	public ResourceLocation getIdentifier() {
		return ID;
	}
	@Override
	public String getTitleTranslationKey() {
		return "covens.hud.bloodviewer.title";
	}
	
	@Override
	public boolean canResize() {
		return true;
	}
	
	@Override
	public boolean isShown() {
		if (ExtraBarButtonsHUD.INSTANCE.actionScroller.length > 0 && ExtraBarButtonsHUD.INSTANCE.actionScroller[0] == ModAbilities.DRAIN_BLOOD && ExtraBarButtonsHUD.INSTANCE.isInExtraBar() && Minecraft.getMinecraft().pointedEntity instanceof EntityLivingBase) {
			IBloodReserve ibr = Minecraft.getMinecraft().pointedEntity.getCapability(CapabilityBloodReserve.CAPABILITY, null);
			int maxBlood = ibr.getMaxBlood();
			return maxBlood > 0;
		}
		return false;
	}
	
}

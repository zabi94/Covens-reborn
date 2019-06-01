package com.covens.client.core.hud;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.covens.api.CovensAPI;
import com.covens.api.infusion.DefaultInfusions;
import com.covens.api.mp.MPContainer;
import com.covens.api.mp.MPUsingItem;
import com.covens.client.ResourceLocations;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.lib.LibMod;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.hud.HudController;
import zabi.minecraft.minerva.client.hud.IHudComponent;

@SideOnly(Side.CLIENT)
public class EnergyHUD implements IHudComponent {
	
	private static final ResourceLocation ID = new ResourceLocation(LibMod.MOD_ID, "mp_hud");

	private int renderTime;
	private float visibilityLeft;
	private int oldEnergy = -1, oldMaxEnergy = -1;
	private ResourceLocation oldInfusion = null;
	private float barPulse;
	private boolean reversePulse;
	private boolean shouldPulse = false; // Only pulsate with white overlay after energy has changed
	private int lastPulsed = 40; // Prevents pulsating incontrollably when recharging fast enough. Min ticks
									// between 2 pulsation

	public EnergyHUD() {
//		super(25, 102, , "covens.hud.energy.title");
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if ((event.phase == TickEvent.Phase.END) && (Minecraft.getMinecraft().player != null)) {
			MPContainer storage = Minecraft.getMinecraft().player.getCapability(MPContainer.CAPABILITY, null);
			if (this.lastPulsed > 0) {
				this.lastPulsed--;
			}
			this.checkIfBarShouldStay(storage);
			this.tickBarTimer(storage);
			this.calculateWhitePulsation();
		}
	}

	private void tickBarTimer(MPContainer storage) {
		if ((this.renderTime > 0) && (storage.getAmount() == storage.getMaxAmount())) {
			if (this.renderTime < 20) {
				this.visibilityLeft -= 0.05F;
				this.visibilityLeft = MathHelper.clamp(this.visibilityLeft, 0F, 1F);
			}
			this.renderTime--;
		}
	}

	private void checkIfBarShouldStay(MPContainer storage) {
		boolean energyChanged = (this.oldEnergy != storage.getAmount()) || (this.oldMaxEnergy != storage.getMaxAmount()) || (this.oldInfusion != CovensAPI.getAPI().getPlayerInfusion(Minecraft.getMinecraft().player).getTexture());
		if (energyChanged) {
			this.shouldPulse = this.lastPulsed == 0;
		}
		if (energyChanged || this.isItemEnergyUsing() || !ModConfig.CLIENT.autoHideMPHud) {
			this.oldEnergy = storage.getAmount();
			this.oldMaxEnergy = storage.getMaxAmount();
			this.oldInfusion = CovensAPI.getAPI().getPlayerInfusion(Minecraft.getMinecraft().player).getTexture();
			this.renderTime = 60;
			this.visibilityLeft = 1F;
		}
	}

	private void calculateWhitePulsation() {
		if (this.shouldPulse) {
			if (!this.reversePulse) {
				this.barPulse += 0.15F;
				if (this.barPulse > 1F) {
					this.barPulse = 1F;
					this.reversePulse = true;
				}
			} else {
				this.barPulse -= 0.15F;
				if (this.barPulse < 0F) {
					this.barPulse = 0;
					this.reversePulse = false;
					this.shouldPulse = false;
					this.lastPulsed = 40;
				}
			}
		}
	}

	private boolean isItemEnergyUsing() { // Don't hide HUD when holding items that use ME/MP/AP
		EntityPlayer p = Minecraft.getMinecraft().player;
		if (p == null) {
			return false;
		}
		if (p.getHeldItemMainhand().hasCapability(MPUsingItem.CAPABILITY, null)) {
			return true;
		}
		if (p.getHeldItemOffhand().hasCapability(MPUsingItem.CAPABILITY, null)) {
			return true;
		}
		return HudController.INSTANCE.isEditModeActive();
	}
	
	
	@Override
	public boolean shouldShowTooltips() {
		return this.renderTime > 0;
	}
	
	@Override
	public List<String> getTooltip(ITooltipFlag flag) {
		MPContainer energy = Minecraft.getMinecraft().player.getCapability(MPContainer.CAPABILITY, null);
		return Lists.newArrayList(energy.getAmount() + "/" + energy.getMaxAmount());
	}

	private void renderTexture(double x, double y, double width, double height, double vMin, double vMax) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();

		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buff.pos(x, y + height, 0).tex(0, vMax).endVertex();
		buff.pos(x + width, y + height, 0).tex(1, vMax).endVertex();
		buff.pos(x + width, y, 0).tex(1, vMin).endVertex();
		buff.pos(x, y, 0).tex(0, vMin).endVertex();

		tessellator.draw();
	}

	private void renderBarContent(double filled, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, this.visibilityLeft);
		Minecraft.getMinecraft().getTextureManager().bindTexture(ResourceLocations.ENERGY_BACKGROUND_FILL);
		this.renderTexture(x + 9, y + 14 + (74 * (1 - filled)), 7, 74 * filled, 0, filled);
		GlStateManager.popMatrix();
	}

	private void renderPulse(double filled, int x, int y) {
		float alpha = this.barPulse * this.visibilityLeft;
		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, alpha);
		Minecraft.getMinecraft().getTextureManager().bindTexture(ResourceLocations.ENERGY_BACKGROUND_PULSE);
		this.renderTexture(x + 9, y + 14 + (74 * (1 - filled)), 7, 74 * filled, 0, filled);
		GlStateManager.popMatrix();
	}

	private void renderFrame(ResourceLocation texture, int x, int y, int w, int h) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, this.visibilityLeft);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.renderTexture(x, y, w, h, 0, 1);
		GlStateManager.popMatrix();
	}

	private void renderText(int amount, int maxAmount) {
		// NO-OP
	}

	private double getFillLevel(MPContainer energy, float partialTicks) {
		double interpEnergy = 0;
		if (this.oldEnergy >= 0) {
			interpEnergy = ((double) (energy.getAmount() - this.oldEnergy) * partialTicks) + this.oldEnergy;
		} else {
			interpEnergy = energy.getAmount();
		}
		return interpEnergy / energy.getMaxAmount();
	}


	@Override
	public void drawAt(int x, int y, int w, int h, RenderMode m) {
		if (m != RenderMode.NORMAL) {
			float fll = (System.currentTimeMillis() % 3000) / 3000f;
			this.renderBarContent(fll, x, y);
			this.renderFrame(DefaultInfusions.NONE.getTexture(), x, y, w, h);
			this.renderText(2000, 2000);
		} else if (this.renderTime > 0) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.color(1, 1, 1, 1);
			MPContainer energy = Minecraft.getMinecraft().player.getCapability(MPContainer.CAPABILITY, null);
			double fill = this.getFillLevel(energy, Minecraft.getMinecraft().getRenderPartialTicks());
			this.renderBarContent(fill, x, y);
			this.renderPulse(fill, x, y);
			this.renderFrame(CovensAPI.getAPI().getPlayerInfusion(Minecraft.getMinecraft().player).getTexture(), x, y, w, h);
			this.renderText(energy.getAmount(), energy.getMaxAmount());
			GlStateManager.popMatrix();
		}
		
	}

	@Override
	public ResourceLocation getIdentifier() {
		return ID;
	}

	@Override
	public String getTitleTranslationKey() {
		return "covens.hud.energy.title";
	}

}

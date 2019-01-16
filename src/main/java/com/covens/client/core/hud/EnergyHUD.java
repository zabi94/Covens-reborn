package com.covens.client.core.hud;

import org.lwjgl.opengl.GL11;

import com.covens.api.CovensAPI;
import com.covens.api.infusion.DefaultInfusions;
import com.covens.api.mp.IMagicPowerContainer;
import com.covens.api.mp.IMagicPowerUsingItem;
import com.covens.client.ResourceLocations;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.hud.HudComponent;
import zabi.minecraft.minerva.client.hud.HudController;

/**
 * This class was created by Arekkuusu on 21/04/2017. It's distributed as part
 * of Covens under the MIT license.
 */
@SideOnly(Side.CLIENT)
public class EnergyHUD extends HudComponent {

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
		super(25, 102);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if ((event.phase == TickEvent.Phase.END) && (Minecraft.getMinecraft().player != null)) {
			IMagicPowerContainer storage = Minecraft.getMinecraft().player.getCapability(IMagicPowerContainer.CAPABILITY, null);
			if (this.lastPulsed > 0) {
				this.lastPulsed--;
			}
			this.checkIfBarShouldStay(storage);
			this.tickBarTimer(storage);
			this.calculateWhitePulsation();
		}
	}

	private void tickBarTimer(IMagicPowerContainer storage) {
		if ((this.renderTime > 0) && (storage.getAmount() == storage.getMaxAmount())) {
			if (this.renderTime < 20) {
				this.visibilityLeft -= 0.05F;
				this.visibilityLeft = MathHelper.clamp(this.visibilityLeft, 0F, 1F);
			}
			this.renderTime--;
		}
	}

	private void checkIfBarShouldStay(IMagicPowerContainer storage) {
		boolean energyChanged = (this.oldEnergy != storage.getAmount()) || (this.oldMaxEnergy != storage.getMaxAmount()) || (this.oldInfusion != CovensAPI.getAPI().getPlayerInfusion(Minecraft.getMinecraft().player).getTexture());
		if (energyChanged) {
			this.shouldPulse = this.lastPulsed == 0;
		}
		if (energyChanged || this.isItemEnergyUsing() || !ModConfig.CLIENT.ENERGY_HUD.autoHide) {
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
		if (p.getHeldItemMainhand().hasCapability(IMagicPowerUsingItem.CAPABILITY, null)) {
			return true;
		}
		if (p.getHeldItemOffhand().hasCapability(IMagicPowerUsingItem.CAPABILITY, null)) {
			return true;
		}
		return HudController.INSTANCE.isEditModeActive();
	}

	@Override
	public void resetConfig() {
		ModConfig.CLIENT.ENERGY_HUD.v_anchor = EnumHudAnchor.CENTER_ABSOLUTE;
		ModConfig.CLIENT.ENERGY_HUD.h_anchor = EnumHudAnchor.START_ABSOULTE;
		ModConfig.CLIENT.ENERGY_HUD.x = 10;
		ModConfig.CLIENT.ENERGY_HUD.y = 0;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		if (this.renderTime > 0) {
			IMagicPowerContainer energy = Minecraft.getMinecraft().player.getCapability(IMagicPowerContainer.CAPABILITY, null);
			return energy.getAmount() + "/" + energy.getMaxAmount();
		}
		return null;
	}

	@Override
	public void onClick(int mouseX, int mouseY) {
		// NO-OP
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

	@Override
	public void render(ScaledResolution resolution, float partialTicks, boolean renderDummy) {
		if (renderDummy) {
			float fll = (System.currentTimeMillis() % 3000) / 3000f;
			this.renderBarContent(fll);
			this.renderFrame(DefaultInfusions.NONE.getTexture());
			this.renderText(2000, 2000);
		} else if (this.renderTime > 0) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.color(1, 1, 1, 1);
			IMagicPowerContainer energy = Minecraft.getMinecraft().player.getCapability(IMagicPowerContainer.CAPABILITY, null);
			double fill = this.getFillLevel(energy, partialTicks);
			this.renderBarContent(fill);
			this.renderPulse(fill);
			this.renderFrame(CovensAPI.getAPI().getPlayerInfusion(Minecraft.getMinecraft().player).getTexture());
			this.renderText(energy.getAmount(), energy.getMaxAmount());
			GlStateManager.popMatrix();
		}
	}

	private void renderBarContent(double filled) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, this.visibilityLeft);
		Minecraft.getMinecraft().getTextureManager().bindTexture(ResourceLocations.ENERGY_BACKGROUND_FILL);
		this.renderTexture(this.getX() + 9, this.getY() + 14 + (74 * (1 - filled)), 7, 74 * filled, 0, filled);
		GlStateManager.popMatrix();
	}

	private void renderPulse(double filled) {
		float alpha = this.barPulse * this.visibilityLeft;
		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, alpha);
		Minecraft.getMinecraft().getTextureManager().bindTexture(ResourceLocations.ENERGY_BACKGROUND_PULSE);
		this.renderTexture(this.getX() + 9, this.getY() + 14 + (74 * (1 - filled)), 7, 74 * filled, 0, filled);
		GlStateManager.popMatrix();
	}

	private void renderFrame(ResourceLocation texture) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, this.visibilityLeft);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.renderTexture(this.getX(), this.getY(), this.w, this.h, 0, 1);
		GlStateManager.popMatrix();
	}

	private void renderText(int amount, int maxAmount) {
		// NO-OP
	}

	private double getFillLevel(IMagicPowerContainer energy, float partialTicks) {
		double interpEnergy = 0;
		if (this.oldEnergy >= 0) {
			interpEnergy = ((double) (energy.getAmount() - this.oldEnergy) * partialTicks) + this.oldEnergy;
		} else {
			interpEnergy = energy.getAmount();
		}
		return interpEnergy / energy.getMaxAmount();
	}

	@Override
	public boolean isActive() {
		return !ModConfig.CLIENT.ENERGY_HUD.deactivate;
	}

	@Override
	public void setHidden(boolean hidden) {
		ModConfig.CLIENT.ENERGY_HUD.deactivate = hidden;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public double getX() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.ENERGY_HUD.h_anchor.dataToPixel(ModConfig.CLIENT.ENERGY_HUD.x, this.getWidth(), sr.getScaledWidth());
	}

	@Override
	public double getY() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.ENERGY_HUD.v_anchor.dataToPixel(ModConfig.CLIENT.ENERGY_HUD.y, this.getHeight(), sr.getScaledHeight());
	}

	@Override
	public void setRelativePosition(double x, double y, EnumHudAnchor horizontal, EnumHudAnchor vertical) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		ModConfig.CLIENT.ENERGY_HUD.v_anchor = vertical;
		ModConfig.CLIENT.ENERGY_HUD.h_anchor = horizontal;
		ModConfig.CLIENT.ENERGY_HUD.x = horizontal.pixelToData(x, this.getWidth(), sr.getScaledWidth());
		ModConfig.CLIENT.ENERGY_HUD.y = vertical.pixelToData(y, this.getHeight(), sr.getScaledHeight());
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public EnumHudAnchor getAnchorHorizontal() {
		return ModConfig.CLIENT.ENERGY_HUD.h_anchor;
	}

	@Override
	public EnumHudAnchor getAnchorVertical() {
		return ModConfig.CLIENT.ENERGY_HUD.v_anchor;
	}

}

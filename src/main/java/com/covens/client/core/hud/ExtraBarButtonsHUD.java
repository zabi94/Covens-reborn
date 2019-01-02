package com.covens.client.core.hud;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.covens.api.hotbar.IHotbarAction;
import com.covens.client.handler.Keybinds;
import com.covens.common.content.actionbar.HotbarAction;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.core.helper.Log;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.net.messages.PlayerUsedAbilityMessage;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExtraBarButtonsHUD extends HudComponent {

	public static final ResourceLocation TEXTURE = new ResourceLocation(LibMod.MOD_ID, "textures/gui/ability_selector.png");
	public static final ExtraBarButtonsHUD INSTANCE = new ExtraBarButtonsHUD();
	private static final IHotbarAction arrows = new IHotbarAction() {

		@Override
		public ResourceLocation getName() {
			return null;
		}

		@Override
		public int getIconIndexY() {
			return 3;
		}

		@Override
		public int getIconIndexX() {
			return 3;
		}

		@Override
		public ResourceLocation getIcon() {
			return HotbarAction.DEFAULT_ICON_TEXTURE;
		}
	};
	public IHotbarAction[] actionScroller = new IHotbarAction[3];// 0: current, 1: prev, 2: next
	private int slotSelected = -1;
	private int cooldown = 0;
	private int selectedItemTemp = 0;
	private List<IHotbarAction> actions = new ArrayList<IHotbarAction>();
	private boolean isInExtraBar = false;

	private ExtraBarButtonsHUD() {
		super(70, 16);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public boolean isInExtraBar() {
		return this.isInExtraBar;
	}

	@SubscribeEvent
	public void rightClickHijacker(MouseEvent evt) {
		if (evt.isButtonstate() && (this.slotSelected >= 0) && (evt.getButton() == 1)) {
			evt.setCanceled(true);
			if (this.cooldown == 0) {
				int e_id = -1;
				if (Minecraft.getMinecraft().pointedEntity != null) {
					e_id = Minecraft.getMinecraft().pointedEntity.getEntityId();
				}
				this.cooldown = 5;
				NetworkHandler.HANDLER.sendToServer(new PlayerUsedAbilityMessage(this.actions.get(this.slotSelected).getName().toString(), e_id));
			}
		}
	}

	@SubscribeEvent
	public void cleanupHUD(ClientDisconnectionFromServerEvent evt) {
		Log.i("Cleaning up Covens's HUD");
		this.actionScroller = new IHotbarAction[3];
		this.slotSelected = -1;
		this.cooldown = 0;
		this.selectedItemTemp = 0;
		this.actions = new ArrayList<IHotbarAction>();
		this.isInExtraBar = false;
	}

	@SubscribeEvent
	public void handRender(RenderHandEvent evt) {
		if (this.isInExtraBar && ModConfig.CLIENT.ACTION_BAR_HUD.hideHandWithAbility) {
			evt.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent evt) {
		if (this.cooldown > 0) {
			this.cooldown--;
		}
	}

	@SubscribeEvent
	public void scrollWheelHijacker(MouseEvent evt) {
		int dir = evt.getDwheel() == 0 ? 0 : evt.getDwheel() > 0 ? -1 : 1;
		if ((dir == 0) || (!Minecraft.getMinecraft().player.isSneaking() && !ModConfig.CLIENT.ACTION_BAR_HUD.autoJumpToBar && !this.isInExtraBar)) {
			return;
		}
		int curItm = Minecraft.getMinecraft().player.inventory.currentItem;
		int max = this.actions.size();
		if (Minecraft.getMinecraft().currentScreen == null) {// Don't mess with scroll wheels if a gui is open, only when playing
			IHotbarAction lastSelected = this.refreshSelected();
			evt.setCanceled(this.isInExtraBar || ((dir < 0) && (this.slotSelected == 0)) || ((dir > 0) && (curItm == 8) && (max > 0)));
			if (evt.isCanceled()) {
				this.handleDirectionChange(dir, max);
			}
			if (this.slotSelected < -1) {
				this.slotSelected = -1;
			}
			IHotbarAction current = this.refreshSelected();
			if ((current != null) && !current.equals(lastSelected)) {
				Minecraft.getMinecraft().player.sendStatusMessage(new TextComponentTranslation(current.getName().getNamespace() + "." + current.getName().getPath()), true);
			}
		}
	}

	private void handleDirectionChange(int dir, int max) {
		if (dir > 0) {
			if (max > 0) {
				this.isInExtraBar = true;
				this.slotSelected++;
				if (this.slotSelected >= max) {
					this.slotSelected = max - 1;
				}
			}
		} else if (dir < 0) {
			this.slotSelected--;
			if (this.slotSelected < 0) {
				this.isInExtraBar = false;
			}
		}
	}

	private IHotbarAction refreshSelected() {
		if (this.slotSelected >= 0) {
			this.actionScroller[0] = this.actions.get(this.slotSelected);
		} else {
			this.actionScroller = new HotbarAction[3];
			if (this.actions.size() > 0) {
				this.actionScroller[0] = this.actions.get(0);
				if (this.actions.size() > 1) {
					this.actionScroller[2] = this.actions.get(1);
				}
			}
			return null;
		}
		if (this.slotSelected > 0) {
			this.actionScroller[1] = this.actions.get(this.slotSelected - 1);
		} else {
			this.actionScroller[1] = null;
		}
		if ((this.slotSelected + 1) < this.actions.size()) {
			this.actionScroller[2] = this.actions.get(this.slotSelected + 1);
		} else {
			this.actionScroller[2] = null;
		}
		return this.actionScroller[0];
	}

	public void setList(List<IHotbarAction> list) {
		this.actions = list;
		this.slotSelected = Math.min(this.slotSelected, list.size() - 1);
		if (this.slotSelected < 0) {
			this.isInExtraBar = false;
		}
		this.refreshSelected();
	}

	@SubscribeEvent
	public void keybordInput(KeyInputEvent evt) {
		if (Keybinds.gotoExtraBar.isPressed()) {
			if (this.actions.size() > 0) {
				int lastSelected = this.slotSelected;
				this.slotSelected = 0;
				this.isInExtraBar = true;
				IHotbarAction current = this.refreshSelected();
				if ((current != null) && (lastSelected != this.slotSelected)) {
					Minecraft.getMinecraft().player.sendStatusMessage(new TextComponentTranslation(current.getName().getNamespace() + "." + current.getName().getPath()), true);
				}
			}
		}
		if (Keybinds.alwaysEnableBar.isPressed()) {
			ModConfig.CLIENT.ACTION_BAR_HUD.autoJumpToBar = !ModConfig.CLIENT.ACTION_BAR_HUD.autoJumpToBar;
			ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_1) || Keyboard.isKeyDown(Keyboard.KEY_2) || Keyboard.isKeyDown(Keyboard.KEY_3) || Keyboard.isKeyDown(Keyboard.KEY_4) || Keyboard.isKeyDown(Keyboard.KEY_5) || Keyboard.isKeyDown(Keyboard.KEY_6) || Keyboard.isKeyDown(Keyboard.KEY_7) || Keyboard.isKeyDown(Keyboard.KEY_8) || Keyboard.isKeyDown(Keyboard.KEY_9)) {
			this.slotSelected = -1;
			this.isInExtraBar = false;
			this.refreshSelected();
		}
	}

	@SubscribeEvent
	public void restoreVanillaItemSelector(RenderGameOverlayEvent.Post event) {
		if ((event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) && this.isInExtraBar) {
			Minecraft.getMinecraft().player.inventory.currentItem = this.selectedItemTemp;
		}
	}

	@SubscribeEvent
	public void removeVanillaItemSelector(RenderGameOverlayEvent.Pre event) {
		if ((event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) && this.isInExtraBar) {
			this.selectedItemTemp = Minecraft.getMinecraft().player.inventory.currentItem;
			Minecraft.getMinecraft().player.inventory.currentItem = 100;// Render overlay to the right (increase to something like 100 to make it
																		// disappear, if we decide to use a custom selection indicator)
		}
	}

	@Override
	public boolean isActive() {
		return !ModConfig.CLIENT.ACTION_BAR_HUD.deactivate;
	}

	@Override
	public void setHidden(boolean hidden) {
		ModConfig.CLIENT.ACTION_BAR_HUD.deactivate = hidden;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public double getX() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.ACTION_BAR_HUD.h_anchor.dataToPixel(ModConfig.CLIENT.ACTION_BAR_HUD.x, this.getWidth(), sr.getScaledWidth());
	}

	@Override
	public double getY() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		return ModConfig.CLIENT.ACTION_BAR_HUD.v_anchor.dataToPixel(ModConfig.CLIENT.ACTION_BAR_HUD.y, this.getHeight(), sr.getScaledHeight());
	}

	@Override
	public void setRelativePosition(double x, double y, EnumHudAnchor horizontal, EnumHudAnchor vertical) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		ModConfig.CLIENT.ACTION_BAR_HUD.v_anchor = vertical;
		ModConfig.CLIENT.ACTION_BAR_HUD.h_anchor = horizontal;
		ModConfig.CLIENT.ACTION_BAR_HUD.x = horizontal.pixelToData(x, this.getWidth(), sr.getScaledWidth());
		ModConfig.CLIENT.ACTION_BAR_HUD.y = vertical.pixelToData(y, this.getHeight(), sr.getScaledHeight());
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public void resetConfig() {
		ModConfig.CLIENT.ACTION_BAR_HUD.v_anchor = EnumHudAnchor.END_ABSOLUTE;
		ModConfig.CLIENT.ACTION_BAR_HUD.h_anchor = EnumHudAnchor.CENTER_ABSOLUTE;
		ModConfig.CLIENT.ACTION_BAR_HUD.x = 130;
		ModConfig.CLIENT.ACTION_BAR_HUD.y = 2;
		ModConfig.CLIENT.ACTION_BAR_HUD.deactivate = false;
		ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
	}

	@Override
	public EnumHudAnchor getAnchorHorizontal() {
		return ModConfig.CLIENT.ACTION_BAR_HUD.h_anchor;
	}

	@Override
	public EnumHudAnchor getAnchorVertical() {
		return ModConfig.CLIENT.ACTION_BAR_HUD.v_anchor;
	}

	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return null;
	}

	@Override
	public void onClick(int mouseX, int mouseY) {
		// NO-OP
	}

	@Override
	public void render(ScaledResolution sr, float partialTicks, boolean renderDummy) {
		if (renderDummy) {
			arrows.render(this.getX(), this.getY(), 16, 16, 1f);
			ModAbilities.DRAIN_BLOOD.render(this.getX() + 18, this.getY(), 16, 16, 0.4f);
			ModAbilities.BAT_SWARM.render(this.getX() + 36, this.getY(), 16, 16, 1f);
			ModAbilities.HOWL.render(this.getX() + 54, this.getY(), 16, 16, 0.4f);
			this.renderSelectionBox(this.getX() + 35, this.getY() - 1);
		} else {
			this.renderReal();
		}
	}

	private void renderReal() {
		if (this.actionScroller[0] != null) {
			this.actionScroller[0].render(this.getX() + 36, this.getY(), 16, 16, this.slotSelected < 0 ? 0.4f : 1f);
			if (this.isInExtraBar) {
				this.renderSelectionBox(this.getX() + 35, this.getY() - 1);
			}
		}
		if (this.actionScroller[2] != null) {
			this.actionScroller[2].render(this.getX() + 54, this.getY(), 16, 16, 0.4f);
		}
		if (this.actionScroller[1] != null) {
			this.actionScroller[1].render(this.getX() + 18, this.getY(), 16, 16, 0.4f);
		}
		if ((this.slotSelected < 0) && (this.actions.size() > 0) && ModConfig.CLIENT.ACTION_BAR_HUD.showArrowsInBar) {
			arrows.render(this.getX(), this.getY(), 16, 16, ((Minecraft.getMinecraft().player.isSneaking() || ModConfig.CLIENT.ACTION_BAR_HUD.autoJumpToBar) ? 1 : 0.2f));
		}
	}

	private void renderSelectionBox(double x, double y) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();

		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buff.pos(x, y + 18, 0).tex(0, 1).endVertex();
		buff.pos(x + 18, y + 18, 0).tex(1, 1).endVertex();
		buff.pos(x + 18, y, 0).tex(1, 0).endVertex();
		buff.pos(x, y, 0).tex(0, 0).endVertex();

		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}

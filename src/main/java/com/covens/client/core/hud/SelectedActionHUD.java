package com.covens.client.core.hud;

import com.covens.api.hotbar.IHotbarAction;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.lib.LibMod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.hud.IHudComponent;

@SideOnly(Side.CLIENT)
public class SelectedActionHUD implements IHudComponent {

	private static final ResourceLocation ID = new ResourceLocation(LibMod.MOD_ID, "selected_action");
	
	public SelectedActionHUD() {
//		super(32, 32, , "covens.hud.selected_action.description");
	}

	@Override
	public void drawAt(int x, int y, int w, int h, RenderMode m) {
		IHotbarAction sel = m != RenderMode.NORMAL ? ModAbilities.NIGHT_VISION_VAMPIRE : ExtraBarButtonsHUD.INSTANCE.actionScroller[0];
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		sel.render(x, y, w, h, 0.4f);
	}

	@Override
	public boolean isShown() {
		return ExtraBarButtonsHUD.INSTANCE.isInExtraBar() && ExtraBarButtonsHUD.INSTANCE.actionScroller[0] != null;
	}
	
	@Override
	public boolean canResize() {
		return true;
	}
	
	@Override
	public ResourceLocation getIdentifier() {
		return ID;
	}

	@Override
	public String getTitleTranslationKey() {
		return "covens.hud.selected_action.title";
	}

}

package com.covens.client.render.tile;

import java.util.Optional;

import org.lwjgl.opengl.GL11;

import com.covens.client.ResourceLocations;
import com.covens.common.tile.tiles.TileEntityCauldron;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;


public class TileRenderCauldron extends TileEntitySpecialRenderer<TileEntityCauldron> {

	@Override
	public void render(TileEntityCauldron te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		Optional<FluidStack> optional = te.getFluid();
		if (optional.isPresent() && (optional.get().amount > 0)) {
			FluidStack fluidStack = optional.get();
			Fluid fluid = fluidStack.getFluid();
			ResourceLocation location = fluid.getStill();
			double level = fluidStack.amount / (Fluid.BUCKET_VOLUME * 2D);

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y + 0.1 + level, z);
			if (fluid == FluidRegistry.WATER) {
				int color = te.getColorRGB();
				float r = ((color >>> 16) & 0xFF) / 256.0F;
				float g = ((color >>> 8) & 0xFF) / 256.0F;
				float b = (color & 0xFF) / 256.0F;
				GlStateManager.color(r, g, b, 0.8f);
				location = ResourceLocations.GRAY_WATER;
			}

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableAlpha();
			RenderHelper.disableStandardItemLighting();
			final float w = 0.125F;
			GlStateManager.translate(w, 0, w);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			final float s = 0.0460425F;
			GlStateManager.scale(s, s, s);
			this.renderWater(location);
			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}

		if ((te.getName() != null) && (Minecraft.getMinecraft().objectMouseOver != null) && te.getPos().equals(Minecraft.getMinecraft().objectMouseOver.getBlockPos())) {
			this.drawNameplate(te, te.getName(), x, y, z, 5);
		}
	}

	private void renderWater(ResourceLocation loc) {
		final TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(loc.toString());
		final Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tessellator.getBuffer().pos(0, 16, 0).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
		tessellator.getBuffer().pos(16, 16, 0).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
		tessellator.getBuffer().pos(16, 0, 0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
		tessellator.getBuffer().pos(0, 0, 0).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
		tessellator.draw();
	}
}

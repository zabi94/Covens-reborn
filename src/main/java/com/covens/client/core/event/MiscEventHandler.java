package com.covens.client.core.event;

import com.covens.client.handler.Keybinds;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.net.messages.PlaceHeldItemMessage;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MiscEventHandler {

	private Minecraft mc;

	public MiscEventHandler(Minecraft minecraft) {
		this.mc = minecraft;
	}

	@SubscribeEvent
	public void onKeyPress(KeyInputEvent evt) {
		if (Keybinds.placeItem.isPressed() && this.isPointingToUpperFace() && this.canReplaceAbove() && this.isTopSolid() && !Minecraft.getMinecraft().player.getHeldItemMainhand().isEmpty()) {
			NetworkHandler.HANDLER.sendToServer(new PlaceHeldItemMessage(this.mc.objectMouseOver.getBlockPos().up()));
		}
	}

	private boolean isPointingToUpperFace() {
		return (this.mc.objectMouseOver.typeOfHit == Type.BLOCK) && (this.mc.objectMouseOver.sideHit == EnumFacing.UP);
	}

	private boolean canReplaceAbove() {
		return this.mc.world.getBlockState(this.mc.objectMouseOver.getBlockPos().up()).getBlock().isReplaceable(this.mc.world, this.mc.objectMouseOver.getBlockPos().up());
	}

	private boolean isTopSolid() {
		return this.mc.world.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlockFaceShape(this.mc.player.world, this.mc.objectMouseOver.getBlockPos(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}
}

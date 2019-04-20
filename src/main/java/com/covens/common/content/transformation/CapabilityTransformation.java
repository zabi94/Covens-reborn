package com.covens.common.content.transformation;

import com.covens.api.transformation.DefaultTransformations;
import com.covens.api.transformation.ITransformation;
import com.covens.common.content.actionbar.HotbarAction;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.capability.SimpleCapability;

public class CapabilityTransformation extends SimpleCapability {

	@CapabilityInject(CapabilityTransformation.class)
	public static final Capability<CapabilityTransformation> CAPABILITY = null;

	public int level = 0;
	public String type;

	public CapabilityTransformation() {
		if (DefaultTransformations.NONE != null) {
			this.type = DefaultTransformations.NONE.getRegistryName().toString();
		}
	}

	public ITransformation getType() {
		return ModTransformations.REGISTRY.getValue(new ResourceLocation(this.type));
	}

	public void setType(ITransformation type) {
		this.type = type.getRegistryName().toString();
		this.markDirty((byte) 1);
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
		this.markDirty((byte) 2);
	}

	@Override
	public boolean isRelevantFor(Entity object) {
		return object instanceof EntityPlayer;
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new CapabilityTransformation();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onSyncMessage(byte mode) {
		HotbarAction.refreshActions(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world);
	}

}

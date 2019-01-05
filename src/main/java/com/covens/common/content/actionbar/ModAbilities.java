package com.covens.common.content.actionbar;

import com.covens.api.CovensAPI;
import com.covens.common.content.transformation.vampire.CapabilityVampire;
import com.covens.common.content.transformation.werewolf.CapabilityWerewolfStatus;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModAbilities {

	public static final HotbarAction BAT_SWARM = new HotbarAction(new ResourceLocation(LibMod.MOD_ID, "bat_swarm"), 2, 0);
	public static final HotbarAction DRAIN_BLOOD = new HotbarAction(new ResourceLocation(LibMod.MOD_ID, "drain_blood"), 1, 0);
	public static final HotbarAction HOWL = new HotbarAction(new ResourceLocation(LibMod.MOD_ID, "howl"), 1, 1);
	public static final HotbarAction MESMERIZE = new HotbarAction(new ResourceLocation(LibMod.MOD_ID, "mesmerize"), 2, 1);
	public static final HotbarAction HYPNOTIZE = new HotbarAction(new ResourceLocation(LibMod.MOD_ID, "hypnotize"), 2, 2);
	public static final HotbarAction COMMAND_FAMILIAR = new HotbarAction(new ResourceLocation(LibMod.MOD_ID, "command_familiar"), 0, 2) {
		@Override
		@SideOnly(Side.CLIENT)
		public int getIconIndexY() {
			int yIndex = super.getIconIndexY();
			Entity e = Minecraft.getMinecraft().pointedEntity;
			if (e != null && CovensAPI.getAPI().isValidFamiliar(e) && e.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).owner.equals(Minecraft.getMinecraft().player.getUniqueID())) {
				return yIndex;
			}
			return yIndex + 1;
		}
	};
	public static final HotbarAction WOLF_SHIFT = new HotbarAction(new ResourceLocation(LibMod.MOD_ID, "wolf_shift"), 3, 0) {
		@Override
		@SideOnly(Side.CLIENT)
		public int getIconIndexY() {
			int yIndex = super.getIconIndexY();
			if (Minecraft.getMinecraft().player.isSneaking()) {
				return yIndex;
			}
			return yIndex + 1;
		}
	};
	public static final HotbarAction NIGHT_VISION_VAMPIRE = new NightVisionAction("vampire") {

		@Override
		protected boolean hasNightVision(EntityPlayer player) {
			return player.getCapability(CapabilityVampire.CAPABILITY, null).nightVision;
		}
	};

	public static final HotbarAction NIGHT_VISION_WEREWOLF = new NightVisionAction("werewolf") {

		@Override
		protected boolean hasNightVision(EntityPlayer player) {
			return player.getCapability(CapabilityWerewolfStatus.CAPABILITY, null).nightVision;
		}
	};

	private ModAbilities() {
		// NO-OP
	}

	public static void dummyMethodToLoadClass() {
		// I only use this method to force the JVM to load this class
	}

	public static abstract class NightVisionAction extends HotbarAction {

		public NightVisionAction(String variant) {
			super(new ResourceLocation(LibMod.MOD_ID, "night_vision_" + variant), 0, 0);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public int getIconIndexY() {
			int yIndex = super.getIconIndexY();
			if (this.hasNightVision(Minecraft.getMinecraft().player)) {
				return yIndex;
			}
			return yIndex + 1;
		}

		protected abstract boolean hasNightVision(EntityPlayer player);
	}
}

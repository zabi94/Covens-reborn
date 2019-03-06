package com.covens.common.core.capability.familiar;

import java.util.HashMap;
import java.util.UUID;

import com.covens.api.mp.IMagicPowerExpander;
import com.covens.common.content.actionbar.HotbarAction;
import com.covens.common.content.familiar.FamiliarDescriptor;
import com.covens.common.lib.LibMod;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.capability.SimpleCapability;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.utils.annotation.DontSync;

public class CapabilityFamiliarOwner extends SimpleCapability implements IMagicPowerExpander {

	@CapabilityInject(CapabilityFamiliarOwner.class)
	public static final Capability<CapabilityFamiliarOwner> CAPABILITY = null;
	public static final CapabilityFamiliarOwner DEFAULT_INSTANCE = new CapabilityFamiliarOwner();
	private static final ResourceLocation EXPANDER_ID = new ResourceLocation(LibMod.MOD_ID, "familiars");
	
	public int familiarCount = 0;
	
	@DontSync 
	@CustomSerializer(reader = SerializerHashmap.class, writer = SerializerHashmap.class) 
	public HashMap<UUID, FamiliarDescriptor> familiars = new HashMap<>();
	
	public UUID selectedFamiliar = UUIDs.NULL_UUID;
	public String selectedFamiliarName = "";

	public void addFamiliar(EntityLiving familiar) {
		if (!familiars.containsKey(UUIDs.of(familiar))) {
			familiars.put(UUIDs.of(familiar), FamiliarDescriptor.of(familiar));
			familiarCount++;
			markDirty((byte) 1); 
		}
	}

	public void removeFamiliar(UUID familiar, String name) {
		if (familiars.containsKey(familiar)) {
			familiars.remove(familiar);
			familiarCount--;
			if (selectedFamiliar.equals(familiar)) {
				deselectFamiliar();
			}
			markDirty((byte) 1); 
		}
	}

	@Override
	public boolean isRelevantFor(Entity object) {
		return object instanceof EntityPlayer;
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new CapabilityFamiliarOwner();
	}

	@Override
	public ResourceLocation getID() {
		return EXPANDER_ID;
	}

	@Override
	public int getExtraAmount(EntityPlayer p) {
		return this.familiarCount * 100;
	}
	
	public void selectFamiliar(UUID id, String name) {
		selectedFamiliar = id;
		if (UUIDs.isNull(id)) {
			selectedFamiliarName = "";
		} else {
			selectedFamiliarName = name;
		}
		markDirty((byte) 4); 
	}
	
	public void deselectFamiliar() {
		selectedFamiliar = UUIDs.NULL_UUID;
		selectedFamiliarName = "";
		markDirty((byte) 4); 
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onSyncMessage(byte mode) {
		HotbarAction.refreshActions(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world);
	}
	
	public static class SerializerHashmap implements SimpleCapability.Reader<HashMap<UUID, FamiliarDescriptor>>, SimpleCapability.Writer<HashMap<UUID, FamiliarDescriptor>> {

		@Override
		public void write(HashMap<UUID, FamiliarDescriptor> map, NBTTagCompound tag, String field) {
			NBTTagList tlist = new NBTTagList();
			map.values().forEach(tup -> {
				tlist.appendTag(tup.serializeNBT());
			});
			tag.setTag("list", tlist);
		}

		@Override
		public HashMap<UUID, FamiliarDescriptor> read(NBTTagCompound buf, String name) {
			HashMap<UUID, FamiliarDescriptor> map = Maps.newHashMap();
			buf.getTagList("list", NBT.TAG_COMPOUND).forEach(nbt -> {
				FamiliarDescriptor desc = new FamiliarDescriptor((NBTTagCompound) nbt);
				map.put(desc.getUuid(), desc);
			});
			return map;
		}
		
	}

}

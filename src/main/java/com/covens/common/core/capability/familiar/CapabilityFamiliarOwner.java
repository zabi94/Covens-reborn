package com.covens.common.core.capability.familiar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.covens.api.mp.IMagicPowerExpander;
import com.covens.common.content.actionbar.HotbarAction;
import com.covens.common.content.familiar.FamiliarDescriptor;
import com.covens.common.lib.LibMod;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
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
	@CustomSerializer(reader = SerializerArrayUUID.class, writer = SerializerArrayUUID.class) 
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
				selectFamiliar(null);
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
	
	public void selectFamiliar(Entity e) {
		selectedFamiliar = UUIDs.of(e);
		selectedFamiliarName = e==null?"":e.getName();
		markDirty((byte) 4); 
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onSyncMessage(byte mode) {
		if (mode == 2) {
			HotbarAction.refreshActions(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world);
		}
	}
	
	public static class SerializerArrayUUID implements SimpleCapability.Reader<ArrayList<Tuple<UUID, String>>>, SimpleCapability.Writer<ArrayList<Tuple<UUID, String>>> {

		@Override
		public void write(ArrayList<Tuple<UUID, String>> list, NBTTagCompound tag, String field) {
			NBTTagList tlist = new NBTTagList();
			list.forEach(tup -> {
				NBTTagCompound entry = new NBTTagCompound();
				entry.setUniqueId("id", tup.getFirst());
				entry.setString("name", tup.getSecond());
				tlist.appendTag(entry);
			});
			tag.setTag("list", tlist);
		}

		@Override
		public ArrayList<Tuple<UUID, String>> read(NBTTagCompound buf, String name) {
			ArrayList<Tuple<UUID, String>> list = Lists.newArrayList();
			buf.getTagList("list", NBT.TAG_COMPOUND).forEach(nbt -> list.add(new Tuple<UUID, String>(((NBTTagCompound)nbt).getUniqueId("id"), ((NBTTagCompound)nbt).getString("name"))));
			return list;
		}
		
	}

}

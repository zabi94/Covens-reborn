package com.covens.common.core.capability.familiar;

import java.util.ArrayList;
import java.util.UUID;

import com.covens.api.mp.IMagicPowerExpander;
import com.covens.common.core.capability.simple.SimpleCapability;
import com.covens.common.core.util.UUIDs;
import com.covens.common.lib.LibMod;
import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants.NBT;

public class CapabilityFamiliarOwner extends SimpleCapability implements IMagicPowerExpander {

	@CapabilityInject(CapabilityFamiliarOwner.class)
	public static final Capability<CapabilityFamiliarOwner> CAPABILITY = null;
	public static final CapabilityFamiliarOwner DEFAULT_INSTANCE = new CapabilityFamiliarOwner();
	private static final ResourceLocation EXPANDER_ID = new ResourceLocation(LibMod.MOD_ID, "familiars");
	
	public int familiarCount = 0;
	
	@DontSync 
	@CustomSerializer(reader = SerializerArrayUUID.class, writer = SerializerArrayUUID.class) 
	public ArrayList<UUID> familiars = new ArrayList<UUID>();
	
	public UUID selectedFamiliar = UUIDs.NULL_UUID;

	public void addFamiliar(UUID familiar) {
		if (!familiars.contains(familiar)) {
			familiars.add(familiar);
			familiarCount++;
			markDirty((byte) 1); 
		}
	}

	public void removeFamiliar(UUID familiar) {
		if (familiars.contains(familiar)) {
			familiars.remove(familiar);
			familiarCount--;
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
	}
	
	public static class SerializerArrayUUID implements SimpleCapability.Reader<ArrayList<UUID>>, SimpleCapability.Writer<ArrayList<UUID>> {

		@Override
		public void write(ArrayList<UUID> list, NBTTagCompound buf, String field) {
			NBTTagList tlist = new NBTTagList();
			list.forEach(uuid -> tlist.appendTag(UUIDs.toNBT(uuid)));
			buf.setTag("list", tlist);
		}

		@Override
		public ArrayList<UUID> read(NBTTagCompound buf, String name) {
			ArrayList<UUID> list = Lists.newArrayList();
			buf.getTagList("list", NBT.TAG_COMPOUND).forEach(nbt -> list.add(UUIDs.fromNBT((NBTTagCompound) nbt)));
			return list;
		}
		
	}

}

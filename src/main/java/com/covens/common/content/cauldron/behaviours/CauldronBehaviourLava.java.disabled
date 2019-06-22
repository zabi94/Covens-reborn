package com.covens.common.content.cauldron.behaviours;

import com.covens.common.tile.tiles.TileEntityCauldron;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CauldronBehaviourLava implements ICauldronBehaviour {

	private static final String ID = "lava";

	private TileEntityCauldron cauldron;

	@Override
	public void setCauldron(TileEntityCauldron tile) {
		this.cauldron = tile;
	}

	@Override
	public void handleParticles(boolean active) {
		// NO-OP
	}

	@Override
	public boolean canAccept(ItemStack stack) {
		return true;
	}

	@Override
	public void update(boolean active) {
		// NO-OP
	}

	@Override
	public int getColor() {
		return 0xff8d00;
	}

	@Override
	public void saveToNBT(NBTTagCompound tag) {
		// NO-OP
	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {
		// NO-OP
	}

	@Override
	public void saveToSyncNBT(NBTTagCompound tag) {
		// NO-OP
	}

	@Override
	public void loadFromSyncNBT(NBTTagCompound tag) {
		// NO-OP
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void statusChanged(boolean active) {
		if (active && (this.cauldron.getInputs().size() > 0)) {
			ItemStack stack = this.cauldron.getInputs().get(this.cauldron.getInputs().size() - 1);
			if ((stack.getItem() == Items.GUNPOWDER) || (stack.getItem() == Items.FIRE_CHARGE)) {
				this.cauldron.getWorld().createExplosion(null, this.cauldron.getPos().getX() + 0.5, this.cauldron.getPos().getY() + 0.5, this.cauldron.getPos().getZ() + 0.5, 1, true);
			} else if (stack.getItem() == Item.getItemFromBlock(Blocks.TNT)) {
				this.cauldron.getWorld().createExplosion(null, this.cauldron.getPos().getX() + 0.5, this.cauldron.getPos().getY() + 0.5, this.cauldron.getPos().getZ() + 0.5, 3, true);
			} else if ((stack.getItem() == Items.FIREWORKS) || (stack.getItem() == Items.FIREWORK_CHARGE)) {
				boolean isCharge = false;
				if (stack.getItem() == Items.FIREWORK_CHARGE) {
					isCharge = true;
					NBTTagCompound fireworks = new NBTTagCompound();
					fireworks.setByte("Flight", (byte) 3);
					NBTTagList explosionList = new NBTTagList();
					if (stack.getTagCompound() != null) {
						explosionList.appendTag(stack.getTagCompound().getCompoundTag("Explosion"));
					}
					fireworks.setTag("Explosions", explosionList);
					stack = new ItemStack(Items.FIREWORKS);
					NBTTagCompound fireworksBaseTag = new NBTTagCompound();
					fireworksBaseTag.setTag("Fireworks", fireworks);
					stack.setTagCompound(fireworksBaseTag);

				}
				EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(this.cauldron.getWorld(), this.cauldron.getPos().getX() + 0.5, this.cauldron.getPos().getY() + 0.5, this.cauldron.getPos().getZ() + 0.5, stack);
				if (isCharge) {
					NBTTagCompound hackTag = new NBTTagCompound();
					entityfireworkrocket.writeEntityToNBT(hackTag);
					hackTag.setInteger("LifeTime", 0);
					entityfireworkrocket.readEntityFromNBT(hackTag);
				}
				this.cauldron.getWorld().spawnEntity(entityfireworkrocket);
			}
		}
	}

	@Override
	public void onDeactivation() {
		// NO-OP
	}
}

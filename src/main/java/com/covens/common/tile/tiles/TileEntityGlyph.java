package com.covens.common.tile.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.covens.api.mp.IMagicPowerConsumer;
import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.state.StateProperties;
import com.covens.common.block.ModBlocks;
import com.covens.common.content.ritual.AdapterIRitual;
import com.covens.common.core.helper.BlockStreamHelper;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.net.messages.SmokeSpawn;
import com.covens.common.core.util.DimensionalPosition;
import com.covens.common.item.ModItems;
import com.covens.common.item.magic.ItemLocationStone;
import com.covens.common.tile.ModTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityGlyph extends ModTileEntity implements ITickable {

	public static final ArrayList<int[]> small = new ArrayList<int[]>();
	public static final ArrayList<int[]> medium = new ArrayList<int[]>();
	public static final ArrayList<int[]> big = new ArrayList<int[]>();
	private static final double DISTANCE_SQUARED_BEFORE_COST_INCREASES = 400d;

	static {
		for (int i = -1; i <= 1; i++) {
			small.add(a(i, 3));
			small.add(a(3, i));
			small.add(a(i, -3));
			small.add(a(-3, i));
		}
		small.add(a(2, 2));
		small.add(a(2, -2));
		small.add(a(-2, 2));
		small.add(a(-2, -2));
		for (int i = -2; i <= 2; i++) {
			medium.add(a(i, 5));
			medium.add(a(5, i));
			medium.add(a(i, -5));
			medium.add(a(-5, i));
		}
		medium.add(a(3, 4));
		medium.add(a(-3, 4));
		medium.add(a(3, -4));
		medium.add(a(-3, -4));
		medium.add(a(4, 3));
		medium.add(a(-4, 3));
		medium.add(a(4, -3));
		medium.add(a(-4, -3));
		for (int i = -3; i <= 3; i++) {
			big.add(a(i, 7));
			big.add(a(7, i));
			big.add(a(i, -7));
			big.add(a(-7, i));
		}
		big.add(a(4, 6));
		big.add(a(6, 4));
		big.add(a(5, 5));
		big.add(a(-4, 6));
		big.add(a(-6, 4));
		big.add(a(-5, 5));
		big.add(a(4, -6));
		big.add(a(6, -4));
		big.add(a(5, -5));
		big.add(a(-4, -6));
		big.add(a(-6, -4));
		big.add(a(-5, -5));
	}

	private AdapterIRitual ritual = null; // If a ritual is active it is stored here
	private int cooldown = 0; // The times that passed since activation
	private BlockPos runningPos = null; // The effective position where to run the ritual, or null if on the spot
	private UUID entityPlayer; // The player that casted it
	private NBTTagCompound ritualData = null; // Extra data for the ritual, includes a list of items used
	private IMagicPowerConsumer altarTracker = IMagicPowerConsumer.CAPABILITY.getDefaultInstance();

	// A list of entities for which some rituals behaves differently, depending on
	// the ritual
	// For instance in Covens there was a ritual that hijacked all tp attempt in an
	// area and
	// redirected them somewhere else. This was used as a blacklist, in order to
	// allow the owner
	// to safely teleport back to their base. The use is left to who codes the
	// specific ritual.
	// The adding of entities is done via a specific ritual (Used to be called
	// Identification rit)
	private ArrayList<Tuple<String, String>> entityList = new ArrayList<Tuple<String, String>>();

	public static ArrayList<int[]> getSmall() {
		return small;
	}

	public static ArrayList<int[]> getMedium() {
		return medium;
	}

	public static ArrayList<int[]> getBig() {
		return big;
	}

	private static int[] a(int... ar) {
		return ar;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand.equals(EnumHand.OFF_HAND)) {
			return false;
		}

		ItemStack held = playerIn.getHeldItem(hand);

		if (held.isEmpty()) {
			if (this.hasRunningRitual()) {
				this.stopRitual(playerIn);
				return true;
			}
			this.startRitual(playerIn, null);
			return true;
		} else if ((held.getItem() == ModItems.location_stone) && !this.hasRunningRitual()) {
			Optional<DimensionalPosition> odp = ItemLocationStone.getLocationAndDamageStack(held, playerIn);
			if (odp.isPresent() && (odp.get().getDim() == worldIn.provider.getDimension())) {
				this.startRitual(playerIn, odp.get().getPosition());
				return true;
			}
			playerIn.sendStatusMessage(new TextComponentTranslation("ritual.failure.wrong_redirection"), true);
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		if (!this.world.isRemote && (this.ritual != null)) {
			EntityPlayer player = this.getWorld().getPlayerEntityByUUID(this.entityPlayer);
			double powerDrainMult = 1;
			BlockPos effPos = this.getPos();
			if (this.runningPos != null) {
				powerDrainMult = MathHelper.ceil(this.runningPos.distanceSq(this.getPos()) / DISTANCE_SQUARED_BEFORE_COST_INCREASES);
				effPos = this.runningPos;
			}

			boolean hasPowerToUpdate = this.altarTracker.drainAltarFirst(player, this.pos, this.world.provider.getDimension(), (int) (this.ritual.getRunningPower() * powerDrainMult));
			if (hasPowerToUpdate) {
				this.cooldown++;
				this.markDirty();
			}
			if ((this.ritual.getTime() <= this.cooldown) && (this.ritual.getTime() >= 0)) {
				this.ritual.onFinish(player, this, this.getWorld(), this.getPos(), this.ritualData, effPos, 1);
				for (ItemStack stack : this.ritual.getOutput(AdapterIRitual.getItemsUsedForInput(this.ritualData), this.ritualData)) {
					EntityItem ei = new EntityItem(this.getWorld(), effPos.getX(), effPos.up().getY(), effPos.getZ(), stack);
					this.getWorld().spawnEntity(ei);
				}
				this.entityPlayer = null;
				this.cooldown = 0;
				this.ritual = null;
				this.runningPos = null;
				this.world.notifyBlockUpdate(this.getPos(), this.world.getBlockState(this.getPos()), this.world.getBlockState(this.getPos()), 3);
				this.markDirty();
				return;
			}
			if (hasPowerToUpdate) {
				this.ritual.onUpdate(player, this, this.getWorld(), this.getPos(), this.ritualData, this.cooldown, effPos, 1);
			} else {
				if (this.ritual.onLowPower(player, this, this.world, this.pos, this.ritualData, this.cooldown, effPos, 1)) {
					this.stopRitual(player);
				}
			}
		}
	}

	public void startRitual(EntityPlayer player, BlockPos startAt) {
		if (player.getEntityWorld().isRemote) {
			return;
		}

		double powerDrainMult = 1;
		BlockPos effPos = this.getPos();
		if (startAt != null) {
			powerDrainMult = MathHelper.ceil(startAt.distanceSq(this.getPos()) / DISTANCE_SQUARED_BEFORE_COST_INCREASES);
			effPos = startAt;
		}

		List<EntityItem> itemsOnGround = this.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.getPos()).grow(3, 0, 3));
		List<BlockPos> placedOnGround = BlockStreamHelper.ofPos(this.getPos().add(3, 0, 3), this.getPos().add(-3, 0, -3)).filter(t -> (this.world.getTileEntity(t) instanceof TileEntityPlacedItem)).collect(Collectors.toList());
		ArrayList<ItemStack> recipe = new ArrayList<>();
		itemsOnGround.stream().map(i -> i.getItem()).forEach(is -> recipe.add(is));
		placedOnGround.stream().map(t -> (TileEntityPlacedItem) this.world.getTileEntity(t)).forEach(te -> recipe.add(te.getItem()));

		for (AdapterIRitual rit : AdapterIRitual.REGISTRY) { // Check every ritual
			if (rit.isValidInput(recipe, this.hasCircles(rit))) { // Check if circles and items match
				if (rit.isValid(player, this.world, this.pos, recipe, effPos, 1)) { // Checks of extra conditions are met

					if (this.altarTracker.drainAltarFirst(player, this.pos, this.world.provider.getDimension(), (int) (rit.getRequiredStartingPower() * powerDrainMult))) { // Check if there is enough starting power (and uses it in case there is)

						this.ritualData = new NBTTagCompound();
						NBTTagList itemsUsed = new NBTTagList();
						itemsOnGround.forEach(ei -> {
							NBTTagCompound item = new NBTTagCompound();
							ei.getItem().writeToNBT(item);
							itemsUsed.appendTag(item);
							NetworkHandler.HANDLER.sendToDimension(new SmokeSpawn(ei.posX, ei.posY, ei.posZ), this.world.provider.getDimension());
							ei.setDead();
						});
						placedOnGround.forEach(bp -> {
							TileEntityPlacedItem te = (TileEntityPlacedItem) this.world.getTileEntity(bp);
							NBTTagCompound item = new NBTTagCompound();
							te.pop().writeToNBT(item);
							itemsUsed.appendTag(item);
							NetworkHandler.HANDLER.sendToDimension(new SmokeSpawn(bp.getX() + 0.5d, bp.getY() + 0.1, bp.getZ() + 0.5d), this.world.provider.getDimension());
						});
						this.ritualData.setTag("itemsUsed", itemsUsed);

						// Sets the ritual up
						this.runningPos = startAt;
						this.ritual = rit;
						this.entityPlayer = player.getPersistentID();
						this.cooldown = 1;
						this.ritual.onStarted(player, this, this.getWorld(), this.getPos(), this.ritualData, effPos, 1);
						// TODO get a better sound
						this.world.playSound(null, this.pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.7f, 0.7f);
						player.sendStatusMessage(new TextComponentTranslation("ritual." + rit.getRegistryName().toString().replace(':', '.') + ".name"), true);
						this.world.notifyBlockUpdate(this.getPos(), this.world.getBlockState(this.getPos()), this.world.getBlockState(this.getPos()), 3);
						this.markDirty();
						return;
					}
					player.sendStatusMessage(new TextComponentTranslation("ritual.failure.power"), true);
					return;
				}
				player.sendStatusMessage(new TextComponentTranslation("ritual.failure.precondition"), true);
				return;

			}
		}
		if (!(itemsOnGround.isEmpty() && placedOnGround.isEmpty())) {
			player.sendStatusMessage(new TextComponentTranslation("ritual.failure.unknown"), true);
		}
	}

	private boolean hasCircles(AdapterIRitual rit) {
		int requiredCircles = rit.getCircles() & 3;
		if (requiredCircles == 3) {
			return false;
		}
		EnumGlyphType typeFirst = EnumGlyphType.fromMeta((rit.getCircles() >> 2) & 3);
		EnumGlyphType typeSecond = EnumGlyphType.fromMeta((rit.getCircles() >> 4) & 3);
		EnumGlyphType typeThird = EnumGlyphType.fromMeta((rit.getCircles() >> 6) & 3);
		if ((requiredCircles > 1) && !this.checkThird(typeThird)) {
			return false;
		}
		if ((requiredCircles > 0) && !this.checkSecond(typeSecond)) {
			return false;
		}
		if (!this.checkFirst(typeFirst)) {
			return false;
		}
		return true;
	}

	public boolean isInList(Entity entity) {
		return this.entityList.stream().map(t -> t.getFirst()).anyMatch(s -> s.equals(entity.getUniqueID().toString()));
	}

	public List<Tuple<String, String>> getWhitelistEntries() {
		return this.entityList;
	}

	public void addEntityToList(Entity entity) {
		this.entityList.add(new Tuple<String, String>(entity.getUniqueID().toString(), entity.getName()));
		this.markDirty();
	}

	public void addEntityUUIDToList(String uuid, String name) {
		this.entityList.add(new Tuple<String, String>(uuid, name));
		this.markDirty();
	}

	private boolean checkFirst(EnumGlyphType typeFirst) {
		EnumGlyphType lastFound = null;
		for (int[] c : small) {
			BlockPos bp = this.pos.add(c[0], 0, c[1]);
			IBlockState bs = this.world.getBlockState(bp);
			if (!bs.getBlock().equals(ModBlocks.ritual_glyphs) || bs.getValue(StateProperties.GLYPH_TYPE).equals(EnumGlyphType.GOLDEN) || (!bs.getValue(StateProperties.GLYPH_TYPE).equals(typeFirst) && !typeFirst.equals(EnumGlyphType.ANY))) {
				return false;
			}
			EnumGlyphType thisOne = bs.getValue(StateProperties.GLYPH_TYPE);
			if ((lastFound != null) && (lastFound != thisOne)) {
				return false;
			}
			lastFound = thisOne;
		}
		return true;
	}

	private boolean checkSecond(EnumGlyphType typeSecond) {
		EnumGlyphType lastFound = null;
		for (int[] c : medium) {
			BlockPos bp = this.pos.add(c[0], 0, c[1]);
			IBlockState bs = this.world.getBlockState(bp);
			if (!bs.getBlock().equals(ModBlocks.ritual_glyphs) || bs.getValue(StateProperties.GLYPH_TYPE).equals(EnumGlyphType.GOLDEN) || (!bs.getValue(StateProperties.GLYPH_TYPE).equals(typeSecond) && !typeSecond.equals(EnumGlyphType.ANY))) {
				return false;
			}
			EnumGlyphType thisOne = bs.getValue(StateProperties.GLYPH_TYPE);
			if ((lastFound != null) && (lastFound != thisOne)) {
				return false;
			}
			lastFound = thisOne;
		}
		return true;
	}

	private boolean checkThird(EnumGlyphType typeThird) {
		EnumGlyphType lastFound = null;
		for (int[] c : big) {
			BlockPos bp = this.pos.add(c[0], 0, c[1]);
			IBlockState bs = this.world.getBlockState(bp);
			if (!bs.getBlock().equals(ModBlocks.ritual_glyphs) || bs.getValue(StateProperties.GLYPH_TYPE).equals(EnumGlyphType.GOLDEN) || (!bs.getValue(StateProperties.GLYPH_TYPE).equals(typeThird) && !typeThird.equals(EnumGlyphType.ANY))) {
				return false;
			}
			EnumGlyphType thisOne = bs.getValue(StateProperties.GLYPH_TYPE);
			if ((lastFound != null) && (lastFound != thisOne)) {
				return false;
			}
			lastFound = thisOne;
		}
		return true;
	}

	public void stopRitual(EntityPlayer player) {
		if (this.ritual != null) {
			this.ritual.onStopped(player, this, this.world, this.pos, this.ritualData, this.runningPos == null ? this.getPos() : this.runningPos, 1);
			this.entityPlayer = null;
			this.cooldown = 0;
			this.ritual = null;
			this.ritualData = null;
			this.runningPos = null;
			IBlockState glyph = this.world.getBlockState(this.pos);
			this.world.notifyBlockUpdate(this.pos, glyph, glyph, 3);
			this.markDirty();
		}
	}

	public boolean hasRunningRitual() {
		return this.cooldown > 0;
	}

	@Override
	public void invalidate() {
		this.stopRitual(null);
		super.invalidate();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == IMagicPowerConsumer.CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == IMagicPowerConsumer.CAPABILITY) {
			return IMagicPowerConsumer.CAPABILITY.cast(this.altarTracker);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	protected void readAllModDataNBT(NBTTagCompound tag) {
		this.cooldown = tag.getInteger("cooldown");
		if (tag.hasKey("ritual")) {
			this.ritual = AdapterIRitual.REGISTRY.getValue(new ResourceLocation(tag.getString("ritual")));
		}
		if (tag.hasKey("player")) {
			this.entityPlayer = UUID.fromString(tag.getString("player"));
		}
		if (tag.hasKey("runningPos")) {
			NBTTagCompound rp = tag.getCompoundTag("runningPos");
			this.runningPos = new BlockPos(rp.getInteger("x"), rp.getInteger("y"), rp.getInteger("z"));
		}
		if (tag.hasKey("data")) {
			this.ritualData = tag.getCompoundTag("data");
		}
		this.altarTracker.readFromNbt(tag.getCompoundTag("altar"));
		if (tag.hasKey("entityList")) {
			this.entityList = new ArrayList<Tuple<String, String>>();
			tag.getTagList("entityList", NBT.TAG_STRING).forEach(nbts -> {
				String[] names = ((NBTTagString) nbts).getString().split("!");
				if (names.length == 2) {
					this.entityList.add(new Tuple<String, String>(names[0], names[1]));
				}
			});
		}
	}

	@Override
	protected void writeAllModDataNBT(NBTTagCompound tag) {
		tag.setInteger("cooldown", this.cooldown);
		if (this.ritual != null) {
			tag.setString("ritual", this.ritual.getRegistryName().toString());
		}
		if (this.entityPlayer != null) {
			tag.setString("player", this.entityPlayer.toString());
		}
		if (this.ritualData != null) {
			tag.setTag("data", this.ritualData);
		}
		if (this.runningPos != null) {
			NBTTagCompound rp = new NBTTagCompound();
			rp.setInteger("x", this.runningPos.getX());
			rp.setInteger("y", this.runningPos.getY());
			rp.setInteger("z", this.runningPos.getZ());
			tag.setTag("runningPos", rp);
		}
		tag.setTag("altar", this.altarTracker.writeToNbt());
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.entityList.size(); i++) {
			Tuple<String, String> t = this.entityList.get(i);
			list.appendTag(new NBTTagString(t.getFirst() + "!" + t.getSecond()));
		}
		tag.setTag("entityList", list);
	}

	@Override
	protected void writeModSyncDataNBT(NBTTagCompound tag) {
		tag.setInteger("cooldown", this.cooldown); // cooldown > 0 --> Particles
	}

	@Override
	protected void readModSyncDataNBT(NBTTagCompound tag) {
		this.cooldown = tag.getInteger("cooldown");
	}
}

package com.covens.common.tile.tiles;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.covens.api.cauldron.ICauldronCraftingRecipe;
import com.covens.api.cauldron.ICauldronRecipe;
import com.covens.api.mp.MPUsingMachine;
import com.covens.api.state.StateProperties;
import com.covens.common.content.cauldron.BrewBuilder;
import com.covens.common.content.cauldron.BrewData;
import com.covens.common.content.cauldron.CauldronRegistry;
import com.covens.common.content.cauldron.teleportCapability.CapabilityCauldronTeleport;
import com.covens.common.core.helper.Log;
import com.covens.common.item.ModItems;
import com.covens.common.tile.util.CauldronFluidTank;
import com.covens.common.tile.util.CauldronMode;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zabi.minecraft.minerva.common.tileentity.ModTileEntity;
import zabi.minecraft.minerva.common.utils.ColorHelper;

public class TileEntityCauldron extends ModTileEntity implements ITickable {

	public static final int DEFAULT_COLOR = 0x42499b;
	public static final String TAG_NO_PICKUP = "cauldron-drop";

	private NonNullList<ItemStack> ingredients = NonNullList.create();
	private AxisAlignedBB collectionZone;
	private CauldronFluidTank tank;
	private CauldronMode mode = CauldronMode.IDLE;
	private MPUsingMachine powerManager = MPUsingMachine.CAPABILITY.getDefaultInstance();
	private boolean boiling = false;
	private boolean shouldUpdateClients = false;
	private int workGoal = 0;
	private int workDone = 0;
	private int workCost = 0;
	private ICauldronRecipe currentRecipeCache = ICauldronRecipe.NONE;
	private String name;

	private int targetColorRGB = DEFAULT_COLOR;
	private int effectiveClientSideColor = DEFAULT_COLOR;

	public TileEntityCauldron() {
		this.collectionZone = new AxisAlignedBB(0, 0, 0, 1, 0.65D, 1);
		this.tank = new CauldronFluidTank(this);
	}

	public static void giveItemToPlayer(EntityPlayer player, ItemStack toGive) {
		if (!player.inventory.addItemStackToInventory(toGive)) {
			player.dropItem(toGive, false);
		} else if (player instanceof EntityPlayerMP) {
			((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking() && playerIn.getHeldItem(hand).isEmpty()) {
			worldIn.setBlockState(pos, state.cycleProperty(StateProperties.HANDLE_DOWN), 3);
		}
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if (!playerIn.world.isRemote && (this.ingredients.size() == 0) && heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			FluidUtil.interactWithFluidHandler(playerIn, hand, this.tank);
			if (playerIn.isCreative() && this.tank.isFull()) {
				this.markDirty();
				this.shouldUpdateClients = true;
			}
		}

		if (heldItem.getItem() == Items.NAME_TAG) {
			String oldname = this.name;
			this.name = heldItem.getDisplayName();
			CapabilityCauldronTeleport ctp = this.world.getCapability(CapabilityCauldronTeleport.CAPABILITY, null);
			if (ctp.put(this.world, pos)) {
				this.markDirty();
				this.shouldUpdateClients = true;
				if (!playerIn.isCreative()) {
					heldItem.shrink(1);
				}
			} else {
				this.name = oldname;
			}
		}

		return true;
	}

	@Override
	public void update() {
		if (this.world.isRemote) {
			if (this.effectiveClientSideColor != this.targetColorRGB) {
				this.effectiveClientSideColor = ColorHelper.blendColor(this.effectiveClientSideColor, this.targetColorRGB, 0.92f);
			}
		} else {
			updateLogic();
		}
	}

	private void updateLogic() {
		Material matBelow = world.getBlockState(getPos().down()).getMaterial();
		boolean wasBoiling = boiling;
		this.boiling = matBelow == Material.FIRE || matBelow == Material.LAVA;
		if (wasBoiling != boiling) {
			markDirty();
			shouldUpdateClients = true;
		}
		if (boiling) {
			updateItemCollection();
			boolean collectedPower = true;
			if (workGoal > 0 && (workCost == 0 || (collectedPower = powerManager.drainAltarFirst(getClosestPlayer(), pos, world.provider.getDimension(), workCost)))) {
				workDone++;
				if (workDone >= workGoal) {
					processRecipe();
				}
				markDirty();
			}
			if (!collectedPower) {
				processLowEnergy();
			}
		}
		if (shouldUpdateClients) {
			this.syncToClient();
			shouldUpdateClients = false;
		}
	}

	private void processLowEnergy() {
		//Spawn particles
	}

	private void processRecipe() {
		createResult();
		cleanupCauldron();
	}

	private void cleanupCauldron() {
		this.ingredients.clear();
		this.switchMode(CauldronMode.IDLE);
		markDirty();
	}

	private void createResult() {
		if (mode == CauldronMode.CRAFTING_ABSORBING && currentRecipeCache != ICauldronRecipe.NONE) {
			ICauldronCraftingRecipe recipe = (ICauldronCraftingRecipe) currentRecipeCache;
			FluidStack fluidResult = recipe.processFluid(ingredients, tank.getFluid());
			ItemStack itemResult = recipe.processOutput(ingredients, tank.getFluid());
			EntityItem ei = new EntityItem(getWorld(), getPos().getX(), getPos().getY() + 0.8, getPos().getZ(), itemResult.copy());
			ei.getTags().add(TAG_NO_PICKUP);
			switchMode(CauldronMode.IDLE);
			this.tank.setFluid(fluidResult);
			getWorld().spawnEntity(ei);
		}
	}

	@Nullable
	private EntityPlayer getClosestPlayer() {
		return null;//TODO
	}

	private void updateItemCollection() {
		ItemStack next = this.gatherNextItemFromTop();
		if (next.getItem() == ModItems.wood_ash || mode.canInsertItems) {
			if (!next.isEmpty()) {
				next = consumeItemSpawnContainer(next);
				if (next.getItem() == ModItems.wood_ash) {
					this.switchMode(CauldronMode.CLEANING);
				} else {
					this.ingredients.add(next);
					if (this.mode == CauldronMode.IDLE) {
						evaluateModeForFirstItem(next);
					}
					evaluateRecipeCache();
				}
				this.markDirty();
				this.shouldUpdateClients = true;
			}
		}
	}
	
	private void evaluateRecipeCache() {
		switch (mode) {
			case BREW_ABSORBING :
			case BREW_UNFINISHED :
				evaluateBrew();
				break;
			case CRAFTING_ABSORBING :
			case CRAFTING_UNFINISHED :
				evaluateCrafting();
				break;
			default :
				break;
		}
	}

	private void evaluateCrafting() {
		Optional<ICauldronCraftingRecipe> data = CauldronRegistry.getCraftingResult(tank.getFluid(), ingredients);
		if (data.isPresent()) {
			currentRecipeCache = data.get();
			workCost = currentRecipeCache.getCostPerTick();
			this.switchMode(CauldronMode.CRAFTING_ABSORBING);
		} else {
			currentRecipeCache = ICauldronRecipe.NONE;
			workCost = 0;
		}
	}

	private void evaluateBrew() {
		Optional<BrewData> data = new BrewBuilder(ingredients).build();
		if (data.isPresent()) {
			currentRecipeCache = data.get();
			workCost = currentRecipeCache.getCostPerTick();
		} else {
			currentRecipeCache = ICauldronRecipe.NONE;
			workCost = 0;
		}
	}

	private void evaluateModeForFirstItem(ItemStack is) {
		if (is.getItem() == Items.NETHER_WART) {
			switchMode(CauldronMode.BREW_UNFINISHED);
		} else {
			switchMode(CauldronMode.CRAFTING_UNFINISHED);
		}
	}

	public void switchMode(CauldronMode newMode) {
		this.mode = newMode;
		this.tank.setCanDrain(newMode.canExtractLiquid);
		this.tank.setCanFill(newMode.canInsertLiquid);
		this.workGoal = mode.work;
		this.workDone = 0;
		this.currentRecipeCache = ICauldronRecipe.NONE;
		evaluateWorkCost();
		this.shouldUpdateClients = true;
		this.markDirty();
	}
	
	private void evaluateWorkCost() {
		this.workCost = currentRecipeCache.getCostPerTick();
	}

	public NonNullList<ItemStack> getInputs() {
		return this.ingredients;
	}

	private ItemStack gatherNextItemFromTop() {
		List<EntityItem> list = this.world.getEntitiesWithinAABB(EntityItem.class, this.collectionZone.offset(this.getPos()), a -> !a.getTags().contains(TAG_NO_PICKUP));
		if (list.isEmpty()) {
			return ItemStack.EMPTY;
		}
		EntityItem selectedEntityItem = list.get(0);
		return selectedEntityItem.getItem();
	}
	
	public ItemStack consumeItemSpawnContainer(ItemStack stack) {
		ItemStack next = stack.splitStack(1);
		ItemStack container = next.getItem().getContainerItem(next);
		if (!container.isEmpty()) {
			EntityItem res = new EntityItem(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.9, this.pos.getZ() + 0.5, container);
			res.addTag(TAG_NO_PICKUP);
			this.world.spawnEntity(res);
		}
		this.world.playSound(null, this.pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1f, (float) ((0.2f * Math.random()) + 1));
		return next;
	}

	public int getColorRGB() {
		return this.effectiveClientSideColor;
	}

	public boolean hasItemsInside() {
		return !this.ingredients.isEmpty();
	}

	public Optional<FluidStack> getFluid() {
		return this.tank.isEmpty() ? Optional.empty() : Optional.ofNullable(this.tank.getFluid());
	}

	public void setColor(int color) {
		this.targetColorRGB = color;
		this.markDirty();
		this.shouldUpdateClients = true;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) || (capability == MPUsingMachine.CAPABILITY);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank);
		}
		if (capability == MPUsingMachine.CAPABILITY) {
			return MPUsingMachine.CAPABILITY.cast(this.powerManager);
		}
		return super.getCapability(capability, facing);
	}

	public void handleParticles() {
	}

	@Override
	protected void writeAllModDataNBT(NBTTagCompound tag) {
		tag.setInteger("color", this.targetColorRGB);
		tag.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
		tag.setTag("ingredients", ItemStackHelper.saveAllItems(new NBTTagCompound(), this.ingredients));
		if (this.name != null) {
			tag.setString("name", this.name);
		}
		tag.setBoolean("cacheReloadRequired", this.currentRecipeCache != ICauldronRecipe.NONE);
		tag.setInteger("workCost", workCost);
		tag.setInteger("workDone", workDone);
		tag.setInteger("workGoal", workGoal);
		tag.setInteger("mode", this.mode.ordinal());
		tag.setBoolean("boiling", boiling);
		tag.setTag("mp", this.powerManager.writeToNbt());
	}

	@Override
	protected void readAllModDataNBT(NBTTagCompound tag) {
		this.targetColorRGB = tag.getInteger("color");
		this.tank.readFromNBT(tag.getCompoundTag("tank"));
		this.ingredients.clear();
		if (tag.hasKey("name")) {
			this.name = tag.getString("name");
		} else {
			this.name = null;
		}
		try {
			this.mode = CauldronMode.values()[tag.getInteger("mode")];
		} catch (ArrayIndexOutOfBoundsException e) {
			this.switchMode(CauldronMode.IDLE);
			Log.w("Resetting cauldron to idle mode after error");
			e.printStackTrace();
			return;
		}
		ItemStackHelper.loadAllItems(tag.getCompoundTag("ingredients"), this.ingredients);
		if (tag.getBoolean("cacheReloadRequired")) {
			evaluateRecipeCache();
		}
		workCost = tag.getInteger("workCost");
		workDone = tag.getInteger("workDone");
		workGoal = tag.getInteger("workGoal");
		boiling = tag.getBoolean("boiling");
		
		this.powerManager.readFromNbt(tag.getCompoundTag("mp"));
	}

	@Override
	protected void writeModSyncDataNBT(NBTTagCompound tag) {
		tag.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
		tag.setInteger("color", this.targetColorRGB);
		tag.setBoolean("hasItemsInside", this.ingredients.size() > 0);
		if (this.name != null) {
			tag.setString("name", this.name);
		}
	}

	@Override
	protected void readModSyncDataNBT(NBTTagCompound tag) {
		this.tank.readFromNBT(tag.getCompoundTag("tank"));
		this.targetColorRGB = tag.getInteger("color");
		if (tag.getBoolean("hasItemsInside")) {
			this.ingredients.clear();
			this.ingredients.add(ItemStack.EMPTY); // Makes the list not empty
		}
		if (tag.hasKey("name")) {
			this.name = tag.getString("name");
		} else {
			this.name = null;
		}
	}

	public void onLiquidChange() {
		if (mode == CauldronMode.IDLE) {
			evaluateModeForLiquidChange();
		}
		this.markDirty();
		this.shouldUpdateClients = true;
	}

	private void evaluateModeForLiquidChange() {
		if (tank.getFluidAmount() == 0) {
			switchMode(CauldronMode.IDLE);
		} else {
			if (tank.getFluid().getFluid() == FluidRegistry.LAVA) {
				switchMode(CauldronMode.LAVA);
			}
		}
	}

}

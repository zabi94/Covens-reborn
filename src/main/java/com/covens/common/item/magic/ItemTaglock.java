package com.covens.common.item.magic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.common.core.capability.bedowner.CapabilityBedOwner;
import com.covens.common.core.capability.simple.TaglockIncarnation;
import com.covens.common.core.helper.Log;
import com.covens.common.core.helper.NBTHelper;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;
import com.covens.common.lib.LibMod;

import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBed.EnumPartType;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.entity.EntityHelper;
import zabi.minecraft.minerva.common.entity.RayTraceHelper;
import zabi.minecraft.minerva.common.entity.UUIDs;


public class ItemTaglock extends ItemMod {

	public static final String TAGLOCK_ENTITY = "entity_id";
	public static final String TAGLOCK_ENTITY_NAME = "name";
	public static final String TAGLOCK_ENTITY_INCARNATION = "incarnation";

	public ItemTaglock() {
		super(LibItemName.TAGLOCK);
	}

	public static Optional<EntityLivingBase> getVictim(ItemStack stack, World world) {
		UUID uuid = NBTHelper.getUniqueID(stack, TAGLOCK_ENTITY);
		EntityLivingBase elb = EntityHelper.getEntityAcrossDimensions(uuid);
		return Optional.ofNullable(elb);
	}

	public static void setVictim(ItemStack stack, TaglockData data) {
		NBTHelper.setUniqueID(stack, TAGLOCK_ENTITY, data.id);
		NBTHelper.setString(stack, TAGLOCK_ENTITY_NAME, data.name);
		NBTHelper.setInteger(stack, TAGLOCK_ENTITY_INCARNATION, data.inc);
	}

	public static void removeVictim(ItemStack stack) {
		NBTHelper.removeTag(stack, TAGLOCK_ENTITY);
		NBTHelper.removeTag(stack, TAGLOCK_ENTITY_NAME);
		NBTHelper.removeTag(stack, TAGLOCK_ENTITY_INCARNATION);
	}
	
	public static boolean isBound(ItemStack stack) {
		return NBTHelper.hasTag(stack, TAGLOCK_ENTITY_NAME);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			RayTraceResult result = RayTraceHelper.rayTraceResult(player, RayTraceHelper.fromLookVec(player, 2), true, true);
			if ((result != null) && (result.typeOfHit == Type.ENTITY) && (result.entityHit instanceof EntityLivingBase)) {
				EntityLivingBase entity = (EntityLivingBase) result.entityHit;
				setVictim(player.getHeldItem(hand), new TaglockData(entity.getName(), UUIDs.of(entity), entity.getCapability(TaglockIncarnation.CAPABILITY, null).incarnation));
			} else if (player.isSneaking() && (result == null || result.typeOfHit == Type.MISS)) {
				setVictim(player.getHeldItem(hand), new TaglockData(player.getName(), UUIDs.of(player), player.getCapability(TaglockIncarnation.CAPABILITY, null).incarnation));
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		if (NBTHelper.hasTag(stack, TAGLOCK_ENTITY_NAME)) {
			tooltip.add(TextFormatting.DARK_GRAY + NBTHelper.getString(stack, TAGLOCK_ENTITY_NAME));
		} else {
			tooltip.add(TextFormatting.DARK_GRAY + I18n.format("item.tag_lock.empty"));
		}
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isBed(state, world, pos, player)) {
			Optional<TaglockData> victim = this.getPlayerFromBed(world, pos);
			if (victim.isPresent()) {
				setVictim(player.getHeldItem(hand), victim.get());
				return EnumActionResult.SUCCESS;
			}
		}

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	private Optional<TaglockData> getPlayerFromBed(World world, BlockPos bedPos) {
		IBlockState bedClicked = world.getBlockState(bedPos);
		if (bedClicked.getBlock() != Blocks.BED) {
			Log.w("Missing bed tile entity! Is there a half broken bed at "+bedPos+"?");
			return Optional.empty();
		}
		if (bedClicked.getValue(BlockBed.PART) == EnumPartType.HEAD) {
			return world.getTileEntity(bedPos).getCapability(CapabilityBedOwner.CAPABILITY, null).getData();
		}
		return getPlayerFromBed(world, bedPos.offset(bedClicked.getValue(BlockHorizontal.FACING)));
	}
	
	public static class TaglockData implements INBTSerializable<NBTTagCompound> {
		
		private String name;
		private UUID id;
		private int inc;
		
		public TaglockData(NBTTagCompound tag) {
			deserializeNBT(tag);
		}
		
		public TaglockData(String entityName, UUID entityUuid, int entityIncarnation) {
			this.name = entityName;
			this.id = entityUuid;
			this.inc = entityIncarnation;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setUniqueId(TAGLOCK_ENTITY, id);
			tag.setString(TAGLOCK_ENTITY_NAME, name);
			tag.setInteger(TAGLOCK_ENTITY_INCARNATION, inc);
			return tag;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			name = nbt.getString(TAGLOCK_ENTITY_NAME);
			id = nbt.getUniqueId(TAGLOCK_ENTITY);
			inc = nbt.getInteger(TAGLOCK_ENTITY_INCARNATION);
		}
		
	}
	
	@Override
	public void registerModel() {
		ResourceLocation full = new ResourceLocation(LibMod.MOD_ID, this.getRegistryName().getPath()+"_filled");
		ModelResourceLocation unbound = new ModelResourceLocation(this.getRegistryName(), "inventory");
		ModelResourceLocation bound = new ModelResourceLocation(full, "inventory");
		ModelBakery.registerItemVariants(this, this.getRegistryName(), full);
		ModelLoader.setCustomMeshDefinition(this, stack -> isBound(stack)?bound:unbound);
	}
}

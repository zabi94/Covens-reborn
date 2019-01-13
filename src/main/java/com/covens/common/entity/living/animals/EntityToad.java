package com.covens.common.entity.living.animals;

import java.util.Set;

import com.covens.client.render.entity.model.AnimationHelper;
import com.covens.common.entity.living.EntityMultiSkin;
import com.covens.common.lib.LibMod;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityToad extends EntityMultiSkin {

	private static final ResourceLocation loot = new ResourceLocation(LibMod.MOD_ID, "entities/toad");
	private static final Set<Item> TAME_ITEMS = Sets.newHashSet(Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE);
	private static final DataParameter<Integer> TINT = EntityDataManager.createKey(EntityToad.class, DataSerializers.VARINT);
	private AnimationHelper jumpingAnim = new AnimationHelper(15);

	public EntityToad(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(TINT, 0xFFFFFF);
		this.aiSit = new EntityAISit(this);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.jumpingAnim.updateTimer();
		boolean isMoving = ((this.motionX * this.motionX) > 0.025) || ((this.motionZ * this.motionZ) > 0.025);
		if (this.world.isRemote) {
			if (isMoving || this.isJumping()) {
				this.jumpingAnim.increaseTimer();
			}
			if (this.jumpingAnim.getTimer() >= this.jumpingAnim.getDuration()) {
				this.jumpingAnim.setTimer(0);
				this.setJumping(false);
			}
		}
		if (isMoving && this.onGround) {
			this.jump();
			this.setJumping(true);
		}

	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		}
		if (this.aiSit != null) {
			this.aiSit.setSitting(false);
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean getCanSpawnHere() {
		int i = MathHelper.floor(this.posX);
		int j = MathHelper.floor(this.getEntityBoundingBox().minY);
		int k = MathHelper.floor(this.posZ);
		BlockPos blockpos = new BlockPos(i, j, k);
		Block block = this.world.getBlockState(blockpos.down()).getBlock();
		return (block instanceof BlockGrass) && (this.world.getLight(blockpos) > 8) && super.getCanSpawnHere();
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 0.3D, false));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.tasks.addTask(4, new EntityAIWatchClosest2(this, EntityPlayer.class, 5f, 1f));
		this.tasks.addTask(3, new EntityAIMate(this, 1d));
		this.tasks.addTask(4, this.aiSit);
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
	}

	@Override
	protected ResourceLocation getLootTable() {
		return loot;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.70d);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 2;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		boolean flag = super.attackEntityAsMob(entity);
		if (flag) {
			if (entity instanceof EntityLivingBase) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 1, true, false));
			}
		}
		return flag;
	}

	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) {
		if (otherAnimal == this) {
			return false;
		} else if (!this.isTamed()) {
			return false;
		} else if (!(otherAnimal instanceof EntityToad)) {
			return false;
		} else {
			EntityToad entitytoad = (EntityToad) otherAnimal;

			if (!entitytoad.isTamed()) {
				return false;
			} else if (entitytoad.isSitting()) {
				return false;
			} else {
				return this.isInLove() && entitytoad.isInLove();
			}
		}
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (!this.isTamed() && TAME_ITEMS.contains(itemstack.getItem())) {
			if (!player.capabilities.isCreativeMode) {
				itemstack.shrink(1);
			}
			if (!this.isSilent()) {
				this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PARROT_EAT, this.getSoundCategory(), 1.0F, 1.0F + ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F));
			}

			if (!this.world.isRemote) {
				if ((this.rand.nextInt(10) == 0) && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
					this.setTamedBy(player);
					this.playTameEffect(true);
					this.world.setEntityState(this, (byte) 7);
				} else {
					this.playTameEffect(false);
					this.world.setEntityState(this, (byte) 6);
				}
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return TAME_ITEMS.contains(stack.getItem());
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityToad(this.world);
	}

	@Override
	public int getSkinTypes() {
		return 4;
	}

	public boolean isJumping() {
		return this.isJumping;
	}

	public float getJumpProgress(float partialRenderTicks) {
		return this.jumpingAnim.getAnimationFraction(partialRenderTicks);
	}

}

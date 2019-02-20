package com.covens.common.content.transformation.vampire;

import java.util.ArrayList;
import java.util.UUID;

import com.covens.api.CovensAPI;
import com.covens.api.event.HotbarActionCollectionEvent;
import com.covens.api.event.HotbarActionTriggeredEvent;
import com.covens.api.event.TransformationModifiedEvent;
import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.entity.EntityBatSwarm;
import com.covens.common.item.ModItems;
import com.covens.common.item.misc.ItemBloodBottle;
import com.covens.common.potion.ModPotions;
import com.covens.common.world.biome.ModBiomes;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.oredict.OreIngredient;
import zabi.minecraft.minerva.common.utils.AttributeModifierModeHelper;
import zabi.minecraft.minerva.common.utils.entity.RayTraceHelper;

@Mod.EventBusSubscriber
public class VampireAbilityHandler {

	public static final DamageSource SUN_DAMAGE = new DamageSource("sun_on_vampire").setDamageBypassesArmor().setDamageIsAbsolute().setFireDamage();

	public static final Ingredient WOOD_WEAPON = new OreIngredient("weaponWood");
	public static final Ingredient SILVER_WEAPON = new OreIngredient("weaponSilver");

	public static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("c73f6d26-65ed-4ba5-ada8-9a96f8712424");
	// TODO Check: can two different living hurt events run down the subscriber list
	// in parallel?
	// If so this doesn't work and one entity might end up receiving the damage
	// amount meant for someone else in some cases
	public static float lastDamage = 0;

	/**
	 * Modifies damage depending on the type. Fire and explosion make it 150%of the
	 * original, all the other types make it 10% of the original provided there's
	 * blood in the pool
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public static void incomingDamageModifier(LivingHurtEvent evt) {
		if (!evt.getEntity().world.isRemote && (evt.getEntityLiving() instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
			CapabilityTransformation data = player.getCapability(CapabilityTransformation.CAPABILITY, null);
			if (data.getType() == DefaultTransformations.VAMPIRE) {
				if (evt.getSource() == SUN_DAMAGE) {
					evt.setCanceled(false); // No immunity for you
					player.dismountRidingEntity();
					return; // No multipliers for sun
				}
				float multiplier = getMultiplier(evt); // A multiplier greater 1 makes the damage not be reduced
				evt.setCanceled(false); // No forms of immunity for vampires
				evt.setAmount(lastDamage); // No reductions either
				if (multiplier > 1) {
					evt.setAmount(evt.getAmount() * multiplier);
				} else if (player.getCapability(CapabilityVampire.CAPABILITY, null).getBlood() > 0) { // Don't mitigate damage when there is no blood in the pool
					evt.setAmount(evt.getAmount() * (1f - (0.1f * data.getLevel())));
				}
				if (player.getRidingEntity() instanceof EntityBatSwarm) {
					evt.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public static void dropInvalidGear(LivingHurtEvent evt) {
		if (!evt.getEntity().world.isRemote && (evt.getEntityLiving() instanceof EntityPlayer)) {
			CapabilityTransformation data = evt.getEntityLiving().getCapability(CapabilityTransformation.CAPABILITY, null);
			if (data.getType() == DefaultTransformations.VAMPIRE) {
				lastDamage = evt.getAmount();
			}
		}
	}

	// NO-SUBSCRIBE
	private static float getMultiplier(LivingHurtEvent evt) {
		float mult = 1;
		if (evt.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer source = (EntityPlayer) evt.getSource().getTrueSource();
			CapabilityTransformation data = source.getCapability(CapabilityTransformation.CAPABILITY, null);
			if (data.getType() == DefaultTransformations.HUNTER) {
				mult += 0.8;
			} else if (data.getType() != DefaultTransformations.NONE) {
				mult += 0.1; // All transformations basically (except hunter)
			}
			if (WOOD_WEAPON.apply(source.getHeldItemMainhand())) {
				mult += 0.1;
			} else if (SILVER_WEAPON.apply(source.getHeldItemMainhand())) {
				mult += 0.3;
			}
		}
		if (evt.getSource().isFireDamage() || evt.getSource().isExplosion() || evt.getSource().canHarmInCreative()) {
			mult += 0.5;
			if (evt.isCanceled()) {
				mult += 1; // Attempts to prevent damage are bad
			}
		}
		return mult;
	}

	private static boolean shouldVampiresBurnHere(World world, BlockPos pos) {
		return ((world.getTotalWorldTime() % 40) == 0) && world.canBlockSeeSky(pos) && world.isDaytime() && !world.isRainingAt(pos);
	}

	private static boolean isPlayerOutsideLair(EntityPlayer player) {
		return player.world.getBiome(player.getPosition()) != ModBiomes.VAMPIRE_LAIR;
	}

	private static boolean canPlayerBurn(EntityPlayer player, CapabilityTransformation data) {
		return (player.getActivePotionEffect(ModPotions.sun_ward) == null) && ((data.getLevel() < 5) || !CovensAPI.getAPI().addVampireBlood(player, -(13 + data.getLevel())));
	}

	@SubscribeEvent
	public static void tickChecks(PlayerTickEvent evt) {
		CapabilityTransformation data = evt.player.getCapability(CapabilityTransformation.CAPABILITY, null);
		if ((data.getType() == DefaultTransformations.VAMPIRE) && evt.side.isServer()) {

			// Check sun damage
			if (shouldVampiresBurnHere(evt.player.world, evt.player.getPosition()) && isPlayerOutsideLair(evt.player) && canPlayerBurn(evt.player, data)) {
				evt.player.attackEntityFrom(SUN_DAMAGE, 11 - data.getLevel());
			}

			// Replace hunger mechanics with blood mechanics
			if ((evt.player.ticksExisted % 30) == 0) {
				evt.player.getFoodStats().setFoodLevel(10); // No healing from food
				if (evt.player.getCapability(CapabilityVampire.CAPABILITY, null).getBlood() == 0) {
					evt.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 200, 2, false, false));
					evt.player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 2, false, false));
				} else {
					if ((evt.player.getHealth() < evt.player.getMaxHealth()) && CovensAPI.getAPI().addVampireBlood(evt.player, -20)) {
						evt.player.heal(1);
					}
				}

				// Hunger drains blood
				PotionEffect effect = evt.player.getActivePotionEffect(MobEffects.HUNGER);
				if (effect != null) {
					CovensAPI.getAPI().addVampireBlood(evt.player, -effect.getAmplifier() * 5);
				}

				// Fire resistance becomes hunger
				PotionEffect pe = evt.player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE);
				if (pe != null) {
					evt.player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, pe.getDuration(), pe.getAmplifier()));
					evt.player.removePotionEffect(MobEffects.FIRE_RESISTANCE);
				}

				// No need for air
				evt.player.setAir(150);

			}
		}
	}

	@SubscribeEvent
	public static void sleepBed(RightClickBlock evt) {
		CapabilityTransformation data = evt.getEntityPlayer().getCapability(CapabilityTransformation.CAPABILITY, null);
		if ((data.getType() == DefaultTransformations.VAMPIRE) && (evt.getEntityPlayer().world.getBlockState(evt.getPos()).getBlock() == Blocks.BED)) {
			evt.setCancellationResult(EnumActionResult.FAIL);
			evt.setCanceled(true);
			evt.getEntityPlayer().sendStatusMessage(new TextComponentTranslation("vampire.bed_blocked"), true);
		}
	}

	@SubscribeEvent
	public static void foodEaten(LivingEntityUseItemEvent.Finish evt) {
		if ((evt.getEntityLiving() instanceof EntityPlayer) && (evt.getItem().getItem() instanceof ItemFood)) {
			CapabilityTransformation data = ((EntityPlayer) evt.getEntityLiving()).getCapability(CapabilityTransformation.CAPABILITY, null);
			if (data.getType() == DefaultTransformations.VAMPIRE) {
				evt.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 2, false, true));
			}
		}
	}

	@SubscribeEvent
	public static void attachHotbarAbilities(HotbarActionCollectionEvent evt) {
		CapabilityTransformation data = evt.player.getCapability(CapabilityTransformation.CAPABILITY, null);
		if (data.getType() == DefaultTransformations.VAMPIRE) {
			evt.getList().add(ModAbilities.DRAIN_BLOOD);
			if (data.getLevel() > 5) {
				evt.getList().add(ModAbilities.NIGHT_VISION_VAMPIRE);
			} else {
				evt.player.getCapability(CapabilityVampire.CAPABILITY, null).setNightVision(false);
			}
			if (data.getLevel() > 5) {
				evt.getList().add(ModAbilities.BAT_SWARM);
				evt.getList().add(ModAbilities.MESMERIZE);
			}
			if (data.getLevel() > 7) {
				evt.getList().add(ModAbilities.HYPNOTIZE);
			}
		} else {
			evt.player.getCapability(CapabilityVampire.CAPABILITY, null).setNightVision(false);
		}
	}

	@SubscribeEvent
	public static void onHotbarAbilityToggled(HotbarActionTriggeredEvent evt) {
		CapabilityTransformation data = evt.player.getCapability(CapabilityTransformation.CAPABILITY, null);
		CapabilityVampire vampire = evt.player.getCapability(CapabilityVampire.CAPABILITY, null);
		if (data.getType() != DefaultTransformations.VAMPIRE) {
			return;
		}
		if (evt.action == ModAbilities.NIGHT_VISION_VAMPIRE) {
			vampire.setNightVision(!vampire.nightVision);
		} else if (evt.action == ModAbilities.DRAIN_BLOOD) {

			if (evt.focusedEntity instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) evt.focusedEntity;
				if (canDrainBloodFrom(evt.player, entity)) {
					CovensAPI.getAPI().drainBloodFromEntity(evt.player, entity);
					if (!evt.world.isRemote && data.getLevel() > 3 && evt.player.getHeldItemOffhand().getItem().equals(Items.GLASS_BOTTLE) && evt.player.getRNG().nextInt(10) == 0) {
						evt.player.getHeldItemOffhand().splitStack(1);
						evt.player.dropItem(ItemBloodBottle.getNewStack(evt.world), true);
					}
				}
			} else if (data.getLevel() > 1) {
				RayTraceResult rtr = RayTraceHelper.rayTracePlayerSight(evt.player, evt.player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue(), false);
				if (rtr != null && rtr.typeOfHit == Type.BLOCK) {
					IBlockState block = evt.world.getBlockState(rtr.getBlockPos());
					if (block.getBlock() == Blocks.WOOL && CovensAPI.getAPI().addVampireBlood(evt.player, -50)) {
						evt.world.setBlockToAir(rtr.getBlockPos());
						ItemStack is = new ItemStack(ModItems.sanguine_fabric);
						EntityItem ei = new EntityItem(evt.world, rtr.getBlockPos().getX() + 0.5, rtr.getBlockPos().getY() + 0.5, rtr.getBlockPos().getZ() + 0.5, is);
						evt.world.spawnEntity(ei);
					}
				}
			}
		} else if (evt.action == ModAbilities.BAT_SWARM) {
			if (!(evt.player.getRidingEntity() instanceof EntityBatSwarm) && (evt.player.isCreative() || CovensAPI.getAPI().addVampireBlood(evt.player, -20))) {
				EntityBatSwarm bs = new EntityBatSwarm(evt.player.world);
				float pitch = (Math.abs(evt.player.rotationPitch) < 7) ? 0 : evt.player.rotationPitch;
				bs.setPositionAndRotation(evt.player.posX, evt.player.posY + evt.player.getEyeHeight(), evt.player.posZ, evt.player.rotationYaw, pitch);
				evt.player.world.spawnEntity(bs);
				evt.player.startRiding(bs);
			}
		} else if (evt.action == ModAbilities.MESMERIZE) {
			if ((evt.focusedEntity instanceof EntityLivingBase) && CovensAPI.getAPI().addVampireBlood(evt.player, -150)) {
				((EntityLivingBase) evt.focusedEntity).addPotionEffect(new PotionEffect(ModPotions.mesmerized, 100, 0, false, true));
			}
		} else if (evt.action == ModAbilities.HYPNOTIZE) {
			evt.player.sendStatusMessage(new TextComponentString("Hypnotize ability not available yet"), true);
		}
	}

	private static boolean canDrainBloodFrom(EntityPlayer player, EntityLivingBase entity) {
		if (entity.getActivePotionEffect(ModPotions.mesmerized) != null) {
			return true;
		}
		if ((player.getLastAttackedEntity() == entity) || (entity.getAttackingEntity() == player)) {
			return false;
		}
		return Math.abs(player.rotationYawHead - entity.rotationYawHead) < 30;
	}

	@SubscribeEvent
	public static void abilityHandler(PlayerTickEvent evt) {
		if ((evt.phase == Phase.START) && !evt.player.world.isRemote && (evt.player.getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.VAMPIRE)) {
			PotionEffect nv = evt.player.getActivePotionEffect(MobEffects.NIGHT_VISION);
			if (((nv == null) || (nv.getDuration() <= 220)) && evt.player.getCapability(CapabilityVampire.CAPABILITY, null).nightVision) {
				if (evt.player.isCreative() || CovensAPI.getAPI().addVampireBlood(evt.player, -2)) {
					evt.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, false));
				} else {
					evt.player.sendStatusMessage(new TextComponentTranslation("vampire.nightvision.low_blood"), true);
					evt.player.getCapability(CapabilityVampire.CAPABILITY, null).setNightVision(false);
				}
			}
		}
	}

	@SubscribeEvent
	public static void refreshModifiers(TransformationModifiedEvent evt) {
		CapabilityTransformation data = evt.getEntityPlayer().getCapability(CapabilityTransformation.CAPABILITY, null);
		IAttributeInstance attack_speed = evt.getEntityPlayer().getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
		AttributeModifier modifier = attack_speed.getModifier(ATTACK_SPEED_MODIFIER_UUID);
		if (modifier != null) {
			attack_speed.removeModifier(modifier);
		}
		if (data.getType() == DefaultTransformations.VAMPIRE) {
			attack_speed.applyModifier(new AttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Vampire Atk Speed", evt.level / 10, AttributeModifierModeHelper.ADD));
		}
	}

	@SubscribeEvent
	public static void onLivingJoinWorld(EntityJoinWorldEvent evt) {
		if (evt.getEntity() instanceof EntityLiving) {
			EntityLiving living = (EntityLiving) evt.getEntity();
			ArrayList<EntityAITaskEntry> tasks = Lists.newArrayList();
			living.tasks.taskEntries.stream().filter(t -> t.action instanceof EntityAIWatchClosest).forEach(t -> tasks.add(t));
			tasks.forEach(t -> living.tasks.taskEntries.remove(t));
			tasks.forEach(t -> living.tasks.taskEntries.add(living.tasks.new EntityAITaskEntry(t.priority, new AIWatchClosestWrapper((EntityAIWatchClosest) t.action))));
		}
	}

	@SubscribeEvent
	public static void checkHealing(LivingHealEvent evt) {
		if ((evt.getEntityLiving() instanceof EntityPlayer) && !evt.getEntityLiving().world.isRemote && (evt.getEntityLiving().getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.VAMPIRE) && (evt.getEntityLiving().getCapability(CapabilityVampire.CAPABILITY, null).getBlood() == 0)) {
			evt.setCanceled(true);
		}
	}

}

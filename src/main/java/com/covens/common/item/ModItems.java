package com.covens.common.item;

import javax.annotation.Nullable;

import com.covens.api.ritual.EnumGlyphType;
import com.covens.common.block.ModBlocks;
import com.covens.common.block.chisel.BlockColdIronChiseled;
import com.covens.common.block.chisel.BlockNetherSteelChiseled;
import com.covens.common.block.chisel.BlockSilverChiseled;
import com.covens.common.block.natural.Gem;
import com.covens.common.core.capability.altar.MixedProvider;
import com.covens.common.core.helper.CropHelper;
import com.covens.common.item.block.ItemBlockColor;
import com.covens.common.item.block.ItemBlockMeta;
import com.covens.common.item.block.ItemBlockMeta.EnumNameMode;
import com.covens.common.item.block.ItemBlockRevealingLantern;
import com.covens.common.item.block.ItemBlockSapling;
import com.covens.common.item.block.ItemGemBlock;
import com.covens.common.item.block.ItemGemOre;
import com.covens.common.item.block.ItemGoblet;
import com.covens.common.item.equipment.ItemSilverArmor;
import com.covens.common.item.equipment.ItemVampireArmor;
import com.covens.common.item.equipment.ItemWitchesArmor;
import com.covens.common.item.equipment.baubles.ItemGirdleOfTheWooded;
import com.covens.common.item.equipment.baubles.ItemHellishBauble;
import com.covens.common.item.equipment.baubles.ItemHorseshoe;
import com.covens.common.item.equipment.baubles.ItemMantle;
import com.covens.common.item.equipment.baubles.ItemOmenNeckalce;
import com.covens.common.item.equipment.baubles.ItemPouch;
import com.covens.common.item.equipment.baubles.ItemRemedyTalisman;
import com.covens.common.item.equipment.baubles.ItemTalisman;
import com.covens.common.item.equipment.baubles.ItemTriskelionAmulet;
import com.covens.common.item.equipment.baubles.ItemWrathfulEye;
import com.covens.common.item.food.ItemFilledBowl;
import com.covens.common.item.food.ItemHeart;
import com.covens.common.item.food.ItemHoney;
import com.covens.common.item.food.ItemJuniperBerries;
import com.covens.common.item.food.ItemMagicSalve;
import com.covens.common.item.magic.ItemBell;
import com.covens.common.item.magic.ItemBroom;
import com.covens.common.item.magic.ItemLocationStone;
import com.covens.common.item.magic.ItemRitualChalk;
import com.covens.common.item.magic.ItemSalt;
import com.covens.common.item.magic.ItemSpellPage;
import com.covens.common.item.magic.ItemTaglock;
import com.covens.common.item.magic.ItemTarots;
import com.covens.common.item.magic.brew.ItemBrewArrow;
import com.covens.common.item.magic.brew.ItemBrewDrinkable;
import com.covens.common.item.magic.brew.ItemBrewThrowable;
import com.covens.common.item.misc.ItemBloodBottle;
import com.covens.common.item.misc.ItemSparkDarkness;
import com.covens.common.item.tool.ItemAthame;
import com.covens.common.item.tool.ItemBoline;
import com.covens.common.item.tool.ItemColdIronAxe;
import com.covens.common.item.tool.ItemColdIronHoe;
import com.covens.common.item.tool.ItemColdIronPickaxe;
import com.covens.common.item.tool.ItemColdIronSpade;
import com.covens.common.item.tool.ItemColdIronSword;
import com.covens.common.item.tool.ItemSilverAxe;
import com.covens.common.item.tool.ItemSilverHoe;
import com.covens.common.item.tool.ItemSilverPickaxe;
import com.covens.common.item.tool.ItemSilverSpade;
import com.covens.common.item.tool.ItemSilverSword;
import com.covens.common.lib.LibItemName;
import com.covens.common.lib.LibMod;

import baubles.api.BaubleType;
import net.minecraft.block.Block;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(LibMod.MOD_ID)
public final class ModItems {

	public static final Item mandrake_root = null;
	public static final Item seed_mandrake = null;
	public static final Item belladonna = null;
	public static final Item seed_belladonna = null;
	public static final Item mint = null;
	public static final Item seed_mint = null;
	public static final Item thistle = null;
	public static final Item seed_thistle = null;
	public static final Item garlic = null;
	public static final Item seed_garlic = null;
	public static final Item aconitum = null;
	public static final Item seed_aconitum = null;
	public static final Item white_sage = null;
	public static final Item seed_white_sage = null;
	public static final Item tulsi = null;
	public static final Item seed_tulsi = null;
	public static final Item wormwood = null;
	public static final Item seed_wormwood = null;
	public static final Item hellebore = null;
	public static final Item seed_hellebore = null;
	public static final Item sagebrush = null;
	public static final Item seed_sagebrush = null;
	public static final Item chrysanthemum = null;
	public static final Item seed_chrysanthemum = null;
	public static final Item moonbell = null;

	public static final Item glass_jar = null;

	public static final Item brew_phial_drink = null;
	public static final Item brew_phial_splash = null;
	public static final Item brew_phial_linger = null;
	public static final Item brew_arrow = null;
	public static final Item empty_brew_drink = null;
	public static final Item empty_brew_splash = null;
	public static final Item empty_brew_linger = null;
	
	//Old fumes
	public static final Item unfired_jar = null; 
	public static final Item empty_jar = null; // Empty
	public static final Item oak_spirit = null; 
	public static final Item birch_soul = null; 
	public static final Item acacia_essence = null; 
	public static final Item spruce_heart = null; // common trees
	public static final Item cloudy_oil = null; // equivalent of foul fume - byproduct
	public static final Item cleansing_aura = null; // connected with cleaning, purifying
	public static final Item emanation_of_dishonesty = null; // connected with evil
	public static final Item everchanging_presence = null; // connected with changing
	public static final Item undying_image = null; // connected with rebirth
	public static final Item demonic_dew = null; // connected with nether/infernal stuff
	public static final Item otherworld_tears = null; // connected with end/ethereal stuff
	public static final Item fiery_breeze = null; // connected with fire
	public static final Item heavenly_winds = null; // connected with air
	public static final Item petrichor_odour = null; // connected with earth
	public static final Item zephyr_of_the_depths = null; // connected with water
	public static final Item reek_of_death = null; //Cypress
	public static final Item vital_essence = null; //Yew
	public static final Item droplet_of_wisdom = null; //Elder
	public static final Item bottled_magic = null; // Juniper

	public static final Item wax = null;
	public static final Item honey = null;
	public static final Item salt = null;
	public static final Item wool_of_bat = null;
	public static final Item tongue_of_dog = null;
	public static final Item wood_ash = null;
	public static final Item honeycomb = null;
	public static final Item empty_honeycomb = null;
	public static final Item needle_bone = null;
	public static final Item cold_iron_ingot = null;
	public static final Item silver_ingot = null;
	public static final Item silver_nugget = null;
	public static final Item athame = null;
	public static final Item boline = null;
	public static final Item taglock = null;
	public static final Item spectral_dust = null;
	public static final Item silver_scales = null;
	public static final Item heart = null;
	public static final Item dimensional_sand = null;
	public static final Item owlets_wing = null;
	public static final Item ravens_feather = null;
	public static final Item equine_tail = null;
	public static final Item stew = null;
	public static final Item cold_iron_nugget = null;
	public static final Item spanish_moss = null;
	public static final Item juniper_berries = null;

	public static final Item sanguine_fabric = null;

	public static final Item golden_thread = null;
	public static final Item regal_silk = null;
	public static final Item witches_stitching = null;
	public static final Item diabolic_vein = null;
	public static final Item pure_filament = null;
	public static final Item soul_string = null;
	public static final Item graveyard_dust = null;

	public static final Item silver_pickaxe = null;
	public static final Item silver_axe = null;
	public static final Item silver_spade = null;
	public static final Item silver_hoe = null;
	public static final Item silver_sword = null;
	public static final Item silver_helmet = null;
	public static final Item silver_chestplate = null;
	public static final Item silver_leggings = null;
	public static final Item silver_boots = null;

	public static final Item witches_hat = null;
	public static final Item witches_robes = null;
	public static final Item witches_pants = null;
	public static final Item vampire_hat = null;
	public static final Item vampire_vest = null;
	public static final Item vampire_pants = null;

	// Baubles
	public static final Item omen_necklace = null;
	public static final Item talisman_aquamarine_crown = null;
	public static final Item talisman_adamantine_star_ring = null;
	public static final Item talisman_diamond_star = null;
	public static final Item talisman_emerald_pendant = null;
	public static final Item talisman_watching_eye = null;
	public static final Item talisman_ruby_orb = null;
	public static final Item horseshoe = null;
	public static final Item remedy_talisman = null;
	public static final Item girdle_of_the_wooded = null;
	public static final Item wrathful_eye = null;
	public static final Item mantle = null;
	public static final Item pouch = null;

	public static final Item magic_salve = null;

	public static final Item tarots = null;
	public static final Item broom = null;
	public static final Item spell_page = null;
	public static final Item ritual_chalk_normal = null;
	public static final Item ritual_chalk_golden = null;
	public static final Item ritual_chalk_nether = null;
	public static final Item ritual_chalk_ender = null;
	public static final Item location_stone = null;
	public static final Item bell = null;

	public static final Item adders_fork = null;

	public static final Item toe_of_frog = null;
	public static final Item eye_of_newt = null;
	public static final Item lizard_leg = null;

	public static final Item pentacle = null;

	public static final Item cold_iron_sword = null;
	public static final Item cold_iron_axe = null;
	public static final Item cold_iron_hoe = null;
	public static final Item cold_iron_spade = null;
	public static final Item cold_iron_pickaxe = null;
	public static final Item blood_bottle = null;
	public static final Item spark_of_darkness = null;
	
	public static final Item gem_garnet = null;
	public static final Item gem_tourmaline = null;
	public static final Item gem_tigers_eye = null;
	public static final Item gem_malachite = null;
	
	public static ItemRitualChalk[] chalkType;

	private ModItems() {
	}

	public static void register(final IForgeRegistry<Item> registry) {
		CropHelper.getFoods().forEach((crop, item) -> registry.register(item));
		CropHelper.getSeeds().forEach((crop, item) -> registry.register(item));
		registry.register(new ItemMod(LibItemName.COLD_IRON_INGOT));
		registry.register(new ItemMod(LibItemName.SILVER_INGOT));
		registry.register(new ItemMod(LibItemName.SILVER_NUGGET));
		
		
		registry.register(new ItemMod(LibItemName.UNFIRED_JAR));
		registry.register(new ItemMod(LibItemName.EMPTY_JAR));
		registry.register(new ItemMod(LibItemName.OAK_SPIRIT));
		registry.register(new ItemMod(LibItemName.BIRCH_SOUL));
		registry.register(new ItemMod(LibItemName.ACACIA_ESSENCE));
		registry.register(new ItemMod(LibItemName.SPRUCE_HEART));
		registry.register(new ItemMod(LibItemName.CLOUDY_OIL));
		registry.register(new ItemMod(LibItemName.CLEANSING_AURA));
		registry.register(new ItemMod(LibItemName.EMANATION_OF_DISHONESTY));
		registry.register(new ItemMod(LibItemName.EVERCHANGING_PRESENCE));
		registry.register(new ItemMod(LibItemName.UNDYING_IMAGE));
		registry.register(new ItemMod(LibItemName.DEMONIC_DEW));
		registry.register(new ItemMod(LibItemName.OTHERWORLD_TEARS));
		registry.register(new ItemMod(LibItemName.FIERY_BREEZE));
		registry.register(new ItemMod(LibItemName.HEAVENLY_WINDS));
		registry.register(new ItemMod(LibItemName.PETRICHOR_ODOUR));
		registry.register(new ItemMod(LibItemName.ZEPHYR_OF_THE_DEPTHS));
		registry.register(new ItemMod(LibItemName.REEK_OF_DEATH));
		registry.register(new ItemMod(LibItemName.VITAL_ESSENCE));
		registry.register(new ItemMod(LibItemName.DROPLET_OF_WISDOM));
		registry.register(new ItemMod(LibItemName.BOTTLED_MAGIC));

		registry.register(new ItemSpellPage(LibItemName.SPELL_PAGE));

		registry.register(new ItemBell());
		
		
		chalkType = new ItemRitualChalk[] {
				new ItemRitualChalk(EnumGlyphType.NORMAL), //
				new ItemRitualChalk(EnumGlyphType.GOLDEN), //
				new ItemRitualChalk(EnumGlyphType.ENDER), //
				new ItemRitualChalk(EnumGlyphType.NETHER) //
		};
		
		// Misc
		registry.registerAll( //
				new ItemHoney(), // 
				new ItemSalt(), // 
				new ItemMod(LibItemName.WAX), // 
				new ItemMod(LibItemName.HONEYCOMB), // 
				new ItemMod(LibItemName.EMPTY_HONEYCOMB), // 
				new ItemBrewDrinkable(), // 
				new ItemBrewThrowable(LibItemName.BREW_PHIAL_SPLASH), // 
				new ItemBrewThrowable(LibItemName.BREW_PHIAL_LINGER), // 
				new ItemMod(LibItemName.EMPTY_BREW_DRINK), // 
				new ItemMod(LibItemName.EMPTY_BREW_SPLASH), // 
				new ItemMod(LibItemName.EMPTY_BREW_LINGER), // 
				new ItemBrewArrow(), // 
				new ItemMod(LibItemName.GLASS_JAR), // 
				new ItemAthame(), // 
				new ItemBoline(),  //
				new ItemTaglock(), // 
				new ItemMod(LibItemName.NEEDLE_BONE), // 
				new ItemMod(LibItemName.WOOL_OF_BAT),  //
				new ItemMod(LibItemName.TONGUE_OF_DOG), // 
				new ItemMod(LibItemName.WOOD_ASH), // 
				new ItemMod(LibItemName.SPECTRAL_DUST), // 
				new ItemMod(LibItemName.SILVER_SCALES), // 
				new ItemMod(LibItemName.DIMENSIONAL_SAND), // 
				new ItemMod(LibItemName.EQUINE_TAIL), // 
				new ItemMod(LibItemName.GOLDEN_THREAD), // 
				new ItemMod(LibItemName.COLD_IRON_NUGGET), //
				new ItemMod(LibItemName.OWLETS_WING), //  //
				new ItemMod(LibItemName.RAVENS_FEATHER), // 
				new ItemMod(LibItemName.REGAL_SILK), // 
				new ItemMod(LibItemName.WITCHES_STITCHING), // 
				new ItemMod(LibItemName.DIABOLIC_VEIN), // 
				new ItemMod(LibItemName.PURE_FILAMENT), // 
				new ItemMod(LibItemName.SOUL_STRING), // 
				new ItemMod(LibItemName.GRAVEYARD_DUST), // 
				new ItemMod(LibItemName.SANGUINE_FABRIC), // 
				new ItemMod(LibItemName.PENTACLE) {
					private final MixedProvider caps = new MixedProvider(-2, 0.3);
					@Override
					@Nullable
					public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
						return caps;
					}
				},  //
				new ItemMod(LibItemName.ADDERS_FORK),  //
				new ItemMod(LibItemName.TOE_OF_FROG),  //
				new ItemMod(LibItemName.LIZARD_LEG),  //
				new ItemMod(LibItemName.EYE_OF_NEWT),  //
				new ItemHeart(),  //
				new ItemJuniperBerries(),  //
				new ItemFilledBowl(), //
				chalkType[0],
				chalkType[1],
				chalkType[2],
				chalkType[3],
				new ItemRemedyTalisman(),  //
				new ItemMagicSalve(), //
				new ItemLocationStone(),  //
				new ItemTarots(LibItemName.TAROTS), //
				new ItemBroom(LibItemName.BROOM), //
				new ItemBloodBottle(LibItemName.BLOOD_BOTTLE), //
				new ItemSparkDarkness(LibItemName.SPARK_DARKNESS) //
				// new ItemMod(LibItemName.WITCHWEED),
				// new ItemMod(LibItemName.INFESTED_WHEAT)
		);
		
		

		// Baubles
		registry.registerAll(//
				new ItemOmenNeckalce(), //
				new ItemHorseshoe(), //
				new ItemTriskelionAmulet(), //
				new ItemHellishBauble(), //
				new ItemWrathfulEye(), //
				new ItemTalisman(BaubleType.HEAD, 35, LibItemName.TALISMAN_AQUAMARINE_CROWN), //
				new ItemTalisman(BaubleType.RING, 18, LibItemName.TALISMAN_ADAMANTINE_STAR_RING), //
				new ItemTalisman(BaubleType.AMULET, 18, LibItemName.TALISMAN_EMERALD_PENDANT), //
				new ItemTalisman(BaubleType.BELT, 30, LibItemName.TALISMAN_RUBY_ORB), //
				new ItemTalisman(BaubleType.CHARM, 18, LibItemName.TALISMAN_WATCHING_EYE), //
				new ItemGirdleOfTheWooded(LibItemName.GIRDLE_OF_THE_WOODED), //
				new ItemMantle(LibItemName.MANTLE), //
				new ItemPouch(LibItemName.POUCH));

		// Equipment
		registry.registerAll(//
				new ItemSilverPickaxe(), //
				new ItemSilverAxe(), //
				new ItemSilverSpade(), //
				new ItemSilverHoe(), //
				new ItemSilverSword(), //
				new ItemSilverArmor(LibItemName.SILVER_HELMET, ModMaterials.ARMOR_SILVER, 1, EntityEquipmentSlot.HEAD), //
				new ItemSilverArmor(LibItemName.SILVER_CHESTPLATE, ModMaterials.ARMOR_SILVER, 1, EntityEquipmentSlot.CHEST), //
				new ItemSilverArmor(LibItemName.SILVER_LEGGINGS, ModMaterials.ARMOR_SILVER, 2, EntityEquipmentSlot.LEGS), //
				new ItemSilverArmor(LibItemName.SILVER_BOOTS, ModMaterials.ARMOR_SILVER, 1, EntityEquipmentSlot.FEET), //
				new ItemWitchesArmor(LibItemName.WITCHES_HAT, ModMaterials.ARMOR_WITCH_LEATHER, 1, EntityEquipmentSlot.HEAD), //
				new ItemWitchesArmor(LibItemName.WITCHES_ROBES, ModMaterials.ARMOR_WITCH_LEATHER, 1, EntityEquipmentSlot.CHEST), //
				new ItemWitchesArmor(LibItemName.WITCHES_PANTS, ModMaterials.ARMOR_WITCH_LEATHER, 2, EntityEquipmentSlot.LEGS), //
				new ItemVampireArmor(LibItemName.VAMPIRE_HAT, ModMaterials.ARMOR_VAMPIRE, 1, EntityEquipmentSlot.HEAD), //
				new ItemVampireArmor(LibItemName.VAMPIRE_VEST, ModMaterials.ARMOR_VAMPIRE, 2, EntityEquipmentSlot.CHEST),
				new ItemVampireArmor(LibItemName.VAMPIRE_PANTS, ModMaterials.ARMOR_VAMPIRE, 2, EntityEquipmentSlot.LEGS),

				new ItemColdIronSword(), //
				new ItemColdIronAxe(), //
				new ItemColdIronHoe(), //
				new ItemColdIronPickaxe(), //
				new ItemColdIronSpade());
		// Item Blocks
		registry.registerAll(//
				new ItemBlockColor(ModBlocks.candle_medium), //
				new ItemBlockColor(ModBlocks.candle_small), //
				new ItemBlockColor(ModBlocks.candle_medium_lit), //
				new ItemBlockColor(ModBlocks.candle_small_lit), //
				itemBlock(ModBlocks.fake_ice), //
				itemBlock(ModBlocks.silver_block), //
				itemBlock(ModBlocks.silver_ore), //
				itemBlock(ModBlocks.cauldron), //
				itemBlock(ModBlocks.oven), //
				itemBlock(ModBlocks.distillery), //
				itemBlock(ModBlocks.apiary), //
				itemBlock(ModBlocks.brazier), //
				itemBlock(ModBlocks.torchwood), //
				itemBlock(ModBlocks.ember_grass), //
				itemBlock(ModBlocks.beehive), //
				itemBlock(ModBlocks.salt_ore), //
				itemBlock(ModBlocks.nethersteel), //
				itemBlock(ModBlocks.log_elder), //
				itemBlock(ModBlocks.log_juniper), //
				itemBlock(ModBlocks.log_yew), //
				itemBlock(ModBlocks.log_cypress), //
				itemBlock(ModBlocks.leaves_elder), //
				itemBlock(ModBlocks.leaves_juniper), //
				itemBlock(ModBlocks.leaves_yew), //
				itemBlock(ModBlocks.leaves_cypress), //
				itemBlock(ModBlocks.planks_elder), //
				itemBlock(ModBlocks.planks_juniper), //
				itemBlock(ModBlocks.planks_yew), //
				itemBlock(ModBlocks.planks_cypress), //
				itemBlock(ModBlocks.purifying_earth), //
				new ItemBlockSapling(),
				itemBlock(ModBlocks.moonbell), //
				itemBlock(ModBlocks.witch_altar), //
				itemBlock(ModBlocks.thread_spinner), //
				itemBlock(ModBlocks.crystal_ball), //
				new ItemGoblet(), //
				itemBlock(ModBlocks.gem_bowl), //
				itemBlock(ModBlocks.magic_mirror), //
				itemBlock(ModBlocks.tarot_table), //
				itemBlock(ModBlocks.cold_iron_block), //
				itemBlock(ModBlocks.graveyard_dirt), //
				new ItemBlockRevealingLantern(ModBlocks.lantern, false), //
				new ItemBlockRevealingLantern(ModBlocks.revealing_lantern, true), //
				itemBlock(ModBlocks.spanish_moss)
		);

		for (Gem g:Gem.values()) {
			registry.register(g.setGemItem(new ItemMod(g.getGemName())));
			registry.register(new ItemGemBlock(g.getGemBlock()));
			registry.register(new ItemGemOre(g.getOreBlock()));
		}
		
		// Chisel
		registry.registerAll(//
				new ItemBlockMeta<>(ModBlocks.silver_block_chisel, BlockSilverChiseled.BlockSilverVariant.values(), EnumNameMode.TOOLTIP), //
				new ItemBlockMeta<>(ModBlocks.cold_iron_block_chisel, BlockColdIronChiseled.BlockColdIronVariant.values(), EnumNameMode.TOOLTIP), //
				new ItemBlockMeta<>(ModBlocks.nethersteel_chisel, BlockNetherSteelChiseled.BlockSteelVariant.values(), EnumNameMode.TOOLTIP));

	}

	private static Item itemBlock(Block block) {
		if (block == null) {
			throw new LoaderException("[" + LibMod.MOD_NAME + "] Trying to register an ItemBlock for a null block");
		}
		if (block.getRegistryName() == null) {
			throw new LoaderException("[" + LibMod.MOD_NAME + "] Trying to register an ItemBlock for a block with null name - " + block.getTranslationKey());
		}
		if (block.getRegistryName().toString() == null) {
			throw new LoaderException("[" + LibMod.MOD_NAME + "] There's something wrong with the registry implementation of " + block.getTranslationKey());
		}
		return new ItemBlockMod(block).setRegistryName(block.getRegistryName());
	}

	public static void init() {
		initOreDictionary();
	}

	private static void initOreDictionary() {
		OreDictionary.registerOre("gemGarnet", new ItemStack(ModItems.gem_garnet));
		OreDictionary.registerOre("gemTourmaline", new ItemStack(ModItems.gem_tourmaline));
		OreDictionary.registerOre("gemTigersEye", new ItemStack(ModItems.gem_tigers_eye));
		OreDictionary.registerOre("gemMalachite", new ItemStack(ModItems.gem_malachite));

		OreDictionary.registerOre("nuggetSilver", new ItemStack(ModItems.silver_nugget));
		OreDictionary.registerOre("ingotSilver", new ItemStack(ModItems.silver_ingot));
		OreDictionary.registerOre("honeyDrop", new ItemStack(ModItems.honey));
		OreDictionary.registerOre("dropHoney", new ItemStack(ModItems.honey));
		OreDictionary.registerOre("foodHoneydrop", new ItemStack(ModItems.honey));
		OreDictionary.registerOre("listAllsugar", new ItemStack(ModItems.honey));
		OreDictionary.registerOre("materialWax", new ItemStack(ModItems.wax));
		OreDictionary.registerOre("materialBeeswax", new ItemStack(ModItems.wax));
		OreDictionary.registerOre("materialPressedWax", new ItemStack(ModItems.wax));
		OreDictionary.registerOre("itemBeeswax", new ItemStack(ModItems.wax));
		OreDictionary.registerOre("foodSalt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("dustSalt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("materialSalt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("lumpSalt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("salt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("listAllsalt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("ingredientSalt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("pinchSalt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("portionSalt", new ItemStack(ModItems.salt));
		OreDictionary.registerOre("cropBelladonna", new ItemStack(ModItems.belladonna));
		OreDictionary.registerOre("cropMandrake", new ItemStack(ModItems.mandrake_root));
		OreDictionary.registerOre("cropMint", new ItemStack(ModItems.mint));
		OreDictionary.registerOre("listAllspice", new ItemStack(ModItems.mint));
		OreDictionary.registerOre("cropSpiceleaf", new ItemStack(ModItems.mint));
		OreDictionary.registerOre("listAllgreenveggie", new ItemStack(ModItems.mint));
		OreDictionary.registerOre("cropThistle", new ItemStack(ModItems.thistle));
		OreDictionary.registerOre("cropGarlic", new ItemStack(ModItems.garlic));
		OreDictionary.registerOre("listAllherb", new ItemStack(ModItems.garlic));
		OreDictionary.registerOre("cropAconitum", new ItemStack(ModItems.aconitum));
		OreDictionary.registerOre("cropWhiteSage", new ItemStack(ModItems.white_sage));
		OreDictionary.registerOre("cropTulsi", new ItemStack(ModItems.tulsi));
		OreDictionary.registerOre("listAllherb", new ItemStack(ModItems.tulsi));
		OreDictionary.registerOre("listAllspice", new ItemStack(ModItems.wormwood));
		OreDictionary.registerOre("cropWormwood", new ItemStack(ModItems.wormwood));
		OreDictionary.registerOre("ingotColdIron", new ItemStack(ModItems.cold_iron_ingot));
		OreDictionary.registerOre("nuggetColdIron", new ItemStack(ModItems.cold_iron_nugget));
	}
}

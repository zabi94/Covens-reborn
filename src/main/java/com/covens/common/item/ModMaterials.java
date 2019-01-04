package com.covens.common.item;

import com.covens.common.lib.LibItemName;
import com.covens.common.lib.LibMod;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

public final class ModMaterials {

	public static final ArmorMaterial ARMOR_SILVER = registerArmorMaterial(LibItemName.SILVER, 2, 4, 5, 2, 24, 0F, 22, SoundEvents.ITEM_ARMOR_EQUIP_GOLD);
	public static final ArmorMaterial ARMOR_WITCH_LEATHER = registerArmorMaterial(LibItemName.BEWITCHED_LEATHER, 1, 2, 3, 1, 24, 0f, 22, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final ArmorMaterial ARMOR_VAMPIRE = registerArmorMaterial(LibItemName.VAMPIRE, 2, 3, 3, 2, 24, 1f, 22, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
	public static final ArmorMaterial ARMOR_COLD_IRON = registerArmorMaterial(LibItemName.COLD_IRON, 2, 4, 5, 2, 24, 0f, 22, SoundEvents.ITEM_ARMOR_EQUIP_IRON);

	public static final ToolMaterial TOOL_ATHAME = EnumHelper.addToolMaterial(LibItemName.RITUAL, 2, 300, 2F, 1.5F, 30);
	public static final ToolMaterial TOOL_SILVER = EnumHelper.addToolMaterial(LibItemName.SILVER, 1, 200, 10.0F, 2.5F, 24);
	public static final ToolMaterial TOOL_COLD_IRON = EnumHelper.addToolMaterial(LibItemName.COLD_IRON, 2, 850, 7.0F, 3.0F, 16);

	private static ArmorMaterial registerArmorMaterial(String name, int boots, int leggings, int chestplate, int helmet, int durability, float toughness, int enchantability, SoundEvent sound) {
		return EnumHelper.addArmorMaterial(name, LibMod.MOD_ID + ":" + name, durability, new int[] {
				boots, leggings, chestplate, helmet
		}, enchantability, sound, toughness);
	}
}

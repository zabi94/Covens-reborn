package com.covens.client.core;

import java.util.ArrayList;

import com.covens.api.hotbar.IHotbarAction;
import com.covens.client.ResourceLocations;
import com.covens.client.core.colorhandlers.BrewColorHandler;
import com.covens.client.core.colorhandlers.ColorPropertyHandler;
import com.covens.client.core.colorhandlers.CrystalBallSmokeColorHandler;
import com.covens.client.core.colorhandlers.GlyphColorHandler;
import com.covens.client.core.colorhandlers.MossColorHandler;
import com.covens.client.core.colorhandlers.SpellPageColorHandler;
import com.covens.client.core.colorhandlers.WitchfireColorHandler;
import com.covens.client.core.event.GirdleOfTheWoodedHUD;
import com.covens.client.core.event.MimicEventHandler;
import com.covens.client.core.event.MiscEventHandler;
import com.covens.client.core.event.RenderingHacks;
import com.covens.client.core.event.WerewolfEventHandler;
import com.covens.client.core.hud.BloodViewerHUD;
import com.covens.client.core.hud.EnergyHUD;
import com.covens.client.core.hud.ExtraBarButtonsHUD;
import com.covens.client.core.hud.MoonHUD;
import com.covens.client.core.hud.SelectedActionHUD;
import com.covens.client.core.hud.VampireBloodBarHUD;
import com.covens.client.fx.ParticleF;
import com.covens.client.gui.GuiTarots;
import com.covens.client.handler.ItemCandleColorHandler;
import com.covens.client.handler.Keybinds;
import com.covens.client.render.entity.layer.MantleLayer;
import com.covens.client.render.entity.renderer.EmptyRenderer;
import com.covens.client.render.entity.renderer.RenderBatSwarm;
import com.covens.client.render.entity.renderer.RenderBrewArrow;
import com.covens.client.render.entity.renderer.RenderBrewBottle;
import com.covens.client.render.entity.renderer.RenderBroom;
import com.covens.client.render.entity.renderer.RenderLizard;
import com.covens.client.render.entity.renderer.RenderOwl;
import com.covens.client.render.entity.renderer.RenderRaven;
import com.covens.client.render.entity.renderer.RenderSnake;
import com.covens.client.render.entity.renderer.RenderToad;
import com.covens.client.render.entity.renderer.SpellRenderer;
import com.covens.client.render.tile.TileRenderCauldron;
import com.covens.client.render.tile.TileRenderGemBowl;
import com.covens.client.render.tile.TileRenderPlacedItem;
import com.covens.common.Covens;
import com.covens.common.block.ModBlocks;
import com.covens.common.block.misc.BlockWitchFire;
import com.covens.common.content.tarot.TarotHandler.TarotInfo;
import com.covens.common.core.net.GuiHandler;
import com.covens.common.core.proxy.ISidedProxy;
import com.covens.common.core.statics.ModFluids;
import com.covens.common.entity.EntityAoE;
import com.covens.common.entity.EntityBatSwarm;
import com.covens.common.entity.EntityBrew;
import com.covens.common.entity.EntityBrewArrow;
import com.covens.common.entity.EntityFlyingBroom;
import com.covens.common.entity.EntityLingeringBrew;
import com.covens.common.entity.EntitySpellCarrier;
import com.covens.common.entity.living.animals.EntityLizard;
import com.covens.common.entity.living.animals.EntityOwl;
import com.covens.common.entity.living.animals.EntityRaven;
import com.covens.common.entity.living.animals.EntitySnake;
import com.covens.common.entity.living.animals.EntityToad;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibGui;
import com.covens.common.tile.tiles.TileEntityCauldron;
import com.covens.common.tile.tiles.TileEntityGemBowl;
import com.covens.common.tile.tiles.TileEntityPlacedItem;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import zabi.minecraft.minerva.client.blockmodels.AllDefaultModelStateMapper;
import zabi.minecraft.minerva.client.hud.HudController;

/**
 * This class was created by <Arekkuusu> on 26/02/2017. It's distributed as part
 * of Covens under the MIT license.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements ISidedProxy {

	@SubscribeEvent
	public static void registerItemModels(ModelRegistryEvent event) {
		unifyVariants(ModBlocks.ember_grass);
		unifyVariants(ModBlocks.leaves_cypress);
		unifyVariants(ModBlocks.leaves_elder);
		unifyVariants(ModBlocks.leaves_juniper);
		unifyVariants(ModBlocks.leaves_yew);
		unifyVariants(ModBlocks.gem_bowl);
		unifyVariants(ModBlocks.moonbell);
	}

	@SubscribeEvent
	public static void stitchEventPre(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(ResourceLocations.STEAM);
		event.getMap().registerSprite(ResourceLocations.BEE);
		event.getMap().registerSprite(ResourceLocations.FLAME);
		event.getMap().registerSprite(ResourceLocations.GRAY_WATER);
		event.getMap().registerSprite(ResourceLocations.BAT);
		event.getMap().registerSprite(ModFluids.HONEY.getFlowing());
		event.getMap().registerSprite(ModFluids.HONEY.getStill());
		event.getMap().registerSprite(ModFluids.MUNDANE_OIL.getFlowing());
		event.getMap().registerSprite(ModFluids.MUNDANE_OIL.getStill());
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		this.registerRenders();
		HudController.registerNewComponent(new BloodViewerHUD());
		HudController.registerNewComponent(new EnergyHUD());
		HudController.registerNewComponent(new MoonHUD());
		HudController.registerNewComponent(new SelectedActionHUD());
		HudController.registerNewComponent(ExtraBarButtonsHUD.INSTANCE);
		HudController.registerNewComponent(new VampireBloodBarHUD());
		MinecraftForge.EVENT_BUS.register(new GirdleOfTheWoodedHUD());
		MinecraftForge.EVENT_BUS.register(new WerewolfEventHandler());
		MinecraftForge.EVENT_BUS.register(new RenderingHacks());
		MinecraftForge.EVENT_BUS.register(new MiscEventHandler(Minecraft.getMinecraft()));
	}

	private static void unifyVariants(Block block) {
		ModelLoader.setCustomStateMapper(block, new AllDefaultModelStateMapper(block));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		Keybinds.registerKeys();
		this.addModelLayers();
		this.registerColorHandlers();
		NetworkRegistry.INSTANCE.registerGuiHandler(Covens.instance, new GuiHandler());
	}

	private void addModelLayers() {
		RenderPlayer steveRenderPlayer = Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default");
		RenderPlayer alexRenderPlayer = Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim");
		steveRenderPlayer.addLayer(new MantleLayer(steveRenderPlayer.getMainModel()));
		alexRenderPlayer.addLayer(new MantleLayer(alexRenderPlayer.getMainModel()));
	}

	private void registerColorHandlers() {
		// Block Colors
		BlockColors blocks = Minecraft.getMinecraft().getBlockColors();
		blocks.registerBlockColorHandler(ColorPropertyHandler.INSTANCE, ModBlocks.candle_medium, ModBlocks.candle_small, ModBlocks.candle_medium_lit, ModBlocks.candle_small_lit, ModBlocks.lantern, ModBlocks.revealing_lantern);
		blocks.registerBlockColorHandler(new WitchfireColorHandler(), ModBlocks.witchfire);
		blocks.registerBlockColorHandler(new GlyphColorHandler(), ModBlocks.ritual_glyphs);
		blocks.registerBlockColorHandler(new MossColorHandler(), ModBlocks.spanish_moss, ModBlocks.spanish_moss_end);
		blocks.registerBlockColorHandler(new CrystalBallSmokeColorHandler(), ModBlocks.crystal_ball);
		// Item Colors
		ItemColors items = Minecraft.getMinecraft().getItemColors();
		items.registerItemColorHandler(new ItemCandleColorHandler(), Item.getItemFromBlock(ModBlocks.candle_medium), Item.getItemFromBlock(ModBlocks.candle_small));
		items.registerItemColorHandler(new SpellPageColorHandler(), ModItems.spell_page);
		items.registerItemColorHandler(new BrewColorHandler(), ModItems.brew_phial_drink, ModItems.brew_phial_linger, ModItems.brew_phial_splash, ModItems.brew_arrow);
		items.registerItemColorHandler(ColorPropertyHandler.INSTANCE, Item.getItemFromBlock(ModBlocks.lantern), Item.getItemFromBlock(ModBlocks.revealing_lantern));
	}

	@Override
	public void spawnParticle(ParticleF particleF, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... args) {
		if (this.doParticle()) {
			Minecraft.getMinecraft().effectRenderer.addEffect(particleF.newInstance(x, y, z, xSpeed, ySpeed, zSpeed, args));
		}
	}

	private boolean doParticle() {
		float chance = 1F;
		if (Minecraft.getMinecraft().gameSettings.particleSetting == 1) {
			chance = 0.6F;
		} else if (Minecraft.getMinecraft().gameSettings.particleSetting == 2) {
			chance = 0.2F;
		}
		return (chance == 1F) || (Math.random() < chance);
	}

	private void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellCarrier.class, SpellRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBroom.class, RenderBroom::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBatSwarm.class, RenderBatSwarm::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBrewArrow.class, RenderBrewArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBrew.class, RenderBrewBottle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLingeringBrew.class, EmptyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAoE.class, EmptyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityOwl.class, RenderOwl::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySnake.class, RenderSnake::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLizard.class, RenderLizard::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityToad.class, RenderToad::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRaven.class, RenderRaven::new);
		MinecraftForge.EVENT_BUS.register(new RenderBatSwarm.PlayerHider());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCauldron.class, new TileRenderCauldron());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGemBowl.class, new TileRenderGemBowl());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlacedItem.class, new TileRenderPlacedItem());
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return Minecraft.getMinecraft().gameSettings.fancyGraphics;
	}

	@Override
	public void handleTarot(ArrayList<TarotInfo> tarots) {
		EntityPlayer p = Minecraft.getMinecraft().player;
		p.openGui(Covens.instance, LibGui.TAROT.ordinal(), p.world, (int) p.posX, (int) p.posY, (int) p.posZ);
		if (Minecraft.getMinecraft().currentScreen instanceof GuiTarots) {
			Minecraft.getMinecraft().addScheduledTask(() -> ((GuiTarots) Minecraft.getMinecraft().currentScreen).loadData(tarots));
		} else {
			GuiTarots gt = new GuiTarots();
			Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(gt));
			Minecraft.getMinecraft().addScheduledTask(() -> gt.loadData(tarots));
		}
	}

	@Override
	public void loadActionsClient(ArrayList<IHotbarAction> actions, EntityPlayer player) {
		if ((Minecraft.getMinecraft().player != null) && (Minecraft.getMinecraft().player.getUniqueID() == player.getUniqueID())) {
			ExtraBarButtonsHUD.INSTANCE.setList(actions);
		}
	}

	@Override
	public boolean isPlayerInEndFire() {
		Minecraft mc = Minecraft.getMinecraft();
		IBlockState ibs = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ));
		boolean res = ((ibs.getBlock() == ModBlocks.witchfire) && (ibs.getValue(BlockWitchFire.TYPE) == BlockWitchFire.EnumFireType.ENDFIRE));
		return res;
	}

	@Override
	public void stopMimicking(EntityPlayer p) {
		MimicEventHandler.stopMimicking((AbstractClientPlayer) p);
	}
}

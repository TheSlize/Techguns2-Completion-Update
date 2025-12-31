package techguns;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.blocks.BlockFluidAcid;
import techguns.init.ITGInitializer;

public class TGFluids implements ITGInitializer {
	
	/**
	 * Same as FluidRegistry.WATER
	 */
	public static Fluid WATER;
	/**
	 * Same as FluidRegistry.LAVA
	 */
	public static Fluid LAVA;
	
	public static Fluid MILK;
	
	public static ArrayList<Fluid> oils = new ArrayList<>();
	
	public static ArrayList<Fluid> worldspawn_oils = new ArrayList<>();
	public static Fluid OIL_WORLDSPAWN = null;
	
	public static ArrayList<Fluid> fuels = new ArrayList<>();
	
	public static Fluid LIQUID_REDSTONE;
	
	public static Fluid LIQUID_COAL;

	public static Fluid ACID;
	
	public static Fluid LIQUID_ENDER;

	public static Fluid LIQUID_TRITIUM;
	
	public static Fluid BIOFUEL;
	public static Fluid STEAM;

	public static Block BLOCK_FLUID_ACID;
	public static Block BLOCK_FLUID_MILK;
	public static Block BLOCK_STEAM;
	public static boolean addedMilk;
	public static boolean addedAcid;
	public static boolean addedSteam;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		WATER=FluidRegistry.WATER;
		LAVA=FluidRegistry.LAVA;

		addedSteam = FluidRegistry.registerFluid(new Fluid("steam", new ResourceLocation(Techguns.MODID, "blocks/steam_still"), new ResourceLocation(Techguns.MODID, "blocks/steam_flow"))
				.setGaseous(true)
				.setDensity(-800)
				.setViscosity(100)
				.setTemperature(373)
				.setColor(0xFFFFFFFF));
		STEAM = FluidRegistry.getFluid("steam");

		if (addedSteam) {
			BLOCK_STEAM = new BlockFluidClassic(STEAM, Material.WATER).setRegistryName(Techguns.MODID, "steam").setTranslationKey(Techguns.MODID + ".steam");
			STEAM.setBlock(BLOCK_STEAM);
		}
		
		addedAcid = FluidRegistry.registerFluid(new Fluid("creeper_acid", new ResourceLocation(Techguns.MODID, "blocks/acid_still"), new ResourceLocation(Techguns.MODID, "blocks/acid_flow")).setGaseous(false).setLuminosity(0).setUnlocalizedName("creeperAcid").setDensity(100));
		ACID = FluidRegistry.getFluid("creeper_acid");
		if(addedAcid) {
			
			BLOCK_FLUID_ACID = new BlockFluidAcid(ACID,Material.WATER).setRegistryName(new ResourceLocation(Techguns.MODID, "block_creeper_acid"))
					.setTranslationKey(Techguns.MODID+".block_creeper_acid").setCreativeTab(Techguns.tabTechgun);
		}
		
		addedMilk = FluidRegistry.registerFluid(new Fluid("milk", new ResourceLocation(Techguns.MODID, "blocks/milk_still"), new ResourceLocation(Techguns.MODID, "blocks/milk_flow")).setUnlocalizedName("milk"));
		MILK = FluidRegistry.getFluid("milk");
		if(addedMilk) {			
			BLOCK_FLUID_MILK = new BlockFluidAcid(MILK,Material.WATER).setRegistryName(new ResourceLocation(Techguns.MODID, "block_milk"))
					.setTranslationKey(Techguns.MODID+".block_milk").setCreativeTab(Techguns.tabTechgun);
		}
	}

	public void registerItems(RegistryEvent.Register<Item> event) {
		if(BLOCK_FLUID_ACID != null) {
			ItemBlock ib = new ItemBlock(BLOCK_FLUID_ACID);
			assert BLOCK_FLUID_ACID.getRegistryName() != null;
			ib.setRegistryName(BLOCK_FLUID_ACID.getRegistryName());
			event.getRegistry().register(ib);
		}
		if(BLOCK_FLUID_MILK != null) {
			ItemBlock ib = new ItemBlock(BLOCK_FLUID_MILK);
			assert BLOCK_FLUID_MILK.getRegistryName() != null;
			ib.setRegistryName(BLOCK_FLUID_MILK.getRegistryName());
			event.getRegistry().register(ib);
		}
		if(BLOCK_STEAM != null) {
			ItemBlock ib = new ItemBlock(BLOCK_STEAM);
			assert BLOCK_STEAM.getRegistryName() != null;
			ib.setRegistryName(BLOCK_STEAM.getRegistryName());
			event.getRegistry().register(ib);
		}
	}
	
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		if(BLOCK_FLUID_ACID != null) {
			event.getRegistry().register(BLOCK_FLUID_ACID);
			FluidRegistry.addBucketForFluid(ACID);
		}
		if(BLOCK_FLUID_MILK != null) {
			event.getRegistry().register(BLOCK_FLUID_MILK);
			FluidRegistry.addBucketForFluid(MILK);
		}
		if (BLOCK_STEAM != null) {
			event.getRegistry().register(BLOCK_STEAM);
			FluidRegistry.addBucketForFluid(STEAM);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		if(BLOCK_FLUID_ACID != null) {
			Techguns.proxy.registerFluidModelsForFluidBlock(BLOCK_FLUID_ACID);
		}
		if(BLOCK_FLUID_MILK != null) {
			Techguns.proxy.registerFluidModelsForFluidBlock(BLOCK_FLUID_MILK);
		}
		if(BLOCK_STEAM != null) {
			Techguns.proxy.registerFluidModelsForFluidBlock(BLOCK_STEAM);
		}
	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	public static void recipeInit() {
			for (String fluidName: TGConfig.fluidListFuel) {
				Fluid f = FluidRegistry.getFluid(fluidName);
				if(f!=null) {
					fuels.add(f);
				}
			}

			for (String fluidName: TGConfig.fluidListOil) {
				Fluid f = FluidRegistry.getFluid(fluidName);
				if (f != null) {
					oils.add(f);
				}
			}
			
			for (String fluidName: TGConfig.fluidListOilWorldspawn) {
				Fluid f = FluidRegistry.getFluid(fluidName);
				if (f != null) {
					worldspawn_oils.add(f);
					if(f.getBlock()!=null && OIL_WORLDSPAWN==null){
						OIL_WORLDSPAWN=f;
					}
				}
			}

			LIQUID_REDSTONE = FluidRegistry.getFluid("redstone");
			if (LIQUID_REDSTONE==null){
				LIQUID_REDSTONE = LAVA;
			}
			
			LIQUID_COAL = FluidRegistry.getFluid("coal");
			if (LIQUID_COAL==null){

				LIQUID_COAL = WATER;
				
			}
			
			LIQUID_ENDER = FluidRegistry.getFluid("ender");
			if (LIQUID_ENDER==null){
				LIQUID_ENDER = LAVA;
			}

			LIQUID_TRITIUM = FluidRegistry.getFluid("deuterium");
			if (LIQUID_TRITIUM==null){
				LIQUID_TRITIUM = LAVA;
			}
			
			BIOFUEL = FluidRegistry.getFluid("biofuel");
			if (BIOFUEL==null){
				BIOFUEL = LAVA;
			}
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}
}

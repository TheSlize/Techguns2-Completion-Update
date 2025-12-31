package techguns.server;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import techguns.CommonProxy;
import techguns.events.TechgunsGuiHandler.GuiHandlerRegister;
import techguns.gui.containers.*;
import techguns.tileentities.*;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy extends CommonProxy {

	protected GuiHandlerRegister guihandler = new GuiHandlerRegister();
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		guihandler.addEntry(CamoBenchTileEnt.class, CamoBenchContainer::new);
		guihandler.addEntry(RepairBenchTileEnt.class, RepairBenchContainer::new);
		guihandler.addEntry(AmmoPressTileEnt.class, AmmoPressContainer::new);
		guihandler.addEntry(MetalPressTileEnt.class, MetalPressContainer::new);
		guihandler.addEntry(ChemLabTileEnt.class, ChemLabContainer::new);
		guihandler.addEntry(TurretTileEnt.class, TurretContainer::new);
		guihandler.addEntry(FabricatorTileEntMaster.class, FabricatorContainer::new);
		guihandler.addEntry(ChargingStationTileEnt.class, ChargingStationContainer::new);
		guihandler.addEntry(ReactionChamberTileEntMaster.class, ReactionChamberContainer::new);
		guihandler.addEntry(DungeonScannerTileEnt.class, DungeonScannerContainer::new);
		guihandler.addEntry(DungeonGeneratorTileEnt.class, DungeonGeneratorContainer::new);
		guihandler.addEntry(Door3x3TileEntity.class, Door3x3Container::new);
		guihandler.addEntry(ExplosiveChargeTileEnt.class, ExplosiveChargeContainer::new);
		guihandler.addEntry(ExplosiveChargeAdvTileEnt.class, ExplosiveChargeContainer::new);
		guihandler.addEntry(OreDrillTileEntMaster.class, OreDrillContainer::new);
		guihandler.addEntry(BlastFurnaceTileEnt.class, BlastFurnaceContainer::new);
		guihandler.addEntry(GrinderTileEnt.class, GrinderContainer::new);
		guihandler.addEntry(UpgradeBenchTileEnt.class, UpgradeBenchContainer::new);
	}
	
	@Override
	public GuiHandlerRegister getGuihandlers() {
		return guihandler;
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	
	
}

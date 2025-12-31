package techguns;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import techguns.init.ITGInitializer;

public class TGPermissions implements ITGInitializer {
	public static final String ALLOW_UNSAFE_MODE = Techguns.MODID+".allowunsafemode";

	@Override
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Override
	public void init(FMLInitializationEvent event) {
		PermissionAPI.registerNode(ALLOW_UNSAFE_MODE, DefaultPermissionLevel.ALL, "Allow player to use the unsafe mode of weapons.");
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}
	
	public boolean canUseUnsafeMode(EntityPlayer ply) {
        return (TGConfig.limitUnsafeModeToOP || !PermissionAPI.hasPermission(ply, TGPermissions.ALLOW_UNSAFE_MODE)) && (!TGConfig.limitUnsafeModeToOP || !isPlayerOp(ply));
	}
	
	public static boolean isPlayerOp(EntityPlayer player){
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null;
	}
}

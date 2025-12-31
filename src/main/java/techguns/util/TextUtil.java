package techguns.util;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.Techguns;

public class TextUtil {
	public static String trans(String key, Object... args){
        return FMLCommonHandler.instance().getSide().isClient() ? ClientOnly.format(key, args) : key;
	}
	/**
	 * Trans with prefixing MODID.
	 */
	public static String transTG(String key, Object... args){
        return FMLCommonHandler.instance().getSide().isClient() ? ClientOnly.format(Techguns.MODID+"."+key, args) : key;
	}

    @SideOnly(Side.CLIENT)
    private static class ClientOnly {
        private static String format(String s, Object... args) {
            return I18n.format(s, args);
        }
    }

    public static String[] resolveKeyArray(String s, Object... args) {
        return trans(s, args).split("\\$");
    }
}

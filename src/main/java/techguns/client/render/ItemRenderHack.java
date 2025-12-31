package techguns.client.render;

import java.util.HashMap;

import net.minecraft.item.Item;
import techguns.api.render.IItemRenderer;

public class ItemRenderHack {

	protected static HashMap<Item, IItemRenderer> renderRegistry = new HashMap<>();
	
	public static void registerItemRenderer(Item item ,IItemRenderer renderer){
		renderRegistry.put(item, renderer);
		item.setTileEntityItemStackRenderer(TGItemStackRenderer.INSTANCE);
	}
	
	
	public static IItemRenderer getRendererForItem(Item item){
		return renderRegistry.get(item);
	}
}

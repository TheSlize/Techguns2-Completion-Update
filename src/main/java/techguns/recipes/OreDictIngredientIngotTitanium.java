package techguns.recipes;

import com.google.gson.JsonObject;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import techguns.TGConfig;

public class OreDictIngredientIngotTitanium implements IIngredientFactory {

	@Override
	public Ingredient parse(JsonContext context, JsonObject json) {
		if(OreDictionary.doesOreNameExist("ingotTitanium") && TGConfig.disableItems.addOreDicts) {
			return new OreIngredient("ingotTitanium");
		}
		return new OreIngredient("ingotTitaniumTG");
	}

}

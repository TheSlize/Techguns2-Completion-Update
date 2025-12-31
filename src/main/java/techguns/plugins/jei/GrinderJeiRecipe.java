package techguns.plugins.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import techguns.tileentities.GrinderTileEnt;
import techguns.tileentities.operation.GrinderRecipes;
import techguns.util.TextUtil;

public class GrinderJeiRecipe extends BasicRecipeWrapper {

    public final GrinderRecipes.GrinderRecipe recipe;

    public GrinderJeiRecipe(GrinderRecipes.GrinderRecipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }

    @Override
    protected int getRFperTick() {
        return GrinderTileEnt.POWER_PER_TICK;
    }

    public static List<GrinderJeiRecipe> getRecipes() {
        List<GrinderJeiRecipe> recipes = new ArrayList<>();
        GrinderRecipes.recipes.forEach(r -> recipes.add(new GrinderJeiRecipe(r)));
        return recipes;
    }

    @Override
    public void getIngredients(@NotNull IIngredients ingredients) {
        List<List<ItemStack>> inputs = recipe.getItemInputs();
        if (inputs != null && !inputs.isEmpty()) {
            ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        }

        List<List<ItemStack>> outputs = recipe.getItemOutputs();
        if (outputs != null && !outputs.isEmpty()) {
            if (outputs.size() > 9) outputs = outputs.subList(0, 9);
            ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
        }
    }
}
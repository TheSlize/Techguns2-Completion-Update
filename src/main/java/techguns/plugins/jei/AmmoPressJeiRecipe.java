package techguns.plugins.jei;

import java.util.ArrayList;
import java.util.List;

import techguns.tileentities.operation.AmmoPressBuildPlans;

public class AmmoPressJeiRecipe extends BasicRecipeWrapper {

    public AmmoPressJeiRecipe(AmmoPressBuildPlans.AmmoPressMachineRecipe recipe) {
        super(recipe);
    }

    @Override
    protected int getDuration() {
        return 100;
    }

    @Override
    protected int getRFperTick() {
        return 5;
    }

    public static List<AmmoPressJeiRecipe> getRecipes() {
        List<AmmoPressJeiRecipe> recipes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            recipes.add(new AmmoPressJeiRecipe((AmmoPressBuildPlans.AmmoPressMachineRecipe) AmmoPressBuildPlans.getRecipeForType(i)));
        }
        return recipes;
    }
}

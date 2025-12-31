package techguns.plugins.jei;

import org.jetbrains.annotations.NotNull;
import techguns.gui.TGBaseGui;
import techguns.tileentities.operation.BlastFurnaceRecipes;
import techguns.tileentities.operation.BlastFurnaceRecipes.BlastFurnaceRecipe;
import techguns.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class BlastFurnaceJeiRecipe extends BasicRecipeWrapper {

	BlastFurnaceRecipe recipe;
	
	public BlastFurnaceJeiRecipe(BlastFurnaceRecipe recipe) {
		super(recipe);
		this.recipe=recipe;
	}

	@Override
	protected int getDuration() {
		return recipe.duration;
	}

	@Override
	protected int getRFperTick() {
		return recipe.powerPerTick;
	}

	public static List<BlastFurnaceJeiRecipe> getRecipes() {

        List<BlastFurnaceJeiRecipe> recipes = new ArrayList<>();
		
		ArrayList<BlastFurnaceRecipe> m_recipes = BlastFurnaceRecipes.getRecipes();
		m_recipes.forEach(r -> recipes.add(new BlastFurnaceJeiRecipe(r)));
		
		return recipes;
	}
	
	@Override
	public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {

		if (TGBaseGui.isInRect(mouseX, mouseY, 22+BasicRecipeCategory.JEI_OFFSET_X, 54+BasicRecipeCategory.JEI_OFFSET_Y, 102, 12)) {
			
			List<String> tooltip = new ArrayList<>();
			tooltip.add(TextUtil.transTG("tooltip.duration")+": "+recipe.duration+" "+TextUtil.transTG("tooltip.ticks"));
			
			return tooltip;
		} else {
			return super.getTooltipStrings(mouseX, mouseY);
		}
		
	}
}

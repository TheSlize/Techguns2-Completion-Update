package techguns.plugins.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;
import techguns.TGItems;
import techguns.tileentities.FabricatorTileEntMaster;
import techguns.tileentities.operation.FabricatorRecipe;
import techguns.util.TextUtil;

public class FabricatorJeiRecipe extends BasicRecipeWrapper {

    public final FabricatorRecipe recipe;

    private static final int BLUEPRINT_X = 105 - 4;
    private static final int BLUEPRINT_Y = 77 - 4;

    public FabricatorJeiRecipe(FabricatorRecipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }

	@Override
	protected int getRFperTick() {
		return FabricatorTileEntMaster.powerPerTick;
	}

    public static List<FabricatorJeiRecipe> getRecipes() {
        List<FabricatorJeiRecipe> recipes = new ArrayList<>();
        ArrayList<FabricatorRecipe> m_recipes = FabricatorRecipe.getRecipes();
        m_recipes.forEach(r -> recipes.add(new FabricatorJeiRecipe(r)));
        return recipes;
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if(this.recipe.outputItem != TGItems.CYBERNETIC_PARTS) return;

        minecraft.getTextureManager().bindTexture(FabricatorJeiRecipeCategory.TEXTURE);
        Gui.drawModalRectWithCustomSizedTexture(BLUEPRINT_X, BLUEPRINT_Y, 240, 240, 16, 16, 256, 256);
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        final int x = 105 - 3;
        final int y = 77 - 3;

        if (this.recipe != null
                && this.recipe.outputItem != null
                && !this.recipe.outputItem.isEmpty()
                && this.recipe.outputItem == TGItems.CYBERNETIC_PARTS
                && mouseX >= x && mouseX < (x + 16)
                && mouseY >= y && mouseY < (y + 16)) {

            return Arrays.asList(TextUtil.resolveKeyArray("techguns.tooltip.needsBlueprint"));
        }

        return super.getTooltipStrings(mouseX, mouseY);
    }
}

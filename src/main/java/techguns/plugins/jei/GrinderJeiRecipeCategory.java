package techguns.plugins.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import techguns.Techguns;
import techguns.gui.AmmoPressGui;
import techguns.tileentities.operation.GrinderRecipes;
import techguns.util.TextUtil;

public class GrinderJeiRecipeCategory extends BasicRecipeCategory<GrinderJeiRecipe> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Techguns.MODID, "textures/gui/jei/grinder.png");

    private static final int BG_U = 4;
    private static final int BG_V = 4;

    private static final int SLOT_IN = 0;

    private static final int SLOT_IN_X = 18 - BG_U;
    private static final int SLOT_IN_Y = 35 - BG_V;

    private static final int PROGRESS_X = 51 - BG_U;
    private static final int PROGRESS_Y = 29 - BG_V;

    public static final int OUT_START_X = 118 - BG_U;
    public static final int OUT_START_Y = 17 - BG_V;

    private final IDrawableAnimated progress;

    public GrinderJeiRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, TEXTURE, "grinder", TGJeiPlugin.GRINDER);
        this.background = guiHelper.createDrawable(TEXTURE, 4, 4, 181, 75);

        IDrawableStatic progress_static = guiHelper.createDrawable(TEXTURE, 204, 0, 52, 31);
        this.progress = guiHelper.createAnimatedDrawable(progress_static, 100, IDrawableAnimated.StartDirection.LEFT, false);
        this.powerbar_static = guiHelper.createDrawable(AmmoPressGui.texture, 251, 1, 4, 48);
        this.powerbar = guiHelper.createAnimatedDrawable(powerbar_static, 100, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull GrinderJeiRecipe recipeWrapper, @NotNull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(SLOT_IN, true, SLOT_IN_X, SLOT_IN_Y);

        int idx = 1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                guiItemStacks.init(idx, false, OUT_START_X + col * 18, OUT_START_Y + row * 18);
                idx++;
            }
        }

        guiItemStacks.set(ingredients);

        if (recipeWrapper.recipe instanceof GrinderRecipes.GrinderRecipeChance) {
            final GrinderRecipes.GrinderRecipeChance rc = (GrinderRecipes.GrinderRecipeChance) recipeWrapper.recipe;

            guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
                if (input) return; // only outputs
                if (ingredient.isEmpty()) return;

                int outIdx = slotIndex - 1; // outputs are slots 1..9
                if (outIdx < 0) return;

                double[] chances = rc.getChances();
                if (chances == null || outIdx >= chances.length) return;

                double chance = chances[outIdx];
                if (chance >= 1.0d) return;

                tooltip.add(TextUtil.trans("techguns.jei.chance") + ": " + (int) Math.round(chance * 100.0d) + "%");
            });
        }
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        this.powerbar.draw(minecraft, 8 - BG_U, 17 - BG_V);
        this.progress.draw(minecraft, PROGRESS_X, PROGRESS_Y);
    }
}

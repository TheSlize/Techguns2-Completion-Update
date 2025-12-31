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
import techguns.TGItems;
import techguns.Techguns;

public class FabricatorJeiRecipeCategory extends BasicRecipeCategory<FabricatorJeiRecipe> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Techguns.MODID, "textures/gui/jei/fabricator.png");

    private static final int BG_U = 3;
    private static final int BG_V = 3;

    private static final int SLOT_IN1 = 0;
    private static final int SLOT_IN2 = 1;
    private static final int SLOT_IN3 = 2;
    private static final int SLOT_IN4 = 3;
    private static final int SLOT_OUT = 4;

    private static final int SLOT_IN1_X = 16 - BG_U;
    private static final int SLOT_IN1_Y = 8 - BG_V;

    private static final int SLOT_IN2_X = 62 - BG_U;
    private static final int SLOT_IN2_Y = 8 - BG_V;

    private static final int SLOT_IN3_X = 108 - BG_U;
    private static final int SLOT_IN3_Y = 8 - BG_V;

    private static final int SLOT_IN4_X = 154 - BG_U;
    private static final int SLOT_IN4_Y = 8 - BG_V;

    private static final int SLOT_OUT_X = 85 - BG_U;
    private static final int SLOT_OUT_Y = 76 - BG_V;

    private static final int PROGRESS_X = 24 - BG_U;
    private static final int PROGRESS_Y = 26 - BG_V;

    private final IDrawableAnimated progress;

    public FabricatorJeiRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, TEXTURE, "fabricator", TGJeiPlugin.FABRICATOR);

        this.background = guiHelper.createDrawable(TEXTURE, 3, 3, 175, 94);

        IDrawableStatic progress_static = guiHelper.createDrawable(TEXTURE, 0, 207, 140, 49);
        this.progress = guiHelper.createAnimatedDrawable(progress_static, 100, IDrawableAnimated.StartDirection.TOP, false);
        this.powerbar_static = guiHelper.createDrawable(TEXTURE, 251, 1, 4, 83);
        this.powerbar = guiHelper.createAnimatedDrawable(powerbar_static, 100, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull FabricatorJeiRecipe recipeWrapper, @NotNull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(SLOT_IN1, true, SLOT_IN1_X, SLOT_IN1_Y);
        guiItemStacks.init(SLOT_IN2, true, SLOT_IN2_X, SLOT_IN2_Y);
        guiItemStacks.init(SLOT_IN3, true, SLOT_IN3_X, SLOT_IN3_Y);
        guiItemStacks.init(SLOT_IN4, true, SLOT_IN4_X, SLOT_IN4_Y);

        guiItemStacks.init(SLOT_OUT, false, SLOT_OUT_X, SLOT_OUT_Y);

        guiItemStacks.set(ingredients);
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        this.powerbar.draw(minecraft, 6 - BG_U, 9 - BG_V);
        this.progress.draw(minecraft, PROGRESS_X, PROGRESS_Y);
    }
}

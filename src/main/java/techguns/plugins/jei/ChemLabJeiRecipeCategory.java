package techguns.plugins.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import techguns.TGItems;
import techguns.Techguns;
import techguns.gui.AmmoPressGui;

public class ChemLabJeiRecipeCategory extends BasicRecipeCategory<ChemLabJeiRecipe> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Techguns.MODID, "textures/gui/jei/chem_lab.png");

    private static final int BG_U = 4;
    private static final int BG_V = 10;

    private static final int SLOT_IN1 = 0;
    private static final int SLOT_IN2 = 1;
    private static final int SLOT_IN3 = 2;
    private static final int SLOT_OUT = 3;
    private static final int SLOT_STEAM = 4;

    private static final int SLOT_IN1_X = 39 - BG_U;
    private static final int SLOT_IN1_Y = 15 - BG_V;

    private static final int SLOT_IN2_X = 39 - BG_U;
    private static final int SLOT_IN2_Y = 36 - BG_V;

    private static final int SLOT_IN3_X = 39 - BG_U;
    private static final int SLOT_IN3_Y = 57 - BG_V;

    private static final int SLOT_OUT_X = 135 - BG_U;
    private static final int SLOT_OUT_Y = 36 - BG_V;

    private static final int SLOT_STEAM_X = 135 - BG_U;
    private static final int SLOT_STEAM_Y = 57 - BG_V;

    public ChemLabJeiRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, TEXTURE, "chemlab", TGJeiPlugin.CHEM_LAB);
        this.background = guiHelper.createDrawable(TEXTURE, 4, 10, 174, 69);

        this.powerbar_static = guiHelper.createDrawable(AmmoPressGui.texture, 251, 1, 4, 48);
        this.powerbar = guiHelper.createAnimatedDrawable(powerbar_static, 100, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull ChemLabJeiRecipe recipeWrapper, @NotNull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(SLOT_IN1, true, SLOT_IN1_X, SLOT_IN1_Y);
        guiItemStacks.init(SLOT_IN2, true, SLOT_IN2_X, SLOT_IN2_Y);
        guiItemStacks.init(SLOT_IN3, true, SLOT_IN3_X, SLOT_IN3_Y);

        guiItemStacks.init(SLOT_OUT, false, SLOT_OUT_X, SLOT_OUT_Y);

        if (recipeWrapper.recipe.reqSteamUpgrade) {
            guiItemStacks.init(SLOT_STEAM, true, SLOT_STEAM_X, SLOT_STEAM_Y);
        }

        guiItemStacks.set(ingredients);

        if (recipeWrapper.recipe.reqSteamUpgrade) {
            ItemStack s = new ItemStack(TGItems.SHARED_ITEM, 1, TGItems.MACHINE_UPGRADE_STEAM.getItemDamage());
            guiItemStacks.set(SLOT_STEAM, s);
        }
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        this.powerbar.draw(minecraft, 8 - BG_U, 17 - BG_V);
    }
}

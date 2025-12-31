package techguns.plugins.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import techguns.gui.MetalPressGui;
import techguns.gui.TGBaseGui;
import techguns.tileentities.MetalPressTileEnt;
import techguns.tileentities.operation.MetalPressRecipes;
import techguns.util.TextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static techguns.plugins.jei.ChemLabJeiRecipe.drawSlotHighlight;

public class MetalPressJeiRecipe extends BasicRecipeWrapper {

    public static final int TEX_W = 256;
    public static final int TEX_H = 256;

    public static final int BG_U = 4;
    public static final int BG_V = 4;

    public static final int TANK_X = 96 - BG_U;
    public static final int TANK_Y = 11 - BG_V;
    public static final int TANK_W = 12;
    public static final int TANK_H = 52;

    public static final int GAUGE_X = 92 - BG_U;
    public static final int GAUGE_Y = 68 - BG_V;
    public static final int GAUGE_W = 18;
    public static final int GAUGE_H = 18;

    public static final int TANK_EMPTY_U = 128;
    public static final int TANK_EMPTY_V = 0;
    public static final int TANK_OVERLAY_U = 116;
    public static final int TANK_OVERLAY_V = 0;

    public final MetalPressRecipes.MetalPressRecipe recipe;

    public MetalPressJeiRecipe(MetalPressRecipes.MetalPressRecipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }

    @Override
    protected int getRFperTick() {
        return MetalPressTileEnt.POWER_PER_TICK;
    }

    public static List<MetalPressJeiRecipe> getRecipes() {
        List<MetalPressJeiRecipe> recipes = new ArrayList<>();
        ArrayList<MetalPressRecipes.MetalPressRecipe> m_recipes = MetalPressRecipes.getRecipes();
        m_recipes.forEach(r -> recipes.add(new MetalPressJeiRecipe(r)));
        return recipes;
    }

    @Override
    public void getIngredients(@NotNull IIngredients ingredients) {
        super.getIngredients(ingredients);

        if (this.recipe.requiresSteam()) {
            FluidStack steam = FluidRegistry.getFluidStack("steam", this.recipe.steamCost);
            if (steam != null && steam.amount > 0) {
                List<List<FluidStack>> in = new ArrayList<>();
                in.add(Collections.singletonList(steam));
                ingredients.setInputLists(VanillaTypes.FLUID, in);
            }
        }
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (!this.recipe.requiresSteam()) return;

        minecraft.getTextureManager().bindTexture(MetalPressJeiRecipeCategory.TEXTURE);
        Gui.drawModalRectWithCustomSizedTexture(TANK_X, TANK_Y, TANK_EMPTY_U, TANK_EMPTY_V, TANK_W, TANK_H, TEX_W, TEX_H);

        FluidStack steam = FluidRegistry.getFluidStack("steam", this.recipe.steamCost);
        if (steam != null && steam.amount > 0) {
            drawFluid(minecraft, steam, steam.amount);
        }

        minecraft.getTextureManager().bindTexture(MetalPressJeiRecipeCategory.TEXTURE);
        Gui.drawModalRectWithCustomSizedTexture(TANK_X, TANK_Y, TANK_OVERLAY_U, TANK_OVERLAY_V, TANK_W, TANK_H, TEX_W, TEX_H);

        minecraft.getTextureManager().bindTexture(MetalPressGui.gauge_texture);
        int pressure = this.recipe.requiredPressure;
        if (pressure < 0) pressure = 0;
        if (pressure > 12) pressure = 12;
        Gui.drawModalRectWithCustomSizedTexture(GAUGE_X, GAUGE_Y, 0, pressure * 18, GAUGE_W, GAUGE_H, 256, 256);

        if (TGBaseGui.isInRect(mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H)) {
            drawSlotHighlight(TANK_X, TANK_Y, TANK_W, TANK_H);
        }
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (this.recipe.requiresSteam()) {
            if (TGBaseGui.isInRect(mouseX, mouseY, TANK_X, TANK_Y, TANK_W, TANK_H)) {
                List<String> tooltip = new ArrayList<>();
                FluidStack steam = FluidRegistry.getFluidStack("steam", this.recipe.steamCost);
                if (steam != null && steam.amount > 0) {
                    tooltip.add(steam.getFluid().getLocalizedName(steam));
                    tooltip.add(steam.amount + "mB");
                } else {
                    tooltip.add("Steam");
                    tooltip.add(this.recipe.steamCost + "mB");
                }
                return tooltip;
            }

            if (TGBaseGui.isInRect(mouseX, mouseY, GAUGE_X, GAUGE_Y, GAUGE_W, GAUGE_H)) {
                List<String> tooltip = new ArrayList<>();
                tooltip.add(TextUtil.trans("techguns.gui.pressure") + ": " + this.recipe.requiredPressure);
                return tooltip;
            }
        }

        return super.getTooltipStrings(mouseX, mouseY);
    }

    private static void drawFluid(Minecraft mc, FluidStack fluidStack, int amount) {
        if (fluidStack == null || fluidStack.amount <= 0) return;

        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) return;

        ResourceLocation still = fluid.getStill(fluidStack);
        if (still == null) return;

        TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(still.toString());

        int scaled = amount * MetalPressJeiRecipe.TANK_H / 16000;
        if (scaled <= 0) return;
        if (scaled > MetalPressJeiRecipe.TANK_H) scaled = MetalPressJeiRecipe.TANK_H;

        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1f, 1f, 1f, 1f);

        int yStart = MetalPressJeiRecipe.TANK_Y + (MetalPressJeiRecipe.TANK_H - scaled);
        int remainingY = scaled;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();

        while (remainingY > 0) {
            int tileH = Math.min(16, remainingY);
            int xPos = MetalPressJeiRecipe.TANK_X;
            int remainingX = MetalPressJeiRecipe.TANK_W;

            while (remainingX > 0) {
                int tileW = remainingX;

                float u0 = sprite.getMinU();
                float u1 = sprite.getInterpolatedU(tileW);
                float v0 = sprite.getInterpolatedV(16 - tileH);
                float v1 = sprite.getMaxV();

                bb.begin(7, DefaultVertexFormats.POSITION_TEX);
                bb.pos(xPos, yStart + tileH, 0).tex(u0, v1).endVertex();
                bb.pos(xPos + tileW, yStart + tileH, 0).tex(u1, v1).endVertex();
                bb.pos(xPos + tileW, yStart, 0).tex(u1, v0).endVertex();
                bb.pos(xPos, yStart, 0).tex(u0, v0).endVertex();
                tess.draw();

                xPos += tileW;
                remainingX -= tileW;
            }

            yStart += tileH;
            remainingY -= tileH;
        }

        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
}

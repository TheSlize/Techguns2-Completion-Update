package techguns.plugins.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import techguns.gui.TGBaseGui;
import techguns.tileentities.operation.ChemLabRecipes;
import techguns.util.TextUtil;

public class ChemLabJeiRecipe extends BasicRecipeWrapper {

    public static final int BG_U = 4;
    public static final int BG_V = 10;

    public static final int TANK_IN_X = 20 - BG_U;
    public static final int TANK_OUT_X = 160 - BG_U;
    public static final int TANK_Y = 16 - BG_V;

    public static final int TANK_W = 12;
    public static final int TANK_H = 52;

    public static final int TANK_INNER_X_OFF = 1;
    public static final int TANK_INNER_Y_OFF = 1;
    public static final int TANK_INNER_W = 10;
    public static final int TANK_INNER_H = 50;

    public static final int TANK_OVERLAY_U = 202;
    public static final int TANK_OVERLAY_V = 0;

    public static final int STEAM_ICON_X = 135 - BG_U;
    public static final int STEAM_ICON_Y = 57 - BG_V;
    public static final int STEAM_ICON_W = 18;
    public static final int STEAM_ICON_H = 18;
    public static final int STEAM_ICON_U = 202;
    public static final int STEAM_ICON_V = 57;

    public final ChemLabRecipes.ChemLabRecipe recipe;

    public ChemLabJeiRecipe(ChemLabRecipes.ChemLabRecipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }

    @Override
    protected int getDuration() {
        return 100;
    }

    @Override
    protected int getRFperTick() {
        return recipe.powerPerTick;
    }

    public static List<ChemLabJeiRecipe> getRecipes() {
        List<ChemLabJeiRecipe> recipes = new ArrayList<>();
        ArrayList<ChemLabRecipes.ChemLabRecipe> m_recipes = ChemLabRecipes.getRecipes();
        m_recipes.forEach(r -> recipes.add(new ChemLabJeiRecipe(r)));
        return recipes;
    }

    @Override
    public void getIngredients(@NotNull IIngredients ingredients) {
        List<List<ItemStack>> itemInputs = recipe.getItemInputs();
        if (itemInputs != null && !itemInputs.isEmpty()) {
            List<List<ItemStack>> filtered = new ArrayList<>();
            for (List<ItemStack> l : itemInputs) {
                if (l == null || l.isEmpty()) continue;
                List<ItemStack> inner = new ArrayList<>();
                for (ItemStack s : l) {
                    if (s != null && !s.isEmpty()) inner.add(s);
                }
                if (!inner.isEmpty()) filtered.add(inner);
            }
            if (!filtered.isEmpty()) {
                ingredients.setInputLists(VanillaTypes.ITEM, filtered);
            }
        }

        List<List<ItemStack>> itemOutputs = recipe.getItemOutputs();
        if (itemOutputs != null && !itemOutputs.isEmpty()) {
            List<List<ItemStack>> filtered = new ArrayList<>();
            for (List<ItemStack> l : itemOutputs) {
                if (l == null || l.isEmpty()) continue;
                List<ItemStack> inner = new ArrayList<>();
                for (ItemStack s : l) {
                    if (s != null && !s.isEmpty()) inner.add(s);
                }
                if (!inner.isEmpty()) filtered.add(inner);
            }
            if (!filtered.isEmpty()) {
                ingredients.setOutputLists(VanillaTypes.ITEM, filtered);
            }
        }

        if (recipe.fluidIn != null && recipe.amounts != null && recipe.amounts.length > 3 && recipe.amounts[3] > 0) {
            FluidStack in = recipe.fluidIn.copy();
            in.amount = recipe.amounts[3];
            List<List<FluidStack>> fluidsIn = new ArrayList<>();
            fluidsIn.add(Collections.singletonList(in));
            ingredients.setInputLists(VanillaTypes.FLUID, fluidsIn);
        }

        if (recipe.fluidOutput != null && recipe.fluidOutput.amount > 0) {
            FluidStack out = recipe.fluidOutput.copy();
            List<List<FluidStack>> fluidsOut = new ArrayList<>();
            fluidsOut.add(Collections.singletonList(out));
            ingredients.setOutputLists(VanillaTypes.FLUID, fluidsOut);
        }
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (recipe.fluidIn != null && recipe.amounts != null && recipe.amounts.length > 3 && recipe.amounts[3] > 0) {
            drawFluidInTank(minecraft, recipe.fluidIn, recipe.amounts[3], TANK_IN_X, TANK_Y, 8000);
        }
        if (recipe.fluidOutput != null && recipe.fluidOutput.amount > 0) {
            drawFluidInTank(minecraft, recipe.fluidOutput, recipe.fluidOutput.amount, TANK_OUT_X, TANK_Y, 16000);
        }

        minecraft.getTextureManager().bindTexture(ChemLabJeiRecipeCategory.TEXTURE);
        Gui.drawModalRectWithCustomSizedTexture(TANK_IN_X, TANK_Y, TANK_OVERLAY_U, TANK_OVERLAY_V, TANK_W, TANK_H, 256, 256);
        Gui.drawModalRectWithCustomSizedTexture(TANK_OUT_X, TANK_Y, TANK_OVERLAY_U, TANK_OVERLAY_V, TANK_W, TANK_H, 256, 256);

        float prog = (Minecraft.getSystemTime() % (getDuration() * 50L)) / (getDuration() * 50.0f);
        drawProgressSegments(minecraft, prog);

        if (recipe.reqSteamUpgrade) {
            minecraft.getTextureManager().bindTexture(ChemLabJeiRecipeCategory.TEXTURE);
            Gui.drawModalRectWithCustomSizedTexture(STEAM_ICON_X, STEAM_ICON_Y, STEAM_ICON_U, STEAM_ICON_V, STEAM_ICON_W, STEAM_ICON_H, 256, 256);
        }

        if (TGBaseGui.isInRect(mouseX, mouseY, TANK_IN_X, TANK_Y, TANK_W, TANK_H)) {
            drawSlotHighlight(TANK_IN_X, TANK_Y, TANK_W, TANK_H);
        }
        if (TGBaseGui.isInRect(mouseX, mouseY, TANK_OUT_X, TANK_Y, TANK_W, TANK_H)) {
            drawSlotHighlight(TANK_OUT_X, TANK_Y, TANK_W, TANK_H);
        }
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (TGBaseGui.isInRect(mouseX, mouseY, TANK_IN_X, TANK_Y, TANK_W, TANK_H)) {
            List<String> tooltip = new ArrayList<>();
            if (recipe.fluidIn != null && recipe.amounts != null && recipe.amounts.length > 3 && recipe.amounts[3] > 0) {
                FluidStack in = recipe.fluidIn.copy();
                in.amount = recipe.amounts[3];
                tooltip.add(in.getFluid().getLocalizedName(in));
                tooltip.add(in.amount + "mB");
            } else {
                tooltip.add(TextUtil.trans("techguns.gui.empty"));
                tooltip.add("0mB");
            }
            return tooltip;
        }

        if (TGBaseGui.isInRect(mouseX, mouseY, TANK_OUT_X, TANK_Y, TANK_W, TANK_H)) {
            List<String> tooltip = new ArrayList<>();
            if (recipe.fluidOutput != null && recipe.fluidOutput.amount > 0) {
                FluidStack out = recipe.fluidOutput.copy();
                tooltip.add(out.getFluid().getLocalizedName(out));
                tooltip.add(out.amount + "mB");
            } else {
                tooltip.add(TextUtil.trans("techguns.gui.empty"));
                tooltip.add("0mB");
            }
            return tooltip;
        }

        return super.getTooltipStrings(mouseX, mouseY);
    }

    private static void drawFluidInTank(Minecraft mc, FluidStack fluidStack, int amount, int x, int y, int capacity) {
        if (fluidStack == null || amount <= 0 || fluidStack.getFluid() == null) return;

        ResourceLocation still = fluidStack.getFluid().getStill(fluidStack);
        if (still == null) return;

        TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(still.toString());

        int scaled = amount * TANK_INNER_H / capacity;
        if (scaled <= 0) return;
        if (scaled > TANK_INNER_H) scaled = TANK_INNER_H;

        drawFluidSprite(mc, sprite,
                x + TANK_INNER_X_OFF,
                y + TANK_INNER_Y_OFF + (TANK_INNER_H - scaled),
                TANK_INNER_W,
                scaled);
    }

    private static void drawFluidSprite(Minecraft mc, TextureAtlasSprite sprite, int x, int y, int w, int h) {
        if (sprite == null || w <= 0 || h <= 0) return;

        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1f, 1f, 1f, 1f);

        int remainingY = h;
        int yStart = y;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();

        while (remainingY > 0) {
            int tileH = Math.min(16, remainingY);
            int xPos = x;
            int remainingX = w;

            while (remainingX > 0) {
                int tileW = Math.min(16, remainingX);

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

    private static void drawProgressSegments(Minecraft minecraft, float prog) {
        minecraft.getTextureManager().bindTexture(ChemLabJeiRecipeCategory.TEXTURE);

        int baseX = 60 - BG_U;
        int baseY = 21 - BG_V;

        if (prog > 0.0f) {
            float p = Math.min(prog, 0.2f) * 5.0f;
            int fullH = 27;
            int drawH = (int) (fullH * p + 0.5f);
            if (drawH > 0) {
                Gui.drawModalRectWithCustomSizedTexture(baseX, (25 - BG_V) + (fullH - drawH), 185, 225 + 4 + (fullH - drawH), 11, drawH, 256, 256);
            }
        }

        if (prog > 0.2f) {
            float p = (Math.min(prog, 0.4f) - 0.2f) * 5.0f;

            int seg1W = 26;
            int seg2W = 3;
            int totalW = seg1W + seg2W;
            int filled = (int) (totalW * p + 0.5f);

            if (filled > 0) {
                int drawW1 = Math.min(filled, seg1W);
                Gui.drawModalRectWithCustomSizedTexture(baseX + 11, baseY, 185 + 11, 225, drawW1, 31, 256, 256);
            }

            if (filled > seg1W) {
                int drawW2 = filled - seg1W;
                Gui.drawModalRectWithCustomSizedTexture(baseX + 37, baseY, 185 + 37, 225, drawW2, 11, 256, 256);
            }
        }

        if (prog > 0.4f) {
            float p = (Math.min(prog, 0.6f) - 0.4f) * 5.0f;
            int fullH = 28;
            int drawH = (int) (fullH * p + 0.5f);
            if (drawH > 0) {
                Gui.drawModalRectWithCustomSizedTexture(baseX + 37, baseY + 3, 185 + 37, 225 + 3, 16, drawH, 256, 256);
            }
        }

        if (prog > 0.6f) {
            float p = (Math.min(prog, 0.8f) - 0.6f) * 5.0f;

            int seg1W = 5;
            int seg2W = 4;
            int seg3W = 4;
            int totalW = seg1W + seg2W + seg3W;
            int filled = (int) (totalW * p + 0.5f);

            if (filled > 0) {
                int drawW1 = Math.min(filled, seg1W);
                Gui.drawModalRectWithCustomSizedTexture(baseX + 53, baseY + 9, 185 + 53, 225 + 9, drawW1, 17, 256, 256);
            }

            if (filled > seg1W) {
                int drawW2 = Math.min(filled - seg1W, seg2W);
                Gui.drawModalRectWithCustomSizedTexture(baseX + 58, baseY + 6, 185 + 58, 225 + 6, drawW2, 8, 256, 256);
            }

            if (filled > seg1W + seg2W) {
                int drawW3 = filled - seg1W - seg2W;
                Gui.drawModalRectWithCustomSizedTexture(baseX + 62, baseY + 6, 185 + 62, 225 + 6, drawW3, 3, 256, 256);
            }
        }

        if (prog > 0.8f) {
            float p = (Math.min(prog, 1.0f) - 0.8f) * 5.0f;
            int fullH = 22;
            int drawH = (int) (fullH * p + 0.5f);
            if (drawH > 0) {
                Gui.drawModalRectWithCustomSizedTexture(baseX + 58, baseY + 9, 185 + 58, 225 + 9, 13, drawH, 256, 256);
            }
        }
    }

    public static void drawSlotHighlight(int x, int y, int w, int h) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.colorMask(true, true, true, false);
        Gui.drawRect(x, y, x + w, y + h, 0x80FFFFFF);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
    }
}

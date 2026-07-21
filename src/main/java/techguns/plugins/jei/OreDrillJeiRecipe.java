package techguns.plugins.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.ingredients.IIngredientRegistry;
import org.jetbrains.annotations.NotNull;
import techguns.TGConfig;
import techguns.TGOreClusters.OreCluster;
import techguns.TGOreClusters.OreClusterWeightedEntry;
import techguns.*;
import techguns.blocks.EnumOreClusterType;
import techguns.gui.PoweredTileEntGui;
import techguns.gui.TGBaseGui;
import techguns.util.TextUtil;

public class OreDrillJeiRecipe extends BasicRecipeWrapper {

    protected final OreDrillMachineRecipe mr;

    public OreDrillJeiRecipe(OreDrillMachineRecipe recipe) {
        super(recipe);
        mr = recipe;
    }

    public static List<OreDrillJeiRecipe> getRecipes(IIngredientRegistry registry) {
        List<OreDrillJeiRecipe> recipes = new ArrayList<>();

        for (EnumOreClusterType type : EnumOreClusterType.values()) {
            OreCluster cluster = Techguns.orecluster.getClusterForType(type);

            ArrayList<OreClusterWeightedEntry> entries = cluster.getOreEntries();
            for (OreClusterWeightedEntry we : entries) {
                recipes.add(new OreDrillJeiRecipe(new OreDrillMachineRecipe(we, type, registry.getFuels())));
            }
        }

        return recipes;
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (TGBaseGui.isInRect(mouseX, mouseY, 9 + BasicRecipeCategory.JEI_OFFSET_X, 18 + BasicRecipeCategory.JEI_OFFSET_Y, 4, 48)) {

            List<String> tooltip = new ArrayList<>();

            //calculate
            OreCluster cluster = Techguns.orecluster.getClusterForType(mr.getType());

            //if clustersize, mininglevel are 1, formula results in 4.5 * ...
            double ore_per_hour = 4.5 * cluster.getMultiplier_amount() * TGConfig.oreDrills.oreDrillMultiplierOres;
            int powerTick = (int) (8 * ore_per_hour * cluster.getMultiplier_power() * TGConfig.oreDrills.oreDrillMultiplierPower);

            tooltip.add(TextUtil.transTG("oredrill.powerperore") + ": ");
            tooltip.add((int) ((powerTick * 20 * 60 * 60) / ore_per_hour) + " " + PoweredTileEntGui.POWER_UNIT);

            return tooltip;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected int getRFperTick() {
        return 0;
    }

    public float getChance() {
        return mr.getChance();
    }
}

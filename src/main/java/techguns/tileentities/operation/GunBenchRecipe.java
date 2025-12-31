package techguns.tileentities.operation;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class GunBenchRecipe implements IMachineRecipe {

    public static ArrayList<GunBenchRecipe> recipes = new ArrayList<>();
    public String recName;
    public ItemStack[] inputs;
    public ItemStack output;

    public GunBenchRecipe(ItemStack[] inputs, ItemStack output) {
        if (inputs.length != 5) {
            throw new IllegalArgumentException("GunBenchRecipe must have exactly 5 inputs.");
        }
        this.inputs = inputs;
        this.output = output;
    }

    public GunBenchRecipe(String name, ItemStack[] inputs, ItemStack output) {
        if (inputs.length != 5) {
            throw new IllegalArgumentException("GunBenchRecipe must have exactly 5 inputs.");
        }
        this.recName = name;
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    public List<List<ItemStack>> getItemInputs() {
        ArrayList<List<ItemStack>> ret = new ArrayList<>();
        for(ItemStack stack: inputs){
            ArrayList<ItemStack> item = new ArrayList<>();
            item.add(stack);
            ret.add(item);
        }
        return ret;

    }

    @Override
    public List<List<ItemStack>> getItemOutputs() {
        ArrayList<List<ItemStack>> ret = new ArrayList<>();
        ArrayList<ItemStack> output = new ArrayList<>();
        output.add(this.output);
        ret.add(output);
        return ret;
    }

    public static GunBenchRecipe addRecipe(ItemStack[] inputs, ItemStack output) {
        GunBenchRecipe rec = new GunBenchRecipe(inputs, output);
        recipes.add(rec);
        return rec;
    }

    public static GunBenchRecipe addRecipe(String name, ItemStack[] inputs, ItemStack output) {
        GunBenchRecipe rec = new GunBenchRecipe(name, inputs, output);
        recipes.add(rec);
        return rec;
    }

    public static ArrayList<GunBenchRecipe> getRecipes() {
        return recipes;
    }

    public static GunBenchRecipe getRecipeFor(List<ItemStack> inputStacks) {
        outer:
        for (GunBenchRecipe recipe : recipes) {
            for (int i = 0; i < 5; i++) {
                ItemStack required = recipe.inputs[i];
                ItemStack actual = inputStacks.get(i);
                if (required.isEmpty()) {
                    if (!actual.isEmpty()) continue outer;
                    continue;
                }
                if (!ItemStack.areItemsEqual(required, actual)) continue outer;
                if (!ItemStack.areItemStackTagsEqual(required, actual)) continue outer;
                if (required.getCount() > actual.getCount()) continue outer;
            }
            return recipe;
        }
        return null;
    }

    public ItemStack getOutput() {
        return output.copy();
    }
}


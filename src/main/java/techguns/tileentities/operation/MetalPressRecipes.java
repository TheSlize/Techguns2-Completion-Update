package techguns.tileentities.operation;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techguns.util.ItemStackOreDict;

public class MetalPressRecipes {

	private static ArrayList<MetalPressRecipe> recipes = new ArrayList<>();

	public static void addRecipe(ItemStack input1, ItemStack input2, ItemStack output, boolean swap){
		recipes.add(new MetalPressRecipe(new ItemStackOreDict(input1),new ItemStackOreDict(input2),swap, output));
	}
	public static void addRecipe(String input1, String input2, ItemStack output,boolean swap){
		recipes.add(new MetalPressRecipe(new ItemStackOreDict(input1),new ItemStackOreDict(input2),swap, output));
	}
	public static void addRecipe(String input1, ItemStack input2, ItemStack output,boolean swap){
		recipes.add(new MetalPressRecipe(new ItemStackOreDict(input1),new ItemStackOreDict(input2),swap, output));
	}
	public static void addRecipe(ItemStack input1, String input2, ItemStack output,boolean swap){
		recipes.add(new MetalPressRecipe(new ItemStackOreDict(input1),new ItemStackOreDict(input2),swap, output));
	}

	public static void addRecipe(ItemStack input1, ItemStack input2, ItemStack output, boolean swap, int steamCost, int requiredPressure){
		recipes.add(new MetalPressRecipe(new ItemStackOreDict(input1), new ItemStackOreDict(input2), swap, output, steamCost, requiredPressure));
	}
	public static void addRecipe(String input1, String input2, ItemStack output, boolean swap, int steamCost, int requiredPressure){
		recipes.add(new MetalPressRecipe(new ItemStackOreDict(input1), new ItemStackOreDict(input2), swap, output, steamCost, requiredPressure));
	}
	public static void addRecipe(ItemStackOreDict input1, ItemStackOreDict input2, ItemStack output, boolean swap, int steamCost, int requiredPressure){
		recipes.add(new MetalPressRecipe(input1, input2, swap, output, steamCost, requiredPressure));
	}

	public static ItemStack getOutputFor(ItemStack slot1, ItemStack slot2){
		MetalPressRecipe recipe = getRecipeForInputs(slot1, slot2);
		return recipe != null ? recipe.output : ItemStack.EMPTY;
	}

	public static MetalPressRecipe getRecipeForInputs(ItemStack slot1, ItemStack slot2){
		for (MetalPressRecipe recipe : recipes) {
			if (recipe.isValidInput(slot1, slot2)) {
				return recipe;
			}
		}
		return null;
	}

	public static boolean hasRecipeUsing(ItemStack item){
		for (MetalPressRecipe recipe : recipes) {
			if (recipe.isItemPartOfRecipe(item)) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<MetalPressRecipe> getRecipesUsing(ItemStack input){
		ArrayList<MetalPressRecipe> ret = new ArrayList<>();

		for (MetalPressRecipe r : recipes){

			if (r.isItemPartOfRecipe(input)){
				ret.add(r);
			}

		}

		return ret;
	}

	public static MetalPressRecipe getRecipesFor(ItemStack output){
		for (MetalPressRecipe r: recipes){
			if (r.isOutputFor(output)){
				return r;
			}
		}
		return null;
	}

	public static ArrayList<MetalPressRecipe> getRecipes() {
		return recipes;
	}
	public static int getTotalPower(int recipe){
		return 20*getTotaltime(recipe);
	}
	public static int getTotaltime(int recipe){
		return 100;
	}

	public static class MetalPressRecipe implements IMachineRecipe {
		public final ItemStackOreDict slot1;
		public final ItemStackOreDict slot2;
		public final int input1Count;
		public final int input2Count;
		public final boolean allowSwap;
		public final ItemStack output;
		public final int steamCost;
		public final int requiredPressure;

		public boolean isOutputFor(ItemStack item){
			return OreDictionary.itemMatches(item, output, true);
		}

		public boolean isItemPartOfRecipe(ItemStack item){
			return this.slot1.isEqualWithOreDict(item) || this.slot2.isEqualWithOreDict(item);
		}

		public MetalPressRecipe(ItemStackOreDict slot1, ItemStackOreDict slot2,
								boolean allowSwap, ItemStack output) {
			this(slot1, slot2, allowSwap, output, 0, 0);
		}

		public MetalPressRecipe(ItemStackOreDict slot1, ItemStackOreDict slot2,
								boolean allowSwap, ItemStack output, int steamCost, int requiredPressure) {
			this.slot1 = slot1;
			this.slot2 = slot2;
			this.allowSwap = allowSwap;
			this.output = output;
			this.input1Count = slot1.stackSize;
			this.input2Count = slot2.stackSize;
			this.steamCost = Math.max(0, steamCost);
			this.requiredPressure = Math.max(0, requiredPressure);
		}

		public boolean requiresSteam() {
			return this.steamCost > 0 && this.requiredPressure > 0;
		}

		//Returns if this 2 itemstacks are a valid input for this recipe;

		public boolean isValidInput(ItemStack slot1, ItemStack slot2) {
			return (this.slot1.isEqualWithOreDict(slot1) && slot1.getCount() >= input1Count &&
					this.slot2.isEqualWithOreDict(slot2) && slot2.getCount() >= input2Count)
					|| (allowSwap && this.slot1.isEqualWithOreDict(slot2) && slot2.getCount() >= input1Count &&
					this.slot2.isEqualWithOreDict(slot1) && slot1.getCount() >= input2Count);
		}

		public ArrayList<ItemStack> getInputs(int slot){
			ItemStackOreDict stack;
			if(slot==1){
				stack = slot1;
			} else {
				stack = slot2;
			}
			return stack.getItemStacks();
		}

		@Override
		public List<List<ItemStack>> getItemInputs() {
			List<List<ItemStack>> inputs = new ArrayList<>();

			inputs.add(this.slot1.getItemStacks());
			inputs.add(this.slot2.getItemStacks());

			return inputs;
		}

		@Override
		public List<List<ItemStack>> getItemOutputs() {
			List<List<ItemStack>> outputs = new ArrayList<>();

			ArrayList<ItemStack> output = new ArrayList<>();
			output.add(this.output);
			outputs.add(output);
			return outputs;
		}
		
		
	}
}


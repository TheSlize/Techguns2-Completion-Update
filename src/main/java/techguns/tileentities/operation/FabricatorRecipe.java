package techguns.tileentities.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import techguns.TGItems;
import techguns.tileentities.FabricatorTileEntMaster;
import techguns.util.I18nUtil;
import techguns.util.ItemStackOreDict;
import techguns.TGConfig;

public class FabricatorRecipe implements IMachineRecipe{

	public static ArrayList<ItemStackOreDict> items_wireslot = new ArrayList<>();

	public static ArrayList<ItemStackOreDict> items_powderslot = new ArrayList<>();

	public static ArrayList<ItemStackOreDict> items_plateslot = new ArrayList<>();


	public static ItemStackOreDict copperWires;
	public static ItemStackOreDict goldWires;

	public static ItemStackOreDict redstone = new ItemStackOreDict("dustRedstone",1);

	public static ItemStackOreDict plastic;
	public static ItemStackOreDict circuit_basic;
	public static ItemStackOreDict circuit_elite;

	public static ItemStackOreDict mechanicalPartsT3 = new ItemStackOreDict(TGItems.MECHANICAL_PARTS_CARBON,1);
	public static ItemStackOreDict carbonFibers = new ItemStackOreDict(TGItems.CARBON_FIBERS,1);

	public static ItemStackOreDict ingotHellish = new ItemStackOreDict(TGItems.INGOT_HELLISH,1);

	public static ItemStackOreDict carbonPlate;
	public static ItemStackOreDict titaniumPlate;
	public static ItemStackOreDict leadPlate = new ItemStackOreDict("plateLead",1);
	private static int recId = -1;
	public String recName;



	static{
		if(TGConfig.addOreDicts) {
			copperWires = new ItemStackOreDict("wireCopper",1);
			goldWires = new ItemStackOreDict("wireGold",1);
			plastic = new ItemStackOreDict("sheetPlastic",1);
			circuit_basic = new ItemStackOreDict("circuitBasic",1);
			circuit_elite = new ItemStackOreDict("circuitElite",1);
			carbonPlate = new ItemStackOreDict("plateCarbon",1);
			titaniumPlate = new ItemStackOreDict("plateTitanium",1);
		}
		else {
			copperWires = new ItemStackOreDict("wireCopperTG",1);
			goldWires = new ItemStackOreDict("wireGoldTG",1);
			plastic = new ItemStackOreDict("sheetPlasticTG",1);
			circuit_basic = new ItemStackOreDict("circuitBasicTG",1);
			circuit_elite = new ItemStackOreDict("circuitEliteTG",1);
			carbonPlate = new ItemStackOreDict("plateCarbonTG",1);
			titaniumPlate = new ItemStackOreDict("plateTitaniumTG",1);
		}

		items_wireslot.add(copperWires);
		items_wireslot.add(goldWires);
		items_wireslot.add(circuit_basic);
		items_wireslot.add(circuit_elite);
		items_wireslot.add(carbonFibers);

		items_powderslot.add(redstone);
		items_powderslot.add(mechanicalPartsT3);

		items_plateslot.add(plastic);
		items_plateslot.add(carbonPlate);
		items_plateslot.add(titaniumPlate);
		items_plateslot.add(leadPlate);
		items_plateslot.add(ingotHellish);
	}


	public static ArrayList<FabricatorRecipe> recipes = new ArrayList<>();

	/**
	 * NON-STATIC VARIABLES
	 */

	public ItemStackOreDict inputItem;
	public byte amountInput;

	public ItemStackOreDict wireSlot;
	public byte amountWire;

	public ItemStackOreDict powderSlot;
	public byte amountPowder;

	public ItemStackOreDict plateSlot;
	public byte amountPlates;

	public ItemStack outputItem;
	public byte amountOutput;


	public static void addRecipe(ItemStackOreDict inputItem, int amountInput, ItemStackOreDict wireSlot, int amountWire, ItemStackOreDict powderSlot,
			int amountPowder, ItemStackOreDict plateSlot, int amountPlates, ItemStack outputItem, int amountOutput){
		recipes.add(new FabricatorRecipe(inputItem, amountInput, wireSlot, amountWire, powderSlot, amountPowder, plateSlot, amountPlates, outputItem, amountOutput));
	}


	public boolean usesItem(ItemStack itm){
		return this.inputItem.isEqualWithOreDict(itm) || this.wireSlot.isEqualWithOreDict(itm) || this.powderSlot.isEqualWithOreDict(itm) || this.plateSlot.isEqualWithOreDict(itm);
	}

	public String generateName(){
		recId++;
		return "recipe_fabricator_"+recId;
	}


	public FabricatorRecipe(ItemStackOreDict inputItem, int amountInput, ItemStackOreDict wireSlot, int amountWire, ItemStackOreDict powderSlot,
			int amountPowder, ItemStackOreDict plateSlot, int amountPlates, ItemStack outputItem, int amountOutput) {
		this.recName = generateName();
		this.inputItem = inputItem;
		this.amountInput = (byte) amountInput;
		this.wireSlot = wireSlot;
		this.amountWire = (byte) amountWire;
		this.powderSlot = powderSlot;
		this.amountPowder = (byte) amountPowder;
		this.plateSlot = plateSlot;
		this.amountPlates = (byte) amountPlates;
		this.outputItem = outputItem;
		this.amountOutput = (byte) amountOutput;
	}

	private boolean matches(ItemStack input, ItemStack wires, ItemStack powder, ItemStack plate){
		if (this.inputItem.isEqualWithOreDict(input) && input.getCount()>=this.amountInput && ((input.hasTagCompound() && input.getTagCompound().equals(this.inputItem.nbt)) || (!input.hasTagCompound() && this.inputItem.nbt == null))){
			if (this.wireSlot.isEmpty() || (!wires.isEmpty() && this.wireSlot.isEqualWithOreDict(wires) && wires.getCount()>=this.amountWire)){
				if (this.powderSlot.isEmpty() || (!powder.isEmpty() && this.powderSlot.isEqualWithOreDict(powder) && powder.getCount()>=this.amountPowder)){
					return this.plateSlot.isEmpty() || (!plate.isEmpty() && this.plateSlot.isEqualWithOreDict(plate) && plate.getCount() >= this.amountPlates);
				}
			}
		}
		return false;
	}


	public static boolean itemStackInList(ArrayList<ItemStackOreDict> list, ItemStack stack){
		for (ItemStackOreDict orestack : list) {
			if (orestack.isEqualWithOreDict(stack)) {
				return true;
			}
		}
		return false;
	}

	public static RecipeData getRecipeDataFor(ItemStack input, ItemStack wires, ItemStack powder, ItemStack plate, int maxMultiplier, ItemStack outputSlot){
		FabricatorRecipe match=null;

		for (FabricatorRecipe rec : recipes) {
			if (rec.matches(input, wires, powder, plate)) {
				match = rec;
				break;
			}
		}

		if (match!=null && canMerge(outputSlot, match.outputItem, match.amountOutput)){

			//calculate multiplier
			int maxMultiInput = getMaxMulti(input.getCount(), match.amountInput, maxMultiplier);
			int maxMultiWires;
			if (!wires.isEmpty()){
				maxMultiWires = getMaxMulti(wires.getCount(), match.amountWire, maxMultiplier);
			} else {
				maxMultiWires = 8;
			}
			int maxMultiPowder;
			if (!powder.isEmpty()){
				maxMultiPowder = getMaxMulti(powder.getCount(), match.amountPowder, maxMultiplier);
			} else {
				maxMultiPowder = 8;
			}
			int maxMultiPlate = getMaxMulti(plate.getCount(), match.amountPlates, maxMultiplier);
			int maxMultiOutput = getMaxMultiOutput(outputSlot.isEmpty()?0:outputSlot.getCount(), match.amountOutput, maxMultiplier, match.outputItem.getMaxStackSize());

			int multi = getMinArgument(maxMultiInput, maxMultiWires, maxMultiPowder, maxMultiPlate, maxMultiOutput);
            return new RecipeData(match.outputItem, match.amountOutput, match.amountInput, match.amountWire, match.amountPowder, match.amountPlates, multi);
		}


		return null;
	}


	public static FabricatorRecipe searchRecByName(String s){
		for (FabricatorRecipe rec : recipes) {
			if (rec.recName.equals(s)){
				return rec;
			}
		}
		return null;
	}

	private static boolean canMerge(ItemStack content, ItemStack newStack, int newAmount){
		if (content.isEmpty()){
			return true;
		}
		if(content.getItem()==newStack.getItem() && content.getItemDamage()== newStack.getItemDamage() && content.getTagCompound() == newStack.getTagCompound()){
			return content.getCount()+newAmount <= content.getMaxStackSize();
		}
		return false;
	}


	private static int getMaxMulti(int stackSize, int consumption, int maxMulti){
		int mult = maxMulti;

		while(mult*consumption > stackSize){
			mult--;
		}
		return mult;
	}

	private static int getMaxMultiOutput(int stackSize, int amount, int maxMulti, int maxStackSize){
		int mult = maxMulti;

		while((mult*amount)+stackSize > maxStackSize){
			mult--;
		}
		return mult;
	}

	private static int getMinArgument(int... values){
		int min=Integer.MAX_VALUE;
		for (int x : values) {
			if (x < min) {
				min = x;
			}
		}
		return min;
	}


	public static class RecipeData {
		public ItemStack output;
		public byte outputAmount;
		public byte inputConsumption;
		public byte wireConsumption;
		public byte powderConsumption;
		public byte plateConsumption;
		public byte stackMultiplier;
		public RecipeData(ItemStack output, int outputAmount, int inputConsumption, int wireConsumption, int powderConsumption, int plateConsumption, int stackMultiplier) {
			super();
			this.output = output;
			this.outputAmount = (byte) outputAmount;
			this.inputConsumption = (byte) inputConsumption;
			this.wireConsumption = (byte) wireConsumption;
			this.powderConsumption = (byte) powderConsumption;
			this.plateConsumption = (byte) plateConsumption;
			this.stackMultiplier = (byte) stackMultiplier;
		}

		public MachineOperation createOperation(FabricatorTileEntMaster tile) {

			ItemStack stack = tile.input.get().copy();
			stack.setCount(inputConsumption);

			ItemStack wires = tile.wireslot.get().copy();
			if(!wires.isEmpty()) {
				wires.setCount(wireConsumption);
			}

			ItemStack powder = tile.powderslot.get().copy();
			if(!powder.isEmpty()) {
				powder.setCount(powderConsumption);
			}

			ItemStack plates = tile.plateslot.get().copy();
			if (!plates.isEmpty()) {
				plates.setCount(plateConsumption);
			}

			ItemStack out = output.copy();
			out.setCount(outputAmount);

			MachineOperation op = new MachineOperation(out, stack, wires, powder, plates);
			op.setStackMultiplier(stackMultiplier);
			op.setPowerPerTick(tile.getNeededPower());
			return op;
		}
	}

	public static ArrayList<FabricatorRecipe> getRecipes() {
		return recipes;
	}


	@Override
	public List<List<ItemStack>> getItemInputs() {
		ArrayList<List<ItemStack>> ret = new ArrayList<>();
		ret.add(this.inputItem.getItemStacks(this.amountInput));
		ret.add(this.wireSlot.getItemStacks(this.amountWire));
		ret.add(this.powderSlot.getItemStacks(this.amountPowder));
		ret.add(this.plateSlot.getItemStacks(this.amountPlates));
		return ret;

	}

	@Override
	public List<List<ItemStack>> getItemOutputs() {
		ArrayList<List<ItemStack>> ret = new ArrayList<>();
		ArrayList<ItemStack> output = new ArrayList<>();
		output.add(this.outputItem);
		ret.add(output);
		return ret;
	}

	public List<String> print() {
		List<String> list = new ArrayList<>();
		list.add(TextFormatting.YELLOW + this.getLocalizedName());
		list.add(TextFormatting.GOLD + "" + TextFormatting.BOLD + I18nUtil.resolveKey("desc.rec_input"));
		if(inputItem != null) {
			list.add("  " + TextFormatting.GRAY + amountInput + "x " + inputItem.getItemStacks().get(0).getDisplayName());
			list.add("  " + TextFormatting.GRAY + amountWire + "x " + wireSlot.getItemStacks().get(0).getDisplayName());
			list.add("  " + TextFormatting.GRAY + amountPowder + "x " + powderSlot.getItemStacks().get(0).getDisplayName());
			list.add("  " + TextFormatting.GRAY + amountPlates + "x " + plateSlot.getItemStacks().get(0).getDisplayName());
		}
		list.add(TextFormatting.GREEN + "" + TextFormatting.BOLD + I18nUtil.resolveKey("desc.rec_output"));
		if(outputItem != null) list.add("  " + TextFormatting.GRAY + amountOutput + "x " + outputItem.getDisplayName());
		return list;
	}

	public String getLocalizedName() {
        return outputItem.getDisplayName();
	}

	/** Default impl only matches localized name substring, can be extended to include ingredients as well */
	public boolean matchesSearch(String substring) {
		return getLocalizedName().toLowerCase(Locale.US).contains(substring.toLowerCase(Locale.US));
	}

	public ItemStack getIcon() {
		if(outputItem != null) {
			return outputItem;
		}
		return ItemStack.EMPTY;
	}
}

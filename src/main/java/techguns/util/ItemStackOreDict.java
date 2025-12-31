package techguns.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackOreDict {
	
	public String oreDictName;
	public int stackSize;
	public ItemStack item;
	//strict mode, check for damage value
	public boolean strict = false;
	public NBTTagCompound nbt;
	
	public static final ItemStackOreDict EMPTY = new ItemStackOreDict();
	
	public ItemStackOreDict(String oreDictName, int stackSize) {
		this.oreDictName = oreDictName;
		this.stackSize = stackSize;
		this.item=ItemStack.EMPTY;
	}
	public ItemStackOreDict(String oreDictName) {
		this.oreDictName = oreDictName;
		this.stackSize = 1;
		this.item=ItemStack.EMPTY;
	}
	
	private ItemStackOreDict() {
		this.oreDictName=null;
		this.stackSize=1;
		this.item=ItemStack.EMPTY;
	}
	
	public ItemStackOreDict(ItemStack item, int stackSize) {
		this.stackSize = stackSize;
		this.item = item;
		this.oreDictName=null;
		if (item.hasTagCompound()) {
			this.nbt = item.getTagCompound().copy();
		}
	}
	public ItemStackOreDict(ItemStack item) {
		if (item !=null){
			this.stackSize = item.getCount();
			this.item = item.copy();
			this.oreDictName=null;
			if (item.hasTagCompound()) {
				this.nbt = item.getTagCompound();
			}
		} else {
			this.stackSize=0;
			this.item=ItemStack.EMPTY;
			this.oreDictName=null;
		}
	}

	public boolean hasItems() {
		if(isEmpty()) return false;
		
		if(this.oreDictName!=null) {
			return !OreDictionary.getOres(this.oreDictName,true).isEmpty();
		}else {
			return !this.item.isEmpty();
		}
	}

	public ItemStackOreDict setCount(int count){
		this.stackSize = count;
		return this;
	}
	
	/**
	 * @return if using an oredict name
	 */
	public boolean isOreDict() {
		return this.oreDictName!=null;
	}
	
	public ItemStackOreDict setNoStrictMode(){
		this.strict=false;
		return this;
	}
	
	public ItemStackOreDict setStrictMode(){
		this.strict=true;
		return this;
	}
	
	public boolean isEmpty(){
		return this.oreDictName==null && this.item.isEmpty();
	}
	
	public boolean isEqualWithOreDict(ItemStack other){
		if (this.oreDictName==null){
			return OreDictionary.itemMatches(this.item, other, strict); //OreDictionary.itemMatches(this.item, other,strict); //ItemUtil.isItemEqual(this.item, other);
		} else {
			ArrayList<ItemStack> items = this.getItemStacks();

			for (ItemStack itemStack : items) {
				if (OreDictionary.itemMatches(itemStack, other, strict)) {
					return true;
				}
			}
			return false;
		}
	}

	public ArrayList<ItemStack> getItemStacks() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		
		if(this.oreDictName==null){
			if(!this.item.isEmpty()){
				ItemStack stack = this.item;
				if(this.nbt!=null){
					stack.setTagCompound(this.nbt.copy());
				}
				list.add(stack);
			}
		} else {
			OreDictionary.getOres(oreDictName).forEach(s -> {
					ItemStack stack = s.copy();
					stack.setCount(this.stackSize);
					list.add(stack);
				});
		}
		
		return list;
		
	} 	
	
	public ArrayList<ItemStack> getItemStacks(int amountOverride) {
		ArrayList<ItemStack> list = this.getItemStacks();
		list.forEach(s -> s.setCount(amountOverride));
		return list;
	}
	
	@Override
	public String toString() {
		if (this.oreDictName!=null){
			return "ItemStackOreDict:"+this.oreDictName;
		} else if (!this.item.isEmpty()){
			return this.item.toString();
		} else {
			return "EMPTY_ItemStackOreDict";
		}
	} 	
	/**
	 * Return if two itemStack ore dict match eachother
	 * @param other
	 * @return
	 */
	public boolean matches (ItemStackOreDict other){
		if (this.oreDictName!=null && other.oreDictName!=null){
			return this.oreDictName.equals(other.oreDictName);
		} else if (this.oreDictName!=null){
			return this.isEqualWithOreDict(other.item);
		} else if (other.oreDictName!=null){
			return other.isEqualWithOreDict(this.item);
		} else {
			return OreDictionary.itemMatches(this.item, other.item, false);
		}
	}

	public boolean matches(ItemStack other) {
		if (!this.matchesWithoutNBT(other)) {
			return false;
		}
		if (this.nbt != null) {
			return this.nbt.equals(other.getTagCompound());
		}
		return true;
	}

	private boolean matchesWithoutNBT(ItemStack other) {
		if (this.oreDictName != null) {
			return this.isEqualWithOreDict(other);
		} else {
			return OreDictionary.itemMatches(this.item, other, strict);
		}
	}

}


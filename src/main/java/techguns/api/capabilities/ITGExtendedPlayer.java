package techguns.api.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

/**
 * Should only be used on EntityPlayer
 * 
 */
public interface ITGExtendedPlayer extends ITGShooterValues {

	public EntityPlayer getEntity();
	
	public int getFireDelay(EnumHand hand);
	public void setFireDelay(EnumHand hand, int delay);
	public IInventory getTGInventory();
	
	public void saveToNBT(final NBTTagCompound tags);
	public void loadFromNBT(final NBTTagCompound tags);
    public boolean hasFabricatorRecipeUnlocked(ItemStack output);
    public boolean unlockFabricatorRecipe(ItemStack output);
}

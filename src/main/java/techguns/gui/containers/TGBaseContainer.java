package techguns.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import techguns.tileentities.BasicInventoryTileEnt;

public class TGBaseContainer extends Container {
	protected BasicInventoryTileEnt basictile;

	public TGBaseContainer(InventoryPlayer player, BasicInventoryTileEnt ent) {
		this.basictile = ent;
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return basictile.isUseableByPlayer(playerIn);
	}
	
	protected static int getArmorIntFromEntityEquipmentSlot(EntityEquipmentSlot type) {
		switch(type) {
			case CHEST:
				return 1;
			case HEAD:
				return 0;
			case LEGS:
				return 2;
			default:
				return 3;
		}
	}

	public void addDefaultPlayerInventorySlots(InventoryPlayer player){
		playerInv(player, 8, 84);
	}

	/** Standard player inventory with default hotbar offset */
	public void playerInv(InventoryPlayer invPlayer, int playerInvX, int playerInvY) {
		playerInv(invPlayer, playerInvX, playerInvY, playerInvY + 58);
	}

	/** Used to quickly set up the player inventory */
	public void playerInv(InventoryPlayer invPlayer, int playerInvX, int playerInvY, int playerHotbarY) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, playerInvX + j * 18, playerInvY + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(invPlayer, i, playerInvX + i * 18, playerHotbarY));
		}
	}
	
}
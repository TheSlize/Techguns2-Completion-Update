package techguns.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import techguns.TGPackets;
import techguns.gui.widgets.SlotItemHandlerOutput;
import techguns.gui.widgets.SlotMachineInput;
import techguns.gui.widgets.SlotRCFocus;
import techguns.packets.PacketUpdateTileEntTanks;
import techguns.tileentities.ReactionChamberTileEntMaster;
import techguns.tileentities.operation.ItemStackHandlerPlus;

public class ReactionChamberContainer extends BasicMachineContainer {
	ReactionChamberTileEntMaster tile;
	
	public static final int SLOT_OUTPUT_X=127;
	public static final int SLOT_OUTPUT_Y=87;
	protected static final int FIELD_SYNC_ID_INTENSITY = FIELD_SYNC_ID_POWER_STORED+2;
	protected int lastIntensity;
	
	public ReactionChamberContainer(InventoryPlayer player, ReactionChamberTileEntMaster ent) {
		super(player, ent);
		this.tile=ent;
		
		IItemHandler inventory = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);
		
		if (inventory instanceof ItemStackHandlerPlus) {
		ItemStackHandlerPlus handler = (ItemStackHandlerPlus) inventory;
	
			this.addSlotToContainer(new SlotMachineInput(handler, ReactionChamberTileEntMaster.SLOT_INPUT, 37, 87));
		
			this.addSlotToContainer(new SlotRCFocus(handler, ReactionChamberTileEntMaster.SLOT_FOCUS, 82, 35));
			
			this.addSlotToContainer(new SlotItemHandlerOutput(inventory, ReactionChamberTileEntMaster.SLOT_OUTPUT, SLOT_OUTPUT_X, SLOT_OUTPUT_Y));
			this.addSlotToContainer(new SlotItemHandlerOutput(inventory, ReactionChamberTileEntMaster.SLOT_OUTPUT+1, SLOT_OUTPUT_X+18, SLOT_OUTPUT_Y - 18));
			this.addSlotToContainer(new SlotItemHandlerOutput(inventory, ReactionChamberTileEntMaster.SLOT_OUTPUT+2, SLOT_OUTPUT_X + 18, SLOT_OUTPUT_Y));
			this.addSlotToContainer(new SlotItemHandlerOutput(inventory, ReactionChamberTileEntMaster.SLOT_OUTPUT+3, SLOT_OUTPUT_X+18, SLOT_OUTPUT_Y+18));
		}
		
		this.playerInv(player, 8, 156);
	}

	
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (IContainerListener listener : this.listeners) {

			if (this.lastIntensity != this.tile.getIntensity()) {
				listener.sendWindowProperty(this, FIELD_SYNC_ID_INTENSITY, this.tile.getIntensity());
			}


			if (listener instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) listener;
				TGPackets.wrapper.sendTo(new PacketUpdateTileEntTanks(this.tile, this.tile.getPos()), player);
			}


		}
        this.lastIntensity=this.tile.getIntensity();
	}



	@Override
	public void updateProgressBar(int id, int data) {
		if (id == FIELD_SYNC_ID_INTENSITY) {
			this.tile.setIntensity((byte) data);
		} else {
			super.updateProgressBar(id, data);
		}
	}



	@Override
	public ItemStack transferStackInSlot(EntityPlayer ply, int id) {
		
		int HIGHEST_MACHINE_SLOT = ReactionChamberTileEntMaster.SLOT_OUTPUT+ReactionChamberTileEntMaster.OUTPUT_SLOTS_COUNT-1;
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(id);

			if(slot.getHasStack()){
				ItemStack stack1 = slot.getStack();
				stack=stack1.copy();
				if (!stack.isEmpty()){
					
					if (id <= HIGHEST_MACHINE_SLOT){
						//PRESSED IN MACHINE GUI 0-input, 1-focus, 2,3,4,5 -> outputs
						if (!this.mergeItemStack(stack1, HIGHEST_MACHINE_SLOT+1, HIGHEST_MACHINE_SLOT+37, false)) {
							return ItemStack.EMPTY;
						}
						slot.onSlotChange(stack1, stack);
					} else if (id < HIGHEST_MACHINE_SLOT+37){
						
						int validslot = tile.getValidSlotForItemInMachine(stack1);
						if (validslot >=0){
							
							if(!this.mergeItemStack(stack1, validslot, validslot+1, false)){
								return ItemStack.EMPTY;
							}
							slot.onSlotChange(stack1, stack);
							
						} else {
							return ItemStack.EMPTY;
						}
						
						
					}

					if (stack1.getCount() == 0) {
						slot.putStack(ItemStack.EMPTY);
					} else {
						slot.onSlotChanged();
					}

					if (stack1.getCount() == stack.getCount()) {
						return ItemStack.EMPTY;
					}

					slot.onTake(ply, stack1);
				}
			}
		
			return stack;
	}
}

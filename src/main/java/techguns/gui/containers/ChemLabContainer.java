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
import org.jetbrains.annotations.NotNull;
import techguns.TGPackets;
import techguns.gui.widgets.SlotItemHandlerOutput;
import techguns.gui.widgets.SlotMachineInput;
import techguns.gui.widgets.SlotMachineUpgrade;
import techguns.gui.widgets.SlotTG;
import techguns.packets.PacketUpdateTileEntTanks;
import techguns.tileentities.ChemLabTileEnt;
import techguns.tileentities.operation.ItemStackHandlerPlus;

public class ChemLabContainer extends BasicMachineContainer {
	public static final int SLOT_INPUT1_X=40;
	public static final int SLOT_INPUT2_X=40;
	public static final int SLOT_OUTPUT_X=136;
	public static final int SLOTS_ROW1_Y=18;
	public static final int SLOTS_ROW2_Y=41;
	ChemLabTileEnt tile;
	public ChemLabContainer(InventoryPlayer player, ChemLabTileEnt ent) {
		super(player, ent);
		this.tile=ent;

		IItemHandler inventory = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);

		if (inventory instanceof ItemStackHandlerPlus) {
		ItemStackHandlerPlus handler = (ItemStackHandlerPlus) inventory;

			this.addSlotToContainer(new SlotMachineInput(handler,ChemLabTileEnt.SLOT_INPUT1, 40, 16));
			this.addSlotToContainer(new SlotMachineInput(handler, ChemLabTileEnt.SLOT_INPUT2, 40, 37));
			this.addSlotToContainer(new SlotMachineInput(handler, ChemLabTileEnt.SLOT_BOTTLE, 40, 58) {
				@Override
				public String getSlotTexture() {
					return SlotTG.BOTTLESLOT_TEX.toString();
				}
			});


			this.addSlotToContainer(new SlotItemHandlerOutput(inventory, ChemLabTileEnt.SLOT_OUTPUT, 153, 37));
			this.addSlotToContainer(new SlotMachineUpgrade(handler, ChemLabTileEnt.SLOT_UPGRADE, 153, 58));
		}

		this.playerInv(player, 19, 106);
	}



	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!this.tile.getWorld().isRemote) {
			for (IContainerListener listener : this.listeners) {
				if (listener instanceof EntityPlayerMP) {
					EntityPlayerMP player = (EntityPlayerMP) listener;
					TGPackets.wrapper.sendTo(new PacketUpdateTileEntTanks(this.tile, this.tile.getPos()), player);
				}
			}
		}
	}

	@Override
	public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer ply, int id) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(id);

			if(slot.getHasStack()){
				ItemStack stack1 = slot.getStack();
				stack=stack1.copy();
				if (!stack.isEmpty()){

					if (id < 5){
						//PRESSED IN MACHINE GUI
						if (!this.mergeItemStack(stack1, 5, 41, false)) {
							return ItemStack.EMPTY;
						}
						slot.onSlotChange(stack1, stack);
					} else if (id < 41){

						int validslot = tile.getValidSlotForItemInMachine(stack1);
						//System.out.println("put it in slot"+validslot);
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

package techguns.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import techguns.TGItems;
import techguns.api.capabilities.ITGExtendedPlayer;
import techguns.capabilities.TGExtendedPlayerCapProvider;
import techguns.gui.widgets.SlotItemHandlerOutput;
import techguns.gui.widgets.SlotMachineInput;
import techguns.gui.widgets.SlotMachineUpgrade;
import techguns.tileentities.FabricatorTileEntMaster;
import techguns.tileentities.operation.ItemStackHandlerPlus;

public class FabricatorContainer extends BasicMachineContainer {
	protected FabricatorTileEntMaster tile;
	
	public static final int SLOT_INPUT1_X=11;
	public static final int SLOT_WIRES_X=57;
	public static final int SLOT_POWDER_X=103;
	public static final int SLOT_PLATE_X=149;
	
	public static final int SLOTS_ROW1_Y=41;
	
	public static final int SLOT_OUTPUT_X=80;
	public static final int SLOT_OUTPUT_Y=109;
	
	public static final int SLOT_UPGRADE_X=149;
	public static final int SLOT_UPGRADE_Y=109;
	
	public FabricatorContainer(InventoryPlayer player, FabricatorTileEntMaster ent) {
		super(player, ent);
		this.tile=ent;
		
		IItemHandler inventory = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);
		
		if (inventory instanceof ItemStackHandlerPlus) {
		ItemStackHandlerPlus handler = (ItemStackHandlerPlus) inventory;
	
			this.addSlotToContainer(new SlotMachineInput(handler, FabricatorTileEntMaster.SLOT_INPUT1, SLOT_INPUT1_X, SLOTS_ROW1_Y));
			this.addSlotToContainer(new SlotMachineInput(handler,  FabricatorTileEntMaster.SLOT_WIRES, SLOT_WIRES_X, SLOTS_ROW1_Y));
			this.addSlotToContainer(new SlotMachineInput(handler,  FabricatorTileEntMaster.SLOT_POWDER, SLOT_POWDER_X, SLOTS_ROW1_Y));
			this.addSlotToContainer(new SlotMachineInput(handler,  FabricatorTileEntMaster.SLOT_PLATE, SLOT_PLATE_X, SLOTS_ROW1_Y));
		
			this.addSlotToContainer(new SlotItemHandlerOutput(inventory, FabricatorTileEntMaster.SLOT_OUTPUT, SLOT_OUTPUT_X, SLOT_OUTPUT_Y));
			this.addSlotToContainer(new SlotMachineUpgrade(handler,  FabricatorTileEntMaster.SLOT_UPGRADE, SLOT_UPGRADE_X, SLOT_UPGRADE_Y));
		}

		this.playerInv(player, 8, 156);

        final EntityPlayer ep = player.player;
        if (!ep.world.isRemote && ent.currentRecipe != null) {
            ItemStack out = ent.currentRecipe.outputItem;
            if (out != null && !out.isEmpty()
                    && ItemStack.areItemsEqual(out, TGItems.CYBERNETIC_PARTS)
                    && ItemStack.areItemStackTagsEqual(out, TGItems.CYBERNETIC_PARTS)) {

                ITGExtendedPlayer ext = ep.getCapability(TGExtendedPlayerCapProvider.TG_EXTENDED_PLAYER, null);
                boolean ok = ext != null && ext.hasFabricatorRecipeUnlocked(TGItems.CYBERNETIC_PARTS);

                if (!ok) {
                    ent.currentRecipe = null;

                    refundSlotToPlayerOrDrop(ep, ent, FabricatorTileEntMaster.SLOT_INPUT1);
                    refundSlotToPlayerOrDrop(ep, ent, FabricatorTileEntMaster.SLOT_WIRES);
                    refundSlotToPlayerOrDrop(ep, ent, FabricatorTileEntMaster.SLOT_POWDER);
                    refundSlotToPlayerOrDrop(ep, ent, FabricatorTileEntMaster.SLOT_PLATE);

                    ep.inventory.markDirty();
                    ent.markChanged();
                    ent.needUpdate();
                }
            }
        }
	}

    private static void refundSlotToPlayerOrDrop(EntityPlayer player, FabricatorTileEntMaster tile, int slot) {
        ItemStack stack = tile.inventory.getStackInSlot(slot);
        if (stack.isEmpty()) return;

        tile.inventory.setStackInSlot(slot, ItemStack.EMPTY);

        ItemStack leftover = stack.copy();
        player.inventory.addItemStackToInventory(leftover);

        if (!leftover.isEmpty()) {
            InventoryHelper.spawnItemStack(player.world, player.posX, player.posY, player.posZ, leftover);
        }
    }

	@Override
	public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			itemstack = stackInSlot.copy();

			final int machineSlots = 6;
			final int outputSlotIndexInContainer = FabricatorTileEntMaster.SLOT_OUTPUT;

			int playerInvEnd = machineSlots + 36;

			if (index == outputSlotIndexInContainer) {
				if (!this.mergeItemStack(stackInSlot, machineSlots, playerInvEnd, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stackInSlot, itemstack);
			} else if (index < machineSlots) {
				if (!this.mergeItemStack(stackInSlot, machineSlots, playerInvEnd, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (tile.currentRecipe == null) {
					return ItemStack.EMPTY;
				}

				boolean moved = false;

				// Направленная укладка по рецепту
				if (tile.currentRecipe.inputItem != null
						&& tile.currentRecipe.inputItem.isEqualWithOreDict(stackInSlot)) {
					moved = this.mergeItemStack(stackInSlot, FabricatorTileEntMaster.SLOT_INPUT1, FabricatorTileEntMaster.SLOT_INPUT1 + 1, false);
				} else if (tile.currentRecipe.wireSlot != null
						&& !tile.currentRecipe.wireSlot.isEmpty()
						&& tile.currentRecipe.wireSlot.isEqualWithOreDict(stackInSlot)) {
					moved = this.mergeItemStack(stackInSlot, FabricatorTileEntMaster.SLOT_WIRES, FabricatorTileEntMaster.SLOT_WIRES + 1, false);
				} else if (tile.currentRecipe.powderSlot != null
						&& !tile.currentRecipe.powderSlot.isEmpty()
						&& tile.currentRecipe.powderSlot.isEqualWithOreDict(stackInSlot)) {
					moved = this.mergeItemStack(stackInSlot, FabricatorTileEntMaster.SLOT_POWDER, FabricatorTileEntMaster.SLOT_POWDER + 1, false);
				} else if (tile.currentRecipe.plateSlot != null
						&& !tile.currentRecipe.plateSlot.isEmpty()
						&& tile.currentRecipe.plateSlot.isEqualWithOreDict(stackInSlot)) {
					moved = this.mergeItemStack(stackInSlot, FabricatorTileEntMaster.SLOT_PLATE, FabricatorTileEntMaster.SLOT_PLATE + 1, false);
				} else if (TGItems.isMachineUpgrade(stackInSlot)) {
					moved = this.mergeItemStack(stackInSlot, FabricatorTileEntMaster.SLOT_UPGRADE, FabricatorTileEntMaster.SLOT_UPGRADE + 1, false);
				}

				if (!moved) {
					return ItemStack.EMPTY;
				}
			}

			if (stackInSlot.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (stackInSlot.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, stackInSlot);
		}

		return itemstack;
	}

}

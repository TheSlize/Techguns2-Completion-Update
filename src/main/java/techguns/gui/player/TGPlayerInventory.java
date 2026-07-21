package techguns.gui.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.jetbrains.annotations.NotNull;
import techguns.TGConfig;
import techguns.capabilities.TGExtendedPlayer;

import java.util.List;

public class TGPlayerInventory implements IInventory {
    public static final int NUMSLOTS = 15;
    public static final int SLOT_FACE = 0;
    public static final int SLOT_BACK = 1;
    public static final int SLOT_HAND = 2;
    public static final int SLOTS_AUTOFOOD_START = 3;
    public static final int SLOTS_AUTOFOOD_END = 5;
    public static final int SLOT_AUTOHEAL = 6;
    public static final int SLOTS_AMMO_START = 7;
    public static final int SLOTS_AMMO_END = 14;
    private static final String name = "TechgunsPlayerInventory";
    public NonNullList<ItemStack> inventory = NonNullList.withSize(NUMSLOTS, ItemStack.EMPTY);// new

    public boolean dirty = false;
    public EntityPlayer player;

    public TGPlayerInventory(EntityPlayer player) {
        this.player = player;
    }


    public void saveNBTData(NBTTagCompound tags) {
        ItemStackHelper.saveAllItems(tags, this.inventory);
    }

    public void loadNBTData(NBTTagCompound tags) {
        this.inventory.clear();

        ItemStackHelper.loadAllItems(tags, this.inventory);
    }

    @Override
    public int getSizeInventory() {
        return NUMSLOTS;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slotid) {
        ItemStack stack = slotid >= 0 && slotid < this.inventory.size() ? this.inventory.get(slotid) : ItemStack.EMPTY;
        // Th3_Sl1ze: not sure this is performant, but idk how to intercept that any other way without mixins
        if (stack.isEmpty() && TGConfig.general.disableAutofeeder && slotid >= SLOTS_AUTOFOOD_START && slotid <= SLOTS_AUTOFOOD_END) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            for (int i = 1; i < trace.length && i < 10; i++) {
                if (trace[i].getClassName().startsWith("ovh.corail.tombstone")) {
                    return new ItemStack(Items.APPLE);
                }
            }
        }

        return stack;
    }

    @Override
    public @NotNull ItemStack decrStackSize(int index, int count) {
        List<ItemStack> list = this.inventory;

        return list != null && !list.get(index).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slotid, ItemStack itemstack) {
        this.inventory.set(slotid, itemstack);

        if (player != null) {
            if (slotid == SLOT_FACE) {
                player.getDataManager().set(TGExtendedPlayer.DATA_FACE_SLOT, itemstack);
            } else if (slotid == SLOT_BACK) {
                player.getDataManager().set(TGExtendedPlayer.DATA_BACK_SLOT, itemstack);
            } else if (slotid == SLOT_HAND) {
                player.getDataManager().set(TGExtendedPlayer.DATA_HAND_SLOT, itemstack);
            }
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        this.dirty = true;
    }

    @Override
    public boolean isItemValidForSlot(int slotid, ItemStack itemstack) {
        return true;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public @NotNull ITextComponent getDisplayName() {
        return new TextComponentTranslation("techguns.extendedinventory");
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack1 : this.inventory) {
            if (!itemstack1.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack removeStackFromSlot(int index) {
        NonNullList<ItemStack> nonnulllist = this.inventory;
        if (nonnulllist != null && !nonnulllist.get(index).isEmpty()) {
            ItemStack itemstack = nonnulllist.get(index);
            nonnulllist.set(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void openInventory(@NotNull EntityPlayer player) {
    }

    @Override
    public void closeInventory(@NotNull EntityPlayer player) {
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public boolean isUsableByPlayer(@NotNull EntityPlayer player) {
        if (this.player == null || this.player.isDead) {
            return false;
        } else {
            return player.getDistanceSq(this.player) <= 64.0D;
        }
    }

}
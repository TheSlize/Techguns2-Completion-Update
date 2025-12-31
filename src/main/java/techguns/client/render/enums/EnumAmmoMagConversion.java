package techguns.client.render.enums;

import net.minecraft.item.ItemStack;
import techguns.TGItems;

public enum EnumAmmoMagConversion {
    PISTOL(TGItems.PISTOL_ROUNDS, TGItems.PISTOL_MAGAZINE, TGItems.SMG_MAGAZINE),
    PISTOL_I(TGItems.PISTOL_ROUNDS_INCENDIARY, TGItems.PISTOL_MAGAZINE_INCENDIARY, TGItems.SMG_MAGAZINE_INCENDIARY),
    RIFLE(TGItems.RIFLE_ROUNDS, TGItems.ASSAULTRIFLE_MAGAZINE, TGItems.LMG_MAGAZINE, TGItems.MINIGUN_DRUM),
    RIFLE_I(TGItems.RIFLE_ROUNDS_INCENDIARY, TGItems.ASSAULTRIFLE_MAGAZINE_INCENDIARY, TGItems.LMG_MAGAZINE_INCENDIARY, TGItems.MINIGUN_DRUM_INCENDIARY),
    SNIPER(TGItems.SNIPER_ROUNDS, TGItems.AS50_MAGAZINE),
    SNIPER_I(TGItems.SNIPER_ROUNDS_INCENDIARY, TGItems.AS50_MAGAZINE_INCENDIARY),
    SNIPER_E(TGItems.SNIPER_ROUNDS_EXPLOSIVE, TGItems.AS50_MAGAZINE_EXPLOSIVE),
    ADVANCED(TGItems.ADVANCED_ROUNDS, TGItems.ADVANCED_MAGAZINE),
    ENERGY(TGItems.ENERGY_CELL, TGItems.REDSTONE_BATTERY);

    public final ItemStack ammo;
    public final ItemStack[] mags;

    EnumAmmoMagConversion(ItemStack ammo, ItemStack... mags) {
        this.ammo = ammo;
        this.mags = mags;
    }

    public static ItemStack getAmmo(ItemStack mag) {
        if (mag.isEmpty()) {
            return ItemStack.EMPTY;
        }

        for (EnumAmmoMagConversion conv : values()) {
            for (ItemStack magStack : conv.mags) {
                if (ItemStack.areItemsEqual(mag, magStack) && ItemStack.areItemStackTagsEqual(mag, magStack)) {
                    return conv.ammo.copy();
                }
            }
        }

        return mag; // in case we're trying to convert ammo into ammo..
    }
}

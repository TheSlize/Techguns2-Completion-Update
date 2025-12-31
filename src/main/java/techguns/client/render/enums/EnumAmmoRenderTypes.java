package techguns.client.render.enums;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.TGItems;
import techguns.Techguns;

public enum EnumAmmoRenderTypes {
    EMPTY(256, 256, 0, 0, ItemStack.EMPTY),
    PISTOL(2, 1, 29, 8, TGItems.PISTOL_ROUNDS),
    PISTOL_I(34, 1, 29, 8, TGItems.PISTOL_ROUNDS_INCENDIARY),
    RIFLE(2, 12, 28, 5, TGItems.RIFLE_ROUNDS),
    RIFLE_I(32, 12, 28, 5, TGItems.RIFLE_ROUNDS_INCENDIARY),
    SNIPER(2, 20, 31, 7, TGItems.SNIPER_ROUNDS),
    SNIPER_I(36, 20, 31, 7, TGItems.SNIPER_ROUNDS_INCENDIARY),
    SNIPER_E(70, 20, 31, 7, TGItems.SNIPER_ROUNDS_EXPLOSIVE),
    SHOTGUN(2, 86, 31, 24, TGItems.SHOTGUN_ROUNDS),
    SHOTGUN_I(36, 86, 31, 24, TGItems.SHOTGUN_ROUNDS_INCENDIARY),
    ADV_BULLET(2, 30, 35, 7, TGItems.ADVANCED_ROUNDS),
    STONE_BULLET(2, 40, 30, 28, TGItems.STONE_BULLETS),
    GAUSS(2, 70, 32, 13, TGItems.GAUSSRIFLE_SLUGS),
    NETHER_CHARGE(2, 113, 30, 31, TGItems.NETHER_CHARGE),
    BIOFUEL(2, 147, 32, 19, TGItems.BIO_TANK),
    ENERGY(33, 209, 14, 25, TGItems.ENERGY_CELL),
    RADIATION(3, 210, 26, 24, TGItems.NUCLEAR_POWERCELL),
    FIRE(2, 183, 20, 26, TGItems.FUEL_TANK),
    ROCKET_REGULAR(217, 1, 37, 10, TGItems.ROCKET),
    ROCKET_HV(217, 13, 37, 10, TGItems.ROCKET_HIGH_VELOCITY),
    ROCKET_NUCLEAR(217, 25, 37, 10, TGItems.ROCKET_NUKE),
    GRENADE(2, 169, 33, 12, TGItems.GRENADE_40MM);

    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final ItemStack stack;
    public static final ResourceLocation ammoTypesTexture = new ResourceLocation(Techguns.MODID,"textures/gui/ammo_types.png");

    EnumAmmoRenderTypes(int x, int y, int width, int height, ItemStack ammo) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.stack = ammo;
    }

    public static EnumAmmoRenderTypes getEnum(ItemStack stack) {
        if (stack.isEmpty()) {
            return EMPTY;
        }
        for (EnumAmmoRenderTypes type : values()) {
            if (!type.stack.isEmpty() && ItemStack.areItemsEqual(type.stack, stack)) {
                return type;
            }
        }
        return EMPTY;
    }

    @SideOnly(Side.CLIENT)
    public void renderAmmoIcon(Minecraft mc, int xPos, int yPos) {
        if (this == EMPTY) return;

        mc.getTextureManager().bindTexture(ammoTypesTexture);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        float u0 = this.x / 256.0F;
        float v0 = this.y / 256.0F;
        float u1 = (this.x + this.width) / 256.0F;
        float v1 = (this.y + this.height) / 256.0F;

        buf.begin(7, DefaultVertexFormats.POSITION_TEX);
        buf.pos(xPos,         yPos + this.height, 0.0D).tex(u0, v1).endVertex();
        buf.pos(xPos + this.width, yPos + this.height, 0.0D).tex(u1, v1).endVertex();
        buf.pos(xPos + this.width, yPos,              0.0D).tex(u1, v0).endVertex();
        buf.pos(xPos,         yPos,              0.0D).tex(u0, v0).endVertex();
        tess.draw();

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }
}

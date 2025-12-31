package techguns.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.api.capabilities.ITGExtendedPlayer;
import techguns.capabilities.TGExtendedPlayer;
import techguns.capabilities.TGExtendedPlayerCapProvider;
import techguns.packets.PacketNBTControl;
import techguns.tileentities.FabricatorTileEntMaster;
import techguns.tileentities.operation.FabricatorRecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// this is made only for fabricator recipes. May generify recipes later though.
public class ScreenRecipeSelectorGui extends GuiScreen {

    protected static final ResourceLocation texture = new ResourceLocation(Techguns.MODID + ":textures/gui/recipe_selector_gui.png");

    //basic GUI setup
    protected int xSize = 176;
    protected int ySize = 132;
    protected int guiLeft;
    protected int guiTop;

    // данные
    protected List<FabricatorRecipe> recipes = new ArrayList<>();
    protected List<FabricatorRecipe> allRecipes;

    protected GuiTextField search;
    protected int pageIndex;
    protected int size;
    protected String selection;
    public static final String NULL_SELECTION = "null";

    // callback
    protected int index;
    protected FabricatorTileEntMaster tile;
    protected GuiScreen previousScreen;

    public static void openSelector(FabricatorTileEntMaster tile, String selection, int index, GuiScreen previousScreen) {
        FMLCommonHandler.instance().showGuiScreen(new ScreenRecipeSelectorGui(tile, selection, index, previousScreen));
    }

    public ScreenRecipeSelectorGui(FabricatorTileEntMaster tile, String selection, int index, GuiScreen previousScreen) {
        this.tile = tile;
        this.selection = (selection == null ? NULL_SELECTION : selection);
        this.index = index;
        this.previousScreen = previousScreen;

        this.allRecipes = new ArrayList<>(FabricatorRecipe.getRecipes());
        regenerateRecipes();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        Keyboard.enableRepeatEvents(true);
        this.search = new GuiTextField(0, this.fontRenderer, guiLeft + 28, guiTop + 111, 102, 12);
        this.search.setTextColor(0xC5C5C5);
        this.search.setDisabledTextColour(0xC5C5C5);
        this.search.setEnableBackgroundDrawing(false);
        this.search.setMaxStringLength(32);

        if (this.tile != null) {
            FabricatorRecipe sel = FabricatorRecipe.searchRecByName(this.selection);
            if ((this.tile.currentRecipe != null && !isRecipeUnlocked(this.tile.currentRecipe)) || (sel != null && !isRecipeUnlocked(sel))) {
                this.selection = NULL_SELECTION;
                this.tile.currentRecipe = null;
                sendSelectionPacket();
            }
        }
    }

    private void regenerateRecipes() {
        this.recipes.clear();
        for (FabricatorRecipe r : this.allRecipes) {
            if (isRecipeUnlocked(r)) {
                this.recipes.add(r);
            }
        }
        resetPaging();
    }

    private void search(String search) {
        this.recipes.clear();

        if (search == null || search.isEmpty()) {
            regenerateRecipes();
        } else {
            String s = search.trim();
            for (FabricatorRecipe recipe : this.allRecipes) {
                if (recipe.matchesSearch(s) && isRecipeUnlocked(recipe)) {
                    this.recipes.add(recipe);
                }
            }
            resetPaging();
        }
    }

    private void resetPaging() {
        this.pageIndex = 0;
        this.size = Math.max(0, (int) Math.ceil((this.recipes.size() - 40) / 8D));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        this.drawDefaultBackground();
        this.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, f);
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.handleScroll();

        if (guiLeft + 7 <= mouseX && guiLeft + 7 + 144 > mouseX && guiTop + 17 < mouseY && guiTop + 17 + 90 >= mouseY) {
            for (int i = pageIndex * 8; i < pageIndex * 8 + 40; i++) {
                if (i >= this.recipes.size()) break;

                int ind = i - pageIndex * 8;
                int ix = 7 + 18 * (ind % 8);
                int iy = 17 + 18 * (ind / 8);

                if (guiLeft + ix <= mouseX && guiLeft + ix + 18 > mouseX && guiTop + iy < mouseY && guiTop + iy + 18 >= mouseY) {
                    FabricatorRecipe recipe = recipes.get(i);
                    this.drawHoveringText(recipe.print(), mouseX, mouseY);
                }
            }
        }

        if (guiLeft + 151 <= mouseX && guiLeft + 151 + 18 > mouseX && guiTop + 71 < mouseY && guiTop + 71 + 18 >= mouseY) {
            for (FabricatorRecipe recipe : this.recipes) {
                if (recipe.recName.equals(this.selection)) {
                    this.drawHoveringText(recipe.print(), mouseX, mouseY);
                }
            }
        }

        if (guiLeft + 152 <= mouseX && guiLeft + 152 + 16 > mouseX && guiTop + 90 < mouseY && guiTop + 90 + 16 >= mouseY) {
            this.drawHoveringText(TextFormatting.YELLOW + "Close", mouseX, mouseY);
        }

        if (guiLeft + 134 <= mouseX && guiLeft + 134 + 16 > mouseX && guiTop + 108 < mouseY && guiTop + 108 + 16 >= mouseY) {
            this.drawHoveringText(TextFormatting.YELLOW + "Clear search", mouseX, mouseY);
        }

        if (guiLeft + 8 <= mouseX && guiLeft + 8 + 16 > mouseX && guiTop + 108 < mouseY && guiTop + 108 + 16 >= mouseY) {
            this.drawHoveringText(TextFormatting.ITALIC + "Press ENTER to toggle focus", mouseX, mouseY);
        }
    }

    protected void handleScroll() {
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && Mouse.next()) {
            int scroll = Mouse.getEventDWheel();
            if (scroll > 0 && this.pageIndex > 0) this.pageIndex--;
            if (scroll < 0 && this.pageIndex < this.size) this.pageIndex++;
        }
    }

    private void drawGuiContainerForegroundLayer(int x, int y) {
        this.search.drawTextBox();
    }

    private void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (this.search.isFocused()) {
            drawTexturedModalRect(guiLeft + 26, guiTop + 108, 0, 132, 106, 16);
        }

        if (guiLeft + 152 <= mouseX && guiLeft + 152 + 16 > mouseX && guiTop + 18 < mouseY && guiTop + 18 + 16 >= mouseY) {
            drawTexturedModalRect(guiLeft + 152, guiTop + 18, 176, 0, 16, 16);
        }

        if (guiLeft + 152 <= mouseX && guiLeft + 152 + 16 > mouseX && guiTop + 36 < mouseY && guiTop + 36 + 16 >= mouseY) {
            drawTexturedModalRect(guiLeft + 152, guiTop + 36, 176, 16, 16, 16);
        }

        if (guiLeft + 152 <= mouseX && guiLeft + 152 + 16 > mouseX && guiTop + 90 < mouseY && guiTop + 90 + 16 >= mouseY) {
            drawTexturedModalRect(guiLeft + 152, guiTop + 90, 176, 32, 16, 16);
        }

        if (guiLeft + 134 <= mouseX && guiLeft + 134 + 16 > mouseX && guiTop + 108 < mouseY && guiTop + 108 + 16 >= mouseY) {
            drawTexturedModalRect(guiLeft + 134, guiTop + 108, 176, 48, 16, 16);
        }

        if (guiLeft + 8 <= mouseX && guiLeft + 8 + 16 > mouseX && guiTop + 108 < mouseY && guiTop + 108 + 16 >= mouseY) {
            drawTexturedModalRect(guiLeft + 8, guiTop + 108, 176, 64, 16, 16);
        }

        for (int i = pageIndex * 8; i < pageIndex * 8 + 40; i++) {
            if (i >= recipes.size()) break;
            int ind = i - pageIndex * 8;
            FabricatorRecipe recipe = recipes.get(i);
            if (recipe.recName.equals(this.selection))
                this.drawTexturedModalRect(guiLeft + 7 + 18 * (ind % 8), guiTop + 17 + 18 * (ind / 8), 192, 0, 18, 18);
        }

        for (int i = pageIndex * 8; i < pageIndex * 8 + 40; i++) {
            if (i >= recipes.size()) break;

            int ind = i - pageIndex * 8;
            FabricatorRecipe recipe = recipes.get(i);

            this.renderItem(recipe.getIcon(), 8 + 18 * (ind % 8), 18 + 18 * (ind / 8));
            this.mc.getTextureManager().bindTexture(texture);
        }

        for (FabricatorRecipe recipe : this.recipes) {
            if (recipe.recName.equals(this.selection)) {
                this.renderItem(recipe.getIcon(), 152, 72);
            }
        }
    }

    public void renderItem(ItemStack stack, int x, int y) {
        if (stack.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.color(1F, 1F, 1F, 1F);
        RenderHelper.enableGUIStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GlStateManager.enableRescaleNormal();

        this.itemRender.zLevel = 100.0F;
        this.itemRender.renderItemAndEffectIntoGUI(stack, this.guiLeft + x, this.guiTop + y);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, this.guiLeft + x, this.guiTop + y, null);
        this.itemRender.zLevel = 0.0F;

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.color(1F, 1F, 1F, 1F);

        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int x, int y, int k) throws IOException {
        super.mouseClicked(x, y, k);

        this.search.mouseClicked(x, y, k);

        if (guiLeft + 152 <= x && guiLeft + 152 + 16 > x && guiTop + 18 < y && guiTop + 18 + 16 >= y) {
            click();
            if (this.pageIndex > 0) this.pageIndex--;
            return;
        }

        if (guiLeft + 152 <= x && guiLeft + 152 + 16 > x && guiTop + 36 < y && guiTop + 36 + 16 >= y) {
            click();
            if (this.pageIndex < this.size) this.pageIndex++;
            return;
        }

        if (guiLeft + 134 <= x && guiLeft + 134 + 16 > x && guiTop + 108 < y && guiTop + 108 + 16 >= y) {
            this.search.setText("");
            this.search("");
            this.search.setFocused(true);
            return;
        }

        for (int i = pageIndex * 8; i < pageIndex * 8 + 40; i++) {
            if (i >= this.recipes.size()) break;

            int ind = i - pageIndex * 8;
            int ix = 7 + 18 * (ind % 8);
            int iy = 17 + 18 * (ind / 8);

            if (guiLeft + ix <= x && guiLeft + ix + 18 > x && guiTop + iy < y && guiTop + iy + 18 >= y) {
                String newSelection = recipes.get(i).recName;
                if (!newSelection.equals(selection))
                    this.selection = newSelection;
                else
                    this.selection = NULL_SELECTION;

                this.tile.currentRecipe = FabricatorRecipe.searchRecByName(this.selection);

                sendSelectionPacket();

                click();
                return;
            }
        }

        if (guiLeft + 151 <= x && guiLeft + 151 + 18 > x && guiTop + 71 < y && guiTop + 71 + 18 >= y) {
            if (!NULL_SELECTION.equals(this.selection)) {
                this.selection = NULL_SELECTION;
                this.tile.currentRecipe = null;
                sendSelectionPacket();
                click();
                return;
            }
        }

        if (guiLeft + 152 <= x && guiLeft + 152 + 16 > x && guiTop + 90 < y && guiTop + 90 + 16 >= y) {
            FMLCommonHandler.instance().showGuiScreen(previousScreen);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private void sendSelectionPacket() {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("index", this.index);
        data.setString("selection", this.selection);
        TileEntity te = tile;
        TGPackets.wrapper.sendToServer(new PacketNBTControl(data, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_RETURN) {
            this.search.setFocused(!this.search.isFocused());
            return;
        }

        if (this.search.textboxKeyTyped(typedChar, keyCode)) {
            search(this.search.getText());
            return;
        }

        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            FMLCommonHandler.instance().showGuiScreen(previousScreen);
        }
    }

    private boolean isRecipeUnlocked(FabricatorRecipe recipe) {
        if (recipe == null) return false;

        ItemStack out = recipe.outputItem;
        if (out == null || out.isEmpty()) return true;

        EntityPlayer p = Minecraft.getMinecraft().player;
        ITGExtendedPlayer ext = p == null ? null : p.getCapability(TGExtendedPlayerCapProvider.TG_EXTENDED_PLAYER, null);

        if (ext != null) {
            return ext.hasFabricatorRecipeUnlocked(out);
        }
        return !TGExtendedPlayer.isCyberneticPartsOutput(out);
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }

    public void click() { mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F)); }
}

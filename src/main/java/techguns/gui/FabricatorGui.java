package techguns.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import techguns.Techguns;
import techguns.gui.containers.FabricatorContainer;
import techguns.tileentities.FabricatorTileEntMaster;
import techguns.util.TextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static techguns.gui.ReactionChamberGui.ru_ru;

public class FabricatorGui extends PoweredTileEntGui {

	public static final ResourceLocation texture = new ResourceLocation(Techguns.MODID,"textures/gui/fabricator_gui.png");
	public static final ResourceLocation texture_additional = new ResourceLocation(Techguns.MODID,"textures/gui/fabricator_gui_1.png");

	protected FabricatorTileEntMaster tile;
	
	public FabricatorGui(InventoryPlayer ply,FabricatorTileEntMaster tileent) {
		super(new FabricatorContainer(ply,tileent),tileent);
		this.tile = tileent;
		this.tex = texture;
		this.upgradeSlotX = FabricatorContainer.SLOT_UPGRADE_X-1;
		this.upgradeSlotY = FabricatorContainer.SLOT_UPGRADE_Y-1;
		this.showUpgradeSlot = false;
		this.showMachineName = false;
		this.showRedstone = false;
		this.heightSecurityButton = 19;
		this.xSize = 210;
		this.ySize = 238;
		this.appearanceType = EnumAppearanceType.ADVANCED;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		if(this.tile.getEnergyStorage().getEnergyStored() > 0){
			this.drawTexturedModalRect(k + 190, l + 16, 246, 0, 9, 12);
		}
		if (this.tile.isWorking()) {
			this.drawTexturedModalRect(k + 9, l + 98, 226, 12, 29, 29);
			int i1 = this.tile.getProgressScaled(50);
			Minecraft.getMinecraft().getTextureManager().bindTexture(texture_additional);
			this.drawTexturedModalRect(k + 18, l + 58, 0, 0, 140, i1);
		}

		if (this.tile.currentRecipe != null && this.tile.currentRecipe.getIcon() != null) {
			ItemStack icon = this.tile.currentRecipe.getIcon();
			if (!icon.isEmpty()) {
				renderItem(icon, 153, 18);
			}
		} else {
			Minecraft.getMinecraft().getTextureManager().bindTexture(texture_additional);
			this.drawTexturedModalRect(k + 153, l + 18, 140, 0, 16, 16);
		}

		if (this.tile.currentRecipe != null) {
			ItemStack in = ItemStack.EMPTY;
			ItemStack wire = ItemStack.EMPTY;
			ItemStack powder = ItemStack.EMPTY;
			ItemStack plate = ItemStack.EMPTY;

			try {
				if (this.tile.currentRecipe.inputItem != null && !this.tile.currentRecipe.inputItem.getItemStacks().isEmpty())
					in = this.tile.currentRecipe.inputItem.getItemStacks().get(0).copy();
				if (this.tile.currentRecipe.wireSlot != null && !this.tile.currentRecipe.wireSlot.isEmpty() && !this.tile.currentRecipe.wireSlot.getItemStacks().isEmpty())
					wire = this.tile.currentRecipe.wireSlot.getItemStacks().get(0).copy();
				if (this.tile.currentRecipe.powderSlot != null && !this.tile.currentRecipe.powderSlot.isEmpty() && !this.tile.currentRecipe.powderSlot.getItemStacks().isEmpty())
					powder = this.tile.currentRecipe.powderSlot.getItemStacks().get(0).copy();
				if (this.tile.currentRecipe.plateSlot != null && !this.tile.currentRecipe.plateSlot.isEmpty() && !this.tile.currentRecipe.plateSlot.getItemStacks().isEmpty())
					plate = this.tile.currentRecipe.plateSlot.getItemStacks().get(0).copy();
			} catch (Exception ignored) {}

			if (!in.isEmpty() && this.tile.inventory.getStackInSlot(FabricatorTileEntMaster.SLOT_INPUT1).isEmpty()) {
				renderItem(in, 11, 41);
				renderSlotGhost(k+10, l+40);
			}
			if (!wire.isEmpty() && this.tile.inventory.getStackInSlot(FabricatorTileEntMaster.SLOT_WIRES).isEmpty()) {
				renderItem(wire, 57, 41);
				renderSlotGhost(k+56, l+40);
			}
			if (!powder.isEmpty() && this.tile.inventory.getStackInSlot(FabricatorTileEntMaster.SLOT_POWDER).isEmpty()) {
				renderItem(powder, 103, 41);
				renderSlotGhost(k+102, l+40);
			}
			if (!plate.isEmpty() && this.tile.inventory.getStackInSlot(FabricatorTileEntMaster.SLOT_PLATE).isEmpty()) {
				renderItem(plate, 149, 41);
				renderSlotGhost(k+148, l+40);
			}
		}

		if ("ru_ru".equalsIgnoreCase(Minecraft.getMinecraft().gameSettings.language)) {
			this.mc.getTextureManager().bindTexture(ru_ru);
			this.drawTexturedModalRect(k+59, l, 0, 12, 58, 12);
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		int mx = mouseX - (this.width - this.xSize) / 2;
		int my = mouseY - (this.height - this.ySize) / 2;
		if(isInRect(mx, my, 152, 17, 18, 18)){
			List<String> tooltip = new ArrayList<>();
			tooltip.add(TextUtil.trans("techguns.fabricator.template"));

			this.drawHoveringText(tooltip, mx, my);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int mx = mouseX - (this.width - this.xSize) / 2;
		int my = mouseY - (this.height - this.ySize) / 2;
		if(isInRect(mx, my, 152, 17, 18, 18))
			ScreenRecipeSelectorGui.openSelector(this.tile, this.tile.currentRecipe != null ? this.tile.currentRecipe.recName : "", 0, this);
	}

	@Override
	protected void drawDefaultEnergyBar(){
		drawEnergyBar(guiLeft, guiTop, 186, 30, 210, 0, 16, 95, texture);
	}

	@Override
	protected void drawDefaultEnergyTooltip(int mouseX, int mouseY){
		drawEnergyTooltip(mouseX, mouseY, 186, 30, 16, 95);
	}

	protected void renderSlotGhost(int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.color(1F, 1F, 1F, 0.5F);
		float prevZ = this.zLevel;
		this.zLevel = 300F;
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(x, y, 226, 41, 18, 18);
		this.zLevel = prevZ;
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	
}

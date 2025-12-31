package techguns.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.gui.containers.AmmoPressContainer;
import techguns.packets.PacketGuiButtonClick;
import techguns.tileentities.AmmoPressTileEnt;

import java.io.IOException;

import static techguns.gui.ReactionChamberGui.ru_ru;

public class AmmoPressGui extends PoweredTileEntGui {
	public static final ResourceLocation texture = new ResourceLocation(Techguns.MODID,"textures/gui/ammo_press_gui.png");// new
	
	protected AmmoPressTileEnt tileent;
	
	public AmmoPressGui(InventoryPlayer ply, AmmoPressTileEnt tileent) {
		super(new AmmoPressContainer(ply, tileent),tileent);
		this.tileent = tileent;
		this.tex = texture;
		this.ySize = 198;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int mx = mouseX - (this.width-this.xSize)/2;
        int my = mouseY - (this.height-this.ySize)/2;
        if(isInRect(mx, my, 42, 32, 29, 31)){
	        switch (this.tileent.buildPlan) {
				case 0:
					this.drawHoveringText(I18n.format("techguns.gui.ammopress.build_plan.pistol"), mx, my);
					break;
				case 1:
					this.drawHoveringText(I18n.format("techguns.gui.ammopress.build_plan.shotgun"), mx, my);
					break;
				case 2:
					this.drawHoveringText(I18n.format("techguns.gui.ammopress.build_plan.rifle"), mx, my);
					break;
				case 3:
					this.drawHoveringText(I18n.format("techguns.gui.ammopress.build_plan.sniper"), mx, my);
					break;
	        }
        }
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(k + 42, l + 32, 227, 132 + this.tileent.buildPlan * 31, 29, 31);
		
		if (this.tileent.isWorking()) {
			int i1 = this.tileent.getProgressScaled(21);
			// this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
			this.drawTexturedModalRect(k + 119, l + 36, 176, 0, 19, i1 + 1);
		}

		if ("ru_ru".equalsIgnoreCase(Minecraft.getMinecraft().gameSettings.language)) {
			this.mc.getTextureManager().bindTexture(ru_ru);
			this.drawTexturedModalRect(k+40, l+18, 0, 33, 33, 9);
		}

	}

	@Override
	protected void drawDefaultEnergyBar(){
		drawEnergyBar(guiLeft, guiTop, 8, 17, 251, 1, 4, 59);
	}

	@Override
	protected void drawDefaultEnergyTooltip(int mouseX, int mouseY){
		drawEnergyTooltip(mouseX, mouseY, 7, 17, 5, 59);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton != 0) return;

		int mx = mouseX - (this.width - this.xSize) / 2;
		int my = mouseY - (this.height - this.ySize) / 2;

		if (isInRect(mx, my, 31, 68, 52 - 31, 80 - 68)) {
			playVanillaButtonSound();
			TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.tileent, AmmoPressTileEnt.BUTTON_ID_PREV));
		}
		else if (isInRect(mx, my, 61, 68, 82 - 61, 80 - 68)) {
			playVanillaButtonSound();
			TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.tileent, AmmoPressTileEnt.BUTTON_ID_NEXT));
		}
	}
	
}

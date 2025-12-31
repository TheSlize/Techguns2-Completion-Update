package techguns.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.gui.containers.ExplosiveChargeContainer;
import techguns.packets.PacketGuiButtonClick;
import techguns.tileentities.ExplosiveChargeAdvTileEnt;
import techguns.tileentities.ExplosiveChargeTileEnt;

public class ExplosiveChargeGui extends TGBaseGui {

	protected static final int FIRST_BUTTON_ID = 0;
	
	public static final ResourceLocation texture = new ResourceLocation(Techguns.MODID, "textures/gui/tnt_charge_gui.png");;
	public static final ResourceLocation texture2 = new ResourceLocation(Techguns.MODID, "textures/gui/exp_charge_gui.png");
	
	protected byte guiType = 0;

	protected ExplosiveChargeTileEnt tileent;
	
	public ExplosiveChargeGui(InventoryPlayer ply, ExplosiveChargeTileEnt tileent) {
		super(new ExplosiveChargeContainer(ply, tileent));
		this.tileent=tileent;
		if(tileent instanceof ExplosiveChargeAdvTileEnt) {
			this.tex=texture2;
		} else {
			this.tex=texture;
		}
		this.showInventoryText=false;
	}

	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}



	@Override
	public void initGui() {
		super.initGui();
		int buttonid=FIRST_BUTTON_ID;
		this.buttonList.add(new GuiButtonExt(buttonid++, this.guiLeft+57, this.guiTop+73, 60, 20, "Взорвать"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.tileent,guibutton.id));
		if(guibutton.id==FIRST_BUTTON_ID){
			Minecraft.getMinecraft().player.closeScreen();
		}
	}
	
}

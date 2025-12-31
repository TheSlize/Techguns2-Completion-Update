package techguns.gui;

import static techguns.gui.ButtonConstants.BUTTON_ID_REDSTONE;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Container;
import techguns.gui.widgets.GuiButtonRedstoneState;
import techguns.tileentities.BasicRedstoneTileEnt;
import techguns.util.TextUtil;

public class RedstoneTileEntGui extends OwnedTileEntGui {
	protected boolean showRedstone = true;
	protected BasicRedstoneTileEnt redstoneTileEnt;
	
	public RedstoneTileEntGui(Container inventorySlotsIn, BasicRedstoneTileEnt tile) {
		super(inventorySlotsIn, tile);
		this.redstoneTileEnt=tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        if(showRedstone) {
			switch(appearanceType){
				case BRONZISH:
					this.drawTexturedModalRect(k - 22 - 5, l + 10 - 5, 195, 0, 22 + 5, 30);
					break;
				case ADVANCED:
					this.drawTexturedModalRect(k - 22 - 5, l + 10 - 5, 195, 30, 22 + 5, 30);
					break;
				case REGULAR:
					this.drawTexturedModalRect(k - 22 - 5, l + 10 - 5, 195, 60, 22 + 5, 30);
					break;
			}

			//draw Redstone control;
			int texX = 199;
			int texY = 0;
			switch(appearanceType) {
				case BRONZISH:
					texY = 202;
					break;
				case ADVANCED:
					texY = 220;
					break;
				case REGULAR:
					texY = 238;
					break;
			}
			if (redstoneTileEnt.isRedstoneEnabled()) {
				this.drawTexturedModalRect(k + 7, l + 5, texX, texY, 5, 5);
				this.drawTexturedModalRect(k + 7, l + 5 + 5, texX + 5, texY + 5, 5, 5);
			} else {
				this.drawTexturedModalRect(k + 7, l + 5, texX, texY + 5, 5, 5);
				this.drawTexturedModalRect(k + 7, l + 5 + 5, texX + 5, texY, 5, 5);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		int mx = mouseX - (this.width - this.xSize) / 2;
		int my = mouseY - (this.height - this.ySize) / 2;
		if (showRedstone){
			if (isInRect(mx, my, 7, 5, 5, 10) || isInRect(mx, my, -22, 10, 20, 20)) {
				List<String> tooltip = new ArrayList<String>();
				tooltip.add(TextUtil.trans("techguns.container.redstone"));
				tooltip.add(TextUtil.trans("techguns.container.redstone.mode") + ": " + TextUtil.trans(redstoneTileEnt.getRedstoneModeText()));
				tooltip.add(TextUtil.trans("techguns.container.redstone.signal") + ": " + TextUtil.trans(redstoneTileEnt.getSignalStateText()));
				this.drawHoveringText(tooltip, mx, my);
			}
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		if(showRedstone) this.buttonList.add(new GuiButtonRedstoneState(BUTTON_ID_REDSTONE, this.guiLeft-22, this.guiTop+10, 20, 20, "", redstoneTileEnt));
	}

	
}

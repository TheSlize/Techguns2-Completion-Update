package techguns.gui;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import techguns.TGConfig;
import techguns.Techguns;
import techguns.tileentities.BasicPoweredTileEnt;
import techguns.util.TextUtil;

public abstract class PoweredTileEntGui extends RedstoneTileEntGui {
	public static final String POWER_UNIT ="FE";
	
	protected BasicPoweredTileEnt poweredTileEnt;
	public static final ResourceLocation power_texture = new ResourceLocation(Techguns.MODID,"textures/gui/ammo_press_gui.png");
	
	protected boolean showUpgradeSlot =true;
	
	protected int upgradeSlotX=151;
	protected int upgradeSlotY=59;
	
	public PoweredTileEntGui(Container container, BasicPoweredTileEnt tile) {
		super(container,tile);
		this.poweredTileEnt=tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

		drawDefaultEnergyBar();
        
        //draw upgrade slot
        if (showUpgradeSlot) {
			switch(appearanceType){
				case BRONZISH:
					this.drawTexturedModalRect(k+upgradeSlotX+1, l+upgradeSlotY+1, 209, 202, 18, 18);
					break;
				case ADVANCED:
					this.drawTexturedModalRect(k+upgradeSlotX+1, l+upgradeSlotY+1, 209, 220, 18, 18);
					break;
				case REGULAR:
					this.drawTexturedModalRect(k+upgradeSlotX+1, l+upgradeSlotY+1, 209, 238, 18, 18);
					break;
			}
        }

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		int mx = mouseX - (this.width-this.xSize)/2;
        int my = mouseY - (this.height-this.ySize)/2;

		drawDefaultEnergyTooltip(mx, my);
	}

	protected void drawDefaultEnergyBar(){
		drawEnergyBar(guiLeft, guiTop, 8, 17, 251, 1, 4, 48);
	}

	protected void drawDefaultEnergyTooltip(int mouseX, int mouseY){
		drawEnergyTooltip(mouseX, mouseY, 8, 17, 4, 48);
	}


	protected void drawEnergyBar(int guiLeft, int guiTop, int x, int y, int textureX, int textureY, int width, int height) {
		int energyStored = this.poweredTileEnt.getEnergyStorage().getEnergyStored();
		int maxEnergy    = this.poweredTileEnt.getEnergyStorage().getMaxEnergyStored();

		int filled = TGConfig.machinesNeedNoPower
				? height
				: (maxEnergy > 0 ? (int)Math.round((double)energyStored * height / (double)maxEnergy) : 0);

		if (filled < 0) filled = 0;
		if (filled > height) filled = height;

		this.mc.getTextureManager().bindTexture(power_texture);

		int screenX = guiLeft + x;
		int screenY = guiTop + y;

		int offset = height - filled;

		this.drawTexturedModalRect(screenX, screenY + offset, textureX, textureY + offset, width, filled);
	}

	protected void drawEnergyTooltip(int mx, int my, int x, int y, int w, int h) {
		if (isInRect(mx, my, x, y, w, h)) {
			if (TGConfig.machinesNeedNoPower) {
				this.drawHoveringText(TextUtil.trans("techguns.container.power"), mx, my);
			} else {
				int cur = this.poweredTileEnt.getEnergyStorage().getEnergyStored();
				int max = this.poweredTileEnt.getEnergyStorage().getMaxEnergyStored();
				this.drawHoveringText(
						TextUtil.trans("techguns.container.power") + ": " + cur + "/" + max + " " + POWER_UNIT,
						mx, my
				);
			}
		}
	}

	protected void drawEnergyBar(int guiLeft, int guiTop, int x, int y, int textureX, int textureY, int width, int height, ResourceLocation power_texture) {
		int energyStored = this.poweredTileEnt.getEnergyStorage().getEnergyStored();
		int maxEnergy    = this.poweredTileEnt.getEnergyStorage().getMaxEnergyStored();

		int filled = TGConfig.machinesNeedNoPower
				? height
				: (maxEnergy > 0 ? (int)Math.round((double)energyStored * height / (double)maxEnergy) : 0);

		if (filled < 0) filled = 0;
		if (filled > height) filled = height;

		this.mc.getTextureManager().bindTexture(power_texture);

		int screenX = guiLeft + x;
		int screenY = guiTop + y;

		int offset = height - filled;

		this.drawTexturedModalRect(screenX, screenY + offset, textureX, textureY + offset, width, filled);
	}
	
}

package techguns.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.gui.containers.ChemLabContainer;
import techguns.packets.PacketGuiButtonClick;
import techguns.tileentities.ChemLabTileEnt;
import techguns.util.TextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static techguns.gui.ReactionChamberGui.ru_ru;

public class ChemLabGui extends PoweredTileEntGui {

	public static final ResourceLocation texture = new ResourceLocation(Techguns.MODID,"textures/gui/chem_lab_gui.png");// new
	
	public static final int INPUT_TANK_X = 20;
	public static final int TANK_Y = 17;
	public static final int OUTPUT_TANK_X = 177;
	
	public static final int TANK_W=10;
	public static final int TANK_H=50;
	
	protected ChemLabTileEnt tile;
	
	public ChemLabGui(InventoryPlayer ply, ChemLabTileEnt tileent) {
		super(new ChemLabContainer(ply, tileent),tileent);
		this.tile = tileent;
		this.tex = texture;
		this.xSize = 199;
		this.ySize = 188;
		this.upgradeSlotX = 151;
		this.upgradeSlotY = 56;
		this.invNameX = 18;
		this.appearanceType = EnumAppearanceType.REGULAR;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
        if(this.tile.inputTank.getFluidAmount()>0){
        	int px = this.tile.inputTank.getFluidAmount()*TANK_H / this.tile.inputTank.getCapacity();

            TextureAtlasSprite tex = null;
            if (tile.inputTank.getFluid() != null) {
                tex = mc.getTextureMapBlocks().getTextureExtry(tile.inputTank.getFluid().getFluid().getStill().toString());
            }

            this.drawFluidWithTesselator(tex, k + 20 + 1, l + 16 + 1, TANK_W, TANK_H, px);
        }
        
        if(this.tile.outputTank.getFluidAmount()>0){
        	int px = this.tile.outputTank.getFluidAmount()*TANK_H / this.tile.outputTank.getCapacity();

            TextureAtlasSprite tex = null;
            if (tile.outputTank.getFluid() != null) {
                tex = mc.getTextureMapBlocks().getTextureExtry(tile.outputTank.getFluid().getFluid().getStill().toString());
            }

            this.drawFluidWithTesselator(tex, k + 177 + 1, l + 16 + 1, TANK_W, TANK_H, px);
        }
		
        this.mc.getTextureManager().bindTexture(texture);

		if(tile.getDrainMode() == 0){
			this.drawTexturedModalRect(k + 177, l + 69, 214, 29, 12, 6);
		} else {
			this.drawTexturedModalRect(k + 20, l + 69, 214, 29, 12, 6);
			this.drawTexturedModalRect(k + 91, l + 57, 214, 35, 29, 17);
		}


		if (this.tile.isWorking()) {
			float prog = this.tile.getProgress();
			int baseX = k + 69;
			int baseY = l + 21;

			if (prog > 0.0f) {
				float p = Math.min(prog, 0.2f) * 5.0f;
				int fullH = 27;
				int drawH = (int) (fullH * p + 0.5f);
				if (drawH > 0) {
					this.drawTexturedModalRect(baseX, l + 25 + (fullH - drawH), 185, 225 + 4 + (fullH - drawH), 11, drawH);
				}
			}

			if (prog > 0.2f) {
				float p = (Math.min(prog, 0.4f) - 0.2f) * 5.0f;

				int seg1W = 26;
				int seg2W = 3;
				int totalW = seg1W + seg2W;
				int filled = (int) (totalW * p + 0.5f);

				if (filled > 0) {
					int drawW1 = Math.min(filled, seg1W);
                    this.drawTexturedModalRect(baseX + 11, baseY, 185 + 11, 225, drawW1, 31);
                }

				if (filled > seg1W) {
					int drawW2 = filled - seg1W;
                    this.drawTexturedModalRect(baseX + 37, baseY, 185 + 37, 225, drawW2, 11);
                }
			}

			if (prog > 0.4f) {
				float p = (Math.min(prog, 0.6f) - 0.4f) * 5.0f;
				int fullH = 28;
				int drawH = (int) (fullH * p + 0.5f);
				if (drawH > 0) {
					this.drawTexturedModalRect(baseX + 37, baseY + 3, 185 + 37, 225 + 3, 16, drawH);
				}
			}

			if (prog > 0.6f) {
				float p = (Math.min(prog, 0.8f) - 0.6f) * 5.0f;

				int seg1W = 5;
				int seg2W = 4;
				int seg3W = 4;
				int totalW = seg1W + seg2W + seg3W;
				int filled = (int) (totalW * p + 0.5f);

				if (filled > 0) {
					int drawW1 = Math.min(filled, seg1W);
                    this.drawTexturedModalRect(baseX + 53, baseY + 9, 185 + 53, 225 + 9, drawW1, 17);
                }

				if (filled > seg1W) {
					int drawW2 = Math.min(filled - seg1W, seg2W);
                    this.drawTexturedModalRect(baseX + 58, baseY + 6, 185 + 58, 225 + 6, drawW2, 8);
                }

				if (filled > seg1W + seg2W) {
					int drawW3 = filled - seg1W - seg2W;
                    this.drawTexturedModalRect(baseX + 62, baseY + 6, 185 + 62, 225 + 6, drawW3, 3);
                }
			}

			if (prog > 0.8f) {
				float p = (Math.min(prog, 1.0f) - 0.8f) * 5.0f;
				int fullH = 22;
				int drawH = (int) (fullH * p + 0.5f);
				if (drawH > 0) {
					this.drawTexturedModalRect(baseX + 58, baseY + 9, 185 + 58, 225 + 9, 13, drawH);
				}
			}
		}

		if ("ru_ru".equalsIgnoreCase(Minecraft.getMinecraft().gameSettings.language)) {
			this.mc.getTextureManager().bindTexture(ru_ru);
			this.drawTexturedModalRect(k+14, l+5, 112, 0, 24, 9);
			this.drawTexturedModalRect(k+171, l+5, 112, 0, 24, 9);
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		int mx = mouseX - (this.width - this.xSize) / 2;
		int my = mouseY - (this.height - this.ySize) / 2;

		 if (isInRect(mx, my, INPUT_TANK_X, TANK_Y-1, TANK_W+1, TANK_H+1)){
			 
        	FluidTankInfo info = this.tile.inputTank.getInfo();
        	List<String> tooltip = new ArrayList<>();
        	tooltip.add(info.fluid!=null?(info.fluid.getFluid().getLocalizedName(info.fluid)):TextUtil.trans("techguns.gui.empty"));
        	tooltip.add(tile.inputTank.getFluidAmount()+"/"+(info.capacity+"mB"));
        	this.drawHoveringText(tooltip, mx, my);        	
        	
		 } else if (isInRect(mx, my, OUTPUT_TANK_X, TANK_Y-1, TANK_W+1, TANK_H+1)){
			 
        	FluidTankInfo info = this.tile.outputTank.getInfo();
			List<String> tooltip = new ArrayList<>();
        	tooltip.add(info.fluid!=null?(info.fluid.getFluid().getLocalizedName(info.fluid)):TextUtil.trans("techguns.gui.empty"));
        	tooltip.add(tile.outputTank.getFluidAmount()+"/"+(info.capacity+"mB"));
        	this.drawHoveringText(tooltip, mx, my);        	
		 
		 } else if(isInRect(mx, my, 14, 5, 24, 9)){
        	
        	List<String> tooltip = new ArrayList<>();
        	tooltip.add(TextUtil.trans("techguns.chemlab.dumpinput"));
        	tooltip.add(TextUtil.trans("techguns.chemlab.dumpinput.tooltip"));
        	this.drawHoveringText(tooltip, mx, my);
		 
		 } else if(isInRect(mx, my, 171, 5, 24, 9)){
			 
        	List<String> tooltip = new ArrayList<>();
        	tooltip.add(TextUtil.trans("techguns.chemlab.dumpoutput"));
        	tooltip.add(TextUtil.trans("techguns.chemlab.dumpoutput.tooltip"));
        	this.drawHoveringText(tooltip, mx, my);
        	
		 } else if(isInRect(mx, my, 90, 57, 31, 18)){
			 	
        	List<String> tooltip = new ArrayList<>();
        	tooltip.add(TextUtil.trans("techguns.chemlab.toogledrain"));
        	tooltip.add(TextUtil.trans("techguns.chemlab.toogledrain.tooltip."+tile.getDrainMode()));
        	this.drawHoveringText(tooltip, mx, my);
		 }
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton != 0) return;

		int mx = mouseX - (this.width - this.xSize) / 2;
		int my = mouseY - (this.height - this.ySize) / 2;

		if (isInRect(mx, my, 90, 57, 31, 18)) {
			playVanillaButtonSound();
			TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.tile, ChemLabTileEnt.BUTTON_ID_TOGGLE_DRAIN));
		} else if (isInRect(mx, my, 14, 5, 24, 9)) {
			playVanillaButtonSound();
			TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.tile, ChemLabTileEnt.BUTTON_ID_DUMP_INPUT));

		} else if (isInRect(mx, my, 171, 5, 24, 9)) {
			playVanillaButtonSound();
			TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.tile, ChemLabTileEnt.BUTTON_ID_DUMP_OUTPUT));
		}
	}
}

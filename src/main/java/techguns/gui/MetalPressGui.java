package techguns.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import techguns.TGPackets;
import techguns.Techguns;
import techguns.gui.containers.MetalPressContainer;
import techguns.packets.PacketGuiButtonClick;
import techguns.tileentities.MetalPressTileEnt;
import techguns.util.TextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static techguns.gui.ButtonConstants.BUTTON_ID_REDSTONE;
import static techguns.gui.ButtonConstants.BUTTON_ID_SECURITY;
import static techguns.gui.ReactionChamberGui.ru_ru;
import static techguns.tileentities.MetalPressTileEnt.BUTTON_ID_AUTOSPLIT;

public class MetalPressGui extends PoweredTileEntGui {
	public static final ResourceLocation texture = new ResourceLocation(Techguns.MODID, "textures/gui/metal_press_gui.png");
	public static final ResourceLocation gauge_texture = new ResourceLocation(Techguns.MODID, "textures/gui/gauges.png");

	MetalPressTileEnt tile;
	private boolean lastHasSteamUpgrade;

	public MetalPressGui(InventoryPlayer ply, MetalPressTileEnt tileent) {
		super(new MetalPressContainer(ply, tileent), tileent);
		this.tile = tileent;
		this.tex = texture;
		this.ySize = 198;
		this.lastHasSteamUpgrade = tileent.hasSteamUpgrade();
		this.xSize = this.lastHasSteamUpgrade ? 201 : 176;
	}

	private void updateLayout() {
		boolean hasSteam = this.tile.hasSteamUpgrade();
		if (hasSteam != this.lastHasSteamUpgrade) {
			this.lastHasSteamUpgrade = hasSteam;
			this.xSize = hasSteam ? 201 : 176;
			this.guiLeft = (this.width - this.xSize) / 2;
			this.guiTop = (this.height - this.ySize) / 2;

			for (GuiButton button : this.buttonList) {
				if (button.id == BUTTON_ID_REDSTONE) {
					button.x = this.guiLeft - 22;
					button.y = this.guiTop + 10;
				} else if (button.id == BUTTON_ID_SECURITY) {
					button.x = this.guiLeft - 22;
					button.y = this.guiTop + heightSecurityButton;
				}
			}
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.updateLayout();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int mx = mouseX - (this.width - this.xSize) / 2;
		int my = mouseY - (this.height - this.ySize) / 2;

        String s2 = "";
		switch (this.tile.getAutoSplitMode()) {
			case 0:
				s2 = TextFormatting.RED + TextUtil.trans("techguns.container.info.off");
				break;
			case 1:
				s2 = TextFormatting.GREEN + TextUtil.trans("techguns.container.info.on");
				break;
		}
		if (isInRect(mx, my, 30, 33, 56, 9)) {
			List<String> tooltip = new ArrayList<>();
			tooltip.add(TextUtil.trans("techguns.container.metalpress.autosplit") + ": " + s2);
			this.drawHoveringText(tooltip, mx, my);
		}
		if (isInRect(mx, my, 35, 56, 43, 12)) {
			List<String> tooltip = new ArrayList<>();
			tooltip.add(TextUtil.trans("techguns.container.metalpress.toggle"));
			this.drawHoveringText(tooltip, mx, my);
		}
		if (this.tile.hasSteamUpgrade()) {
			if (isInRect(mx, my, 185, 12, 10, 50)) {
				FluidStack fluid = this.tile.getSteamTank().getFluid();
				List<String> tooltip = new ArrayList<>();
				if (fluid != null && fluid.amount > 0) {
					tooltip.add(fluid.getFluid().getLocalizedName(fluid));
					tooltip.add(fluid.amount + "/" + this.tile.getSteamTank().getCapacity() + "mB");
				} else {
					tooltip.add(TextUtil.trans("techguns.gui.empty"));
					tooltip.add("0/" + this.tile.getSteamTank().getCapacity() + "mB");
				}
				this.drawHoveringText(tooltip, mx, my);
			}

			if (isInRect(mx, my, 181, 68, 18, 18)) {
				List<String> tooltip = new ArrayList<>();
				tooltip.add(TextUtil.trans("techguns.gui.pressure") + ": " + this.tile.getPressureLevel());
				this.drawHoveringText(tooltip, mx, my);
			}
		}

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

		if (this.tile.isWorking()) {
			int i1 = this.tile.getProgressScaled(21);
			this.drawTexturedModalRect(k + 119, l + 36, 176, 0, 19, i1 + 1);
		}

		if (this.tile.hasSteamUpgrade()) {
			FluidStack fluid = this.tile.getSteamTank().getFluid();
			if (fluid != null && fluid.amount > 0) {
				int px = fluid.amount * 50 / this.tile.getSteamTank().getCapacity();
				Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

				ResourceLocation still = fluid.getFluid().getStill(fluid);
				TextureAtlasSprite tex = mc.getTextureMapBlocks().getAtlasSprite(still.toString());

                this.drawFluidWithTesselator(tex, k + 185, l + 12, 10, 50, px);
			}

			Minecraft.getMinecraft().getTextureManager().bindTexture(gauge_texture);
			int pressure = this.tile.getPressureLevel();
			if (pressure < 0) {
				pressure = 0;
			} else if (pressure > 12) {
				pressure = 12;
			}
			this.drawTexturedModalRect(k + 181, l + 68, 0, pressure * 18, 18, 18);
		}

		if(this.tile.getAutoSplitMode() == 1) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(gauge_texture);
			this.drawTexturedModalRect(k + 35, l + 56, 200, 21, 44, 12);
		}

		if ("ru_ru".equalsIgnoreCase(Minecraft.getMinecraft().gameSettings.language)) {
			this.mc.getTextureManager().bindTexture(ru_ru);
			this.drawTexturedModalRect(k+30, l+33, 0, 24, 56, 9);
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
	public void initGui() {
		super.initGui();
		this.updateLayout();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton != 0) return;

		int mx = mouseX - (this.width - this.xSize) / 2;
		int my = mouseY - (this.height - this.ySize) / 2;

		if (isInRect(mx, my, 35, 56, 43, 12)) {
			playVanillaButtonSound();
			TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.ownedTile, BUTTON_ID_AUTOSPLIT));
		}
	}
}

package techguns.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.gui.containers.GrinderContainer;
import techguns.tileentities.GrinderTileEnt;

public class GrinderGui extends PoweredTileEntGui {
	public static final ResourceLocation texture = new ResourceLocation(Techguns.MODID,"textures/gui/grinder_gui.png");
	
	protected GrinderTileEnt tile;
	public GrinderGui(InventoryPlayer ply, GrinderTileEnt tile) {
		super(new GrinderContainer(ply, tile), tile);
		
		this.tile = tile;
		this.tex = texture;
		this.xSize = 199;
		this.ySize = 188;
		this.upgradeSlotX = GrinderContainer.SLOT_UPGRADE_X - 2;
		this.upgradeSlotY = GrinderContainer.SLOT_UPGRADE_Y - 2;
		this.appearanceType = EnumAppearanceType.REGULAR;
		this.invNameX = 18;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(texture);

		int progress = this.tile.getProgressScaled(52);
		if (progress > 0) {
			this.drawTexturedModalRect(k + 51, l + 29, 204, 0, progress, 31);
		}

		float totalTime = (float) this.tile.totaltime;
		float progressRatio = totalTime > 0.0F ? this.tile.progress / totalTime : 0.0F;
		if (progressRatio < 0.0F) {
			progressRatio = 0.0F;
		} else if (progressRatio > 1.0F) {
			progressRatio = 1.0F;
		}

		float angleUpper = -progressRatio * 1080.0F;
		float angleLower = progressRatio * 1080.0F;

		this.drawSaw(k + 62, l + 15, 227, 198, angleUpper);
		this.drawSaw(k + 62, l + 44, 227, 227, angleLower);
	}

	private void drawSaw(int x, int y, int u, int v, float angle) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.translate(x + 14.5F, y + 14.5F, 0.0F);
		if (angle != 0.0F) {
			GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
		}
		GlStateManager.translate(-14.5F, -14.5F, 0.0F);

		this.drawTexturedModalRect(0, 0, u, v, 29, 29);

		GlStateManager.popMatrix();
	}
}

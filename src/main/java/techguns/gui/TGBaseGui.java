package techguns.gui;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import techguns.tileentities.BasicInventoryTileEnt;
import techguns.util.TextUtil;

public abstract class TGBaseGui extends GuiContainer {
	protected ResourceLocation tex;

	protected boolean showInventoryText = true;
	protected boolean showMachineName = true;
	protected int invNameX = 8;

	public TGBaseGui(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public static boolean isInRect(int mouseX, int mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);
	}

	public static String trans(String key) {
		return TextUtil.trans("techguns." + key);
	}
	
	public void drawInventoryContainerName(BasicInventoryTileEnt tileent) 
	{
		int x = 0;
        int y = 0;
        int color = 4210752; //0xff101010;
        
        if(this.showInventoryText) {
        	this.fontRenderer.drawString(TextUtil.trans("container.inventory"), invNameX, this.ySize - 96 + 2, color);
        }
        
        String s = tileent.getDisplayName().getFormattedText();
		if(showMachineName) this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, color);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f, 1f);
		this.mc.getTextureManager().bindTexture(tex);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	protected void drawFluidWithTesselator(TextureAtlasSprite tex, int x, int y, int width, int height, int px){
		int offset =0;
		
		if (tex==null){
			return;
		}
		
    	int len=tex.getIconHeight();
    	while (offset < px){
    		if(offset+len>px){
    			len=px-offset;
    		}
    		this.drawTexturedModelRectFromIconFluidTank(x, y+height-offset-len, tex,width,len);
    		offset+=tex.getIconHeight();
    	}
	}

	public void renderItem(ItemStack stack, int x, int y) {
		if (stack.isEmpty()) return;

		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		RenderHelper.enableGUIStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.enableRescaleNormal();

		this.itemRender.zLevel = 10.0F;
		this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(stack.getItem(), 1, stack.getMetadata()), this.guiLeft + x, this.guiTop + y);
		this.itemRender.zLevel = 0.0F;

		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableAlpha();
		GlStateManager.color(1F, 1F, 1F, 1F);

		GlStateManager.popMatrix();
	}
	
	protected void drawTexturedModelRectFromIconFluidTank(int x, int y, TextureAtlasSprite icon, int w, int h)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos((double)(x + 0), (double)(y + h), (double)this.zLevel).tex((double)icon.getMinU(), (double)icon.getMaxV()).endVertex();
        buffer.pos((double)(x + w), (double)(y + h), (double)this.zLevel).tex((double)icon.getMaxU(), (double)icon.getMaxV()).endVertex();
        buffer.pos((double)(x + w), (double)(y + 0), (double)this.zLevel).tex((double)icon.getMaxU(), (double)icon.getInterpolatedV(icon.getIconHeight()-h)).endVertex();
        buffer.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)icon.getMinU(), (double)icon.getInterpolatedV(icon.getIconHeight()-h)).endVertex();
        tessellator.draw();
    }
	
    /**
     * Draws a textured rectangle at the current z-value, with color as ints
     */
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int r, int g, int b, int a)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).color(r, g, b, a).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).color(r, g, b, a).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).color(r, g, b, a).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).color(r, g, b, a).endVertex();
        tessellator.draw();
    }

	protected void playVanillaButtonSound() {
		mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
    
}

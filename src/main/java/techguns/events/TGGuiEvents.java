package techguns.events;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.TGConfig;
import techguns.TGItems;
import techguns.TGRadiationSystem;
import techguns.Techguns;
import techguns.api.guns.GunHandType;
import techguns.api.guns.GunManager;
import techguns.api.radiation.TGRadiation;
import techguns.api.render.IItemRenderer;
import techguns.capabilities.TGExtendedPlayer;
import techguns.client.render.ItemRenderHack;
import techguns.client.render.enums.EnumAmmoMagConversion;
import techguns.client.render.enums.EnumAmmoRenderTypes;
import techguns.client.render.item.RenderGunBase;
import techguns.gui.player.TGPlayerInventory;
import techguns.gui.player.TGPlayerInventoryGui;
import techguns.items.additionalslots.ItemTGSpecialSlotAmmo;
import techguns.items.armors.PoweredArmor;
import techguns.items.guns.EnumCrosshairStyle;
import techguns.items.guns.GenericGun;
import techguns.util.InventoryUtil;
import techguns.util.MathUtil;

public class TGGuiEvents extends Gui {

	public static ResourceLocation crosshairsTexture = new ResourceLocation(Techguns.MODID,"textures/gui/crosshairs.png");
	public static ResourceLocation tgCrosshairsTexture = new ResourceLocation(Techguns.MODID,"textures/gui/tg_crosshairs.png");

    private static float AMMO_TEXT_SCALE;
    private static float AMMO_ICON_SCALE;
    private static float AMMO_MAG_TEXT_SCALE;
    private static int HUD_MARGIN_RIGHT;
    private static int HUD_MARGIN_BOTTOM;
    private static int ICON_TEXT_GAP;
    private static int TEXT_MAG_GAP;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRenderGameOverlyEvent(RenderGameOverlayEvent event){
		if(event.isCancelable() || event.getType() != ElementType.EXPERIENCE) return;
		Minecraft mc = Minecraft.getMinecraft();
        updateHudScaling(mc);
		
		EntityPlayer ply = mc.player;
		
		if(ply.isSpectator()) return;
		
		TGExtendedPlayer props = TGExtendedPlayer.get(ply);
		ItemStack item = ply.getHeldItemMainhand();
		ItemStack itemOff = ply.getHeldItemOffhand();
		ScaledResolution sr = new ScaledResolution(mc);
		
		int offsetY = sr.getScaledHeight()-32;
		
		boolean showSafemode=false;
		
		if(!item.isEmpty() && item.getItem() instanceof GenericGun){
			GenericGun gun = ((GenericGun) item.getItem());
						
			this.drawGunAmmoCount(mc, sr, gun, item, ply, props, 0);
			showSafemode=true;
		}

		if(!itemOff.isEmpty() && itemOff.getItem() instanceof GenericGun){
			if (GunManager.canUseOffhand(item,itemOff,ply)) {
				GenericGun gun = ((GenericGun) itemOff.getItem());
				if(TGConfig.cl_enableLegacyHud) this.drawGunAmmoCount(mc, sr, gun, itemOff, ply, props, -12);
				else this.drawGunAmmoCount(mc, sr, gun, itemOff, ply, props, 0);
				showSafemode=true;
			}
		}
		
		if(props != null && props.showTGHudElements && showSafemode) {
			mc.getTextureManager().bindTexture(TGPlayerInventoryGui.texture);
			this.drawTexturedModalRect(sr.getScaledWidth()-10, offsetY, 242+7*(props.enableSafemode?1:0), 14, 7,7);
		}
		
		if(props!=null && props.showTGHudElements){
			offsetY -=10;
		
			mc.getTextureManager().bindTexture(TGPlayerInventoryGui.texture);
		
			//draw power icon
			ItemStack chest =ply.inventory.armorInventory.get(2);
			if(chest.getItem() instanceof PoweredArmor){
				this.drawTexturedModalRect(sr.getScaledWidth()-10, offsetY, 249, 35, 7,7);
				
				PoweredArmor pwrchest = (PoweredArmor) chest.getItem();
				double percent = (PoweredArmor.getPower(chest)*1.0D) / (pwrchest.maxpower*1.0D);
				
				ItemStack ammoitem  = pwrchest.getBattery();
				int count = InventoryUtil.countItemInInv(ply.inventory.mainInventory, ammoitem, 0, ply.inventory.mainInventory.size());
				count += InventoryUtil.countItemInInv(props.tg_inventory.inventory, ammoitem, TGPlayerInventory.SLOTS_AMMO_START, TGPlayerInventory.SLOTS_AMMO_END+1);
				
				String text = ChatFormatting.YELLOW+""+count+"x"+ChatFormatting.WHITE+(int)(percent*100)+"%";
				mc.fontRenderer.drawString(text, sr.getScaledWidth()-2-text.length()*6-8+24, offsetY, 0xFFFFFFFF);
				offsetY-=10;
				
			}
			
			ItemStack backslot = props.tg_inventory.inventory.get(TGPlayerInventory.SLOT_BACK);
			//TODO: unhardcode this
			if (backslot.getItem() == TGItems.JETPACK || backslot.getItem() == TGItems.JUMPPACK || backslot.getItem() == TGItems.ANTI_GRAV_PACK){

				int x = 242;
				if (props.enableJetpack){
					x+=7;
				}
				//bind again because string drawing fucks it up
				mc.getTextureManager().bindTexture(TGPlayerInventoryGui.texture);
				this.drawTexturedModalRect(sr.getScaledWidth()-10, offsetY, x, 42, 7,7);

				double percent = 1.0D-(backslot.getItemDamage()*1.0f) / (backslot.getMaxDamage()*1.0f);

				ItemStack ammoitem  = ((ItemTGSpecialSlotAmmo)backslot.getItem()).getAmmo();
				int count = InventoryUtil.countItemInInv(ply.inventory.mainInventory, ammoitem, 0, ply.inventory.mainInventory.size());
				count += InventoryUtil.countItemInInv(props.tg_inventory.inventory, ammoitem, TGPlayerInventory.SLOTS_AMMO_START, TGPlayerInventory.SLOTS_AMMO_END+1);

				String text = ChatFormatting.YELLOW+""+count+"x"+ChatFormatting.WHITE+(int)(percent*100)+"%";
				mc.fontRenderer.drawString(text, sr.getScaledWidth()-2-text.length()*6-8+24, offsetY, 0xFFFFFFFF);

				offsetY-=10;

			}

			//needs rebind after string drawing
			mc.getTextureManager().bindTexture(TGPlayerInventoryGui.texture);
			if(Techguns.proxy.getHasNightvision()){
				this.drawTexturedModalRect(sr.getScaledWidth()-10, offsetY, 242+7*(props.enableNightVision?1:0), 7, 7,7);
				offsetY-=10;
			}
			if(Techguns.proxy.getHasStepassist()){
				this.drawTexturedModalRect(sr.getScaledWidth()-10, offsetY, 242+7*(props.enableStepAssist?1:0), 21, 7,7);
				offsetY-=10;
			}
			
			//Check armor durability
			byte mode=0;
			for(int i=0;i<4;i++){
				ItemStack armor=ply.inventory.armorInventory.get(i);
				if(!armor.isEmpty()){
					double dur = ((armor.getMaxDamage()-armor.getItemDamage())*1.0D)/(armor.getMaxDamage()*1.0D);
					if (dur<0.35D && mode<1){
						mode=1;
					} else if(armor.getItemDamage()>=(armor.getMaxDamage()-1)){
						mode=2;
						break;
					}
				}
				
			}
			if(mode==2){
				this.drawTexturedModalRect(sr.getScaledWidth()-10, offsetY, 242+7, 28, 7,7);
				offsetY-=10;
			} else if (mode==1){
				this.drawTexturedModalRect(sr.getScaledWidth()-10, offsetY, 242, 28, 7,7);
				offsetY-=10;
			}
			
			if(props.radlevel>0) {
				
				
				String prefix = ChatFormatting.WHITE.toString();
				if(props.radlevel>=1000) {
					prefix = ChatFormatting.RED.toString();
				} else if ( props.radlevel >= 750) {
					prefix = ChatFormatting.GOLD.toString();
				} else if (props.radlevel >= 500 ) {
					prefix = ChatFormatting.YELLOW.toString();
				}
				String radtext = props.radlevel+ " RAD";
				mc.fontRenderer.drawString(prefix+radtext, sr.getScaledWidth()-radtext.length()*6, offsetY, 0xFFFFFFFF);
				
				offsetY-=10;
				
				String currentRadText;
				PotionEffect currentRad = ply.getActivePotionEffect(TGRadiationSystem.radiation_effect);
				if(currentRad!=null) {
					int res = (int) ply.getEntityAttribute(TGRadiation.RADIATION_RESISTANCE).getAttributeValue();
					
					int amount = techguns.util.MathUtil.clamp(currentRad.getAmplifier()+1-res, 0, 1000);
					
					currentRadText= "+["+amount+" RAD/s]";
					mc.fontRenderer.drawString(currentRadText, sr.getScaledWidth()-currentRadText.length()*6 + 4, offsetY, 0xFFFFFFFF);
				}
			}
			
		}

	}

    private static void updateHudScaling(Minecraft mc) {
        int guiScale = mc.gameSettings.guiScale == 0 ? 4 : mc.gameSettings.guiScale;
        float scaleFactor = 4F / guiScale;

        AMMO_TEXT_SCALE = TGConfig.cl_ammoTextScale * scaleFactor;
        AMMO_ICON_SCALE = TGConfig.cl_ammoIconScale * scaleFactor;
        AMMO_MAG_TEXT_SCALE = TGConfig.cl_ammoMagTextScale * scaleFactor;
        HUD_MARGIN_RIGHT = (int)(TGConfig.cl_hudMarginRight * scaleFactor);
        HUD_MARGIN_BOTTOM = (int)(TGConfig.cl_hudMarginBottom * scaleFactor);
        ICON_TEXT_GAP = (int)(TGConfig.cl_iconTextGap * scaleFactor);
        TEXT_MAG_GAP = (int)(TGConfig.cl_textMagGap * scaleFactor);
    }
	
	private int getAmmoCountOfStack(ItemStack ammoitem, GenericGun gun, EntityPlayer ply, TGExtendedPlayer props) {
		int count = InventoryUtil.countItemInInv(ply.inventory.mainInventory, ammoitem, 0, ply.inventory.mainInventory.size());
		count += InventoryUtil.countItemInInv(props.tg_inventory.inventory, ammoitem, TGPlayerInventory.SLOTS_AMMO_START, TGPlayerInventory.SLOTS_AMMO_END+1);
		
		if (gun.getAmmoCount()>1){
			count = count / gun.getAmmoCount();
		} 
		return count;
	}

    private void drawGunAmmoCount(Minecraft mc, ScaledResolution sr, GenericGun gun, ItemStack item, EntityPlayer ply, TGExtendedPlayer props, int offsetY) {
        ItemStack[] ammoitem  = gun.getReloadItem(item);
        ItemStack ammoIcon = EnumAmmoMagConversion.getAmmo(gun.getAmmoType().getAmmo(gun.getCurrentAmmoVariant(item))[0]);
        int minCount = 0;
        for(ItemStack stack: ammoitem) {
            int c = getAmmoCountOfStack(stack, gun, ply, props);
            if (c>minCount) {
                minCount = c;
            }
        }

        EnumAmmoRenderTypes type = EnumAmmoRenderTypes.getEnum(ammoIcon);
        if (type == EnumAmmoRenderTypes.EMPTY) {
            return;
        }

        if (!TGConfig.cl_enableLegacyHud) {
            int screenW = sr.getScaledWidth();
            int screenH = sr.getScaledHeight();

            boolean offhand = ply.getHeldItemOffhand() == item;

            String ammoText = gun.getAmmoLeftCountTooltip(item)+"/"+gun.getClipsizeTooltip();
            String fullAmmoText = gun.getClipsizeTooltip()+"/"+gun.getClipsizeTooltip();

            int ammoTextWidth = mc.fontRenderer.getStringWidth(ammoText);
            int maxAmmoTextWidth = mc.fontRenderer.getStringWidth(fullAmmoText);

            int scaledTextWidth = Math.round(ammoTextWidth * AMMO_TEXT_SCALE);
            int scaledMaxTextWidth = Math.round(maxAmmoTextWidth * AMMO_TEXT_SCALE);
            int scaledTextHeight = Math.round(mc.fontRenderer.FONT_HEIGHT * AMMO_TEXT_SCALE);

            float scaledIconWidth = type.width * AMMO_ICON_SCALE;
            float scaledIconHeight = type.height * AMMO_ICON_SCALE;

            String magText = ChatFormatting.YELLOW + "x" + minCount;
            int magTextWidth = mc.fontRenderer.getStringWidth(magText);
            int scaledMagTextWidth = Math.round(magTextWidth * AMMO_MAG_TEXT_SCALE);

            int textDrawY = screenH - HUD_MARGIN_BOTTOM - scaledTextHeight + offsetY;
            int textCenterY = textDrawY + scaledTextHeight / 2;
            int iconY = Math.round(textCenterY - scaledIconHeight / 2.0F);

            if (!offhand) {
                int textRight = screenW - HUD_MARGIN_RIGHT;
                int textDrawX = textRight - scaledTextWidth;

                int iconX = Math.round(textRight - scaledMaxTextWidth - ICON_TEXT_GAP - scaledIconWidth);

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) textDrawX, (float) textDrawY, 0.0F);
                GlStateManager.scale(AMMO_TEXT_SCALE, AMMO_TEXT_SCALE, 1.0F);
                mc.fontRenderer.drawStringWithShadow(ammoText, 0, 0, 0xFFFFFFFF);
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) iconX, (float) iconY, 0.0F);
                GlStateManager.scale(AMMO_ICON_SCALE, AMMO_ICON_SCALE, 1.0F);
                type.renderAmmoIcon(mc, 0, 0);
                GlStateManager.popMatrix();

                int magX = textRight - scaledMagTextWidth;
                int magY = textDrawY + scaledTextHeight + TEXT_MAG_GAP;

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) magX, (float) magY, 0.0F);
                GlStateManager.scale(AMMO_MAG_TEXT_SCALE, AMMO_MAG_TEXT_SCALE, 1.0F);
                mc.fontRenderer.drawStringWithShadow(magText, 0, 0, 0xFFFFFFFF);
                GlStateManager.popMatrix();
            } else {
                int textLeft = HUD_MARGIN_RIGHT;

                int iconX = Math.round(textLeft + scaledMaxTextWidth + ICON_TEXT_GAP);

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) textLeft, (float) textDrawY, 0.0F);
                GlStateManager.scale(AMMO_TEXT_SCALE, AMMO_TEXT_SCALE, 1.0F);
                mc.fontRenderer.drawStringWithShadow(ammoText, 0, 0, 0xFFFFFFFF);
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) iconX, (float) iconY, 0.0F);
                GlStateManager.scale(AMMO_ICON_SCALE, AMMO_ICON_SCALE, 1.0F);
                type.renderAmmoIcon(mc, 0, 0);
                GlStateManager.popMatrix();

                int magY = textDrawY + scaledTextHeight + TEXT_MAG_GAP;

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) textLeft, (float) magY, 0.0F);
                GlStateManager.scale(AMMO_MAG_TEXT_SCALE, AMMO_MAG_TEXT_SCALE, 1.0F);
                mc.fontRenderer.drawStringWithShadow(magText, 0, 0, 0xFFFFFFFF);
                GlStateManager.popMatrix();
            }
        } else {
            String text= gun.getAmmoLeftCountTooltip(item)+"/"+gun.getClipsizeTooltip() +ChatFormatting.YELLOW+"x" +minCount;
            mc.fontRenderer.drawString(text, sr.getScaledWidth()+1-text.length()*6,sr.getScaledHeight()-mc.fontRenderer.FONT_HEIGHT-2+offsetY , 0xFFFFFFFF);
        }
    }
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRenderCrosshairs(RenderGameOverlayEvent event){
		if(event.isCanceled() || event.getType() != ElementType.CROSSHAIRS) return;
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
				
		if (player.getHeldItemMainhand().getItem() instanceof GenericGun) {
			if(mc.gameSettings.thirdPersonView != 0) {
				return;
			}
			
			/*
			 * Render Lock on GUI effect
			 */
			ScaledResolution sr = new ScaledResolution(mc);
			TGExtendedPlayer epc = TGExtendedPlayer.get(player);
			
			int x = sr.getScaledWidth() / 2;
			int y = sr.getScaledHeight() / 2;

			GenericGun gun = (GenericGun)player.getHeldItemMainhand().getItem();
			if (gun.getLockOnTicks() > 0 && epc.lockOnEntity != null && epc.lockOnTicks > 0) {
				event.setCanceled(true);
				
				float maxTicks = (float)gun.getLockOnTicks(); //TODO: Store in capabilities
				float progress = (float)epc.lockOnTicks/maxTicks;
				
				mc.getTextureManager().bindTexture(crosshairsTexture);

				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();
				
				int offset = (int)(Math.max(0.0f, (1.0f-progress)*16.0f))+5;
				//Outer parts
				
				this.drawTexturedModalRect(x-offset-3, y-offset-3, 0, 0, 7,7);
				this.drawTexturedModalRect(x+offset-3, y-offset-3, 7, 0, 7,7);
				this.drawTexturedModalRect(x-offset-3, y+offset-3, 14, 0, 7,7);
				this.drawTexturedModalRect(x+offset-3, y+offset-3, 21, 0, 7,7);
				
				//draw center dot
				this.drawTexturedModalRect(x, y, 0, 0, 1, 1);
				
				if (progress < 1.0f) {
					String text = "Locking... : "+epc.lockOnEntity.getName();
					mc.fontRenderer.drawString(text, (int)(sr.getScaledWidth_double()*0.5)+2, (int)(sr.getScaledHeight_double()*0.5)+10, 0xFFFFFFFF);
				} else {
					this.drawTexturedModalRect(x-6, y-6, 28, 0, 13,13);
					if (Minecraft.getSystemTime() / 250 % 2 == 0) {
						this.drawTexturedModalRect(x-9, y-9, 41, 0, 19,19);
					}
					
						
					String text = "Locked On: "+epc.lockOnEntity.getName();
					mc.fontRenderer.drawString(text, (int)(sr.getScaledWidth_double()*0.5)+2, (int)(sr.getScaledHeight_double()*0.5)+10, 0xFFFF0000);
				}
				
				//Restore settings
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();
			} else {
				//gun crosshair
				if(gun.isZooming()) {
					IItemRenderer irenderer = ItemRenderHack.getRendererForItem(gun);
					if(irenderer instanceof RenderGunBase) {
						RenderGunBase rgun = (RenderGunBase) irenderer;
						
						if(rgun.hasScopeTexture()) {
							event.setCanceled(true);
							return;
						}
					}
				}
				
					if(gun.getCrossHairStyle() != EnumCrosshairStyle.VANILLA) {
						event.setCanceled(true);
		
						//Vanilla does this setting too
					    Minecraft.getMinecraft().getTextureManager().bindTexture(tgCrosshairsTexture);
					    GlStateManager.enableBlend();
		
		                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		                GlStateManager.enableAlpha();
		                //end vanilla gl states
						
		                float spread = gun.getSpread();
		                
		            	if(player.isActiveItemStackBlocking()) {
			        		if(gun.getHandType()==GunHandType.ONE_HANDED) {
			        			spread *= 4.0f;
			        		} else {
			        			spread *= 8.0f;
			        		}
			        	}
			        	if(gun.isZooming()){
			        		spread*=gun.getZoombonus();
			        	}
			        	
		                int spacing = MathUtil.clamp(Math.round(100*spread),1,10);
		                
		                if (gun.getCrossHairStyle()==EnumCrosshairStyle.GUN_DYNAMIC) {
		                 
			                this.drawTexturedModalRect(x - (4+spacing) , y,3,7,4,1);
			                this.drawTexturedModalRect(x + (1+spacing) , y,8,7,4,1);
			                this.drawTexturedModalRect(x , y,7,7,1,1);
			                
			                this.drawTexturedModalRect(x, y - (4+spacing), 7, 3, 1,4);
			                this.drawTexturedModalRect(x, y + (1+spacing), 7, 8, 1,4);
			                
		                } else {
		                	EnumCrosshairStyle crosshair = gun.getCrossHairStyle();
		                	
		                	switch(crosshair.dynamicType) {
								case BOTH:
									//draw the center
									this.drawTexturedModalRect(x-1, y-1, crosshair.getLocX()+6, crosshair.getLocY()+6, 3, 3);
									
									this.drawTexturedModalRect(x-6-spacing, y-2, crosshair.getLocX(), crosshair.getLocY()+5, 6, 5);
									this.drawTexturedModalRect(x+1+spacing, y-2, crosshair.getLocX()+9, crosshair.getLocY()+5, 6, 5);
									
									this.drawTexturedModalRect(x-2, y-6-spacing, crosshair.getLocX()+5, crosshair.getLocY(), 5, 6);
									this.drawTexturedModalRect(x-2, y+1+spacing, crosshair.getLocX()+5, crosshair.getLocY()+9, 5, 6);
									
									break;
								case HORIZONTAL:
									this.drawTexturedModalRect(x-1, y-1, crosshair.getLocX()+6, crosshair.getLocY()+6, 3, 3);
									
									this.drawTexturedModalRect(x-6-spacing, y-7, crosshair.getLocX(), crosshair.getLocY(), 6, 16);
									this.drawTexturedModalRect(x+1+spacing, y-7, crosshair.getLocX()+9, crosshair.getLocY(), 6, 16);
									break;
									
								case X:
									this.drawTexturedModalRect(x-1, y-1, crosshair.getLocX()+6, crosshair.getLocY()+6, 3, 3);
									
									this.drawTexturedModalRect(x-6-spacing, y-6-spacing, crosshair.getLocX(), crosshair.getLocY(), 6, 6);
									this.drawTexturedModalRect(x+1+spacing, y-6-spacing, crosshair.getLocX()+9, crosshair.getLocY(), 6, 6);
									
									this.drawTexturedModalRect(x-6-spacing, y+1+spacing, crosshair.getLocX(), crosshair.getLocY()+9, 6, 6);
									this.drawTexturedModalRect(x+1+spacing, y+1+spacing, crosshair.getLocX()+9, crosshair.getLocY()+9, 6, 6);
									break;
									
								case TRI:
									this.drawTexturedModalRect(x-1, y-1, crosshair.getLocX()+6, crosshair.getLocY()+6, 3, 3);
									
									this.drawTexturedModalRect(x-7, y-6-spacing, crosshair.getLocX(), crosshair.getLocY(), 16, 6);
									
									this.drawTexturedModalRect(x-6-spacing, y+1+spacing, crosshair.getLocX(), crosshair.getLocY()+9, 6, 6);
									this.drawTexturedModalRect(x+1+spacing, y+1+spacing, crosshair.getLocX()+9, crosshair.getLocY()+9, 6, 6);
									break;
									
								case TRI_INV:
								
									this.drawTexturedModalRect(x-1, y-1, crosshair.getLocX()+6, crosshair.getLocY()+6, 3, 3);
									
									this.drawTexturedModalRect(x-6-spacing, y-6-spacing, crosshair.getLocX(), crosshair.getLocY(), 6, 6);
									this.drawTexturedModalRect(x+1+spacing, y-6-spacing, crosshair.getLocX()+9, crosshair.getLocY(), 6, 6);
									
									this.drawTexturedModalRect(x-7, y+1+spacing, crosshair.getLocX(), crosshair.getLocY()+9, 16, 6);
									
									break;
									
								case VERTICAL:
								default:
									this.drawTexturedModalRect(x-7, y-7, crosshair.getLocX(), crosshair.getLocY(), 16,16);
									break;
		                	} 
		                	 
		                }
		                
		                //do same bind as vanilla afterwards
		                Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
				}
			}
			
		}
		
	}
}

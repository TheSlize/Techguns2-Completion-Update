package techguns.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import org.jetbrains.annotations.NotNull;
import techguns.TGItems;
import techguns.TGSounds;
import techguns.Techguns;
import techguns.api.capabilities.ITGExtendedPlayer;
import techguns.api.render.IItemTGRenderer;
import techguns.api.tginventory.ITGSpecialSlot;
import techguns.api.tginventory.TGSlotType;
import techguns.capabilities.TGExtendedPlayerCapProvider;
import techguns.items.armors.TGArmorBonus;
import techguns.tileentities.operation.UpgradeBenchRecipes;
import techguns.tileentities.operation.UpgradeBenchRecipes.UpgradeBenchRecipe;
import techguns.util.InventoryUtil;
import techguns.util.TextUtil;

public class GenericItemShared extends GenericItem implements IItemTGRenderer, ITGSpecialSlot {
	protected ArrayList<SharedItemEntry> sharedItems = new ArrayList<>();
	
	public GenericItemShared() {
		super("itemshared", false);
	}

	public ItemStack addsharedVariant(String name){
		return addsharedVariant(name, false);
	}
	
	public ItemStack addsharedVariant(String name, boolean useRenderHack){
		return addsharedVariant(name, useRenderHack, TGSlotType.NORMAL);
	}
	
	public ItemStack addsharedVariant(String name, TGSlotType slottype){
		return addsharedVariant(name, false, slottype);
	}
	
	public ItemStack addsharedVariant(String name, boolean useRenderHack, TGSlotType slottype){
		return addsharedVariant(name, useRenderHack,slottype,64,true);
	}
	
	public ItemStack addsharedVariantOptional(String name, boolean enabled) {
		return this.addsharedVariant(name, false, TGSlotType.NORMAL, 64, enabled);
	}
	
	public ItemStack addsharedVariant(String name, boolean useRenderHack, TGSlotType slottype, int maxStackSize, boolean enabled){
		int newMeta = sharedItems.size();
		sharedItems.add(new SharedItemEntry(name, newMeta, slottype, (short)maxStackSize, useRenderHack, enabled));
		return new ItemStack(this,1,newMeta);
	}
	
	
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		int dmg = stack.getItemDamage();
		if (dmg <this.sharedItems.size()) {
			return this.sharedItems.get(dmg).maxStackSize;
		}
		return super.getItemStackLimit(stack);
	}

	public SharedItemEntry get(int damageValue){
		return this.sharedItems.get(damageValue);
	}
	
	public static class SharedItemEntry {
		protected String name;
		protected TGSlotType slottype;
		protected short maxStackSize;
		protected int meta;
		protected boolean useRenderHack;
		protected boolean enabled;
		protected SharedItemAmmoEntry ammoEntry;
		
		public SharedItemEntry(String name, int meta, TGSlotType slottype, short maxStackSize, boolean useRenderHack, boolean enabled) {
			super();
			this.name = name;
			this.slottype = slottype;
			this.maxStackSize = maxStackSize;
			this.meta = meta;
			this.useRenderHack=useRenderHack;
			this.enabled = enabled;
		}

		public String getName() {
			return name;
		}

        public int getMeta() {
			return meta;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public SharedItemAmmoEntry getAmmoEntry() {
			return ammoEntry;
		}

		public boolean usesRenderHack() { return useRenderHack; }
	
		public void setAmmoType(ItemStack bullet, ItemStack magazine,  int amountBullet) {
			this.ammoEntry = new SharedItemAmmoEntry(bullet, magazine, amountBullet);
		}
	}

	public static class SharedItemAmmoEntry {
		public ItemStack bullet;
		public ItemStack magazine;
		public int amountBullet;
		public int amountMag;
		
		public SharedItemAmmoEntry(ItemStack bullet, ItemStack magazine, int amountBullet, int amountMag) {
			super();
			this.bullet = bullet;
			this.magazine = magazine;
			this.amountBullet = amountBullet;
			this.amountMag = amountMag;
		}

		public SharedItemAmmoEntry(ItemStack bullet, ItemStack magazine, int amountBullet) {
			this(bullet, magazine, amountBullet,1);
		}
		
	}

	@Override
	public void initModel() {
        for (int i = 0; i < this.sharedItems.size(); i++) {
            ResourceLocation rl = new ResourceLocation(Techguns.MODID, this.sharedItems.get(i).name);

            if (i == TGItems.PLATE_COPPER.getItemDamage()) rl = new ResourceLocation(Techguns.MODID, "platecopper");
            if (i == TGItems.PLATE_STEEL.getItemDamage())  rl = new ResourceLocation(Techguns.MODID, "platesteel");

            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(rl, "inventory"));
        }
	}

	public ArrayList<SharedItemEntry> getSharedItems() {
		return sharedItems;
	}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@Override
	public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
		if(this.isInCreativeTab(tab)){
			for (int i=0;i<this.sharedItems.size();i++){
				if ( this.sharedItems.get(i).isEnabled()) {
					items.add(new ItemStack(this,1,i));
				}
			}
		}
	}

	@Override
	public @NotNull String getTranslationKey(ItemStack stack) {
		return "item."+Techguns.MODID+"."+getSharedItems().get(stack.getItemDamage()).name;
	}

	@Override
	public boolean shouldUseRenderHack(ItemStack stack) {
		return this.getSharedItems().get(stack.getItemDamage()).useRenderHack;
	}

	@Override
	public TGSlotType getSlot(ItemStack item) {
		int dmg = item.getItemDamage();
		if (dmg <this.sharedItems.size()) {
			return this.sharedItems.get(dmg).slottype;
		}
		return TGSlotType.NORMAL;
	}

	protected SharedItemEntry getSharedEntry(ItemStack item) {
		int dmg = item.getItemDamage();
		if (dmg <this.sharedItems.size()) {
			return this.sharedItems.get(dmg);
		}
		return null;
	}

	
	@Override
	public float getBonus(TGArmorBonus type, ItemStack stack, boolean consume, EntityPlayer player) {
		if(stack.getItemDamage() == TGItems.OXYGEN_MASK.getItemDamage()) {
			if(type == TGArmorBonus.OXYGEN_GEAR){
				return 1.0f;
			}
		} else if (stack.getItemDamage() == TGItems.WORKING_GLOVES.getItemDamage()) {
			if(type==TGArmorBonus.BREAKSPEED) {
				return 0.10f;
			}
		}
		return 0f;
	}

	private float getBonus(TGArmorBonus type, ItemStack stack) {
		return getBonus(type, stack, false, null);
	}
	
	@Override
	public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getItemDamage() == TGItems.BLUEPRINTS.getItemDamage()) {
            tooltip.addAll(Arrays.asList(TextUtil.resolveKeyArray("techguns.tooltip.blueprints")));
        }
		if(this.getSlot(stack) != TGSlotType.NORMAL) {
			tooltip.add(ChatFormatting.GRAY+TextUtil.transTG("tooltip.slot")+": "+this.getSlot(stack));
			if(this.getSlot(stack) == TGSlotType.AMMOSLOT) {
				tooltip.add(ChatFormatting.GRAY+TextUtil.transTG("tooltip.rightclickammoslot"));
				
				if(this.getSharedEntry(stack).ammoEntry!=null){
					tooltip.add(ChatFormatting.GRAY+TextUtil.transTG("tooltip.sneakrightclickunload"));
				}
			} else if (this.getSlot(stack) == TGSlotType.ARMOR_UPGRADE) {
				this.addArmorEnchantTooltip(stack, tooltip, flagIn);
			}
		}
		if(this.getBonus(TGArmorBonus.EXTRA_HEART, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.healthbonus")+": +"+(int)this.getBonus(TGArmorBonus.EXTRA_HEART, stack)+" "+TextUtil.transTG("armorTooltip.hearts"));
		} else if (this.getBonus(TGArmorBonus.SPEED, stack) != 0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.movespeed")+": +"+this.getBonus(TGArmorBonus.SPEED, stack)*100.0f+"%");
		} else if(this.getBonus(TGArmorBonus.JUMP, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.jumpheight")+": +"+this.getBonus(TGArmorBonus.JUMP, stack));
		} if(this.getBonus(TGArmorBonus.FALLDMG, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.falldamage")+": -"+this.getBonus(TGArmorBonus.FALLDMG, stack)*100.0f+"%");
		} if(this.getBonus(TGArmorBonus.FREEHEIGHT, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.fallheight")+": -"+this.getBonus(TGArmorBonus.FREEHEIGHT, stack));
		} if(this.getBonus(TGArmorBonus.BREAKSPEED, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.miningspeed")+": +"+this.getBonus(TGArmorBonus.BREAKSPEED, stack)*100.0f+"%");
		} if(this.getBonus(TGArmorBonus.BREAKSPEED_WATER, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.underwatermining")+": +"+this.getBonus(TGArmorBonus.BREAKSPEED_WATER, stack)*100.0f+"%");
		} if(this.getBonus(TGArmorBonus.KNOCKBACK_RESISTANCE, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.knockbackresistance")+": +"+this.getBonus(TGArmorBonus.KNOCKBACK_RESISTANCE, stack)*100.0f+"%");
		} if(this.getBonus(TGArmorBonus.NIGHTVISION, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.nightvision"));
		} if(this.getBonus(TGArmorBonus.STEPASSIST, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.stepassist"));
		} if(this.getBonus(TGArmorBonus.OXYGEN_GEAR, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.oxygengear"));
		} if(this.getBonus(TGArmorBonus.COOLING_SYSTEM, stack)>0.0f){
			tooltip.add(TextUtil.transTG("armorTooltip.coolingsystem"));
		} 
		if(stack.getItemDamage() == TGItems.OREDRILLHEAD_STEEL.getItemDamage() || stack.getItemDamage() == TGItems.OREDRILLHEAD_MEDIUM_STEEL.getItemDamage() || stack.getItemDamage() == TGItems.OREDRILLHEAD_LARGE_STEEL.getItemDamage()) {
			tooltip.add(TextUtil.transTG("oredrill.mininglevel")+" +1");
		} else if (stack.getItemDamage() == TGItems.OREDRILLHEAD_OBSIDIANSTEEL.getItemDamage() || stack.getItemDamage() == TGItems.OREDRILLHEAD_MEDIUM_OBSIDIANSTEEL.getItemDamage() || stack.getItemDamage() == TGItems.OREDRILLHEAD_LARGE_OBSIDIANSTEEL.getItemDamage()) {
			tooltip.add(TextUtil.transTG("oredrill.mininglevel")+ " +2");
		} else if (stack.getItemDamage() == TGItems.OREDRILLHEAD_CARBON.getItemDamage() || stack.getItemDamage() == TGItems.OREDRILLHEAD_MEDIUM_CARBON.getItemDamage() || stack.getItemDamage() == TGItems.OREDRILLHEAD_LARGE_CARBON.getItemDamage()) {
			tooltip.add(TextUtil.transTG("oredrill.mininglevel")+ " +3");
		}
	}
	
	protected void addArmorEnchantTooltip(ItemStack stack, List<String> tooltip, ITooltipFlag flagIn) {
		UpgradeBenchRecipe rec = UpgradeBenchRecipes.getUpgradeRecipeForUpgradeItem(stack);
		if(rec != null) {

			Enchantment ench = rec.getEnch();
			tooltip.add(TextFormatting.AQUA + ench.getTranslatedName(rec.getLevel()));
			
			if(flagIn.isAdvanced() && ench.type != null) {
				tooltip.add(" " + ench.type);
			}
		}
	}

	
	protected static void addAmmoToPlayerOrDropInWorld(EntityPlayer player, ItemStack stack) {
		int amount = InventoryUtil.addAmmoToPlayerInventory(player, stack);
		
		//only drop on server side
		if (amount >0 && !player.world.isRemote) {
			ItemStack stackDrop = stack.copy();
			stackDrop.setCount(amount);
			InventoryHelper.spawnItemStack(player.world, player.posX, player.posY, player.posZ, stackDrop);
		}
	}
	
	@Override
	public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand) {
		
		ItemStack stack = player.getHeldItem(hand);

        if (stack.getItemDamage() == TGItems.BLUEPRINTS.getItemDamage()) {
            ITGExtendedPlayer ext = player.getCapability(TGExtendedPlayerCapProvider.TG_EXTENDED_PLAYER, null);
            if (ext != null) {
                boolean newlyUnlocked = ext.unlockFabricatorRecipe(TGItems.CYBERNETIC_PARTS);
                if (newlyUnlocked) {
                    if(!world.isRemote) stack.shrink(1);
                    else player.sendMessage(new TextComponentTranslation("techguns.message.bpunlock"));
                    player.playSound(TGSounds.BLUEPRINT_UNLOCK, 1.0f, 1.0f);
                }
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

		if (player.isSneaking()){
			SharedItemEntry entry = this.getSharedEntry(stack);
			if(entry!=null) {
				SharedItemAmmoEntry ammoEntry=entry.getAmmoEntry();
				if(ammoEntry!=null) {
					ItemStack stackBullet = ammoEntry.bullet.copy();
					stackBullet.setCount(ammoEntry.amountBullet);
					
					ItemStack stackMag = ammoEntry.magazine.copy();
					stackMag.setCount(ammoEntry.amountMag);
					
					addAmmoToPlayerOrDropInWorld(player, stackBullet);
					addAmmoToPlayerOrDropInWorld(player, stackMag);
					
					stack.shrink(1);
					
					return new ActionResult<>(EnumActionResult.SUCCESS, stack);
				}
				
			}
			
		} else if(this.getSlot(stack) == TGSlotType.AMMOSLOT) {
			
			ItemStack newStack = stack.copy();
			int amount = InventoryUtil.addAmmoToAmmoInventory(player, newStack);
			
			Techguns.logger.warn("amount not merged:"+amount);
			
			stack.setCount(amount);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		
		return super.onItemRightClick(world, player, hand);
	}
	
	
}

package techguns.plugins.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techguns.*;
import techguns.api.damagesystem.DamageType;
import techguns.items.armors.EnumArmorStat;
import techguns.items.armors.GenericArmor;
import techguns.items.armors.PoweredArmor;
import techguns.items.armors.TGArmorMaterial;
import techguns.util.MathUtil;

@ZenClass("mods.techguns.ArmorStats")
public class ArmorStatTweaker {

	@ZenMethod
	public static void setArmorStat(String armorname, String stat, float value) {
		CraftTweakerAPI.apply(new setArmorStatAction(armorname, stat, value));
	}
	
	@ZenMethod
	public static void setArmorStat(String armorname, String stat, float powered, float unpowered) {
		CraftTweakerAPI.apply(new setArmorStatAction(armorname, stat, powered, unpowered));
	}
	
	@ZenMethod
	public static void setMaterialArmorValue(String material, String damagetype, float amount) {
		CraftTweakerAPI.apply(new setArmorMaterialValue(material, damagetype, amount));
	}

	@ZenMethod
	public static void setArmorDurability(String armorname, int durability) {
		CraftTweakerAPI.apply(new setArmorDurabilityAction(armorname, durability));
	}

	@ZenMethod
	public static void setArmorDisplayValue(String armorname, int value) {
		CraftTweakerAPI.apply(new setArmorDisplayValueAction(armorname, value));
	}

	@ZenMethod
	public static void setMaterialBaseDurability(String material, int baseDurability) {
		CraftTweakerAPI.apply(new setArmorMaterialBaseDurabilityAction(material, baseDurability));
	}

	@ZenMethod
	public static void setMaterialDurabilityFactor(String material, String slot, float factor) {
		CraftTweakerAPI.apply(new setArmorMaterialDurabilityFactorAction(material, slot, factor));
	}

	@ZenMethod
	public static void setMaterialToughness(String material, float amount) {
		CraftTweakerAPI.apply(new setArmorMaterialToughnessAction(material, amount));
	}

	@ZenMethod
	public static void setArmorKnockbackResistance(String armorname, float value) {
		CraftTweakerAPI.apply(new setArmorKnockbackResistanceAction(armorname, value));
	}

	@ZenMethod
	public static void setArmorRadResistance(String armorname, float value) {
		CraftTweakerAPI.apply(new setArmorRadResistanceAction(armorname, value));
	}

	@ZenMethod
	public static void setArmorHiddenSlots(String armorname, boolean hideFace, boolean hideBack, boolean hideGlove) {
		CraftTweakerAPI.apply(new setArmorHiddenSlotsAction(armorname, hideFace, hideBack, hideGlove));
	}

	@ZenMethod
	public static void setArmorRepairMats(String armorname, IItemStack metal, IItemStack cloth, float metalPercent, int totalMats) {
		CraftTweakerAPI.apply(new setArmorRepairMatsAction(armorname, metal, cloth, metalPercent, totalMats));
	}

	@ZenMethod
	public static void setPoweredArmorMaxPower(String armorname, int maxPower) {
		CraftTweakerAPI.apply(new setPoweredArmorMaxPowerAction(armorname, maxPower));
	}

	@ZenMethod
	public static void setPoweredArmorBattery(String armorname, IItemStack battery) {
		CraftTweakerAPI.apply(new setPoweredArmorBatteryAction(armorname, battery, null));
	}

	@ZenMethod
	public static void setPoweredArmorBattery(String armorname, IItemStack battery, IItemStack emptyBattery) {
		CraftTweakerAPI.apply(new setPoweredArmorBatteryAction(armorname, battery, emptyBattery));
	}

	private static GenericArmor getArmor(String armorname) {
		Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(Tags.MOD_ID, armorname));
		if (item instanceof GenericArmor) {
			return (GenericArmor) item;
		}
		return null;
	}

	private static PoweredArmor getPoweredArmor(String armorname) {
		Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(Tags.MOD_ID, armorname));
		if (item instanceof PoweredArmor) {
			return (PoweredArmor) item;
		}
		return null;
	}

	private static TGArmorMaterial getArmorMaterial(String materialname) {
		for (TGArmorMaterial mat : TGArmorMaterial.MATERIALS) {
			if (mat.name.equalsIgnoreCase(materialname)) {
				return mat;
			}
		}
		return null;
	}

	private static void refreshArmorsForMaterial(TGArmorMaterial material) {
		for (GenericArmor armor : TGArmors.armors) {
			if (armor.getTGArmorMaterial() == material) {
				armor.refreshMaterialDerivedStats();
			}
		}
	}

	private static EntityEquipmentSlot parseArmorSlot(String slotname) {
		for (EntityEquipmentSlot slot : new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET}) {
			if (slot.name().equalsIgnoreCase(slotname)) {
				return slot;
			}
		}
		return null;
	}
	
	
	private static class setArmorMaterialValue implements IAction {

		String materialname;
		float value;
		String typename;
		
		DamageType type;
		TGArmorMaterial material;
		
		public setArmorMaterialValue(String materialname, String typename, float value) {
			super();
			this.materialname = materialname;
			this.value = MathUtil.clamp(value, 0f, 24f);
			this.typename = typename;
			
			for(DamageType dt : DamageType.values()) {
				if(dt.name().equalsIgnoreCase(typename)) {
					this.type=dt;
					break;
				}
			}

			this.material = getArmorMaterial(materialname);
		}

		@Override
		public void apply() {
			if(this.material!=null && this.type!=null) {
				this.material.setArmorValueForType(type, value);
				refreshArmorsForMaterial(this.material);
			}
		}

		@Override
		public String describe() {
			if(material==null) {
				return "Failed setting ArmorValue ["+typename+"] for Material: ["+materialname+"]: MATERIAL DOES NOT EXIST!";
			}
			if (type==null) {
				return "Failed setting ArmorValue ["+typename+"] for Material: ["+materialname+"]: UNKNOWN DAMAGE TYPE!";
			}

			return "Set ArmorValue ["+typename+"] for Material: ["+materialname+"] to: "+value;
		}
		
	}

	private static class setArmorDurabilityAction implements IAction {
		protected String armorname;
		protected int durability;
		protected GenericArmor armor;

		public setArmorDurabilityAction(String armorname, int durability) {
			this.armorname = armorname;
			this.durability = Math.max(1, durability);
			this.armor = getArmor(armorname);
		}

		@Override
		public void apply() {
			if (this.armor != null) {
				this.armor.setDurability(this.durability);
			}
		}

		@Override
		public String describe() {
			if (this.armor == null) {
				return "Failed setting durability for Armor: [" + armorname + "]: ITEM IS NOT A TECHGUNS ARMOR";
			}
			return "Set durability for Armor: [" + armorname + "] to: " + durability;
		}
	}

	private static class setArmorDisplayValueAction implements IAction {
		protected String armorname;
		protected int value;
		protected GenericArmor armor;

		public setArmorDisplayValueAction(String armorname, int value) {
			this.armorname = armorname;
			this.value = Math.max(0, value);
			this.armor = getArmor(armorname);
		}

		@Override
		public void apply() {
			if (this.armor != null) {
				this.armor.setArmorDisplayValue(this.value);
			}
		}

		@Override
		public String describe() {
			if (this.armor == null) {
				return "Failed setting display armor value for Armor: [" + armorname + "]: ITEM IS NOT A TECHGUNS ARMOR";
			}
			return "Set display armor value for Armor: [" + armorname + "] to: " + value;
		}
	}

	private static class setArmorMaterialBaseDurabilityAction implements IAction {
		protected String materialname;
		protected int baseDurability;
		protected TGArmorMaterial material;

		public setArmorMaterialBaseDurabilityAction(String materialname, int baseDurability) {
			this.materialname = materialname;
			this.baseDurability = Math.max(1, baseDurability);
			this.material = getArmorMaterial(materialname);
		}

		@Override
		public void apply() {
			if (this.material != null) {
				this.material.setBaseDurability(this.baseDurability);
				refreshArmorsForMaterial(this.material);
			}
		}

		@Override
		public String describe() {
			if (this.material == null) {
				return "Failed setting base durability for Material: [" + materialname + "]: MATERIAL DOES NOT EXIST!";
			}
			return "Set base durability for Material: [" + materialname + "] to: " + baseDurability;
		}
	}

	private static class setArmorMaterialDurabilityFactorAction implements IAction {
		protected String materialname;
		protected String slotname;
		protected float factor;
		protected TGArmorMaterial material;
		protected EntityEquipmentSlot slot;

		public setArmorMaterialDurabilityFactorAction(String materialname, String slotname, float factor) {
			this.materialname = materialname;
			this.slotname = slotname;
			this.factor = Math.max(0.0f, factor);
			this.material = getArmorMaterial(materialname);
			this.slot = parseArmorSlot(slotname);
		}

		@Override
		public void apply() {
			if (this.material != null && this.slot != null) {
				this.material.setDurabilityFactor(this.slot, this.factor);
				refreshArmorsForMaterial(this.material);
			}
		}

		@Override
		public String describe() {
			if (this.material == null) {
				return "Failed setting durability factor for Material: [" + materialname + "]: MATERIAL DOES NOT EXIST!";
			}
			if (this.slot == null) {
				return "Failed setting durability factor for Material: [" + materialname + "]: UNKNOWN SLOT [" + slotname + "]";
			}
			return "Set durability factor for Material: [" + materialname + "] slot [" + slotname + "] to: " + factor;
		}
	}

	private static class setArmorMaterialToughnessAction implements IAction {
		protected String materialname;
		protected float toughness;
		protected TGArmorMaterial material;

		public setArmorMaterialToughnessAction(String materialname, float toughness) {
			this.materialname = materialname;
			this.toughness = Math.max(0.0f, toughness);
			this.material = getArmorMaterial(materialname);
		}

		@Override
		public void apply() {
			if (this.material != null) {
				this.material.setToughness(this.toughness);
			}
		}

		@Override
		public String describe() {
			if (this.material == null) {
				return "Failed setting toughness for Material: [" + materialname + "]: MATERIAL DOES NOT EXIST!";
			}
			return "Set toughness for Material: [" + materialname + "] to: " + toughness;
		}
	}

	private static class setArmorKnockbackResistanceAction implements IAction {
		protected String armorname;
		protected float value;
		protected GenericArmor armor;

		public setArmorKnockbackResistanceAction(String armorname, float value) {
			this.armorname = armorname;
			this.value = value;
			this.armor = getArmor(armorname);
		}

		@Override
		public void apply() {
			if (this.armor != null) {
				this.armor.setKnockbackResistance(this.value);
			}
		}

		@Override
		public String describe() {
			if (this.armor == null) {
				return "Failed setting knockback resistance for Armor: [" + armorname + "]: ITEM IS NOT A TECHGUNS ARMOR";
			}
			return "Set knockback resistance for Armor: [" + armorname + "] to: " + value;
		}
	}

	private static class setArmorRadResistanceAction implements IAction {
		protected String armorname;
		protected float value;
		protected GenericArmor armor;

		public setArmorRadResistanceAction(String armorname, float value) {
			this.armorname = armorname;
			this.value = value;
			this.armor = getArmor(armorname);
		}

		@Override
		public void apply() {
			if (this.armor != null) {
				this.armor.setRADResistance(this.value);
			}
		}

		@Override
		public String describe() {
			if (this.armor == null) {
				return "Failed setting radiation resistance for Armor: [" + armorname + "]: ITEM IS NOT A TECHGUNS ARMOR";
			}
			return "Set radiation resistance for Armor: [" + armorname + "] to: " + value;
		}
	}

	private static class setArmorHiddenSlotsAction implements IAction {
		protected String armorname;
		protected boolean hideFace;
		protected boolean hideBack;
		protected boolean hideGlove;
		protected GenericArmor armor;

		public setArmorHiddenSlotsAction(String armorname, boolean hideFace, boolean hideBack, boolean hideGlove) {
			this.armorname = armorname;
			this.hideFace = hideFace;
			this.hideBack = hideBack;
			this.hideGlove = hideGlove;
			this.armor = getArmor(armorname);
		}

		@Override
		public void apply() {
			if (this.armor != null) {
				this.armor.setHideFaceslot(this.hideFace);
				this.armor.setHideBackslot(this.hideBack);
				this.armor.setHideGloveslot(this.hideGlove);
			}
		}

		@Override
		public String describe() {
			if (this.armor == null) {
				return "Failed setting hidden slots for Armor: [" + armorname + "]: ITEM IS NOT A TECHGUNS ARMOR";
			}
			return "Set hidden slots for Armor: [" + armorname + "] to face/back/glove = " + hideFace + "/" + hideBack + "/" + hideGlove;
		}
	}

	private static class setArmorRepairMatsAction implements IAction {
		protected String armorname;
		protected ItemStack metal;
		protected ItemStack cloth;
		protected float metalPercent;
		protected int totalMats;
		protected GenericArmor armor;

		public setArmorRepairMatsAction(String armorname, IItemStack metal, IItemStack cloth, float metalPercent, int totalMats) {
			this.armorname = armorname;
			this.metal = CraftTweakerMC.getItemStack(metal);
			this.cloth = CraftTweakerMC.getItemStack(cloth);
			this.metalPercent = MathUtil.clamp(metalPercent, 0.0f, 1.0f);
			this.totalMats = Math.max(0, totalMats);
			this.armor = getArmor(armorname);
		}

		@Override
		public void apply() {
			if (this.armor != null) {
				this.armor.setRepairMats(this.metal, this.cloth, this.metalPercent, this.totalMats);
			}
		}

		@Override
		public String describe() {
			if (this.armor == null) {
				return "Failed setting repair mats for Armor: [" + armorname + "]: ITEM IS NOT A TECHGUNS ARMOR";
			}
			return "Set repair mats for Armor: [" + armorname + "]";
		}
	}

	private static class setPoweredArmorMaxPowerAction implements IAction {
		protected String armorname;
		protected int maxPower;
		protected PoweredArmor armor;

		public setPoweredArmorMaxPowerAction(String armorname, int maxPower) {
			this.armorname = armorname;
			this.maxPower = Math.max(1, maxPower);
			this.armor = getPoweredArmor(armorname);
		}

		@Override
		public void apply() {
			if (this.armor != null) {
				this.armor.maxpower = this.maxPower;
			}
		}

		@Override
		public String describe() {
			if (this.armor == null) {
				return "Failed setting max power for Armor: [" + armorname + "]: ITEM IS NOT A POWERED ARMOR";
			}
			return "Set max power for Armor: [" + armorname + "] to: " + maxPower;
		}
	}

	private static class setPoweredArmorBatteryAction implements IAction {
		protected String armorname;
		protected ItemStack battery;
		protected ItemStack emptyBattery;
		protected boolean hasEmptyBattery;
		protected PoweredArmor armor;

		public setPoweredArmorBatteryAction(String armorname, IItemStack battery, IItemStack emptyBattery) {
			this.armorname = armorname;
			this.battery = CraftTweakerMC.getItemStack(battery);
			this.hasEmptyBattery = emptyBattery != null;
			if (this.hasEmptyBattery) {
				this.emptyBattery = CraftTweakerMC.getItemStack(emptyBattery);
			} else {
				this.emptyBattery = ItemStack.EMPTY;
			}
			this.armor = getPoweredArmor(armorname);
		}

		@Override
		public void apply() {
			if (this.armor != null) {
				this.armor.setBattery(this.battery);
				if (this.hasEmptyBattery) {
					this.armor.setEmptyBattery(this.emptyBattery);
				}
			}
		}

		@Override
		public String describe() {
			if (this.armor == null) {
				return "Failed setting battery for Armor: [" + armorname + "]: ITEM IS NOT A POWERED ARMOR";
			}
			return "Set battery settings for Armor: [" + armorname + "]";
		}
	}
	
	private static class setArmorStatAction implements IAction {

		protected String armorname;
		protected String statname;
		
		protected GenericArmor armor;
		protected EnumArmorStat stat;
		
		protected float value;
		protected float value_unpowered;
		
		protected boolean itemOk;
		
		public setArmorStatAction(String armorname, String statname, float value) {
			this(armorname, statname, value, 0f);
		}
		public setArmorStatAction(String armorname, String statname, float value, float value_unpowered) {
			super();
			this.armorname = armorname;
			this.statname = statname;
			this.value = value;
			this.value_unpowered = value_unpowered;

			Item item = getArmor(armorname);
			this.stat = EnumArmorStat.parseFromString(statname);
		

			this.itemOk = item instanceof GenericArmor;
			if(itemOk) {
				this.armor = (GenericArmor) item;
			}
		}

		@Override
		public void apply() {
			if(itemOk && stat!=null) {
				if(this.armor instanceof PoweredArmor) {
					((PoweredArmor)armor).setArmorStat(stat, value, value_unpowered);
				} else {
					this.armor.setArmorStat(stat, value);
				}
			}
		}

		@Override
		public String describe() {
			if(!itemOk) {
				return "Failed setting ["+statname+"] for Weapon: ["+armorname+"]: ITEM IS NOT A TECHGUNS ARMOR";
			}
			if (stat==null) {
				return "Failed setting ["+statname+"] for Weapon: ["+armorname+"]: UNKNOWN STAT";
			}

			return "Set ["+statname+"] for Armor: ["+armorname+"] to: "+value;
		}
		
	}
	
}

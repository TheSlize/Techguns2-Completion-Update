package techguns.plugins.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techguns.*;
import techguns.api.guns.GunHandType;
import techguns.items.guns.GenericGun;

@ZenClass("mods.techguns.GunStats")
public class GunStatTweaker {

	@ZenMethod
	public static void setWeaponStat(String weaponname, String fieldname, float value) {
		CraftTweakerAPI.apply(new setGunStatAction(weaponname, fieldname, value));
	}

	@ZenMethod
	public static void setWeaponDamageDrop(String weaponname, float start, float end, float minDamage) {
		CraftTweakerAPI.apply(new setWeaponDamageDropAction(weaponname, start, end, minDamage));
	}

	@ZenMethod
	public static void setWeaponDamageDrop(String weaponname, double start, double end, double minDamage) {
		setWeaponDamageDrop(weaponname, (float) start, (float) end, (float) minDamage);
	}

	@ZenMethod
	public static void setWeaponZoom(String weaponname, float zoomMult, boolean toggleZoom, float zoomSpreadMultiplier, boolean fireCenteredZoomed) {
		CraftTweakerAPI.apply(new setWeaponZoomAction(weaponname, zoomMult, toggleZoom, zoomSpreadMultiplier, fireCenteredZoomed));
	}

	@ZenMethod
	public static void setWeaponZoom(String weaponname, double zoomMult, boolean toggleZoom, double zoomSpreadMultiplier, boolean fireCenteredZoomed) {
		setWeaponZoom(weaponname, (float) zoomMult, toggleZoom, (float) zoomSpreadMultiplier, fireCenteredZoomed);
	}

	@ZenMethod
	public static void setWeaponShotgunSpread(String weaponname, int bulletCount, float spread, boolean burst) {
		CraftTweakerAPI.apply(new setWeaponShotgunSpreadAction(weaponname, bulletCount, spread, burst));
	}

	@ZenMethod
	public static void setWeaponShotgunSpread(String weaponname, int bulletCount, double spread, boolean burst) {
		setWeaponShotgunSpread(weaponname, bulletCount, (float) spread, burst);
	}

	@ZenMethod
	public static void setWeaponSilenced(String weaponname, boolean value) {
		CraftTweakerAPI.apply(new setWeaponSilencedAction(weaponname, value));
	}

	@ZenMethod
	public static void setWeaponShootWithLeftClick(String weaponname, boolean value) {
		CraftTweakerAPI.apply(new setWeaponShootWithLeftClickAction(weaponname, value));
	}

	@ZenMethod
	public static void setWeaponHandType(String weaponname, String handType) {
		CraftTweakerAPI.apply(new setWeaponHandTypeAction(weaponname, handType));
	}

	@ZenMethod
	public static void setWeaponAIStats(String weaponname, float attackRange, int attackTime, int burstCount, int burstAttackTime) {
		CraftTweakerAPI.apply(new setWeaponAIStatsAction(weaponname, attackRange, attackTime, burstCount, burstAttackTime));
	}

	@ZenMethod
	public static void setWeaponAIStats(String weaponname, double attackRange, int attackTime, int burstCount, int burstAttackTime) {
		setWeaponAIStats(weaponname, (float) attackRange, attackTime, burstCount, burstAttackTime);
	}

	private static GenericGun getGun(String weaponname) {
		Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(Tags.MOD_ID, weaponname));
		if (item instanceof GenericGun) {
			return (GenericGun) item;
		}
		return null;
	}

	private static GunHandType parseHandType(String handType) {
		for (GunHandType type : GunHandType.values()) {
			if (type.name().equalsIgnoreCase(handType)) {
				return type;
			}
		}
		return null;
	}
	
	private static class setGunStatAction implements IAction {

		protected String weaponname;
		protected String fieldname;
		
		protected GenericGun gun;
		protected EnumGunStat field;
		protected float value;
		
		protected boolean gunOk;
		
		public setGunStatAction(String weaponname, String fieldname, float value) {
			this.fieldname=fieldname;
			this.weaponname=weaponname;
			Item item = getGun(weaponname);
			this.field = EnumGunStat.parseFromString(fieldname);
			this.value = value;

			this.gunOk = item instanceof GenericGun;
			this.gun = (GenericGun) item;
		}

		@Override
		public void apply() {
			if(this.field!=null && this.gunOk) {
				this.gun.setGunStat(field, value);
			}
		}

		@Override
		public String describe() {
			if(!gunOk) {
				return "Failed setting ["+fieldname+"] for Weapon: ["+weaponname+"]: ITEM IS NOT A GUN";
			}
			if (field==null) {
				return "Failed setting ["+fieldname+"] for Weapon: ["+weaponname+"]: UNKNOWN FIELD";
			}

			return "Set ["+fieldname+"] for Weapon: ["+weaponname+"] to: "+value;
		}
		
	}

	private static class setWeaponDamageDropAction implements IAction {
		protected String weaponname;
		protected float start;
		protected float end;
		protected float minDamage;
		protected GenericGun gun;

		public setWeaponDamageDropAction(String weaponname, float start, float end, float minDamage) {
			this.weaponname = weaponname;
			this.start = start;
			this.end = end;
			this.minDamage = minDamage;
			this.gun = getGun(weaponname);
		}

		@Override
		public void apply() {
			if (this.gun != null) {
				this.gun.setDamageDrop(this.start, this.end, this.minDamage);
			}
		}

		@Override
		public String describe() {
			return this.gun == null
					? "Failed setting damage drop for Weapon: [" + weaponname + "]: ITEM IS NOT A GUN"
					: "Set damage drop for Weapon: [" + weaponname + "]";
		}
	}

	private static class setWeaponZoomAction implements IAction {
		protected String weaponname;
		protected float zoomMult;
		protected boolean toggleZoom;
		protected float zoomSpreadMultiplier;
		protected boolean fireCenteredZoomed;
		protected GenericGun gun;

		public setWeaponZoomAction(String weaponname, float zoomMult, boolean toggleZoom, float zoomSpreadMultiplier, boolean fireCenteredZoomed) {
			this.weaponname = weaponname;
			this.zoomMult = zoomMult;
			this.toggleZoom = toggleZoom;
			this.zoomSpreadMultiplier = zoomSpreadMultiplier;
			this.fireCenteredZoomed = fireCenteredZoomed;
			this.gun = getGun(weaponname);
		}

		@Override
		public void apply() {
			if (this.gun != null) {
				this.gun.setZoom(this.zoomMult, this.toggleZoom, this.zoomSpreadMultiplier, this.fireCenteredZoomed);
			}
		}

		@Override
		public String describe() {
			return this.gun == null
					? "Failed setting zoom for Weapon: [" + weaponname + "]: ITEM IS NOT A GUN"
					: "Set zoom for Weapon: [" + weaponname + "]";
		}
	}

	private static class setWeaponShotgunSpreadAction implements IAction {
		protected String weaponname;
		protected int bulletCount;
		protected float spread;
		protected boolean burst;
		protected GenericGun gun;

		public setWeaponShotgunSpreadAction(String weaponname, int bulletCount, float spread, boolean burst) {
			this.weaponname = weaponname;
			this.bulletCount = bulletCount;
			this.spread = spread;
			this.burst = burst;
			this.gun = getGun(weaponname);
		}

		@Override
		public void apply() {
			if (this.gun != null) {
				this.gun.setShotgunSpread(this.bulletCount, this.spread, this.burst);
			}
		}

		@Override
		public String describe() {
			return this.gun == null
					? "Failed setting shotgun spread for Weapon: [" + weaponname + "]: ITEM IS NOT A GUN"
					: "Set shotgun spread for Weapon: [" + weaponname + "]";
		}
	}

	private static class setWeaponSilencedAction implements IAction {
		protected String weaponname;
		protected boolean value;
		protected GenericGun gun;

		public setWeaponSilencedAction(String weaponname, boolean value) {
			this.weaponname = weaponname;
			this.value = value;
			this.gun = getGun(weaponname);
		}

		@Override
		public void apply() {
			if (this.gun != null) {
				this.gun.setSilenced(this.value);
			}
		}

		@Override
		public String describe() {
			return this.gun == null
					? "Failed setting silenced for Weapon: [" + weaponname + "]: ITEM IS NOT A GUN"
					: "Set silenced for Weapon: [" + weaponname + "] to: " + value;
		}
	}

	private static class setWeaponShootWithLeftClickAction implements IAction {
		protected String weaponname;
		protected boolean value;
		protected GenericGun gun;

		public setWeaponShootWithLeftClickAction(String weaponname, boolean value) {
			this.weaponname = weaponname;
			this.value = value;
			this.gun = getGun(weaponname);
		}

		@Override
		public void apply() {
			if (this.gun != null) {
				this.gun.setShootWithLeftClick(this.value);
			}
		}

		@Override
		public String describe() {
			return this.gun == null
					? "Failed setting left-click mode for Weapon: [" + weaponname + "]: ITEM IS NOT A GUN"
					: "Set left-click mode for Weapon: [" + weaponname + "] to: " + value;
		}
	}

	private static class setWeaponHandTypeAction implements IAction {
		protected String weaponname;
		protected String handTypeName;
		protected GenericGun gun;
		protected GunHandType handType;

		public setWeaponHandTypeAction(String weaponname, String handTypeName) {
			this.weaponname = weaponname;
			this.handTypeName = handTypeName;
			this.gun = getGun(weaponname);
			this.handType = parseHandType(handTypeName);
		}

		@Override
		public void apply() {
			if (this.gun != null && this.handType != null) {
				this.gun.setHandType(this.handType);
			}
		}

		@Override
		public String describe() {
			if (this.gun == null) {
				return "Failed setting hand type for Weapon: [" + weaponname + "]: ITEM IS NOT A GUN";
			}
			if (this.handType == null) {
				return "Failed setting hand type for Weapon: [" + weaponname + "]: UNKNOWN HAND TYPE";
			}
			return "Set hand type for Weapon: [" + weaponname + "] to: " + handTypeName;
		}
	}

	private static class setWeaponAIStatsAction implements IAction {
		protected String weaponname;
		protected float attackRange;
		protected int attackTime;
		protected int burstCount;
		protected int burstAttackTime;
		protected GenericGun gun;

		public setWeaponAIStatsAction(String weaponname, float attackRange, int attackTime, int burstCount, int burstAttackTime) {
			this.weaponname = weaponname;
			this.attackRange = attackRange;
			this.attackTime = attackTime;
			this.burstCount = burstCount;
			this.burstAttackTime = burstAttackTime;
			this.gun = getGun(weaponname);
		}

		@Override
		public void apply() {
			if (this.gun != null) {
				this.gun.setAIStats(this.attackRange, this.attackTime, this.burstCount, this.burstAttackTime);
			}
		}

		@Override
		public String describe() {
			return this.gun == null
					? "Failed setting AI stats for Weapon: [" + weaponname + "]: ITEM IS NOT A GUN"
					: "Set AI stats for Weapon: [" + weaponname + "]";
		}
	}
}

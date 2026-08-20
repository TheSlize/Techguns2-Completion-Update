package techguns.items.armors;

import java.util.ArrayList;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import techguns.*;
import techguns.api.damagesystem.DamageType;

public class TGArmorMaterial {

    public static ArrayList<TGArmorMaterial> MATERIALS = new ArrayList<>();

    public String name;

    /**
     * The sum of default durability weight values
     */
    private static final int VANILLA_DURABILITY_FACTOR_SUM = 55;

    protected float armorPhys;
    protected float armorProjectile;
    protected float armorExplosion;
    protected float armorEnergy;
    protected float armorFire;
    protected float armorLightning;
    protected float armorIce;
    protected float armorDark;
    protected float armorPoison;
    protected float armorRadiation;

    protected float factorHead = 0.25f;
    protected float factorBoots = 0.20f;
    protected float factorLegs = 0.25f;
    protected float factorChest = 0.30f;

    protected float durHead = 0.25f;
    protected float durBoots = 0.25f;
    protected float durLegs = 0.25f;
    protected float durChest = 0.25f;

    protected float toughness;

    protected SoundEvent equipSound;

    protected int baseDur;
    protected int enchantability;

    protected String texture;

    protected String modid;


    /**
     * Don't use this constructor outside of techguns, use the one with modid as 1st parameter
     * <p>
     * Set the armor values with standard factors, phys and proj are equal to armor, rad/poison are 0, all others are 0.75*armor;
     *
     * @param armor
     * @return
     */
    public TGArmorMaterial(String name, int baseDurability, int enchantability, float armor, SoundEvent equipSound, float toughness) {
        this(Tags.MOD_ID, name, baseDurability, enchantability, armor, equipSound, toughness);
    }

    /**
     * Set the armor values with standard factors, phys and proj are equal to armor, rad/poison are 0, all others are 0.75*armor;
     *
     * @param armor
     * @return
     */
    public TGArmorMaterial(String modid, String name, int baseDurability, int enchantability, float armor, SoundEvent equipSound, float toughness) {
        this.name = name;
        this.baseDur = baseDurability;
        this.enchantability = enchantability;
        float f = 0.75f;
        this.armorPhys = armor;
        this.armorProjectile = armor;
        this.armorFire = armor * f;
        this.armorExplosion = armor * f;
        this.armorEnergy = armor * f;
        this.armorIce = armor * f;
        this.armorLightning = armor * f;
        this.armorDark = armor * f;
        this.armorPoison = armor * f;
        this.armorRadiation = 0.0f;
        this.toughness = toughness;
        this.equipSound = equipSound;
        this.texture = name.toLowerCase();
        this.modid = modid;

        MATERIALS.add(this);
    }

    /**
     * Set all 'Elemental' or 'Magic' values: fire, lightning, ice and energy
     *
     * @param value
     * @return
     */
    public TGArmorMaterial setArmorElemental(float value) {
        this.armorEnergy = value;
        this.armorFire = value;
        this.armorIce = value;
        this.armorLightning = value;
        return this;
    }

    public float getArmorValueTotal(DamageType damageType) {
        return switch (damageType) {
            case ENERGY -> this.armorEnergy;
            case EXPLOSION -> this.armorExplosion;
            case FIRE -> this.armorFire;
            case ICE -> this.armorIce;
            case LIGHTNING -> this.armorLightning;
            case PHYSICAL -> this.armorPhys;
            case POISON -> this.armorPoison;
            case PROJECTILE -> this.armorProjectile;
            case RADIATION -> this.armorRadiation;
            case DARK -> this.armorDark;
            default -> 0;
        };
    }

    public float getArmorValueSlot(EntityEquipmentSlot slot, DamageType type) {
        float materialArmor = this.getArmorValueTotal(type);
        return switch (slot) {
            case HEAD -> //helmet:
                    this.factorHead * materialArmor;
            case CHEST -> //chest
                    this.factorChest * materialArmor;
            case LEGS -> //legs
                    this.factorLegs * materialArmor;
            case FEET -> //boots
                    this.factorBoots * materialArmor;
            default -> 0;
        };
    }

	/**
     * gets a vanilla armor material from this
     *
     * @return
     */
    public ArmorMaterial createVanillaMaterial() {
        return EnumHelper.addArmorMaterial(name, this.modid + ":" + this.texture, baseDur, new int[]{0, 0, 0, 0}, enchantability, equipSound, toughness);
    }

	public TGArmorMaterial setArmorExplosion(float armorExplosion) {
        this.armorExplosion = armorExplosion;
        return this;
    }

    public TGArmorMaterial setArmorEnergy(float armorEnergy) {
        this.armorEnergy = armorEnergy;
        return this;
    }

    public TGArmorMaterial setArmorFire(float armorFire) {
        this.armorFire = armorFire;
        return this;
    }

    public TGArmorMaterial setArmorLightning(float armorLightning) {
        this.armorLightning = armorLightning;
        return this;
    }

    public TGArmorMaterial setArmorIce(float armorIce) {
        this.armorIce = armorIce;
        return this;
    }

    public TGArmorMaterial setArmorPoison(float armorPoison) {
        this.armorPoison = armorPoison;
        return this;
    }

    public TGArmorMaterial setArmorRadiation(float armorRadiation) {
        this.armorRadiation = armorRadiation;
        return this;
    }

	public int getDurability(EntityEquipmentSlot type) {
        return switch (type) {
            case HEAD -> //helmet:
                    Math.round(this.durHead * VANILLA_DURABILITY_FACTOR_SUM * this.baseDur);
            case CHEST -> //chest
                    Math.round(this.durChest * VANILLA_DURABILITY_FACTOR_SUM * this.baseDur);
            case LEGS -> //legs
                    Math.round(this.durLegs * VANILLA_DURABILITY_FACTOR_SUM * this.baseDur);
            case FEET -> //boots
                    Math.round(this.durBoots * VANILLA_DURABILITY_FACTOR_SUM * this.baseDur);
            default -> 0;
        };
    }

    public float getToughness() {
        return toughness;
    }

    public TGArmorMaterial setToughness(float toughness) {
        this.toughness = Math.max(0.0f, toughness);
        return this;
    }

    public TGArmorMaterial setBaseDurability(int baseDurability) {
        this.baseDur = Math.max(1, baseDurability);
        return this;
    }

    public TGArmorMaterial setDurabilityFactor(EntityEquipmentSlot slot, float factor) {
        float clamped = Math.max(0.0f, factor);
        switch (slot) {
            case HEAD:
                this.durHead = clamped;
                break;
            case CHEST:
                this.durChest = clamped;
                break;
            case LEGS:
                this.durLegs = clamped;
                break;
            case FEET:
                this.durBoots = clamped;
                break;
            default:
                break;
        }
        return this;
    }

    public void setArmorValueForType(DamageType type, float value) {
        switch (type) {
            case DARK:
                this.armorDark = value;
                break;
            case ENERGY:
                this.armorEnergy = value;
                break;
            case EXPLOSION:
                this.armorExplosion = value;
                break;
            case FIRE:
                this.armorFire = value;
                break;
            case ICE:
                this.armorIce = value;
                break;
            case LIGHTNING:
                this.armorLightning = value;
                break;
            case PHYSICAL:
                this.armorPhys = value;
                break;
            case POISON:
                this.armorPoison = value;
                break;
            case PROJECTILE:
                this.armorProjectile = value;
                break;
            case RADIATION:
                this.armorRadiation = value;
                break;
            default:
                break;

        }
    }
}	
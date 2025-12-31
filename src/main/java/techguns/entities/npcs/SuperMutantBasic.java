package techguns.entities.npcs;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import techguns.TGSounds;
import techguns.TGuns;
import techguns.Techguns;
import techguns.api.npc.factions.ITGNpcTeam;
import techguns.damagesystem.TGDamageSource;

public class SuperMutantBasic extends GenericNPC {
	
	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/supermutantbasic");

	public SuperMutantBasic(World world) {
		super(world);
		this.setSize(getMutantWidth(), 2F*this.getModelScale());
		setTGArmorStats(5.0f, 0f);
	}
	
	public int gettype() {
		return 0;
	};
	
	protected float getMutantWidth() {
		return 1.0f;
	}
	
	public double getModelHeightOffset(){
		return 0.55d;
	}
	
	public float getModelScale() {
		return 1.35f;
	}

	@Override
	public float getWeaponPosY() {
		return 0f;
	}
	
	@Override
	public float getWeaponPosX() {
		return 0.13f;
	}

	@Override
	public float getWeaponPosZ() {
		return -0.18f;
	}

	
	@Override
	public float getTotalArmorAgainstType(TGDamageSource dmgsrc) {
		switch(dmgsrc.damageType){
			case EXPLOSION:
			case LIGHTNING:
			case ENERGY:
			case FIRE:
			case ICE:
				return 10.0f;
			case PHYSICAL:
				return 8.0f;
			case PROJECTILE:
				return 7.0f;
			case POISON:
			case RADIATION:
				return 15.0f;
			case UNRESISTABLE:
		default:
			return 0.0f;
		}
	}

	@Override
	public int getTotalArmorValue() {
		return 7;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(1.5D);
	}

	

	@Override
	protected void addRandomArmor(int difficulty) {

			// Weapons
		Random r = new Random();
		Item weapon = null;
		switch (r.nextInt(5)) {
			case 0:
				weapon = TGuns.rocketlauncher;
				break;
			case 1:
				weapon = TGuns.pulserifle;
				break;
			case 2:
				weapon = TGuns.blasterrifle;
				break;
			default:
				weapon = TGuns.lasergun;
				break;
		}
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(weapon));
	}

	@Override
	public SoundEvent getAmbientSound() {
		return TGSounds.CYBERDEMON_IDLE;
	}

	@Override
	public @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
		return TGSounds.CYBERDEMON_HURT;
	}

	@Override
	public @NotNull SoundEvent getDeathSound() {
		return TGSounds.CYBERDEMON_DEATH;
	}

	public SoundEvent getStepSound() {
		return TGSounds.CYBERDEMON_STEP;
	}
	
	@Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
	
	
	@Override
	protected ResourceLocation getLootTable() {
		return LOOT;
	}

    @Override
    public boolean attackEntityFrom(@NotNull DamageSource source, float amount) {
        if (this.isFriendlyDamage(source)) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    private boolean isFriendlyDamage(DamageSource source) {
        return this.isFriendlyEntity(source.getTrueSource()) || this.isFriendlyEntity(source.getImmediateSource());
    }

    private boolean isFriendlyEntity(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity == this) {
            return true;
        }
        if (entity instanceof ITGNpcTeam) {
            return ((ITGNpcTeam) entity).getTGFaction() == this.getTGFaction();
        }
        return false;
    }

    @Override
    public int getMaxFallHeight() {
        return 1;
    }

    @Override
    protected boolean useTargetOffsetPathing() {
        return true;
    }
}

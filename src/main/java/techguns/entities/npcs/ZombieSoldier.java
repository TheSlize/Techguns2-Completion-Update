package techguns.entities.npcs;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
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
import techguns.TGArmors;
import techguns.TGuns;
import techguns.Techguns;
import techguns.api.npc.factions.ITGNpcTeam;

public class ZombieSoldier extends GenericNPCUndead {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/zombiesoldier");
	
	public ZombieSoldier(World world) {
		super(world);
		setTGArmorStats(5.0f, 0f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0D);
	}

	@Override
	protected void addRandomArmor(int difficulty) {

		// Armors
		double chance = 0.5;
		if (Math.random() <= chance)
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TGArmors.t1_combat_Helmet));
		if (Math.random() <= chance)
			 this.setItemStackToSlot(EntityEquipmentSlot.CHEST,new ItemStack(TGArmors.t1_combat_Chestplate));
		if (Math.random() <= chance)
			 this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TGArmors.t1_combat_Leggings));
		if (Math.random() <= chance)
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TGArmors.t1_combat_Boots));

		// Weapons
		Random r = new Random();
		Item weapon = null;
		switch (r.nextInt(4)) {
		case 0:
			weapon = TGuns.revolver;
			break;
		case 1:
			weapon = TGuns.thompson;
			break;
		case 2:
			weapon = Items.IRON_SHOVEL;
			break;
		case 3:
		default:
			weapon = Items.STONE_SHOVEL;
			break;
		}
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(weapon));
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	public @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	@Override
	public @NotNull SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_DEATH;
	}

	public SoundEvent getStepSound() {
		return SoundEvents.ENTITY_ZOMBIE_STEP;
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
	protected boolean shouldBurnInDay() {
		return false;
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

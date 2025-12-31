package techguns.entities.npcs;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import techguns.TGArmors;
import techguns.TGuns;
import techguns.Techguns;
import techguns.items.armors.GenericArmorMultiCamo;

import java.util.List;

public class PsychoSteve extends GenericNPC implements ILivingSoldier {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/psychosteve");

    private int soundAggroCooldown = 0;

	public PsychoSteve(World world) {
		super(world);
		setTGArmorStats(5.0f, 0f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.60D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(75);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(60.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(1D);
		this.experienceValue = 25;
	}

	@Override
	protected void addRandomArmor(int difficulty) {

		// Armors
		
		int camo = GenericArmorMultiCamo.getRandomCamoIndexFor((GenericArmorMultiCamo) TGArmors.t1_miner_Chestplate);
			
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, GenericArmorMultiCamo.getNewWithCamo(
				TGArmors.t1_miner_Helmet,camo));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, GenericArmorMultiCamo.getNewWithCamo(
					TGArmors.t1_miner_Chestplate,camo));
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, GenericArmorMultiCamo.getNewWithCamo(
					TGArmors.t1_miner_Leggings,camo));
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, GenericArmorMultiCamo.getNewWithCamo(
					TGArmors.t1_miner_Boots,camo));
			
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TGuns.chainsaw));

	}

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote) {
            this.tickHearingAI();
        }
    }
    @Override
    public void tickHearingAI() {
        if ((this.ticksExisted & 3) != 0) {
            return;
        }
        if (this.soundAggroCooldown > 0) {
            this.soundAggroCooldown--;
        }
        EntityLivingBase currentTarget = this.getAttackTarget();
        if (currentTarget != null) {
            if (!this.isValidAttackTarget(currentTarget)) {
                this.setAttackTarget(null);
            } else {
                return;
            }
        }
        if (this.soundAggroCooldown > 0) {
            return;
        }
        this.tryAggroBySound();
    }

    private void tryAggroBySound() {
        EntityPlayer loudest = null;
        double loudestWeight = 0.0D;
        AxisAlignedBB scanBox = this.getEntityBoundingBox().grow(SOUND_SCAN_RANGE, 10.0D, SOUND_SCAN_RANGE);
        List<EntityPlayer> players = this.world.getEntitiesWithinAABB(EntityPlayer.class, scanBox);
        for (EntityPlayer player : players) {
            if (!this.canHearPlayer(player)) {
                continue;
            }
            float loudness = this.getPlayerLoudness(player);
            if (loudness <= 0.0f) {
                continue;
            }
            double hearingRange = BASE_HEARING_RANGE + loudness * 16.0D;
            double distSq = this.getDistanceSq(player);
            if (distSq <= hearingRange * hearingRange && loudness > loudestWeight) {
                loudestWeight = loudness;
                loudest = player;
            }
        }
        if (loudest != null) {
            this.setAttackTarget(loudest);
            this.soundAggroCooldown = SOUND_REACTION_COOLDOWN;
        }
    }

    private boolean canHearPlayer(EntityPlayer player) {
        return this.isValidPlayerTarget(player);
    }
	
	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	public @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_VILLAGER_HURT;
	}

	@Override
	public @NotNull SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VILLAGER_DEATH;
	}

	public SoundEvent getStepSound() {
		return SoundEvents.ENTITY_ZOMBIE_STEP;
	}
	
	@Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull Block blockIn)
    {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
	
	@Override
	protected ResourceLocation getLootTable() {
		return LOOT;
	}
}

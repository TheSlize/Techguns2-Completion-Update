package techguns.entities.npcs;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import techguns.TGArmors;
import techguns.TGuns;
import techguns.Techguns;
import techguns.api.npc.factions.ITGNpcTeam;

import java.util.List;

public class DictatorDave extends GenericNPC implements ILivingSoldier {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/dictatordave");

    private int soundAggroCooldown = 0;

	public DictatorDave(World world) {
		super(world);
		setTGArmorStats(15.0f, 0f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.40D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(60.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2D);
		this.experienceValue = 35;
	}

	@Override
	protected void addRandomArmor(int difficulty) {
		// Armors
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TGArmors.t2_beret));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST,new ItemStack(TGArmors.t1_combat_Chestplate));
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TGArmors.t1_scout_Leggings));
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TGArmors.t1_combat_Boots));

		// Weapons
		Item weapon = TGuns.goldenrevolver;
				
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(weapon));
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
		return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
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
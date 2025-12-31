package techguns.entities.npcs;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.*;
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

public class ArmySoldier extends GenericNPC implements ILivingSoldier {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/armysoldier");

    private int soundAggroCooldown = 0;

    public ArmySoldier(World world) {
        super(world);
        setTGArmorStats(8.0f, 0f);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    @Override
    protected boolean canNPCUseWaterPaths() {
        return false;
    }

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(75.0D);
	}

	@Override
	protected void addRandomArmor(int difficulty) {

		// Armors
		double chance = 0.5;
		if (Math.random() <= chance) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TGArmors.t2_combat_Helmet));
		}
		else {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TGArmors.t2_beret));
		}
		if (Math.random() <= chance)
			 this.setItemStackToSlot(EntityEquipmentSlot.CHEST,new ItemStack(TGArmors.t2_combat_Chestplate));
		if (Math.random() <= chance)
			 this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TGArmors.t2_combat_Leggings));
		if (Math.random() <= chance)
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TGArmors.t2_combat_Boots));

		// Weapons
		Random r = new Random();
		Item weapon;
		switch (r.nextInt(3)) {
		case 0:
			weapon = TGuns.m4;
			break;
		case 1:
			weapon = TGuns.combatshotgun;
			break;
		default:
			weapon = TGuns.boltaction;
			break;
		}
		if (weapon != null) this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(weapon));
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
    protected void playStepSound(@NotNull BlockPos pos, @NotNull Block blockIn)
    {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
	
	@Override
	protected ResourceLocation getLootTable() {
		return LOOT;
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
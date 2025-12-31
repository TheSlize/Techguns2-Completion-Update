package techguns.entities.npcs;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import techguns.TGArmors;
import techguns.TGuns;
import techguns.Techguns;
import techguns.items.armors.GenericArmorMultiCamo;

public class Outcast extends GenericNPCGearSpecificStats implements ILivingSoldier {

	public static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/outcast");

    private int soundAggroCooldown = 0;

	public Outcast(World world) {
		super(world);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(60.0D);
	}

	@Override
	protected void addRandomArmor(int difficulty) {

		Random r = new Random();
		// Armors

		int camo=r.nextInt(2);
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, GenericArmorMultiCamo.getNewWithCamo(TGArmors.t3_power_Helmet,camo+1));
		camo=r.nextInt(2);
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, GenericArmorMultiCamo.getNewWithCamo(TGArmors.t3_power_Chestplate,camo+1));
		camo=r.nextInt(2);
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, GenericArmorMultiCamo.getNewWithCamo(TGArmors.t3_power_Leggings,camo+1));
		camo=r.nextInt(2);
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, GenericArmorMultiCamo.getNewWithCamo(TGArmors.t3_power_Boots,camo+1));
		

		// Weapons

		Item weapon = null;
		switch (r.nextInt(7)) {
		case 0:
			weapon = TGuns.lasergun;
			break;
		case 1:
			weapon = TGuns.grimreaper;
			break;
		case 2:
			weapon = TGuns.minigun;
			break;
		case 3:
			weapon = TGuns.biogun;
			break;
		case 4:
			weapon = TGuns.flamethrower;
			break;
		case 5:
			weapon = TGuns.teslagun;
			break;
		case 6:
			weapon = TGuns.lmg;
			break;
		default:
			weapon = TGuns.lasergun;
			break;
		}
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
	protected ResourceLocation getLootTable() {
		return LOOT;
	}
}
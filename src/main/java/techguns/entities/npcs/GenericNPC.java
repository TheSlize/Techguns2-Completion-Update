package techguns.entities.npcs;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;
import techguns.TGArmors;
import techguns.TGItems;
import techguns.TGuns;
import techguns.api.npc.INPCTechgunsShooter;
import techguns.api.npc.INpcTGDamageSystem;
import techguns.api.npc.factions.ITGNpcTeam;
import techguns.api.npc.factions.TGNpcFaction;
import techguns.blocks.BlockTGDoor2x1;
import techguns.blocks.BlockTGDoor3x3;
import techguns.capabilities.TGSpawnerNPCData;
import techguns.damagesystem.DamageSystem;
import techguns.damagesystem.TGDamageSource;
import techguns.entities.ai.EntityAIAttackTGMelee;
import techguns.entities.ai.EntityAIHurtByTargetTGFactions;
import techguns.entities.ai.EntityAIRangedAttack;
import techguns.items.guns.GenericGun;

public class GenericNPC extends EntityMob implements IRangedAttackMob, INPCTechgunsShooter, INpcTGDamageSystem, ITGNpcTeam, ITGSpawnerNPC{
		
	protected boolean hasAimedBowAnim;
	    
	    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(GenericNPC.class, DataSerializers.BOOLEAN);
	    private EntityAIRangedAttack aiRangedAttack =  null;
	    private final EntityAIAttackTGMelee aiAttackOnCollide = new EntityAIAttackTGMelee(this, 1.2D, false);
	  
	    boolean tryLink=true;
	    
	    protected float armor=0f;
	    protected float penetrationResistance=0f;
    @Nullable
    private Vec3d attackTargetOffset = null;
	    
	public GenericNPC(World world) {
	    super(world);
	    this.dataManager.register(SWINGING_ARMS, false);
	    this.tasks.addTask(1, new EntityAISwimming(this));
	    this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
	    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	    this.tasks.addTask(6, new EntityAILookIdle(this));
	    this.targetTasks.addTask(1, new EntityAIHurtByTargetTGFactions(this, false));
	    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));

	    if (!world.isRemote) this.setCombatTask();
	    hasAimedBowAnim = true;
	}

    protected GenericGun pickRandomGun(int difficulty) {
			Random r = new Random();
			GenericGun gun;
			switch (r.nextInt(4)) {
				case 0:
					gun = TGuns.revolver;
					break;
				case 1:
					gun = TGuns.thompson;
					break;
				case 2:
					gun = TGuns.as50;
					break;
				case 3:
					gun = TGuns.grimreaper;
					break;
                default:
					gun = TGuns.handcannon;
			}
			return gun;
		}

	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}


	@Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

	@Override protected @NotNull SoundEvent getDeathSound()
	    {
	        return SoundEvents.ENTITY_VILLAGER_DEATH;
	    }

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    @Override
    protected void dropFewItems(boolean hitByPlayer, int level) {
        int j;
        int k;

        j = this.rand.nextInt(3 + level);

        for (k = 0; k < j; ++k)
        {
            ItemStack it = this.getRandomItemFromLoottable();
            if (it != null){
                //this.dropItem(it.getItem(),it.stackSize,it.getItemDamage());
                this.entityDropItem(TGItems.newStack(it, it.getCount()), 0.0f);
            }
        }
    }

    protected ItemStack getRandomItemFromLoottable(){
	    	return null;
	    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Nullable
    public IEntityLivingData onInitialSpawn(@NotNull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEquipmentBasedOnDifficulty(difficulty);
        // this.setEnchantmentBasedOnDifficulty(difficulty);
        this.setCombatTask();
	    // this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());
        this.setCanPickUpLoot(false);
	        
        return livingdata;
    }
	    
	    
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		int d = Math.round(difficulty.getClampedAdditionalDifficulty()*3f);
		this.addRandomArmor(d);
	}
	    
	    
	protected void addRandomArmor(int difficulty){
	    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(this.pickRandomGun(difficulty)));
	    	
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TGArmors.t1_combat_Helmet));
	    this.setItemStackToSlot(EntityEquipmentSlot.CHEST,new ItemStack(TGArmors.t1_combat_Chestplate));
	    this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TGArmors.t1_combat_Leggings));
	    this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TGArmors.t1_combat_Boots));
	}

	public void onSpawnByManager(int difficulty) {
	    this.setCanPickUpLoot(false);
		this.addRandomArmor(difficulty);
		this.setCombatTask();
	}
	    
	    
	/**
	 * sets this entity's combat AI.
	 */
    public void setCombatTask() {
	    this.tasks.removeTask(this.aiAttackOnCollide);
	    this.tasks.removeTask(this.aiRangedAttack);
	    ItemStack itemstack = this.getHeldItemMainhand();

	    if (!itemstack.isEmpty() && itemstack.getItem() instanceof techguns.items.guns.GenericGun) {
	    	GenericGun gun = (GenericGun) itemstack.getItem();
	    	this.aiRangedAttack = gun.getAIAttack(this);
	        this.tasks.addTask(4, this.aiRangedAttack);
	        } else {
	        this.tasks.addTask(4, this.aiAttackOnCollide);
	    }
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(@NotNull EntityLivingBase target, float distance) {
        if(!this.getHeldItemMainhand().isEmpty()) {
			Item gun = this.getHeldItemMainhand().getItem();
			if (gun instanceof techguns.items.guns.GenericGun) {
				EnumDifficulty difficulty = this.world.getDifficulty();
		    	float acc = 1.0f;
		    	float dmg = 1.0f;
		    	switch(difficulty){
		    		case EASY:
		    			acc = 1.3f;
		    			dmg = 0.6f;
		    			break;
		    		case NORMAL:
		    			acc = 1.15f;
		    			dmg = 0.8f;
		    			break;
		    		case HARD:
                        break;
					default:
						break;
		    	}

				((GenericGun) gun).fireWeaponFromNPC(this,dmg,acc);
			}
		}
	}


	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(@NotNull NBTTagCompound p_70037_1_) {
	    super.readEntityFromNBT(p_70037_1_);
	    this.setCombatTask();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(@NotNull NBTTagCompound p_70014_1_)
	    {
	        super.writeEntityToNBT(p_70014_1_);
	    }

	/**
	 * Returns the Y Offset of this entity.
	 */
	public double getYOffset()
	    {
	        return super.getYOffset() - 0.5D;
	    }

	@Override
	public float getWeaponPosX() {
			return 0;//-0.35f;
		}

	@Override
	public float getWeaponPosY() {
			return 0;//1.0f;
		}

	@Override
	public float getWeaponPosZ() {
			return 0;//;1.1f;
		}

	protected void setTGArmorStats(float armor, float penetrationResistance) {
		this.armor=armor;
		this.penetrationResistance=penetrationResistance;
	}
		
	@Override
	public float getTotalArmorAgainstType(TGDamageSource dmgsrc) {
		return DamageSystem.getArmorAgainstDamageTypeDefault(this, this.armor, dmgsrc.damageType);
	}

	@Override
	public float getPenetrationResistance(TGDamageSource dmgsrc) {
			return this.penetrationResistance;
		}

	@Override
	public TGNpcFaction getTGFaction() {
			return TGNpcFaction.HOSTILE;
		}

	@Override
	public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(SWINGING_ARMS, swingingArms);
	}

    @Override
	protected void despawnEntity() {
		super.despawnEntity();
		this.despawnEntitySpawner(world, dead);
	}

	@Override
	public void onDeath(@NotNull DamageSource cause) {
		super.onDeath(cause);
		this.onDeathSpawner(world, dead);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.onUpdateSpawner(this.world);
	}

	@Override
	public boolean getTryLink() {
			return this.tryLink;
		}

	@Override
	public void setTryLink(boolean value) {
			this.tryLink=value;
		}

	@Override
	public TGSpawnerNPCData getCapability(Capability<TGSpawnerNPCData> tgGenericnpcData) {
        return this.getCapability(tgGenericnpcData,null);
	}

    protected boolean useTargetOffsetPathing() {
        return false;
    }

    @Override
    protected @NotNull PathNavigate createNavigator(@NotNull World worldIn) {
        PathNavigateGround navigator = new PathNavigateGround(this, worldIn) {
            @Override
            protected @NotNull PathFinder getPathFinder() {
                this.nodeProcessor = new TGWalkNodeProcessor();
                this.nodeProcessor.setCanEnterDoors(true);
                this.nodeProcessor.setCanOpenDoors(true);
                this.nodeProcessor.setCanSwim(GenericNPC.this.canNPCUseWaterPaths());
                return new PathFinder(this.nodeProcessor);
            }

            @Override
            public @Nullable Path getPathToEntityLiving(@NotNull Entity entityIn) {
                if (GenericNPC.this.useTargetOffsetPathing() && entityIn == GenericNPC.this.getAttackTarget()) {
                    if (GenericNPC.this.attackTargetOffset == null && entityIn instanceof EntityLivingBase) {
                        GenericNPC.this.attackTargetOffset = GenericNPC.this.createAttackTargetOffset((EntityLivingBase) entityIn);
                    }
                    Vec3d offset = GenericNPC.this.attackTargetOffset;
                    if (offset != null) {
                        Vec3d targetPos = new Vec3d(entityIn.posX + offset.x, entityIn.posY + offset.y, entityIn.posZ + offset.z);
                        Path offsetPath = this.getPathToPos(new BlockPos(targetPos));
                        if (offsetPath != null) {
                            return offsetPath;
                        }
                    }
                }
                return super.getPathToEntityLiving(entityIn);
            }
        };
        navigator.setBreakDoors(true);
        navigator.setEnterDoors(true);
        navigator.setCanSwim(this.canNPCUseWaterPaths());
        return navigator;
    }

    protected @NotNull Vec3d createAttackTargetOffset(@NotNull EntityLivingBase target) {
        float radius = 1.8f + this.rand.nextFloat() * 2.2f;
        float angle = this.rand.nextFloat() * (float) Math.PI * 2.0f;
        double offsetX = MathHelper.cos(angle) * radius;
        double offsetZ = MathHelper.sin(angle) * radius;
        return new Vec3d(offsetX, 0.0D, offsetZ);
    }

    protected boolean canNPCUseWaterPaths() {
        return true;
    }

    protected boolean isValidPlayerTarget(@NotNull EntityPlayer player) {
        return player.isEntityAlive() && !player.capabilities.isCreativeMode && !player.isSpectator();
    }

    protected boolean isValidAttackTarget(@Nullable EntityLivingBase target) {
        if (target == null || !target.isEntityAlive()) {
            return false;
        }
        return !(target instanceof EntityPlayer) || this.isValidPlayerTarget((EntityPlayer) target);
    }

    private static class TGWalkNodeProcessor extends WalkNodeProcessor {

        @Override
        protected @NotNull PathNodeType getPathNodeTypeRaw(IBlockAccess blockaccess, int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            IBlockState state = blockaccess.getBlockState(pos);
            Block block = state.getBlock();

            if (block instanceof BlockTGDoor2x1) {
                boolean open = state.getValue(BlockDoor.OPEN);
                return open ? PathNodeType.DOOR_OPEN : PathNodeType.DOOR_WOOD_CLOSED;
            }

            if (block instanceof BlockTGDoor3x3) {
                BlockTGDoor3x3<?> tgDoor = (BlockTGDoor3x3<?>) block;
                return tgDoor.isStateOpen(state) ? PathNodeType.DOOR_OPEN : PathNodeType.DOOR_WOOD_CLOSED;
            }

            return super.getPathNodeTypeRaw(blockaccess, x, y, z);
        }
    }

    @Override
    public void setAttackTarget(@org.jetbrains.annotations.Nullable EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        if (entitylivingbaseIn != null && this.useTargetOffsetPathing()) {
            this.attackTargetOffset = this.createAttackTargetOffset(entitylivingbaseIn);
        } else {
            this.attackTargetOffset = null;
        }
    }
		
}
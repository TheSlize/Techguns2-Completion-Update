package techguns.entities.npcs;

import javax.annotation.Nullable;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import techguns.TGPackets;
import techguns.TGSounds;
import techguns.Techguns;
import techguns.api.npc.INPCTechgunsShooter;
import techguns.api.npc.INpcTGDamageSystem;
import techguns.api.npc.factions.ITGNpcTeam;
import techguns.api.npc.factions.TGNpcFaction;
import techguns.capabilities.TGSpawnerNPCData;
import techguns.client.audio.TGSoundCategory;
import techguns.damagesystem.TGDamageSource;
import techguns.entities.projectiles.EnumBulletFirePos;
import techguns.entities.projectiles.RocketProjectile;
import techguns.packets.PacketPlaySound;

public class Ghastling extends EntityMob implements IMob, INpcTGDamageSystem, INPCTechgunsShooter, ITGSpawnerNPC, ITGNpcTeam {

    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(Ghastling.class, DataSerializers.BOOLEAN);

    protected static final ResourceLocation LOOT = new ResourceLocation(Techguns.MODID, "entities/ghastling");
    
    protected boolean tryLink=true;
    
	public Ghastling(World worldIn) {
		super(worldIn);
		this.setSize(1.0F, 2.1F);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.isImmuneToFire = true;
        this.experienceValue = 10;
	}
	
	@Override
    @Nullable
    protected ResourceLocation getLootTable()
    { 
        return LOOT;
        //return LootTableList.ENTITIES_GHAST;
    }

    @Override
    public @NotNull EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }
    

    /**
     * Checks to make sure the light is not too bright where the mob is spawning
     */
    protected boolean isValidLightLevel()
    {
        return true;
    }
	
    protected void initEntityAI()
    {
    	 this.tasks.addTask(4, new AIFireballAttack(this));
         this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
         this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
         this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
         this.tasks.addTask(8, new EntityAILookIdle(this));
         this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
         this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @SideOnly(Side.CLIENT)
    public boolean isAttacking()
    {
        return this.dataManager.get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.dataManager.set(ATTACKING, attacking);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(ATTACKING, Boolean.FALSE);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(72.0D);
       	this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0);
       	this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(0);
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_GHAST_HURT;
    }

    protected @NotNull SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }
	
    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 5.0F;
    }
    
    public float getEyeHeight()
    {
        return 1.5f;
    }

	@Override
	public float getWeaponPosX() {
		return 0;
	}

	@Override
	public float getWeaponPosY() {
		return 0;
	}

	@Override
	public float getWeaponPosZ() {
		return 0;
	}

	@Override
	public float getTotalArmorAgainstType(TGDamageSource dmgsrc) {
        switch(dmgsrc.damageType){
            case EXPLOSION:
                return 30.0f;
            case LIGHTNING:
            case ENERGY:
            case FIRE:
                return 10.0f;
            case ICE:
            case PHYSICAL:
            case PROJECTILE:
            case POISON:
            case RADIATION:
            case UNRESISTABLE:
            default:
                return 0.0f;
        }
	}

	@Override
	public float getPenetrationResistance(TGDamageSource dmgsrc) {
        switch(dmgsrc.damageType){
            case PROJECTILE:
                return 0.1f;
            case EXPLOSION:
                return 0.9f;
            case LIGHTNING:
            case ENERGY:
            case FIRE:
            case ICE:
            case PHYSICAL:
            case POISON:
            case RADIATION:
            case UNRESISTABLE:
            default:
                return 0.0f;
        }
	}

    @Override
    public TGNpcFaction getTGFaction() {
        return TGNpcFaction.HOSTILE;
    }

	protected static class AIGhastlingAttack extends EntityAIBase
    {
        private final Ghastling parentEntity;
        public int attackTimer;

        public AIGhastlingAttack(Ghastling mob)
        {
            this.parentEntity = mob;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return this.parentEntity.getAttackTarget() != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.attackTimer = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            this.parentEntity.setAttacking(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            //double d0 = 64.0D;

            if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen(entitylivingbase))
            {
                World world = this.parentEntity.world;
                ++this.attackTimer;

                /*if (this.attackTimer >= 14 && (this.attackTimer <24) && (this.attackTimer % 2 == 0))
                {
                	if(this.attackTimer==14) {
                		
            			TGPackets.network.sendToAllAround(new PacketPlaySound(TGSounds.HELICOPTER_BURST, this.parentEntity, 8.0f, 1.0f, false, false, TGSoundCategory.GUN_FIRE), TGPackets.targetPointAroundEnt(parentEntity, 100.0f));
                	}
 
                    GenericProjectile bullet = new GenericProjectile(this.parentEntity.world, this.parentEntity,12.0f, 1.0f, 100, 0.05f, 30, 40, 8.0f, 0.25f,false,EnumBulletFirePos.CENTER);         
                    world.spawnEntity(bullet);

                } else*/
                if (attackTimer == 15) {
                	

                   TGPackets.wrapper.sendToAllAround(new PacketPlaySound(TGSounds.NETHERBLASTER_FIRE, this.parentEntity, 1.0f, 1.0f, false, false, TGSoundCategory.GUN_FIRE), TGPackets.targetPointAroundEnt(parentEntity, 100.0f));
                   RocketProjectile rocket = new RocketProjectile(this.parentEntity.world, this.parentEntity,12.0f, 1.15f, 100, 0.05f, 30, 40, 8.0f, 0.25f,false,this.parentEntity.rand.nextBoolean()?EnumBulletFirePos.LEFT:EnumBulletFirePos.RIGHT, 2.0f, 0.0f);
            	    
            	    world.spawnEntity(rocket);
                	
                	
                } else if (attackTimer >30) {
                	this.attackTimer=0;
                }
            }
            else if (this.attackTimer > 0)
            {
                --this.attackTimer;
            }

            this.parentEntity.setAttacking(this.attackTimer > 10);
        }
    }

	protected static class AIFireballAttack extends EntityAIBase
     {
         private final Ghastling parentEntity;
         private int attackStep;
         private int attackTime;

         public AIFireballAttack(Ghastling mob)
         {
             this.parentEntity = mob;
             this.setMutexBits(3);
         }

         /**
          * Returns whether the EntityAIBase should begin execution.
          */
         public boolean shouldExecute()
         {
             EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
             return entitylivingbase != null && entitylivingbase.isEntityAlive();
         }

         /**
          * Execute a one shot task or start executing a continuous task
          */
         public void startExecuting()
         {
             this.attackStep = 0;
         }

         /**
          * Reset the task's internal state. Called when this task is interrupted by another one
          */
         public void resetTask()
         {
             //this.parentEntity.setOnFire(false);
        	 this.parentEntity.setAttacking(false);
         }

         /**
          * Keep ticking a continuous task that has already been started
          */
         public void updateTask() {
             --this.attackTime;
             EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
             double d0 = this.parentEntity.getDistanceSq(entitylivingbase);

             if (d0 < 4.0D) {
                 if (this.attackTime <= 0) {
                     this.attackTime = 20;
                     this.parentEntity.attackEntityAsMob(entitylivingbase);
                 }

                 this.parentEntity.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
             }
             else if (d0 < this.getFollowDistance() * this.getFollowDistance()) {
                 if (this.attackTime <= 0) {
                     ++this.attackStep;

                     if (this.attackStep == 1) {
                         this.attackTime = 30;
                         this.parentEntity.setAttacking(true);
                     } else if (this.attackStep <= 4) {
                         this.attackTime = 6;
                     } else {
                         this.attackTime = 50;
                         this.attackStep = 0;
                         //this.parentEntity.setOnFire(false);
                         this.parentEntity.setAttacking(false);
                     }

                     if (this.attackStep > 1) {
                         this.parentEntity.world.playEvent(null, 1018, new BlockPos((int)this.parentEntity.posX, (int)this.parentEntity.posY, (int)this.parentEntity.posZ), 0);
                 	    RocketProjectile rocket = new RocketProjectile(this.parentEntity.world, this.parentEntity,15.0f, 1.0f, 100, 0.05f, 30, 40, 8.0f, 0.25f,false,this.parentEntity.rand.nextBoolean()?EnumBulletFirePos.LEFT:EnumBulletFirePos.RIGHT, 1.0f, 0.0f);
                 	    
                 	    this.parentEntity.world.spawnEntity(rocket);
                         
                     }
                 }

                 this.parentEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
             } else {
                 this.parentEntity.getNavigator().clearPath();
                 this.parentEntity.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
             }

             super.updateTask();
         }

         private double getFollowDistance() {
             IAttributeInstance iattributeinstance = this.parentEntity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
             return iattributeinstance.getAttributeValue();
         }
     }
	
	/***
	 * Spawner linking behavior
	 */
	@Override
	public boolean getTryLink() {
		return tryLink;
	}

	@Override
	public void setTryLink(boolean value) {
		this.tryLink=value;
	}

	@Override
	public TGSpawnerNPCData getCapability(Capability<TGSpawnerNPCData> tgGenericnpcData) {
		return this.getCapability(tgGenericnpcData, null);
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
	
    /**
     * Called to update the entity's position/logic.
     */
	@Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            this.setDead();
        }
        
        this.onUpdateSpawner(world);
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

    private boolean isFriendlyEntity(@org.jetbrains.annotations.Nullable Entity entity) {
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
}

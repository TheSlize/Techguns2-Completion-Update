package techguns.world.dungeon.presets.specialblocks;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techguns.TGBlocks;
import techguns.TGConfig;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.SkeletonSoldier;
import techguns.entities.npcs.ZombieSoldier;
import techguns.tileentities.TGSpawnerTileEnt;
import techguns.util.MBlock;

public class MBlockTGSpawner extends MBlock {

    /**
     * 0 / negative delay = use {@link TGConfig#getSpawnerBlockIntervalTicks()} at placement.
     * Set by {@link #MBlockTGSpawner(MBlock)} copies from dungeon templates.
     */
    protected int mobsleft;
    protected int maxActive;
    protected int spawndelay = -1;
    protected int spawnrange = 2;

    protected ArrayList<Class> classes = new ArrayList<>();
    protected ArrayList<Integer> weights = new ArrayList<>();

    protected ItemStack weaponOverride = ItemStack.EMPTY;

    public MBlockTGSpawner(MBlock other) {
        super(other);
        this.hasTileEntity = true;
        addMobType(SkeletonSoldier.class, 1);
        addMobType(ZombieSoldier.class, 1);
    }

    public MBlockTGSpawner(EnumMonsterSpawnerType type, int mobsleft, int maxactive, int spawndelay, int spawnrange) {
        super(TGBlocks.MONSTER_SPAWNER, type.ordinal());
        this.mobsleft = mobsleft;
        this.maxActive = maxactive;
        this.spawndelay = spawndelay;
        this.spawnrange = spawnrange;
        this.hasTileEntity = true;
    }

    public MBlockTGSpawner setWeaponOverride(ItemStack weapon) {
        this.weaponOverride = weapon;
        return this;
    }

    public MBlockTGSpawner addMobType(Class clazz, int weight) {
        this.classes.add(clazz);
        this.weights.add(weight);
        return this;
    }

    @Override
    public void tileEntityPostPlacementAction(World w, IBlockState state, BlockPos p, int rotation) {
        TileEntity tile = w.getTileEntity(p);
        if (tile instanceof TGSpawnerTileEnt spawner) {

            int ml = this.mobsleft > 0 ? this.mobsleft : Math.max(1, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsTotal);
            int ma = this.maxActive > 0 ? this.maxActive : Math.max(1, Math.min(TGConfig.spawnerBlock.spawnerBlockWorldgenMobsConcurrent, ml));
            if (ma > ml) {
                ma = ml;
            }
            int ticks = this.spawndelay >= 0 ? this.spawndelay : TGConfig.getSpawnerBlockIntervalTicks();
            spawner.setParams(ml, ma, ticks, spawnrange);

            if (!this.weaponOverride.isEmpty()) {
                spawner.setWeaponOverride(weaponOverride);
            }

            for (int i = 0; i < classes.size(); i++) {
                spawner.addMobType(classes.get(i), weights.get(i));
            }

        }

    }
}

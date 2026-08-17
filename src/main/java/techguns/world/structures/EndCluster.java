package techguns.world.structures;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import techguns.TGConfig;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.StormTrooper;
import techguns.entities.npcs.SuperMutantElite;
import techguns.util.BlockUtils;
import techguns.util.MBlock;
import techguns.world.dungeon.presets.specialblocks.MBlockTGSpawner;

import java.util.ArrayList;
import java.util.Random;

public class EndCluster extends WorldgenStructure {

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;

    static {
        blockList.add(new MBlock("minecraft:end_stone", 0));
        blockList.add(new MBlock("techguns:basicore", 4));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.HOLE, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsTotal, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsConcurrent, TGConfig.getSpawnerBlockIntervalTicks(), 0).addMobType(StormTrooper.class, 20).addMobType(SuperMutantElite.class, 40));
        blockList.add(new MBlock("techguns:orecluster", 4));

        blocks = BlockUtils.loadStructureFromFile("end_cluster");
    }

    public EndCluster() {
        super(0, 0, 0, 0, 0, 0);
        this.setXYZSize(34, 15, 34);
    }

    @Override
    public void spawnStructureEndWorldgen(World world, int chunkX, int chunkZ, int sizeX, int sizeY, int sizeZ, Random rnd, Biome biome) {
        int direction = rnd.nextInt(4);

        int sizeXr = direction == 0 || direction == 2 ? sizeX : sizeZ;
        int sizeZr = direction == 0 || direction == 2 ? sizeZ : sizeX;

        int centerX = (int) (sizeX / 2.0f);
        int centerZ = (int) (sizeZ / 2.0f);

        int[] p0 = rotatePoint(0, 0, direction, centerX, centerZ);
        int[] p1 = rotatePoint(sizeX, 0, direction, centerX, centerZ);
        int[] p2 = rotatePoint(0, sizeZ, direction, centerX, centerZ);
        int[] p3 = rotatePoint(sizeX, sizeZ, direction, centerX, centerZ);

        int minX = Math.min(Math.min(p0[0], p1[0]), Math.min(p2[0], p3[0]));
        int minZ = Math.min(Math.min(p0[1], p1[1]), Math.min(p2[1], p3[1]));

        int x = minX + chunkX * 16;
        int z = minZ + chunkZ * 16;
        int y = 80;

        if (!BlockUtils.isAreaClear(world, x, y, z, sizeXr, sizeY, sizeZr)) {
            return;
        }

        this.setBlocks(world, x, y, z, sizeX, sizeY, sizeZ, direction, BiomeColorType.WOODLAND, rnd);
    }


    @Override
    public void setBlocks(World world, int posX, int posY, int posZ, int sizeX,
                          int sizeY, int sizeZ, int direction, WorldgenStructure.BiomeColorType colorType, Random rnd) {
        int centerX, centerZ;

        if (((sizeX < this.minX) && (sizeZ > this.minX) && (sizeX >= this.minZ))
                || ((sizeZ < this.minZ) && (sizeX > this.minZ) && (sizeZ >= this.minX))) {
            direction = (direction + 1) % 4;
            centerZ = (int) (sizeX / 2.0f);
            centerX = (int) (sizeZ / 2.0f);
        } else {
            centerX = (int) (sizeX / 2.0f);
            centerZ = (int) (sizeZ / 2.0f);
        }

        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0, this.lootTier, colorType);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 1, this.lootTier, colorType);
    }
}

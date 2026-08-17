package techguns.world.structures;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import techguns.TGConfig;
import techguns.Tags;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.CyberDemon;
import techguns.entities.npcs.StormTrooper;
import techguns.entities.npcs.ZombiePigmanSoldier;
import techguns.util.BlockUtils;
import techguns.util.MBlock;
import techguns.world.dungeon.presets.specialblocks.MBlockChestLoottable;
import techguns.world.dungeon.presets.specialblocks.MBlockTGSpawner;

import java.util.ArrayList;
import java.util.Random;

public class NetherOreClusterMedium extends WorldgenStructure {

    private static final ResourceLocation CHEST_LOOT = new ResourceLocation(Tags.MOD_ID, "chests/nether_cluster_secret");

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;

    static {
        blockList.add(new MBlock("minecraft:netherrack", 0));
        blockList.add(new MBlock("minecraft:air", 0));
        blockList.add(new MBlock("techguns:oredrill", 4));
        blockList.add(new MBlock("techguns:oredrill", 0));
        blockList.add(new MBlock("techguns:oredrill", 1));
        blockList.add(new MBlock("techguns:nethermetal", 4));
        blockList.add(new MBlock("techguns:nethermetal", 7));
        blockList.add(new MBlock("techguns:metalpanel", 7));
        blockList.add(new MBlock("techguns:oredrill", 3));
        blockList.add(new MBlock("minecraft:glowstone", 0));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.HOLE, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsTotal, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsConcurrent, TGConfig.getSpawnerBlockIntervalTicks(), 0).addMobType(CyberDemon.class, 30).addMobType(ZombiePigmanSoldier.class, 15).addMobType(StormTrooper.class, 10));
        blockList.add(new MBlock("techguns:orecluster", 7));
        blockList.add(new MBlock("techguns:oredrill", 2));
        blockList.add(new MBlock("techguns:concrete", 3));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 4, CHEST_LOOT));

        blocks = BlockUtils.loadStructureFromFile("nether_orecluster_medium");
    }

    public NetherOreClusterMedium() {
        super(0, 0, 0, 0, 0, 0);
        this.setXYZSize(18, 17, 12);
    }

    @Override
    public void setBlocks(World world, int posX, int posY, int posZ, int sizeX,
                          int sizeY, int sizeZ, int direction, BiomeColorType colorType, Random rnd) {
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

        BlockUtils.cleanUpwards(world, blocks, posX, posY, posZ, centerX, centerZ, direction, 3);
        BlockUtils.placeFoundationNether(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0, 4);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0, this.lootTier, colorType);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 1, this.lootTier, colorType);
    }
}

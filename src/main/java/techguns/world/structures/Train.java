package techguns.world.structures;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import techguns.TGConfig;
import techguns.Tags;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.Bandit;
import techguns.entities.npcs.Commando;
import techguns.util.BlockUtils;
import techguns.util.MBlock;
import techguns.world.dungeon.presets.specialblocks.MBlockChestLoottable;
import techguns.world.dungeon.presets.specialblocks.MBlockTGSpawner;

import java.util.ArrayList;
import java.util.Random;

public class Train extends WorldgenStructure {
    private static final ResourceLocation CHEST_LOOT_HEAD = new ResourceLocation(Tags.MOD_ID, "chests/train_head");
    private static final ResourceLocation CHEST_LOOT_ORANGE = new ResourceLocation(Tags.MOD_ID, "chests/train_orange");
    private static final ResourceLocation CHEST_LOOT_GREEN = new ResourceLocation(Tags.MOD_ID, "chests/train_green");
    private static final ResourceLocation CHEST_LOOT_RED = new ResourceLocation(Tags.MOD_ID, "chests/train_red");

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;

    static {
        blockList.add(new MBlock("minecraft:stone_slab", 5));
        blockList.add(new MBlock("techguns:ladder0", 4));
        blockList.add(new MBlock("techguns:stairs_metal", 3));
        blockList.add(new MBlock("minecraft:iron_bars", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 11));
        blockList.add(new MBlock("techguns:stairs_metal", 15));
        blockList.add(new MBlock("techguns:metalpanel", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 12));
        blockList.add(new MBlock("techguns:stairs_metal", 7));
        blockList.add(new MBlock("techguns:stairs_metal", 8));
        blockList.add(new MBlock("minecraft:monster_egg", 4));
        blockList.add(new MBlock("minecraft:double_stone_slab", 5));
        blockList.add(new MBlock("techguns:stairs_metal", 6));
        blockList.add(new MBlock("techguns:metalpanel", 7));
        blockList.add(new MBlock("techguns:metalpanel", 4));
        blockList.add(new MBlock("techguns:metalpanel", 1));
        blockList.add(new MBlock("techguns:metalpanel", 3));
        blockList.add(new MBlock("techguns:metalpanel", 6));
        blockList.add(new MBlock("techguns:concrete", 5));
        blockList.add(new MBlock("techguns:metalpanel", 2));
        blockList.add(new MBlock("minecraft:tripwire_hook", 3));
        blockList.add(new MBlock("minecraft:glass", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 13));
        blockList.add(new MBlock("techguns:stairs_metal", 9));
        blockList.add(new MBlock("minecraft:monster_egg", 2));
        blockList.add(new MBlock("minecraft:monster_egg", 3));
        blockList.add(new MBlock("minecraft:air", 0));
        blockList.add(new MBlock("minecraft:concrete", 15));
        blockList.add(new MBlock("minecraft:stone_slab", 13));
        blockList.add(new MBlock("techguns:stairs_metal", 14));
        blockList.add(new MBlock("minecraft:wooden_slab", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 10));
        blockList.add(new MBlock("minecraft:magma", 0));
        blockList.add(new MBlock("techguns:lamp0", 3));
        blockList.add(new MBlock("techguns:military_crate", 1));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 1));
        blockList.add(new MBlock("techguns:lamp0", 2));
        blockList.add(new MBlock("minecraft:fence", 0));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 5, CHEST_LOOT_ORANGE));
        blockList.add(new MBlock("techguns:military_crate", 7));
        blockList.add(new MBlock("techguns:military_crate", 3));
        blockList.add(new MBlock("techguns:military_crate", 0));
        blockList.add(new MBlock("techguns:military_crate", 2));
        blockList.add(new MBlock("minecraft:coal_block", 0));
        blockList.add(new MBlock("techguns:bunkerdoor", 0));
        blockList.add(new MBlock("minecraft:log", 8));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 5, CHEST_LOOT_HEAD));
        blockList.add(new MBlock("minecraft:oak_stairs", 3));
        blockList.add(new MBlock("techguns:stairs_metal", 4));
        blockList.add(new MBlock("techguns:military_crate", 6));
        blockList.add(new MBlock("techguns:bunkerdoor", 8));
        blockList.add(new MBlock("techguns:bunkerdoor", 9));
        blockList.add(new MBlock("minecraft:lever", 1));
        blockList.add(new MBlock("techguns:stairs_metal", 0));
        blockList.add(new MBlock("techguns:military_crate", 8));
        blockList.add(new MBlock("galacticraftcore:grating", 0));
        blockList.add(new MBlock("minecraft:redstone_block", 0));
        blockList.add(new MBlock("minecraft:redstone_torch", 4));
        blockList.add(new MBlock("techguns:sandbags", 0));
        blockList.add(new MBlock("minecraft:stone", 0));
        blockList.add(new MBlock("techguns:concrete", 1));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.HOLE, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsTotal, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsConcurrent, TGConfig.getSpawnerBlockIntervalTicks(), 0).addMobType(Commando.class, 20).addMobType(Bandit.class, 40));
        blockList.add(new MBlock("techguns:military_crate", 5));
        blockList.add(new MBlock("techguns:military_crate", 4));
        blockList.add(new MBlock("minecraft:iron_block", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 2));
        blockList.add(new MBlock("techguns:bunkerdoor", 5));
        blockList.add(new MBlock("techguns:ladder0", 8));
        blockList.add(new MBlock("techguns:stairs_metal", 1));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 4, CHEST_LOOT_RED));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 0));
        blockList.add(new MBlock("minecraft:stone", 6));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 4, CHEST_LOOT_GREEN));
        blockList.add(new MBlock("minecraft:double_wooden_slab", 0));
        blockList.add(new MBlock("minecraft:bed", 10));
        blockList.add(new MBlock("minecraft:bed", 2));
        blockList.add(new MBlock("minecraft:bed", 0));
        blockList.add(new MBlock("minecraft:bed", 8));
        blockList.add(new MBlock("techguns:bunkerdoor", 2));
        blockList.add(new MBlock("techguns:ladder0", 0));
        blockList.add(new MBlock("minecraft:lever", 2));
        blockList.add(new MBlock("techguns:stairs_metal", 5));
        blockList.add(new MBlock("techguns:ladder0", 12));
        blockList.add(new MBlock("minecraft:tripwire_hook", 1));

        blocks = BlockUtils.loadStructureFromFile("train");
    }

    public Train() {
        super(0, 0, 0, 0, 0, 0);
        this.setXYZSize(9, 12, 100);
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

        BlockUtils.cleanUpwards(world, blocks, posX, posY, posZ, centerX, centerZ, direction, 7);
        BlockUtils.placeFoundation(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0, 3);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0, this.lootTier, colorType);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 1, this.lootTier, colorType);
    }
}

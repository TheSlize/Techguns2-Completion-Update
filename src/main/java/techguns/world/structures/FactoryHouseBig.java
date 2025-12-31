package techguns.world.structures;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import techguns.Techguns;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.ZombieMiner;
import techguns.entities.npcs.ZombieSoldier;
import techguns.util.BlockUtils;
import techguns.util.MBlock;
import techguns.world.dungeon.presets.specialblocks.MBlockChestLoottable;
import techguns.world.dungeon.presets.specialblocks.MBlockTGSpawner;

import java.util.ArrayList;
import java.util.Random;

public class FactoryHouseBig extends WorldgenStructure {

    private static final ResourceLocation CHEST_LOOT = new ResourceLocation(Techguns.MODID,"chests/factory_building");

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;
    static {
        blockList.add(new MBlock("minecraft:air", 0));
        blockList.add(new MBlock("minecraft:air", 0));
        blockList.add(new MBlock("techguns:concrete", 2));
        blockList.add(new MBlock("techguns:metalpanel", 7));
        blockList.add(new MBlock("minecraft:brick_block", 0));
        blockList.add(new MBlock("minecraft:iron_bars", 0));
        blockList.add(new MBlock("minecraft:glass_pane", 0));
        blockList.add(new MBlock("techguns:concrete", 1));
        blockList.add(new MBlock("techguns:metalpanel", 0));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 5, CHEST_LOOT));
        blockList.add(new MBlock("techguns:lamp0", 3));
        blockList.add(new MBlock("minecraft:hardened_clay", 0));
        blockList.add(new MBlock("techguns:lamp0", 4));
        blockList.add(new MBlock("techguns:bunkerdoor", 1));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.HOLE,6,2,100,2).addMobType(ZombieMiner.class, 1).addMobType(ZombieSoldier.class, 1));
        blockList.add(new MBlock("minecraft:crafting_table", 0));
        blockList.add(new MBlock("techguns:bunkerdoor", 9));
        blockList.add(new MBlock("techguns:lamp0", 2));
        blockList.add(new MBlock("minecraft:furnace", 2));
        blockList.add(new MBlock("techguns:lamp0", 5));
        blockList.add(new MBlock("techguns:ladder0", 8));
        blockList.add(new MBlock("techguns:sandbags", 0));
        blockList.add(new MBlock("techguns:bunkerdoor", 3));
        blockList.add(new MBlock("minecraft:tallgrass", 1));
        blockList.add(new MBlock("techguns:camonet_top", 0));
        blockList.add(new MBlock("minecraft:brick_stairs", 7));
        blockList.add(new MBlock("minecraft:stone_slab", 12));
        blockList.add(new MBlock("minecraft:brick_stairs", 6));
        blockList.add(new MBlock("techguns:lamp0", 1));
        blockList.add(new MBlock("techguns:bunkerdoor", 8));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 4, CHEST_LOOT));

        blocks = BlockUtils.loadStructureFromFile("factory_building_big");
    }

    public FactoryHouseBig(int minX, int minY, int minZ, int maxX, int maxY,
                             int maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.setSwapXZ(true);
    }

    @Override
    public void setBlocks(World world, int posX, int posY, int posZ, int sizeX,
                          int sizeY, int sizeZ, int direction, BiomeColorType colorType, Random rnd) {
        int centerX, centerZ;

        if (((sizeX < this.minX) && (sizeZ > this.minX) && (sizeX >= this.minZ))
                ||((sizeZ < this.minZ) && (sizeX > this.minZ) && (sizeZ >= this.minX)))
        {
            direction = (direction+1) % 4;
            centerZ = (int) (sizeX/2.0f);
            centerX = (int) (sizeZ/2.0f);
        }else {
            centerX = (int) (sizeX/2.0f);
            centerZ = (int) (sizeZ/2.0f);
        }

        BlockUtils.cleanUpwards(world, blocks, posX, posY, posZ, centerX, centerZ, direction, 7);
        BlockUtils.placeFoundation(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0,3);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0,this.lootTier,colorType);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 1,this.lootTier,colorType);
    }
}

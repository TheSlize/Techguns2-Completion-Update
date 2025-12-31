package techguns.world.structures;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import techguns.Techguns;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.Bandit;
import techguns.entities.npcs.Commando;
import techguns.util.BlockUtils;
import techguns.util.MBlock;
import techguns.world.dungeon.presets.specialblocks.MBlockChestLoottable;
import techguns.world.dungeon.presets.specialblocks.MBlockTGSpawner;

import java.util.ArrayList;
import java.util.Random;

public class FactoryBig extends WorldgenStructure {

    private static final ResourceLocation CHEST_LOOT = new ResourceLocation(Techguns.MODID,"chests/factory_building_big");

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;
    static {
        blockList.add(new MBlock("techguns:concrete", 3));
        blockList.add(new MBlock("minecraft:tallgrass", 1));
        blockList.add(new MBlock("minecraft:red_flower", 4));
        blockList.add(new MBlock("minecraft:air", 0));
        blockList.add(new MBlock("techguns:metalpanel", 6));
        blockList.add(new MBlock("minecraft:iron_bars", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 13));
        blockList.add(new MBlock("techguns:concrete", 2));
        blockList.add(new MBlock("minecraft:yellow_flower", 0));
        blockList.add(new MBlock("minecraft:red_flower", 3));
        blockList.add(new MBlock("techguns:stairs_concrete", 7));
        blockList.add(new MBlock("techguns:lamp0", 5));
        blockList.add(new MBlock("techguns:stairs_concrete", 0));
        blockList.add(new MBlock("techguns:stairs_concrete", 3));
        blockList.add(new MBlock("techguns:stairs_concrete", 2));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.HOLE,6,2,150,2).addMobType(Bandit.class, 1).addMobType(Commando.class, 1));
        blockList.add(new MBlock("techguns:stairs_concrete", 4));
        blockList.add(new MBlock("techguns:metalpanel", 7));
        blockList.add(new MBlock("techguns:stairs_concrete", 6));
        blockList.add(new MBlock("techguns:military_crate", 8));
        blockList.add(new MBlock("techguns:metalpanel", 1));
        blockList.add(new MBlock("techguns:metalpanel", 2));
        blockList.add(new MBlock("techguns:military_crate", 5));
        blockList.add(new MBlock("techguns:military_crate", 6));
        blockList.add(new MBlock("techguns:metalpanel", 3));
        blockList.add(new MBlock("techguns:stairs_metal", 14));
        blockList.add(new MBlock("minecraft:stone_slab", 5));
        blockList.add(new MBlock("techguns:stairs_concrete", 5));
        blockList.add(new MBlock("techguns:military_crate", 2));
        blockList.add(new MBlock("techguns:military_crate", 7));
        blockList.add(new MBlock("techguns:military_crate", 3));
        blockList.add(new MBlock("techguns:military_crate", 0));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 0));
        blockList.add(new MBlock("techguns:metalpanel", 0));
        blockList.add(new MBlock("minecraft:brick_block", 0));
        blockList.add(new MBlock("techguns:bunkerdoor", 4));
        blockList.add(new MBlock("techguns:bunkerdoor", 9));
        blockList.add(new MBlock("techguns:stairs_metal", 12));
        blockList.add(new MBlock("techguns:stairs_metal", 15));
        blockList.add(new MBlock("techguns:bunkerdoor", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 11));
        blockList.add(new MBlock("techguns:bunkerdoor", 8));
        blockList.add(new MBlock("minecraft:stone_slab", 13));
        blockList.add(new MBlock("techguns:metalpanel", 4));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 5));
        blockList.add(new MBlock("techguns:ladder0", 0));
        blockList.add(new MBlock("techguns:camonet", 0));
        blockList.add(new MBlock("minecraft:lever", 0));
        blockList.add(new MBlock("minecraft:stained_hardened_clay", 9));
        blockList.add(new MBlock("minecraft:unlit_redstone_torch", 2));
        blockList.add(new MBlock("techguns:camonet_top", 0));
        blockList.add(new MBlock("minecraft:water", 0));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 6));
        blockList.add(new MBlock("minecraft:lava", 0));
        blockList.add(new MBlock("minecraft:brick_stairs", 4));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 11));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 8));
        blockList.add(new MBlock("minecraft:lever", 6));
        blockList.add(new MBlock("minecraft:brick_stairs", 6));
        blockList.add(new MBlock("minecraft:brick_stairs", 0));
        blockList.add(new MBlock("minecraft:double_stone_slab", 4));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 3));
        blockList.add(new MBlock("minecraft:iron_block", 0));
        blockList.add(new MBlock("minecraft:brick_stairs", 3));
        blockList.add(new MBlock("minecraft:cauldron", 0));
        blockList.add(new MBlock("minecraft:brick_stairs", 7));
        blockList.add(new MBlock("minecraft:rail", 6));
        blockList.add(new MBlock("minecraft:rail", 0));
        blockList.add(new MBlock("minecraft:hopper", 8));
        blockList.add(new MBlock("minecraft:unlit_redstone_torch", 4));
        blockList.add(new MBlock("minecraft:redstone_block", 0));
        blockList.add(new MBlock("minecraft:unlit_redstone_torch", 3));
        blockList.add(new MBlock("minecraft:brick_stairs", 1));
        blockList.add(new MBlock("techguns:concrete", 5));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 2));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 4));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 5));
        blockList.add(new MBlock("minecraft:water", 1));
        blockList.add(new MBlock("techguns:concrete", 4));
        blockList.add(new MBlock("minecraft:stone_slab", 12));
        blockList.add(new MBlock("minecraft:double_stone_slab", 5));
        blockList.add(new MBlock("minecraft:stained_hardened_clay", 8));
        blockList.add(new MBlock("minecraft:brick_stairs", 5));
        blockList.add(new MBlock("minecraft:rail", 1));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 6));
        blockList.add(new MBlock("minecraft:stone_slab", 4));
        blockList.add(new MBlock("minecraft:unlit_redstone_torch", 1));
        blockList.add(new MBlock("minecraft:brick_stairs", 2));
        blockList.add(new MBlock("minecraft:glass_pane", 0));
        blockList.add(new MBlock("minecraft:stained_hardened_clay", 7));
        blockList.add(new MBlock("minecraft:dispenser", 3));
        blockList.add(new MBlock("minecraft:dispenser", 2));
        blockList.add(new MBlock("techguns:lamp0", 3));
        blockList.add(new MBlock("techguns:lamp0", 2));
        blockList.add(new MBlock("minecraft:lava", 10));
        blockList.add(new MBlock("minecraft:lava", 2));
        blockList.add(new MBlock("minecraft:rail", 8));
        blockList.add(new MBlock("minecraft:rail", 5));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 7));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 2, CHEST_LOOT));
        blockList.add(new MBlock("minecraft:rail", 3));
        blockList.add(new MBlock("techguns:stairs_metal", 1));
        blockList.add(new MBlock("minecraft:redstone_torch", 5));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 1));
        blockList.add(new MBlock("minecraft:rail", 7));
        blockList.add(new MBlock("techguns:stairs_concrete", 1));
        blockList.add(new MBlock("minecraft:rail", 9));
        blockList.add(new MBlock("minecraft:glass", 0));
        blockList.add(new MBlock("techguns:lamp0", 1));
        blockList.add(new MBlock("minecraft:stone", 6));
        blockList.add(new MBlock("minecraft:cobblestone_wall", 1));
        blockList.add(new MBlock("minecraft:cobblestone_wall", 0));
        blockList.add(new MBlock("minecraft:stone_brick_stairs", 4));
        blockList.add(new MBlock("minecraft:crafting_table", 0));
        blockList.add(new MBlock("techguns:lamp0", 4));
        blockList.add(new MBlock("techguns:stairs_metal", 3));
        blockList.add(new MBlock("minecraft:furnace", 4));
        blockList.add(new MBlock("minecraft:anvil", 8));
        blockList.add(new MBlock("minecraft:monster_egg", 3));
        blockList.add(new MBlock("futuremc:stone_brick_wall", 0));
        blockList.add(new MBlock("minecraft:monster_egg", 2));
        blockList.add(new MBlock("futuremc:mossy_stone_brick_wall", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 9));
        blockList.add(new MBlock("techguns:stairs_metal", 5));
        blockList.add(new MBlock("minecraft:red_flower", 0));

        blocks = BlockUtils.loadStructureFromFile("factory_big");

    }

    public FactoryBig(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.setSwapXZ(true);
    }

    @Override
    public void setBlocks(World world, int posX, int posY, int posZ, int sizeX, int sizeY, int sizeZ, int direction, BiomeColorType colorType, Random rnd) {
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

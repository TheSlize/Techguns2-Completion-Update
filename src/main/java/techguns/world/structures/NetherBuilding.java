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
import techguns.world.dungeon.presets.specialblocks.MBlockBanner;
import techguns.world.dungeon.presets.specialblocks.MBlockChestLoottable;
import techguns.world.dungeon.presets.specialblocks.MBlockSkull;
import techguns.world.dungeon.presets.specialblocks.MBlockTGSpawner;

import java.util.ArrayList;
import java.util.Random;

public class NetherBuilding extends WorldgenStructure {
    private static final ResourceLocation CHEST_LOOT = new ResourceLocation(Tags.MOD_ID, "chests/nether_building_common");
    private static final ResourceLocation CHEST_LOOT_RARE = new ResourceLocation(Tags.MOD_ID, "chests/nether_building_trap");

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;

    static {
        blockList.add(new MBlock("minecraft:netherrack", 0));
        blockList.add(new MBlock("minecraft:air", 0));
        blockList.add(new MBlock("techguns:lamp0", 5));
        blockList.add(new MBlock("minecraft:tnt", 0));
        blockList.add(new MBlock("techguns:metalpanel", 6));
        blockList.add(new MBlock("techguns:metalpanel", 7));
        blockList.add(new MBlock("minecraft:stained_hardened_clay", 14));
        blockList.add(new MBlock("minecraft:stained_glass_pane", 7));
        blockList.add(new MBlock("minecraft:double_stone_slab", 0));
        blockList.add(new MBlock("minecraft:nether_wart_block", 0));
        blockList.add(new MBlock("techguns:nethermetal", 0));
        blockList.add(new MBlock("minecraft:nether_brick_stairs", 1));
        blockList.add(new MBlockChestLoottable(Blocks.TRAPPED_CHEST, 2, CHEST_LOOT_RARE));
        blockList.add(new MBlock("minecraft:wall_sign", 5));
        blockList.add(new MBlock("minecraft:air", 0));
        blockList.add(new MBlock("minecraft:stone_slab", 0));
        blockList.add(new MBlock("minecraft:red_nether_brick", 0));
        blockList.add(new MBlock("minecraft:stone_slab", 14));
        blockList.add(new MBlock("minecraft:stone_slab", 8));
        blockList.add(new MBlock("minecraft:bookshelf", 0));
        blockList.add(new MBlockSkull(3, 10, "89eddb62-24bd-48a3-b1d8-3f5fea05e754", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdkOTc3ZTI4ODlhMWY1MGJiYjIzZDQyMjI3NWFiOWRhYTNjMTUwYzg5ODMxZTc5ODRiNDliNDA5ZGIwNjcifX19"));
        blockList.add(new MBlockSkull(3, 11, "41b9983f-af6b-4a69-8117-eacbe0d49038", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdhNTMwZjVjNTc0MmJmMTllMTc1YTRkNzhhZDQzNWFjMGY0Mzk2ZDNiNTQ2NGJkNjE4MmFiMzgyYWNhNDE3ZCJ9fX0="));
        blockList.add(new MBlockSkull(3, 14, "f48302da-bcfd-4882-9386-c19056465c02", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWViZmQyMzk2Y2JhYmRiNDJjMzQ4YmNmNDE1OTljODdhNTA2YTcxZWY2MDk0OGM0OTZmOTVjNmNiNjMxNDEifX19"));
        blockList.add(new MBlockSkull(1, 5, null, null, null));
        blockList.add(new MBlock("minecraft:flower_pot", 0));
        blockList.add(new MBlockSkull(3, 3, "dbb97ff5-b3d5-4ee6-91eb-1c129046317b", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y4ZDkxNTg1YmZhZWExZDBiZWIxYjFiNGU1Zjk4NzYyOTYzOTlhOWZlNWFjOTNkMzE5ZmEyMTI3M2JjMCJ9fX0="));
        blockList.add(new MBlock("techguns:lamp0", 3));
        blockList.add(new MBlock("techguns:military_crate", 8));
        blockList.add(new MBlock("techguns:military_crate", 1));
        blockList.add(new MBlock("techguns:bunkerdoor", 1));
        blockList.add(new MBlock("techguns:bunkerdoor", 3));
        blockList.add(new MBlock("minecraft:iron_bars", 0));
        blockList.add(new MBlock("techguns:bunkerdoor", 9));
        blockList.add(new MBlockBanner(false, 3, 15, MBlockBanner.createPatterns("cr", 1, "sc", 1, "ss", 0, "cbo", 0, "bo", 0, "flo", 0)));
        blockList.add(new MBlock("minecraft:nether_brick_stairs", 3));
        blockList.add(new MBlock("minecraft:nether_brick_fence", 0));
        blockList.add(new MBlock("minecraft:wall_sign", 4));
        blockList.add(new MBlock("minecraft:iron_door", 0));
        blockList.add(new MBlock("minecraft:light_weighted_pressure_plate", 0));
        blockList.add(new MBlock("minecraft:iron_door", 8));
        blockList.add(new MBlock("techguns:bunkerdoor", 8));
        blockList.add(new MBlock("techguns:lamp0", 2));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.SOLDIER_SPAWN, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsTotal, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsConcurrent, TGConfig.getSpawnerBlockIntervalTicks(), 0).addMobType(ZombiePigmanSoldier.class, 2).addMobType(StormTrooper.class, 1).addMobType(CyberDemon.class, 1));
        blockList.add(new MBlock("techguns:stairs_metal", 13));
        blockList.add(new MBlockChestLoottable(Blocks.TRAPPED_CHEST, 3, CHEST_LOOT));
        blockList.add(new MBlock("techguns:ladder0", 11));
        blockList.add(new MBlock("minecraft:bed", 3));
        blockList.add(new MBlock("minecraft:lever", 1));
        blockList.add(new MBlock("techguns:lamp0", 4));
        blockList.add(new MBlock("minecraft:bed", 11));
        blockList.add(new MBlock("minecraft:wall_sign", 3));
        blockList.add(new MBlock("minecraft:nether_brick_stairs", 2));
        blockList.add(new MBlockSkull(3, 8, "7bb124f7-41b2-48d9-8267-ec2078dd9b0f", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJkZDdkODE4Y2JhNjUyYjAxY2YzNTBkMmIyYTFjZWVkZDRmNDZhY2FkMDViMmNlODFjM2Y4NzdlYWI3MTcifX19"));
        blockList.add(new MBlock("techguns:nethermetal", 5));
        blockList.add(new MBlock("techguns:bunkerdoor", 0));
        blockList.add(new MBlockChestLoottable(Blocks.TRAPPED_CHEST, 4, CHEST_LOOT));
        blockList.add(new MBlock("techguns:military_crate", 5));
        blockList.add(new MBlock("techguns:bunkerdoor", 2));
        blockList.add(new MBlock("techguns:military_crate", 4));
        blockList.add(new MBlockChestLoottable(Blocks.TRAPPED_CHEST, 5, CHEST_LOOT));
        blockList.add(new MBlock("techguns:sandbags", 0));
        blockList.add(new MBlock("minecraft:stained_glass", 15));
        blockList.add(new MBlock("techguns:ladder0", 2));
        blockList.add(new MBlock("techguns:block_creeper_acid", 0));
        blockList.add(new MBlock("techguns:military_crate", 0));
        blockList.add(new MBlockBanner(false, 5, 15, MBlockBanner.createPatterns("cr", 1, "sc", 1, "ss", 0, "cbo", 0, "bo", 0, "flo", 0)));

        blocks = BlockUtils.loadStructureFromFile("nether_building");
    }

    public NetherBuilding() {
        super(0, 0, 0, 0, 0, 0);
        this.setXYZSize(21, 12, 22);
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
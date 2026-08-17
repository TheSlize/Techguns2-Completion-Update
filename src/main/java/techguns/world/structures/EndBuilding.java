package techguns.world.structures;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import techguns.TGConfig;
import techguns.Tags;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.Outcast;
import techguns.entities.npcs.SuperMutantElite;
import techguns.util.BlockUtils;
import techguns.util.MBlock;
import techguns.util.MEntity;
import techguns.world.dungeon.presets.specialblocks.MBlockChestLoottable;
import techguns.world.dungeon.presets.specialblocks.MBlockItemFrame;
import techguns.world.dungeon.presets.specialblocks.MBlockSkull;
import techguns.world.dungeon.presets.specialblocks.MBlockTGSpawner;
import techguns.world.dungeon.presets.specialentities.MEntityEndCrystal;
import techguns.world.dungeon.presets.specialentities.MEntityShulker;

import java.util.ArrayList;
import java.util.Random;

public class EndBuilding extends WorldgenStructure {
    private static final ResourceLocation CHEST_LOOT = new ResourceLocation(Tags.MOD_ID, "chests/end_building");

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;

    static ArrayList<MEntity> entityList = new ArrayList<>();
    static short[][] entities;

    static {
        blockList.add(new MBlock("minecraft:chorus_plant", 0));
        blockList.add(new MBlock("minecraft:chorus_flower", 5));
        blockList.add(new MBlock("minecraft:end_bricks", 0));
        blockList.add(new MBlock("minecraft:end_stone", 0));
        blockList.add(new MBlock("minecraft:obsidian", 0));
        blockList.add(new MBlock("minecraft:purple_glazed_terracotta", 0));
        blockList.add(new MBlock("minecraft:stained_glass", 10));
        blockList.add(new MBlock("minecraft:concrete", 10));
        blockList.add(new MBlock("minecraft:purpur_block", 0));
        blockList.add(new MBlock("minecraft:purpur_stairs", 7));
        blockList.add(new MBlock("minecraft:stained_hardened_clay", 11));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.HOLE, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsTotal, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsConcurrent, TGConfig.getSpawnerBlockIntervalTicks(), 1).addMobType(SuperMutantElite.class, 20).addMobType(Outcast.class, 20));
        blockList.add(new MBlock("minecraft:purpur_stairs", 6));
        blockList.add(new MBlock("minecraft:purpur_slab", 0));
        blockList.add(new MBlock("minecraft:purpur_stairs", 0));
        blockList.add(new MBlock("minecraft:purpur_slab", 8));
        blockList.add(new MBlock("minecraft:purpur_stairs", 4));
        blockList.add(new MBlock("minecraft:purpur_stairs", 5));
        blockList.add(new MBlock("minecraft:purpur_stairs", 3));
        blockList.add(new MBlock("minecraft:purpur_double_slab", 0));
        blockList.add(new MBlock("minecraft:purpur_stairs", 2));
        blockList.add(new MBlock("minecraft:purpur_stairs", 1));
        blockList.add(new MBlock("minecraft:chorus_flower", 0));
        blockList.add(new MBlock("minecraft:chorus_flower", 1));
        blockList.add(new MBlockChestLoottable(Blocks.PURPLE_SHULKER_BOX, 1, CHEST_LOOT));
        blockList.add(new MBlock("minecraft:ender_chest", 4));
        blockList.add(new MBlockChestLoottable(Blocks.PURPLE_SHULKER_BOX, 4, CHEST_LOOT));
        blockList.add(new MBlockSkull(3, 11, "ee81faf1-e1f2-49f6-a034-6148217ba388", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWNiN2MyMWNjNDNkYzE3Njc4ZWU2ZjE2NTkxZmZhYWIxZjYzN2MzN2Y0ZjZiYmQ4Y2VhNDk3NDUxZDc2ZGI2ZCJ9fX0="));
        blockList.add(new MBlockItemFrame(2, "techguns:pulserifle", 0, 1, "{ammo:12s,ammovariant:'default',camo:0b}", 1));

        entityList.add(new MEntityEndCrystal(0.5, 0.0, 0.5, false));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.EAST));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.SOUTH));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.UP));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.WEST));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.DOWN));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.NORTH));

        blocks = BlockUtils.loadStructureFromFile("end_building");
        entities = BlockUtils.loadEntityDataFromFile("end_building");
    }

    public EndBuilding() {
        super(0, 0, 0, 0, 0, 0);
        this.setXYZSize(39, 20, 61);
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

        int y = BlockUtils.getValidEndIslandSpawnY(world, x, z, sizeXr, sizeZr, 8, 6);

        if (y < 0) {
            return;
        }

        BlockUtils.placeFoundation(world, blocks, blockList, x, y, z, centerX, centerZ, direction, 0, 5);
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

        BlockUtils.cleanUpwards(world, blocks, posX, posY, posZ, centerX, centerZ, direction, 10);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0, this.lootTier, colorType);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 1, this.lootTier, colorType);
        BlockUtils.placeScannedEntities(world, entities, entityList, posX, posY, posZ, centerX, centerZ, direction, 0, this.lootTier, colorType);
        BlockUtils.placeScannedEntities(world, entities, entityList, posX, posY, posZ, centerX, centerZ, direction, 1, this.lootTier, colorType);
    }
}

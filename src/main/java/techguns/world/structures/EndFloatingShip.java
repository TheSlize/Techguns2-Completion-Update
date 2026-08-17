package techguns.world.structures;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import techguns.TGConfig;
import techguns.Tags;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.Commando;
import techguns.entities.npcs.DictatorDave;
import techguns.entities.npcs.Outcast;
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

public class EndFloatingShip extends WorldgenStructure {
    private static final ResourceLocation CHEST_LOOT = new ResourceLocation(Tags.MOD_ID, "chests/end_floating_ship");

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;

    static ArrayList<MEntity> entityList = new ArrayList<>();
    static short[][] entities;

    static {
        blockList.add(new MBlock("minecraft:end_rod", 4));
        blockList.add(new MBlock("minecraft:purpur_slab", 8));
        blockList.add(new MBlock("minecraft:purpur_stairs", 4));
        blockList.add(new MBlock("minecraft:purpur_slab", 0));
        blockList.add(new MBlock("minecraft:purpur_pillar", 4));
        blockList.add(new MBlock("minecraft:purpur_stairs", 0));
        blockList.add(new MBlock("minecraft:end_bricks", 0));
        blockList.add(new MBlock("minecraft:purpur_stairs", 3));
        blockList.add(new MBlock("minecraft:purpur_stairs", 2));
        blockList.add(new MBlock("techguns:neonlights", 0));
        blockList.add(new MBlock("minecraft:purpur_stairs", 1));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.HOLE, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsTotal, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsConcurrent, TGConfig.getSpawnerBlockIntervalTicks(), 1).addMobType(DictatorDave.class, 2).addMobType(Outcast.class, 20).addMobType(Commando.class, 13));
        blockList.add(new MBlock("minecraft:purpur_pillar", 8));
        blockList.add(new MBlock("minecraft:purpur_block", 0));
        blockList.add(new MBlock("minecraft:purpur_pillar", 0));
        blockList.add(new MBlockSkull(3, 5, "36122cdc-6c97-4b97-990a-ef4df57db922", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFhOGZjOGRlNjQxN2I0OGQ0OGM4MGI0NDNjZjUzMjZlM2Q5ZGE0ZGJlOWIyNWZjZDQ5NTQ5ZDk2MTY4ZmMwIn19fQ=="));
        blockList.add(new MBlockSkull(3, 3, "4871fc40-b2c7-431d-9eb8-b54cd666dca7", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg0MGI4N2Q1MjI3MWQyYTc1NWRlZGM4Mjg3N2UwZWQzZGY2N2RjYzQyZWE0NzllYzE0NjE3NmIwMjc3OWE1In19fQ=="));
        blockList.add(new MBlock("minecraft:birch_fence", 0));
        blockList.add(new MBlock("minecraft:concrete", 10));
        blockList.add(new MBlockSkull(3, 8, "42db67b6-9dd1-4bfe-b478-8829c1622218", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTZjYzQ4NmMyYmUxY2I5ZGZjYjJlNTNkZDlhM2U5YTg4M2JmYWRiMjdjYjk1NmYxODk2ZDYwMmI0MDY3In19fQ=="));
        blockList.add(new MBlockSkull(3, 3, "3b525dc9-ee93-4652-aef2-9e3611281ace", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZhM2ZkOGYyZjNlMzExNGY2NWY5Nzg5NzU4YmQ5NTk1MWNmYTBkNWExMmNhNmRiNGYyYTlhZGZhMjQ5YmQifX19"));
        blockList.add(new MBlock("minecraft:wool", 10));
        blockList.add(new MBlockChestLoottable(Blocks.PURPLE_SHULKER_BOX, 1, CHEST_LOOT));
        blockList.add(new MBlock("minecraft:stained_glass", 10));
        blockList.add(new MBlock("minecraft:purpur_stairs", 6));
        blockList.add(new MBlock("minecraft:purpur_stairs", 5));
        blockList.add(new MBlock("minecraft:purpur_stairs", 7));
        blockList.add(new MBlock("minecraft:concrete_powder", 10));
        blockList.add(new MBlock("minecraft:end_rod", 5));
        blockList.add(new MBlock("minecraft:lever", 5));
        blockList.add(new MBlock("minecraft:purpur_double_slab", 0));
        blockList.add(new MBlock("minecraft:stone_button", 1));
        blockList.add(new MBlock("minecraft:tripwire_hook", 3));
        blockList.add(new MBlock("minecraft:ender_chest", 4));
        blockList.add(new MBlockSkull(3, 13, "2cf928cb-6162-49e0-987b-fd95e0102ed2", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzYmNhZjZkMjY3OWQ4ZDdkOWJmNmE0NzRhNDhhNzdhOGU5MTc0N2ExMDg0YzA5MjU2ZWJjODZjYjc0ODExIn19fQ=="));
        blockList.add(new MBlockItemFrame(5, "minecraft:chorus_flower", 0, 1, null, 0));

        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.DOWN));
        entityList.add(new MEntityEndCrystal(0.5, 0.0, 0.5, false));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.EAST));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.UP));
        entityList.add(new MEntityShulker(0.5, 0.0, 0.5, EnumFacing.WEST));

        blocks = BlockUtils.loadStructureFromFile("end_floating_ship");
        entities = BlockUtils.loadEntityDataFromFile("end_floating_ship");
    }

    public EndFloatingShip() {
        super(0, 0, 0, 0, 0, 0);
        this.setXYZSize(72, 51, 37);
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
        int y = 150;

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
        BlockUtils.placeScannedEntities(world, entities, entityList, posX, posY, posZ, centerX, centerZ, direction, 0, this.lootTier, colorType);
        BlockUtils.placeScannedEntities(world, entities, entityList, posX, posY, posZ, centerX, centerZ, direction, 1, this.lootTier, colorType);
    }
}

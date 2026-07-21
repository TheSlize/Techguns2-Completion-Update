package techguns.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import techguns.TGConfig;
import techguns.world.structures.NetherSubmarine;
import techguns.world.structures.WorldgenStructure;

public class WorldGenTGStructureSpawn implements IWorldGenerator {
    public static final int NETHER_STRUCT_MIN_Y = 20;
    public static final int NETHER_STRUCT_MAX_y = 100;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {

        // overworld
        if (world.provider.getDimension() == 0) {
            Biome biome = world.getBiome(new BlockPos(chunkX * 16, 64, chunkZ * 16));
            generateSurface(world, random, chunkX, chunkZ, biome);

        // Nether
        } else if (world.provider.getDimension() == -1) {
            Biome biome = world.getBiome(new BlockPos(chunkX * 16, 64, chunkZ * 16));
            generateNether(world, random, chunkX, chunkZ, biome);

        // End
        } else if (world.provider.getDimension() == 1) {
            Biome biome = world.getBiome(new BlockPos(chunkX * 16, 64, chunkZ * 16));
            generateEnd(world, random, chunkX, chunkZ, biome);
        }
    }

    private void generateEnd(World world, Random random, int cx, int cz, Biome biome) {
        int blockX = cx * 16;
        int blockZ = cz * 16;
        double distanceFromOrigin = Math.sqrt((double) blockX * blockX + (double) blockZ * blockZ);
        if (distanceFromOrigin < 1300) {
            return;
        }

        int SPAWNWEIGHT_SMALL = TGConfig.worldgen.spawnWeightTGStructureSmallEnd;
        int SPAWNWEIGHT_MEDIUM = TGConfig.worldgen.spawnWeightTGStructureMediumEnd;
        int SPAWNWEIGHT_BIG = TGConfig.worldgen.spawnWeightTGStructureBigEnd;

        StructureSize size = null;
        int sizeX;
        int sizeZ;
        int sizeY;

        if ((cx % SPAWNWEIGHT_BIG == 0) && (cz % SPAWNWEIGHT_BIG == 0)) {
            size = StructureSize.BIG;
        } else if ((cx % SPAWNWEIGHT_MEDIUM == 0) && (cz % SPAWNWEIGHT_MEDIUM == 0)) {
            size = StructureSize.MEDIUM;
        } else if ((cx % SPAWNWEIGHT_SMALL == 0) && (cz % SPAWNWEIGHT_SMALL == 0)) {
            size = StructureSize.SMALL;
        }

        if (size != null) {
            StructureLandType type = StructureLandType.LAND;

            WorldgenStructure s = TGStructureSpawnRegister.choseStructure(random, biome, size, type, world.provider.getDimension());

            if (s != null) {
                sizeX = s.getSizeX(random);
                sizeZ = s.getSizeZ(random);
                sizeY = s.getSizeY(random);

                s.spawnStructureEndWorldgen(world, cx, cz, sizeX, sizeY, sizeZ, random, biome);
            }
        }
    }


    private void generateNether(World world, Random random, int cx, int cz, Biome biome) {

        int SPAWNWEIGHT_SMALL = TGConfig.worldgen.spawnWeightTGStructureSmallNether;
        int SPAWNWEIGHT_MEDIUM = TGConfig.worldgen.spawnWeightTGStructureMediumNether;
        int SPAWNWEIGHT_BIG = TGConfig.worldgen.spawnWeightTGStructureBigNether;

        StructureSize size = null;
        int sizeX;
        int sizeZ;
        int sizeY;

        if ((cx % SPAWNWEIGHT_BIG == 0) && (cz % SPAWNWEIGHT_BIG == 0)) {
            size = StructureSize.BIG;
        } else if ((cx % SPAWNWEIGHT_MEDIUM == 0) && (cz % SPAWNWEIGHT_MEDIUM == 0)) {
            size = StructureSize.MEDIUM;
        } else if ((cx % SPAWNWEIGHT_SMALL == 0) && (cz % SPAWNWEIGHT_SMALL == 0)) {
            size = StructureSize.SMALL;
        }

        if (size != null) {
            StructureLandType type;

            if (random.nextInt(3) == 0) {
                type = StructureLandType.LAVA;
            } else {
                type = StructureLandType.LAND;
            }

            WorldgenStructure s = TGStructureSpawnRegister.choseStructure(random, biome, size, type, world.provider.getDimension());

            if (s == null && type == StructureLandType.LAVA) {
                type = StructureLandType.LAND;
                s = TGStructureSpawnRegister.choseStructure(random, biome, size, type, world.provider.getDimension());
            }

            if (s != null) {
                sizeX = s.getSizeX(random);
                sizeZ = s.getSizeZ(random);
                sizeY = s.getSizeY(random);

                if (type == StructureLandType.LAVA && s instanceof NetherSubmarine) { // don't think I'd spawn anything else ONLY in lava...
                    ((NetherSubmarine) s).spawnStructureLavaWorldgen(world, cx, cz, sizeX, sizeY, sizeZ, random, biome);
                } else {
                    s.spawnStructureCaveWorldgen(world, cx, cz, sizeX, sizeY, sizeZ, random, biome);
                }
            }
        }
    }

    private void generateSurface(World world, Random random, int cx, int cz, Biome biome) {

        int SPAWNWEIGHT_SMALL = TGConfig.worldgen.spawnWeightTGStructureSmallOverworld;
        int SPAWNWEIGHT_MEDIUM = TGConfig.worldgen.spawnWeightTGStructureMediumOverworld;
        int SPAWNWEIGHT_BIG = TGConfig.worldgen.spawnWeightTGStructureBigOverworld;

        StructureSize size = null;
        int sizeX;
        int sizeZ;
        int sizeY;

        if ((cx % SPAWNWEIGHT_BIG == 0) && (cz % SPAWNWEIGHT_BIG == 0)) {
            size = StructureSize.BIG;
        } else if ((cx % SPAWNWEIGHT_MEDIUM == 0) && (cz % SPAWNWEIGHT_MEDIUM == 0)) {

            size = StructureSize.MEDIUM;
        } else if ((cx % SPAWNWEIGHT_SMALL == 0) && (cz % SPAWNWEIGHT_SMALL == 0)) {
            size = StructureSize.SMALL;
        }


        if (size != null) {
            StructureLandType type = StructureLandType.LAND;

            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
                type = StructureLandType.WATER;
            }
            WorldgenStructure s = TGStructureSpawnRegister.choseStructure(random, biome, size, type, world.provider.getDimension());
            if (s != null) {
                sizeX = s.getSizeX(random);
                sizeZ = s.getSizeZ(random);
                sizeY = s.getSizeY(random);
                s.spawnStructureWorldgen(world, cx, cz, sizeX, sizeY, sizeZ, random, biome);
            }

        }

    }


}

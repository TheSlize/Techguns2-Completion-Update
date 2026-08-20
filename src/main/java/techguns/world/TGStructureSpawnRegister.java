package techguns.world;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import techguns.TGConfig;
import techguns.TGFluids;
import techguns.world.structures.*;

public class TGStructureSpawnRegister {
    protected static ArrayList<TGStructureSpawn> spawns_small = new ArrayList<>();
    protected static ArrayList<TGStructureSpawn> spawns_big = new ArrayList<>();
    protected static ArrayList<TGStructureSpawn> spawns_medium = new ArrayList<>();

    protected static ArrayList<StructureLandType> LAND = new ArrayList<>(1);
    protected static ArrayList<StructureLandType> WATER = new ArrayList<>(1);
    protected static ArrayList<StructureLandType> LAVA = new ArrayList<>(1);

    protected static ArrayList<BiomeDictionary.Type> DESERTS_ONLY = new ArrayList<>(2);

    static {
        LAND.add(StructureLandType.LAND);
        WATER.add(StructureLandType.WATER);
        LAVA.add(StructureLandType.LAVA);

        DESERTS_ONLY.add(BiomeDictionary.Type.SANDY);
        DESERTS_ONLY.add(BiomeDictionary.Type.WASTELAND);

        spawns_small.add(new TGStructureSpawn(new FactoryHouseSmall(8, 0, 7, 9, 5, 10).setXZSize(11, 10), 10, null, 0, LAND, StructureSize.SMALL));
        spawns_small.add(new TGStructureSpawn(new FactoryHouseBig(23, 0, 14, 23, 9, 14).setXYZSize(23, 9, 14), 10, null, 0, LAND, StructureSize.SMALL));
        spawns_small.add(new TGStructureSpawn(new SmallTrainstation(0, 0, 0, 0, 0, 0).setXZSize(11, 12), 10, null, 0, LAND, StructureSize.SMALL));
        spawns_small.add(new TGStructureSpawn(new SmallMine().setXZSize(17, 11), 10, null, 0, LAND, StructureSize.SMALL));
        spawns_small.add(new TGStructureSpawn(new GasStation(), 10, null, 0, LAND, StructureSize.SMALL));

        spawns_medium.add(new TGStructureSpawn(new AlienBugNestStructure().setXZSize(4, 4), 20, DESERTS_ONLY, 0, LAND, StructureSize.MEDIUM));

        spawns_medium.add(new TGStructureSpawn(new PoliceStation(), 10, null, 0, LAND, StructureSize.MEDIUM));
        spawns_medium.add(new TGStructureSpawn(new FactoryBig(20, 0, 43, 20, 15, 43).setXYZSize(20, 15, 43), 10, null, 0, LAND, StructureSize.MEDIUM));

        spawns_medium.add(new TGStructureSpawn(new SurvivorHideout(), 10, null, 0, LAND, StructureSize.MEDIUM));

        spawns_big.add(new TGStructureSpawn(new MilitaryBaseStructure(0, 0, 0, 0, 0, 0), 1, null, 0, LAND, StructureSize.BIG));

        spawns_big.add(new TGStructureSpawn(new CastleStructure(), 1, null, 0, LAND, StructureSize.BIG));

        spawns_big.add(new TGStructureSpawn(new AircraftCarrier(54, 24, 21, 54, 24, 21).setXZSize(54, 21), 1, null, 0, WATER, StructureSize.BIG));
        spawns_big.add(new TGStructureSpawn(new Train(), 1, null, 0, LAND, StructureSize.BIG)); // Let's say I don't know if ppl will even find it..


        //NETHER

        spawns_small.add(new TGStructureSpawn(new NetherAltarSmall().setXZSize(11, 11), 10, null, -1, LAND, StructureSize.SMALL));
        spawns_small.add(new TGStructureSpawn(new NetherSoulPlatform().setXZSize(11, 11), 10, null, -1, LAND, StructureSize.SMALL));
        spawns_small.add(new TGStructureSpawn(new NetherLoot01().setXZSize(6, 6), 10, null, -1, LAND, StructureSize.SMALL));
        spawns_small.add(new TGStructureSpawn(new NetherAcidHole().setXZSize(9, 9), 10, null, -1, LAND, StructureSize.SMALL));

        spawns_medium.add(new TGStructureSpawn(new NetherAltarMedium().setXZSize(16, 16), 10, null, -1, LAND, StructureSize.MEDIUM));
        spawns_medium.add(new TGStructureSpawn(new NetherGhastSpawner().setXZSize(10, 10), 10, null, -1, LAND, StructureSize.MEDIUM));
        spawns_medium.add(new TGStructureSpawn(new NetherBuilding().setXZSize(21, 22), 10, null, -1, LAND, StructureSize.MEDIUM));
        spawns_medium.add(new TGStructureSpawn(new NetherSubmarine(), 10, null, -1, LAVA, StructureSize.MEDIUM));

        //TODO: finish?.. Maybe. I don't know.
        //spawns_big.add(new TGStructureSpawn(new NetherDungeonStructure(), 1, null, -1, LAND, StructureSize.BIG));

        // END
        spawns_big.add(new TGStructureSpawn(new EndFloatingShip(), 10, null, 1, LAND, StructureSize.BIG));
        spawns_big.add(new TGStructureSpawn(new EndBuilding(), 10, null, 1, LAND, StructureSize.BIG));

        if (TGConfig.worldgen.spawnOreClusterStructures) {
            spawns_medium.add(new TGStructureSpawn(new OreClusterSpike().setXZSize(8, 8), 10, null, 0, LAND, StructureSize.MEDIUM));
            spawns_medium.add(new TGStructureSpawn(new OreClusterMeteorBasis().setXZSize(17, 17), 5, null, 0, LAND, StructureSize.MEDIUM));

            spawns_small.add(new TGStructureSpawn(new NetherOreClusterSmall().setXZSize(3, 3), 10, null, -1, LAND, StructureSize.SMALL));
            spawns_medium.add(new TGStructureSpawn(new NetherOreClusterMedium().setXZSize(18, 12), 10, null, -1, LAND, StructureSize.MEDIUM));
            spawns_medium.add(new TGStructureSpawn(new NetherOreClusterCastle().setXZSize(11, 11), 10, null, -1, LAND, StructureSize.MEDIUM));
            spawns_big.add(new TGStructureSpawn(new EndCluster(), 10, null, 1, LAND, StructureSize.BIG));

            if (TGFluids.OIL_WORLDSPAWN != null) {
                spawns_medium.add(new TGStructureSpawn(new DesertOilCluster().setXZSize(11, 11), 15, DESERTS_ONLY, 0, LAND, StructureSize.MEDIUM));
            }
        }
    }


    public static WorldgenStructure choseStructure(Random rnd, Biome biome, StructureSize size, StructureLandType type, int dimension) {
        int totalweight = 0;

        ArrayList<TGStructureSpawn> spawns;
        if (size == StructureSize.BIG) {
            spawns = spawns_big;
        } else if (size == StructureSize.MEDIUM) {
            spawns = spawns_medium;
        } else {
            spawns = spawns_small;
        }

        for (TGStructureSpawn spawn : spawns) {
            totalweight += spawn.getWeightForBiome(biome, size, type, dimension);
        }

        if (totalweight > 0) {
            int roll = rnd.nextInt(totalweight) + 1;
            int weight = 0;
            for (TGStructureSpawn spawn : spawns) {
                weight += spawn.getWeightForBiome(biome, size, type, dimension);
                if (roll <= weight) {
                    return spawn.structure;
                }

            }
        }
        return null;
    }

}
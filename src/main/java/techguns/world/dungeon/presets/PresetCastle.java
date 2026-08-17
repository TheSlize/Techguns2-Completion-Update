package techguns.world.dungeon.presets;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.util.ResourceLocation;
import techguns.*;
import techguns.entities.npcs.SkeletonSoldier;
import techguns.entities.npcs.ZombieSoldier;
import techguns.tileentities.TGSpawnerTileEnt;
import techguns.world.dungeon.DungeonSegment;
import techguns.world.dungeon.DungeonTemplate;
import techguns.world.dungeon.IDungeonPath;
import techguns.world.dungeon.MazeDungeonPath;
import techguns.world.dungeon.TemplateSegment.SegmentType;

public class PresetCastle implements IDungeonPreset {

    int sizeXZ = 0;
    int sizeY = 0;

    private static final ResourceLocation LOOTTABLE = new ResourceLocation(Tags.MOD_ID, "chests/castle");

    ArrayList<DungeonTemplate> undergroundTemplates = new ArrayList<>();

    ArrayList<DungeonTemplate> lowerTemplates = new ArrayList<>();

    ArrayList<DungeonTemplate> midTemplates = new ArrayList<>();

    ArrayList<DungeonTemplate> upperTemplates = new ArrayList<>();

    ArrayList<DungeonTemplate> topTemplates = new ArrayList<>();

    ArrayList<DungeonTemplate> roofTemplates = new ArrayList<>();

    public PresetCastle() {
        addTemplateIfPresent(undergroundTemplates, "ncdung1");
        addTemplateIfPresent(lowerTemplates, "nclower1");
        addTemplateIfPresent(midTemplates, "ncmid1");
        addTemplateIfPresent(upperTemplates, "ncupper1");
        addTemplateIfPresent(topTemplates, "nctop1");
        addTemplateIfPresent(roofTemplates, "ncroof1");
    }

    private void addTemplateIfPresent(ArrayList<DungeonTemplate> list, String key) {
        DungeonTemplate t = DungeonTemplate.dungeonTemplates.get(key);
        if (t != null) {
            list.add(t.setLoottable(LOOTTABLE));
        }
    }

    @Override
    public DungeonSegment getSegment(SegmentType type, int y, int yMin, int yMax, boolean isSegmentAbove, boolean isSegmentBelow, int seed) {
        Random rand = new Random(seed);
        if (y == -1) {
            return roofTemplates.get(rand.nextInt(roofTemplates.size())).segments.get(type);
        } else if (y == yMin) {
            return undergroundTemplates.get(rand.nextInt(undergroundTemplates.size())).segments.get(type);
        } else if (y <= yMin + 1) {
            return lowerTemplates.get(rand.nextInt(lowerTemplates.size())).segments.get(type);
        } else if (y < yMax - 2) {
            return midTemplates.get(rand.nextInt(midTemplates.size())).segments.get(type);
        } else if (y < yMax - 1) {
            return upperTemplates.get(rand.nextInt(upperTemplates.size())).segments.get(type);
        } else {
            return topTemplates.get(rand.nextInt(topTemplates.size())).segments.get(type);
        }
    }

    @Override
    public int getSizeXZ() {
        if (this.sizeXZ == 0) this.sizeXZ = this.midTemplates.get(0).sizeXZ;
        return this.sizeXZ;
    }

    @Override
    public int getSizeY() {
        if (this.sizeY == 0) this.sizeY = this.midTemplates.get(0).sizeY;
        return this.sizeY;
    }

    @Override
    public void initDungeonPath(IDungeonPath d_path) {
        MazeDungeonPath path = (MazeDungeonPath) d_path;
        path.startHeightLevel = 1;
        path.chanceStraight = 0.8f; //next segment will go forward; -> inverse chance to left/right
        path.chanceRamp = 0.5f; //when straight, roll again for a ramp
        path.chanceRoom = 0.25f; //else, roll for a room
        path.chanceFork = 0.4f; //roll again for another direction, when not a ramp
        path.chanceUp = 0.65f;
        path.useFoundations = true;
        path.usePillars = true;
        path.useRoof = true;
    }

    @Override
    public void initSpawner(TGSpawnerTileEnt spawner) {
        spawner.setParams(TGConfig.spawnerBlock.spawnerBlockWorldgenMobsTotal, TGConfig.spawnerBlock.spawnerBlockWorldgenMobsConcurrent, TGConfig.getSpawnerBlockIntervalTicks(), 2);
        spawner.addMobType(ZombieSoldier.class, 1);
        spawner.addMobType(SkeletonSoldier.class, 1);
    }


}

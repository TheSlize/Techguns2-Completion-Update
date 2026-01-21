package techguns.entities.spawn;

import java.util.ArrayList;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import techguns.entities.npcs.GenericNPC;

public class TGNpcSpawn {

	protected Class<? extends GenericNPC> type;
	protected Class<? extends EntityMob> type_vanillish;
	protected int spawnWeight;
	
	/**
	 * Restricted to these biomeTypes, null = all biomes
	 */
	ArrayList<Biome> biomeWhitelist;
	
	ArrayList<Integer> dimensionIDs;
	
	public TGNpcSpawn(Class<? extends GenericNPC> type, int spawnWeight){
		this.type=type;
		this.spawnWeight=spawnWeight;
		this.biomeWhitelist = null;
		this.dimensionIDs = null;
	}

	public TGNpcSpawn(Class<? extends EntityMob> type, int spawnWeight, boolean dummy){
		this.type_vanillish=type;
		this.spawnWeight=spawnWeight;
		this.biomeWhitelist = null;
		this.dimensionIDs = null;
	}

	public int getWeightForBiome(Biome biome){
		if (this.biomeWhitelist == null) {
			return this.spawnWeight;
		} else {
			if(this.biomeWhitelist.contains(biome)){
				return this.spawnWeight;
			} else {
				return 0;
			}
		}
	}

	public boolean dimensionMatches(World w){
		int id = w.provider.getDimension();
		return this.dimensionIDs == null || this.dimensionIDs.contains(id);
	}
}